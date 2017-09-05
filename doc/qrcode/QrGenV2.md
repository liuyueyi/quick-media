# 二维码的基础服务拓展
> zxing 提供了二维码一些列的功能，在日常生活中，可以发现很多二维码并不仅仅是简单的黑白矩形块，有的添加了文字，加了logo，定制颜色，背景等，本片博文则着手于此，进行基础服务的拓展

本片博文拓展的功能点：

- 支持在二维码中间添加logo
- logo样式选择：支持圆角/直角logo，支持logo的边框选择
- 二维码颜色选择（可自由将原来的黑白色进行替换）
- 支持背景图片
- 支持探测图形的前置色选择

一个包含上面所有功能点的二维码如下图

![http://s2.mogucdn.com/mlcdn/c45406/170728_45a54147f26eh3lf1aiek04c1620h_300x300.png](http://s2.mogucdn.com/mlcdn/c45406/170728_45a54147f26eh3lf1aiek04c1620h_300x300.png)

## 准备
> 由于之前有一篇博文[《spring-boot & zxing 搭建二维码服务》](https://my.oschina.net/u/566591/blog/1457164) 较为消息的介绍了设计一个二维码服务的过程，因此这篇则不再整体设计上多做说明，主要的功能点将集中在以上几个功能点设计与实现上

源码地址: [https://github.com/liuyueyi/quick-media](https://github.com/liuyueyi/quick-media)

这篇博文，将不对二维码生成的细节进行说明，某些地方如有疑惑（如二维码生成时的一些参数，渲染逻辑等）请直接查看代码，or百度谷歌，或者私聊也可。

下面简单说明一下这个工程中与二维码相关的几个类的作用

### 1. `QrCodeOptions.java`

二维码的各种配置参数

### 2. `QrCodeGenWrapper.java`

封装了二维码的参数设置和处理方法，通常来讲对于使用者而言，只需要使用这个类中的方法即可实现二维码的生成，如生成上面的二维码测试代码如下

```java
@Test
public void testGenColorCode() {
    String msg = "https://my.oschina.net/u/566591/blog/1359432";
    // 根据本地文件生成待logo的二维码， 重新着色位置探测图像
    try {
        String logo = "logo.jpg";
        String bg = "bg.png";
        BufferedImage img = QrCodeGenWrapper.of(msg)
                .setW(300)
                .setPreColor(0xff0000ff)
                .setBgColor(0xffFFFF00)
                .setDetectCornerPreColor(0xffff0000)
                .setPadding(2)
                .setLogo(logo)
                .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                .setLogoBgColor(0xff00cc00)
                .setBackground(bg)
                .asBufferedImage();


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(img, "png", outputStream);
        System.out.println(Base64Util.encode(outputStream));
    } catch (Exception e) {
        System.out.println("create qrcode error! e: " + e);
        Assert.assertTrue(false);
    }
}
```

### 3.`QrCodeUtil.java`

二维码工具类，包括生成二维码矩阵信息，二维码图片渲染，输出`BufferedIamge`对象等


### 4. `ImageUtil.java`

图片处理辅助类，实现图片圆角化，添加边框，插入logo，绘制背景图等

---

## 设计与实现

### 1. 二维码颜色可配置
> 二维码颜色的选择，主要在将二维码矩阵转换成图的时候，选择不同的颜色进行渲染即可，我们主要的代码将放在 `com.hust.hui.quickmedia.common.util.QrCodeUtil#toBufferedImage` 方法中

先看一下实现逻辑

```java
/**
 * 根据二维码配置 & 二维码矩阵生成二维码图片
 *
 * @param qrCodeConfig
 * @param bitMatrix
 * @return
 * @throws IOException
 */
public static BufferedImage toBufferedImage(QrCodeOptions qrCodeConfig, BitMatrixEx bitMatrix) throws IOException {
    int qrCodeWidth = bitMatrix.getWidth();
    int qrCodeHeight = bitMatrix.getHeight();
    BufferedImage qrCode = new BufferedImage(qrCodeWidth, qrCodeHeight, BufferedImage.TYPE_INT_RGB);

    for (int x = 0; x < qrCodeWidth; x++) {
        for (int y = 0; y < qrCodeHeight; y++) {
                qrCode.setRGB(x, y,
                        bitMatrix.get(x, y) ?
                                qrCodeConfig.getMatrixToImageConfig().getPixelOnColor() :
                            qrCodeConfig.getMatrixToImageConfig().getPixelOffColor());
        }
    }
    
    ...
}
```

**注意**

`BitMatrixEx` 是 `com.google.zxing.common.BitMatrix` 的拓展，后面说明为什么这么做，

此处知晓 `com.hust.hui.quickmedia.common.qrcode.BitMatrixEx#get` 等同于 `com.google.zxing.common.BitMatrix#get`即可

**说明**

- 上面的逻辑比较清晰，先创建一个置顶大小的图像，然后遍历 `bitMatrix`，对图像进行着色

- `bitMatrix.get(x, y) == true` 表示该处为二维码的有效信息（这个是在二维码生成时决定，zxing的二维码生成逻辑负责生成BitMatrix对象，原理此处省略，因为我也没仔细研究），然后涂上配置的前置色；否则表示空白背景，涂上背景色即可


### 2. 位置探测图行可配置
> 位置探测图形就是二维码的左上角，右上角，左下角的三个矩形框（前面途中的三个红框），用于定位二维码使用，这里的实现确保它的颜色可以与二维码的前置色不同

经过上面的二维码颜色渲染，很容易就可以想到，在二维码的最终渲染时，对位置探测图形采用不同的颜色进行渲染即可，所以渲染代码如下


```java
/**
 * 根据二维码配置 & 二维码矩阵生成二维码图片
 *
 * @param qrCodeConfig
 * @param bitMatrix
 * @return
 * @throws IOException
 */
public static BufferedImage toBufferedImage(QrCodeOptions qrCodeConfig, BitMatrixEx bitMatrix) throws IOException {
    int qrCodeWidth = bitMatrix.getWidth();
    int qrCodeHeight = bitMatrix.getHeight();
    BufferedImage qrCode = new BufferedImage(qrCodeWidth, qrCodeHeight, BufferedImage.TYPE_INT_RGB);

    for (int x = 0; x < qrCodeWidth; x++) {
        for (int y = 0; y < qrCodeHeight; y++) {
            if (bitMatrix.isDetectCorner(x, y)) { // 着色位置探测图形
                qrCode.setRGB(x, y,
                        bitMatrix.get(x, y) ?
                                qrCodeConfig.getDetectCornerColor().getPixelOnColor() :
                                qrCodeConfig.getDetectCornerColor().getPixelOffColor());
            } else { // 着色二维码主题
                qrCode.setRGB(x, y,
                        bitMatrix.get(x, y) ?
                                qrCodeConfig.getMatrixToImageConfig().getPixelOnColor() :
                                qrCodeConfig.getMatrixToImageConfig().getPixelOffColor());
            }
        }
    }
    
    ....
}
```

相比较与之前，在遍历逻辑中，多了一个是否为位置探测图形的分支判断

```java
if (bitMatrix.isDetectCorner(x, y)) { // 着色位置探测图形
  qrCode.setRGB(x, y,
      bitMatrix.get(x, y) ?
        qrCodeConfig.getDetectCornerColor().getPixelOnColor() :
        qrCodeConfig.getDetectCornerColor().getPixelOffColor());
} 
```

所以我们的问题就是如何判断(x,y)坐标对应的位置是否为位置探测图形？

#### 位置探测图形判定

这个判定的逻辑，就需要深入到二维码矩阵的生成逻辑中，直接给出对应代码位置

```java
// Embed basic patterns
// The basic patterns are:
// - Position detection patterns
// - Timing patterns
// - Dark dot at the left bottom corner
// - Position adjustment patterns, if need be
com.google.zxing.qrcode.encoder.MatrixUtil#embedBasicPatterns


// 确定位置探测图形的方法
com.google.zxing.qrcode.encoder.MatrixUtil#embedPositionDetectionPatternsAndSeparators

// 自适应调整矩阵的方法
com.google.zxing.qrcode.encoder.MatrixUtil#maybeEmbedPositionAdjustmentPatterns
```

直接看代码，会发现位置探测图形的二维数组如下

```java
private static final int[][] POSITION_DETECTION_PATTERN =  {
    {1, 1, 1, 1, 1, 1, 1},
    {1, 0, 0, 0, 0, 0, 1},
    {1, 0, 1, 1, 1, 0, 1},
    {1, 0, 1, 1, 1, 0, 1},
    {1, 0, 1, 1, 1, 0, 1},
    {1, 0, 0, 0, 0, 0, 1},
    {1, 1, 1, 1, 1, 1, 1},
};

private static final int[][] POSITION_ADJUSTMENT_PATTERN = {
    {1, 1, 1, 1, 1},
    {1, 0, 0, 0, 1},
    {1, 0, 1, 0, 1},
    {1, 0, 0, 0, 1},
    {1, 1, 1, 1, 1},
};
```

到这里，我们的判断就比较清晰了，位置探测图形有两种规格，5 or 7

在看具体的判定逻辑之前，先看 `BitMatrixEx`增强类，可以判定(x,y)坐标处是否为位置探测图形，内部判定逻辑和 `BitMatrix`中是否为二维码有效信息的判定一致

```java
@Getter
@Setter
public class BitMatrixEx {
    private final int width;
    private final int height;
    private final int rowSize;
    private final int[] bits;


    private BitMatrix bitMatrix;

    public BitMatrixEx(BitMatrix bitMatrix) {
        this(bitMatrix.getWidth(), bitMatrix.getHeight());
        this.bitMatrix = bitMatrix;

    }

    private BitMatrixEx(int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Both dimensions must be greater than 0");
        }

        this.width = width;
        this.height = height;
        this.rowSize = (width + 31) / 32;
        bits = new int[rowSize * height];
    }



    public void setRegion(int left, int top, int width, int height) {
        int right = left + width;
        int bottom = top + height;

        for (int y = top; y < bottom; y++) {
            int offset = y * rowSize;
            for (int x = left; x < right; x++) {
                bits[offset + (x / 32)] |= 1 << (x & 0x1f);
            }
        }
    }


    public boolean get(int x, int y) {
        return bitMatrix.get(x, y);
    }


    public boolean isDetectCorner(int x, int y) {
        int offset = y * rowSize + (x / 32);
        return ((bits[offset] >>> (x & 0x1f)) & 1) != 0;
    }
}
```


**位置判定逻辑**

位置判定逻辑在 `com.hust.hui.quickmedia.common.util.QrCodeUtil#renderResult` 方法中，简单说一下这个方法的作用

- 根据 `com.google.zxing.qrcode.encoder.QRCode` 生成 `BitMatrixEx` 对象
- 内部实现二维码白边的修复（详情参考博文：[《zxing 二维码大白边一步一步修复指南》](https://my.oschina.net/u/566591/blog/872770)）
- 内部实现位置探测图形的判定逻辑

直接看判定逻辑

```java
// 获取位置探测图形的size，根据源码分析，有两种size的可能
// {@link com.google.zxing.qrcode.encoder.MatrixUtil.embedPositionDetectionPatternsAndSeparators}
ByteMatrix input = qrCode.getMatrix();
// 因为位置探测图形的下一位必然是0，所以下面的一行可以判定选择的是哪种规格的位置判定
int detectCornerSize = input.get(0, 5) == 1 ? 7 : 5;

for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
    // Write the contents of this row of the barcode
    for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
        if (input.get(inputX, inputY) == 1) {
            // 二维码的有效信息设置（即传统二维码中黑色局域的确定）
            output.setRegion(outputX, outputY, multiple, multiple);
        }


        // 设置三个位置探测图形
        if (inputX < detectCornerSize && inputY < detectCornerSize // 左上角
                || (inputX < detectCornerSize && inputY >= inputHeight - detectCornerSize) // 左下脚
                || (inputX >= inputWidth - detectCornerSize && inputY < detectCornerSize)) { // 右上角
            res.setRegion(outputX, outputY, multiple, multiple);
        }
    }
}

```

### 3. 背景图支持
> 前面两个涉及到二维码本身的修改，接下来的背景 & logo则基本上无二维码无关，只是图片的操作而已，背景图支持，即将背景图作为图层，将二维码渲染在正中间即可

对于图片的覆盖，直接借用 java.awt 包下的工具类即可实现

```java
/**
 * 绘制背景图
 *
 * @param source     原图
 * @param background 背景图
 * @param bgW        背景图宽
 * @param bgH        背景图高
 * @return
 * @throws IOException
 */
public static BufferedImage drawBackground(BufferedImage source, String background, int bgW, int bgH) throws IOException {
    int sW = source.getWidth();
    int sH = source.getHeight();


    // 背景的图宽高不应该小于原图
    if (bgW < sW) {
        bgW = sW;
    }

    if (bgH < sH) {
        bgH = sH;
    }


    // 获取背景图
    BufferedImage bg = getImageByPath(background);
    if (bg.getWidth() != bgW || bg.getHeight() != bgH) { // 需要缩放
        BufferedImage temp = new BufferedImage(bgW, bgH, BufferedImage.TYPE_INT_ARGB);
        temp.getGraphics().drawImage(bg.getScaledInstance(bgW, bgH, Image.SCALE_SMOOTH)
                , 0, 0, null);
        bg = temp;
    }


    // 绘制背景图
    int x = (bgW - sW) >> 1;
    int y = (bgH - sH) >> 1;
    Graphics2D g2d = bg.createGraphics();
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.8f)); // 透明度， 避免看不到背景
    g2d.drawImage(source, x, y, sW, sH, null);
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
    g2d.dispose();
    bg.flush();
    return bg;
}

```

简单说一下上面的实现逻辑

- 获取背景图
- 根据置顶的背景图大小，对原背景图进行缩放
- 将目标图片（二维码）绘制在背景图正中间

其中，我们对二维码的覆盖设置了透明度为0.8，确保不会完全覆盖背景图，导致完全看不到背景是什么，此处如有其他的需求场景可以进行可配置化处理


### 4. logo支持
> 其实logo的支持和背景的支持逻辑基本没什么差别，都是将一个图绘制在另一个图上

具体的实现如下, 先无视logo样式的选择问题

```java
/**
 * 在图片中间,插入圆角的logo
 *
 * @param qrCode      原图
 * @param logo        logo地址
 * @param logoStyle   logo 的样式 （圆角， 直角）
 * @param logoBgColor logo的背景色
 * @throws IOException
 */
public static void insertLogo(BufferedImage qrCode,
                              String logo,
                              QrCodeOptions.LogoStyle logoStyle,
                              Color logoBgColor) throws IOException {
    int QRCODE_WIDTH = qrCode.getWidth();
    int QRCODE_HEIGHT = qrCode.getHeight();

    // 获取logo图片
    BufferedImage bf = getImageByPath(logo);
    int boderSize = bf.getWidth() / 15;
    // 生成圆角边框logo
    bf = makeRoundBorder(bf, logoStyle, boderSize, logoBgColor); // 边距为二维码图片的1/15

    // logo的宽高
    int w = bf.getWidth() > QRCODE_WIDTH * 2 / 10 ? QRCODE_WIDTH * 2 / 10 : bf.getWidth();
    int h = bf.getHeight() > QRCODE_HEIGHT * 2 / 10 ? QRCODE_HEIGHT * 2 / 10 : bf.getHeight();

    // 插入LOGO
    Graphics2D graph = qrCode.createGraphics();

    int x = (QRCODE_WIDTH - w) >> 1 ;
    int y = (QRCODE_HEIGHT - h) >> 1;

    graph.drawImage(bf, x, y, w, h, null);
    graph.dispose();
    bf.flush();
}
```

上面的主要逻辑，其实没啥区别，接下来主要关心的则是圆角图形生成以及边框的支持

### 5. 圆角图形
> 生成圆角图片是一个非常常见的需求

先借用`new RoundRectangle2D.Float(0, 0, w, h, cornerRadius,
            cornerRadius)`绘制一个圆角的画布出来
            
将原图绘制在画布上即可

```java
/**
 * 生成圆角图片
 *
 * @param image        原始图片
 * @param cornerRadius 圆角的弧度
 * @return 返回圆角图
 */
public static BufferedImage makeRoundedCorner(BufferedImage image,
                                              int cornerRadius) {
    int w = image.getWidth();
    int h = image.getHeight();
    BufferedImage output = new BufferedImage(w, h,
            BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2 = output.createGraphics();

    // This is what we want, but it only does hard-clipping, i.e. aliasing
    // g2.setClip(new RoundRectangle2D ...)

    // so instead fake soft-clipping by first drawing the desired clip shape
    // in fully opaque white with antialiasing enabled...
    g2.setComposite(AlphaComposite.Src);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(Color.WHITE);
    g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius,
            cornerRadius));

    // ... then compositing the image on top,
    // using the white shape from above as alpha source
    g2.setComposite(AlphaComposite.SrcAtop);
    g2.drawImage(image, 0, 0, null);

    g2.dispose();

    return output;
}
```

### 6. 圆角边框的图片
> 上面实现圆角图片之后，再考虑生成一个带圆角边框的图片就很简单了，直接绘制一个大一号的存色边框，然后将圆角图片绘制上去即可

```java
/**
 * <p>
 * 生成圆角图片 & 圆角边框
 *
 * @param image     原图
 * @param logoStyle 圆角的角度
 * @param size      边框的边距
 * @param color     边框的颜色
 * @return 返回带边框的圆角图
 */
public static BufferedImage makeRoundBorder(BufferedImage image,
                                            QrCodeOptions.LogoStyle logoStyle,
                                            int size, Color color) {
    // 将图片变成圆角
    int cornerRadius = 0;
    if (logoStyle == QrCodeOptions.LogoStyle.ROUND) {
        cornerRadius = image.getWidth() / 4;
        image = makeRoundedCorner(image, cornerRadius);
    }

    int w = image.getWidth() + size;
    int h = image.getHeight() + size;
    BufferedImage output = new BufferedImage(w, h,
            BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2 = output.createGraphics();
    g2.setComposite(AlphaComposite.Src);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(color == null ? Color.WHITE : color);
    g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius,
            cornerRadius));

    // ... then compositing the image on top,
    // using the white shape from above as alpha source
//        g2.setComposite(AlphaComposite.SrcAtop);
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
    g2.drawImage(image, size / 2, size / 2, null);
    g2.dispose();

    return output;
}
```


## 测试

上面分别对每一个点进行了实现并加以简单说明，最后就是需要将上面的都串起来进行测试了，因为我们的工程是在前面已经搭建好的二维码服务上进行的，所以测试代码也比较简单，如下

```java
@Test
public void testGenColorCode() {
    String msg = "https://my.oschina.net/u/566591/blog/1359432";
    // 根据本地文件生成待logo的二维码， 重新着色位置探测图像
    try {
        String logo = "logo.jpg";
        String bg = "bg.png";
        BufferedImage img = QrCodeGenWrapper.of(msg)
                .setW(300)
                .setPreColor(0xff0000ff)
                .setBgColor(0xffFFFF00)
                .setDetectCornerPreColor(0xffff0000)
                .setPadding(2)
                .setLogo(logo)
                .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                .setLogoBgColor(0xff00cc00)
                .setBackground(bg)
                .asBufferedImage();


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(img, "png", outputStream);
        System.out.println(Base64Util.encode(outputStream));
    } catch (Exception e) {
        System.out.println("create qrcode error! e: " + e);
        Assert.assertTrue(false);
    }
}
```

测试执行示意图

![http://s2.mogucdn.com/mlcdn/c45406/170728_2lebbba9b47037cc0g03hd42hf6ga_1224x639.gif](http://s2.mogucdn.com/mlcdn/c45406/170728_2lebbba9b47037cc0g03hd42hf6ga_1224x639.gif)

---
## 其他

项目源码: [https://github.com/liuyueyi/quick-media](https://github.com/liuyueyi/quick-media)

相关博文: 

- [zxing 二维码大白边一步一步修复指南](https://my.oschina.net/u/566591/blog/872770)
- [spring-boot & zxing 搭建二维码服务](https://my.oschina.net/u/566591/blog/1457164)


个人博客：[一灰的个人博客](http://zbang.online:8080/articles/2017/07/18/1500369136069.html)

公众号获取更多:

![https://static.oschina.net/uploads/img/201707/09205944_0PzS.jpg](https://static.oschina.net/uploads/img/201707/09205944_0PzS.jpg)