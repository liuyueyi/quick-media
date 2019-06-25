# Batik渲染png图片异常的bug修复

batik是apache的一个开源项目，可以实现svg的渲染，后端借助它可以比较简单的实现图片渲染，当然和java一贯处理图片不太方便一样，使用起来也有不少坑

下面记录一个bug的修复过程

<!-- more -->

## 3.1. 问题重现

svg文件:

```xml
<svg width="200" height="200" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
<image y="0" width="100%" height="100%" x="0"
    xlink:href="http://image.uc.cn/o/wemedia/s/upload/2017/39c53604fe3587a4876396cf3785b801x200x200x13.png"/>
    <!--xlink:href="https://s17.mogucdn.com/mlcdn/c45406/180119_46ld8kkb54d3el06hela5d61e18f5_1024x966.png"/>-->
    <!--xlink:href="http://avatar.csdn.net/A/8/B/3_u010889145.jpg"/>-->
</svg>
```

依次测试了三个图片，两个png，一个jpg，很不幸第一个png会抛异常


输出的堆栈信息如

```sh
The URI "http://image.uc.cn/o/wemedia/s/upload/2017/39c53604fe3587a4876396cf3785b801x200x200x13.png"
on element <image> can't be opened because:
PNG URL is corrupt or unsupported variant
    at org.apache.batik.bridge.UserAgentAdapter.getBrokenLinkDocument(UserAgentAdapter.java:448)
    at org.apache.batik.bridge.SVGImageElementBridge.createRasterImageNode(SVGImageElementBridge.java:642)
    at org.apache.batik.bridge.SVGImageElementBridge.createImageGraphicsNode(SVGImageElementBridge.java:340)
    at org.apache.batik.bridge.SVGImageElementBridge.buildImageGraphicsNode(SVGImageElementBridge.java:180)
    at org.apache.batik.bridge.SVGImageElementBridge.createGraphicsNode(SVGImageElementBridge.java:122)
    at org.apache.batik.bridge.GVTBuilder.buildGraphicsNode(GVTBuilder.java:213)
    at org.apache.batik.bridge.GVTBuilder.buildComposite(GVTBuilder.java:171)
    at org.apache.batik.bridge.GVTBuilder.build(GVTBuilder.java:82)
    at org.apache.batik.transcoder.SVGAbstractTranscoder.transcode(SVGAbstractTranscoder.java:208)
    at org.apache.batik.transcoder.image.ImageTranscoder.transcode(ImageTranscoder.java:92)
    at org.apache.batik.transcoder.XMLAbstractTranscoder.transcode(XMLAbstractTranscoder.java:142)
    at org.apache.batik.transcoder.SVGAbstractTranscoder.transcode(SVGAbstractTranscoder.java:156)
    ...
```

---

## 3.2. 问题定位及分析

既然出现了这个问题，那么就要去修复解决了，当然遇到这么鬼畜的问题，最常见的几个步骤：

1. 其他人遇到过么 （问百度） -- 结果度娘没有给出任何有效的建议，也没有搜到任何有用的信息
2. 然后问谷歌，靠谱了一点，至少有些相关的主题了，但建设性的意见也没收到
3. 外援实在找不到，只能debug查问题了


### 1. DEBUG的一路

通过上面的堆栈信息，可以想见，debug的几个地方也和明确了，首先定位到下面这一行

```sh
at org.apache.batik.bridge.UserAgentAdapter.getBrokenLinkDocument(UserAgentAdapter.java:448)
```

为什么这么干？因为首先得确认下这个异常是怎么抛出来的，逆向推，直接看源码，发现直接抛出异常

![2A02AB38-25ED-4B71-8CE7-76460623FE08.png](https://s17.mogucdn.com/mlcdn/c45406/180119_67ik6b6l9kb199gjaachbkdce89dk_1274x284.jpg)

再往上走

```sh
at org.apache.batik.bridge.SVGImageElementBridge.createRasterImageNode(SVGImageElementBridge.java:642)
```

![D1CECFCF-D940-4A17-87F9-7E12B00517D9.png](https://s17.mogucdn.com/mlcdn/c45406/180119_61el9dg4f32iafif6i57g0hac8e7i_1328x832.jpg)

所以说因为这个if条件判断成立，导致进入了这个异常逻辑，判断的逻辑也没啥好说的，现在的关键是这个参数对象img是怎么来的

```sh
at org.apache.batik.bridge.SVGImageElementBridge.createImageGraphicsNode(SVGImageElementBridge.java:340)
```

![IMAGE](https://s17.mogucdn.com/mlcdn/c45406/180119_645jcjh7hae6g6e7k3bakhje593a7_1320x508.jpg)

然后就稍微清晰一点了，直接将火力放在下面的方法中

```java
org.apache.batik.ext.awt.image.spi.ImageTagRegistry#readURL(java.io.InputStream, 
    org.apache.batik.util.ParsedURL, 
    org.apache.xmlgraphics.java2d.color.ICCColorSpaceWithIntent, 
    boolean, 
    boolean)
```

在这个方法内部，也没什么好说的，单步多调几次，就能发现异常的case是怎么来的了，省略掉中间各种单步debug的过程，下面直接进入关键链路

### 2. 火力全开，问题定位

```java
org.apache.batik.ext.awt.image.codec.imageio.AbstractImageIORegistryEntry
```

通过上面的一路之后，发现最终的关键就是上面这个抽象类，顺带也可以看下这个抽象类的几个子类，有JPEGxxx, PNGxxx, TIFFxxx，然后问题来了，都已经有相关实现了，所以png讲道理应该是会支持的才对吧，但和实际的表现太不一样了吧，所以有必要撸一把源码了


```java
public Filter handleStream(InputStream inIS,
                           ParsedURL   origURL,
                           boolean     needRawData) {
    final DeferRable  dr  = new DeferRable();
    final InputStream is  = inIS;
    final String      errCode;
    final Object []   errParam;
    if (origURL != null) {
        errCode  = ERR_URL_FORMAT_UNREADABLE;
        errParam = new Object[] {getFormatName(), origURL};
    } else {
        errCode  = ERR_STREAM_FORMAT_UNREADABLE;
        errParam = new Object[] {getFormatName()};
    }

    Thread t = new Thread() {
        @Override
        public void run() {
            Filter filt;
            try{
                Iterator<ImageReader> iter = ImageIO.getImageReadersByMIMEType(
                        getMimeTypes().get(0).toString());
                if (!iter.hasNext()) {
                    throw new UnsupportedOperationException(
                            "No image reader for "
                                + getFormatName() + " available!");
                }
                ImageReader reader = iter.next();
                ImageInputStream imageIn = ImageIO.createImageInputStream(is);
                reader.setInput(imageIn, true);
      
                int imageIndex = 0;
                dr.setBounds(new Rectangle2D.Double
                             (0, 0,
                              reader.getWidth(imageIndex),
                              reader.getHeight(imageIndex)));
                CachableRed cr;
                //Naive approach possibly wasting lots of memory
                //and ignoring the gamma correction done by PNGRed :-(
                //Matches the code used by the former JPEGRegistryEntry, though.
                BufferedImage bi = reader.read(imageIndex);
                cr = GraphicsUtil.wrap(bi);
                cr = new Any2sRGBRed(cr);
                cr = new FormatRed(cr, GraphicsUtil.sRGB_Unpre);
                WritableRaster wr = (WritableRaster)cr.getData();
                ColorModel cm = cr.getColorModel();
                BufferedImage image = new BufferedImage
                    (cm, wr, cm.isAlphaPremultiplied(), null);
                cr = GraphicsUtil.wrap(image);
                filt = new RedRable(cr);
            } catch (IOException ioe) {
                    // Something bad happened here...
                    filt = ImageTagRegistry.getBrokenLinkImage
                        (AbstractImageIORegistryEntry.this,
                         errCode, errParam);
            } catch (ThreadDeath td) {
                filt = ImageTagRegistry.getBrokenLinkImage
                    (AbstractImageIORegistryEntry.this,
                     errCode, errParam);
                dr.setSource(filt);
                throw td;
            } catch (Throwable t) {
                filt = ImageTagRegistry.getBrokenLinkImage
                    (AbstractImageIORegistryEntry.this,
                     errCode, errParam);
            }
  
            dr.setSource(filt);
        }
  };
  t.start();
  return dr;
}
```

看上面的实现是一个非常有意思的事情， 开了一个线程做事情，而且直接就返回了，相当于给了别人一个储物箱的钥匙，虽然现在储物箱是空的，但是回头我会填满的

言归正传，主要的业务逻辑就在这个线程里了，核心的几行代码就是

```java
// 加载图片，转为BufferedImage对象
BufferedImage bi = reader.read(imageIndex);
cr = GraphicsUtil.wrap(bi);
// 下面实现对图片的ARGB进行修改
cr = new Any2sRGBRed(cr);
cr = new FormatRed(cr, GraphicsUtil.sRGB_Unpre);
WritableRaster wr = (WritableRaster)cr.getData();
ColorModel cm = cr.getColorModel();
BufferedImage image = new BufferedImage
    (cm, wr, cm.isAlphaPremultiplied(), null);
cr = GraphicsUtil.wrap(image);
filt = new RedRable(cr);
```

debug上面的几行代码，发现问题比较明显了，就是这个图片的转换跪了，至于为啥？ java的图片各种蛋疼至极，这里面的逻辑，真心搞不进去，so深挖到此为止

---

## 3.3. 兼容逻辑

问题定位到了，当然就是想办法来修复了，简单来说，需要兼容的就是图片的类型转换上了，直接用原来的可能会抛异常，所以做了一个简单的兼容逻辑

```java
if(bi.getType() == BufferedImage.TYPE_BYTE_INDEXED) {
    BufferedImage image = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    g2d.drawImage(bi, 0, 0, null);
    g2d.dispose();
    cr = GraphicsUtil.wrap(image);
} else {
    cr = GraphicsUtil.wrap(bi);
    cr = new Any2sRGBRed(cr);
    cr = new FormatRed(cr, GraphicsUtil.sRGB_Unpre);
    WritableRaster wr = (WritableRaster)cr.getData();
    ColorModel cm = cr.getColorModel();
    BufferedImage image = new BufferedImage
        (cm, wr, cm.isAlphaPremultiplied(), null);
    cr = GraphicsUtil.wrap(image);
}
```


再次验证，ok

**注意：**

一个问题来了，上面的兼容是需要修改源码的，我们可以怎么办？有几种解决方法

- 猥琐方法一：down下源码，修改版本，然后传到自己的私服，使用自己的vip包
- 猥琐方法二：把 batik-codec 工程原样拷贝到自己的项目中，就可以随意的使用改了
- 猥琐方法三：写一个完全相同的类（包路径完全相同），然后构造一个自定义类加载器，加载这个自己的这个兼容版本的，替换原来的（未测试，不确定是否能行）


至于我的选择，就是使用了猥琐方法二
