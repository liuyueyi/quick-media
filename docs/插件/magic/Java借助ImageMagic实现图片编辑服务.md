# Java 借助ImageMagic实现图片编辑服务

java原生对于图片的编辑处理并没有特别友好，而且问题也有不少，那么作为一个java后端，如果要提供图片的编辑服务可以怎么办？也得想办法去支持业务需求，本片博文基于此进行展开

<!-- more -->

## I. 调研

首先最容易想到的就是目前是不是已经有了相关的开源库，直接用不就很high了嘛，git上搜一下

### 1. thumbnailator

差不多四年都没有更新了，基于awt进行图片的编辑处理，目前提供了基本的图片编辑接口，开始用了一段时间，有几个绕不够去的坑，所以最后放弃了

使用姿势：

```xml
<dependency>
  <groupId>net.coobird</groupId>
  <artifactId>thumbnailator</artifactId>
  <version>0.4.8</version>
</dependency>
```

一个使用case:

```java
BufferedImage originalImage = ImageIO.read(new File("original.jpg"));

BufferedImage thumbnail = Thumbnails.of(originalImage)
        .size(200, 200)
        .rotate(90)
        .asBufferedImage();
```

问题说明：

- jpg图片编辑后，输出图片变红的问题（详情参考：[兼容ImageIO读取jpeg图片变红](https://liuyueyi.github.io/hexblog/2018/01/22/%E5%85%BC%E5%AE%B9ImageIO%E8%AF%BB%E5%8F%96jpeg%E5%9B%BE%E7%89%87%E5%8F%98%E7%BA%A2/)）
- 图片精度丢失（对于精度要求较高的场景下，直接使用Jdk的BufferedImage会丢失精度）


上面两个问题中，第二个精度丢失在某些对图片质量有要求的场景下比较严重，如果业务场景没那么将就的话，用这个库还是可以减少很多事情的，下面基于ImageMagic的接口设计，很大程度上参考了该工程的使用规范，因为使用起来（+阅读）确实特别顺畅


### 2. simpleimage

阿里的开源库，文档极其欠缺，而且良久没有人维护，没有实际使用过，感觉属于玩票的性质（个人猜测是KPI为导向下的产物）


如果想造轮子的话，参考它的源码，某些图片的处理方案还是不错的

### 3. imagemagic + im4java

ImageMagic/GraphicMagic 是c++的图象处理软件，很多服务基于此来搭建图片处理服务的

- 优点：稳定、性能高、支持接口多、开箱即用、靠谱
- 缺点：得提前配置环境，基本上改造不动，内部有问题也没辙

这个方法也是下面的主要讲述重点，放弃Thumbnailator选择imagemagic的原因如下：

- 支持更多的服务功能（比Thumbnailator多很多的接口）
- 没有精度丢失问题
- 没有图片失真问题（颜色变化，alpha值变化问题）

## II. 环境准备

首先得安装ImageMagic环境，有不少的第三方依赖，下面提供linux和mac的安装过程

### 1. linux安装过程


```sh
# 依赖安装
yum install libjpeg-devel
yum install libpng-devel
yum install libwebp-devel


## 也可以使用源码方式安装
安装jpeg 包 `wget ftp://223.202.54.10/pub/web/php/libjpeg-6b.tar.gz`
安装webp 包 `wget http://www.imagemagick.org/download/delegates/libwebp-0.5.1.tar.gz`
安装png 包 `wget http://www.imagemagick.org/download/delegates/libpng-1.6.24.tar.gz`


## 下载并安装ImageMagic
wget http://www.imagemagick.org/download/ImageMagick.tar.gz

tar -zxvf ImageMagick.tar.gz
cd ImageMagick-7.0.7-28
./configure; sudo make; sudo make install
```

安装完毕之后，进行测试

```sh
$ convert --version

Version: ImageMagick 7.0.7-28 Q16 x86_64 2018-04-17 http://www.imagemagick.org
Copyright: © 1999-2018 ImageMagick Studio LLC
License: http://www.imagemagick.org/script/license.php
Features: Cipher DPC HDRI OpenMP
Delegates (built-in): fontconfig freetype jng jpeg lzma png webp x xml zlib
```


### 2. mac安装过程

依赖安装

```sh
sudo brew install jpeg
sudo brew install libpng
sudo brew install libwebp
sudo brew install GraphicsMagick
sudo brew install ImageMagick
```

源码安装方式与上面一致


### 3. 问题及修复

如果安装完毕之后，可能会出现下面的问题

提示找不到png依赖:

- 安装：一直找不到 png的依赖，查阅需要安装 http://pkgconfig.freedesktop.org/releases/pkg-config-0.28.tar.gz

执行 convert 提示linux shared libraries 不包含某个库

- 临时方案：`export LD_LIBRARY_PATH=/usr/local/lib:$LD_LIBRARY_PATH`
- 永久方案：

    ```sh
    vi /etc/ld.so.conf
    在这个文件里加入：/usr/local/lib 来指明共享库的搜索位置
    然后再执行/sbin/ldconf
    ```

### 4. 常见Convert命令

imagemagic的场景使用命令如下

裁图 

- convert test.jpg -crop 640x960+0+0 output.jpg

旋转 

- convert test.jpg -rotate 90 output.jpg

缩放 

- convert test.jpg -resize 200x200 output.jpg

强制宽高缩放 

- convert test.jpg -resize 200x200! output.jpg

缩略图 

- convert -thumbnail 200x300 test.jpg thumb.jpg

上下翻转：

- convert -flip foo.png bar.png

左右翻转：

- convert -flop foo.png bar.png

水印：

- composite -gravity northwest -dissolve 100 -geometry +0+0 water.png temp.jpg out.jpg

添加边框 :

- convert -border 6x6 -bordercolor "#ffffff" test.jpg bord.jpg

去除边框 :

- convert -thumbnail 200x300 test.jpg thumb.jpg

## III. 接口设计与实现

java调用ImageMagic的方式有两种，一个是基于命令行的，一种是基于JNI的，我们选则im4java来操作imagemagic的接口（基于命令行的操作）

**目标：**

对外的使用姿势尽可能如 `Thumbnailtor`，采用builder模式来设置参数，支持多种输入输出


### 1. im4java使用姿势

几个简单的case，演示下如何使用im4java实现图片的操作

```java
IMOperation op = new IMOperation();

// 裁剪
op.crop(operate.getWidth(), operate.getHeight(), operate.getX(), operate.getY());


// 旋转
op.rotate(rotate);


// 压缩
op.resize(operate.getWidth(), operate.getHeight());
op.quality(operate.getQuality().doubleValue()); // 精度


// 翻转
op.flip();

// 镜像
op.flop();

// 水印
op.geometry(operate.getWidth(), operate.getHeight(), operate.getX(), operate.getY()).composite();

// 边框
op.border(operate.getWidth(), operate.getHeight()).bordercolor(operate.getColor());


// 原始命令方式添加
op.addRawArgs("-resize", "!100x200");


// 添加原始图片地址
op.addImage(sourceFilename);
// 目标图片地址
op.addImage(outputFilename);


/** 传true到构造函数中,则表示使用GraphicMagic, 裁图时,图片大小会变 */
ConvertCmd convert = new ConvertCmd();
convert.run(op);
```

### 2. 使用姿势

在具体的设计接口之前，不妨先看一下最终的使用姿势，然后逆向的再看是如何设计的

```java
private static final String localFile = "blogInfoV2.png";


/**
 * 复合操作
 */
@Test
public void testOperate() {
    BufferedImage img;
    try {
        img = ImgWrapper.of(localFile)
                .board(10, 10, "red")
                .flip()
                .rotate(180)
                .crop(0, 0, 1200, 500)
                .asImg();
        System.out.println("--- " + img);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

上面这个方法，演示了图片的多个操作，首先是加个红色边框，然后翻转，然后旋转180°，再裁剪输出图片


所以这个封装，肯定是使用了Builder模式了，接下来看下配置参数


### 3. 接口设计

首先确定目前支持的几个方法：`OperateType`

其次就是相关的配置参数： `Operate<T>`

```java
@Data
public static class Operate<T> {
    /**
     * 操作类型
     */
    private OperateType operateType;

    /**
     * 裁剪宽; 缩放宽
     */
    private Integer width;
    /**
     * 高
     */
    private Integer height;
    /**
     * 裁剪时,起始 x
     */
    private Integer x;
    /**
     * 裁剪时,起始y
     */
    private Integer y;
    /**
     * 旋转角度
     */
    private Double rotate;

    /**
     * 按照整体的缩放参数, 1 表示不变, 和裁剪一起使用
     */
    private Double radio;

    /**
     * 图片精度, 1 - 100
     */
    private Integer quality;

    /**
     * 颜色 (添加边框中的颜色; 去除图片中某颜色)
     */
    private String color;

    /**
     * 水印图片, 可以为图片名, uri, 或者inputstream
     */
    private T water;

    /**
     * 水印图片的类型
     */
    private String waterImgType;

    /**
     * 强制按照给定的参数进行压缩
     */
    private boolean forceScale;


    public boolean valid() {
        switch (operateType) {
            case CROP:
                return width != null && height != null && x != null && y != null;
            case SCALE:
                return width != null || height != null || radio != null;
            case ROTATE:
                return rotate != null;
            case WATER:
                // 暂时不支持水印操作
                return water != null;
            case BOARD:
                if (width == null) {
                    width = 3;
                }
                if (height == null) {
                    height = 3;
                }
                if (color == null) {
                    color = "#ffffff";
                }
            case FLIP:
            case FLOP:
                return true;
            default:
                return false;
        }
    }

    /**
     * 获取水印图片的路径
     *
     * @return
     */
    public String getWaterFilename() throws ImgOperateException {
        try {
            return FileWriteUtil.saveFile(water, waterImgType).getAbsFile();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


public enum OperateType {
    /**
     * 裁剪
     */
    CROP,
    /**
     * 缩放
     */
    SCALE,
    /**
     * 旋转
     */
    ROTATE,
    /**
     * 水印
     */
    WATER,

    /**
     * 上下翻转
     */
    FLIP,

    /**
     * 水平翻转
     */
    FLOP,
    /**
     * 添加边框
     */
    BOARD;
}
```

### 4. Builder实现

简化使用成本，因此针对图片裁剪、旋转等接口，封装了更友好的接口方式

```java
public static class Builder<T> {
    private T sourceFile;

    /**
     * 图片类型 JPEG, PNG, GIF ...
     * <p>
     * 默认为jpg图片
     */
    private String outputFormat = "jpg";

    private List<Operate> operates = new ArrayList<>();

    public Builder(T sourceFile) {
        this.sourceFile = sourceFile;
    }


    private static Builder<String> ofString(String str) {
        return new Builder<String>(ImgWrapper.class.getClassLoader().getResource(str).getFile());
    }


    private static Builder<URI> ofUrl(URI url) {
        return new Builder<URI>(url);
    }

    private static Builder<InputStream> ofStream(InputStream stream) {
        return new Builder<InputStream>(stream);
    }


    /**
     * 设置输出的文件格式
     *
     * @param format
     * @return
     */
    public Builder<T> setOutputFormat(String format) {
        this.outputFormat = format;
        return this;
    }


    private void updateOutputFormat(String originType) {
        if (this.outputFormat != null || originType == null) {
            return;
        }

        int index = originType.lastIndexOf(".");
        if (index <= 0) {
            return;
        }
        this.outputFormat = originType.substring(index + 1);
    }

    /**
     * 缩放
     *
     * @param width
     * @param height
     * @return
     */
    public Builder<T> scale(Integer width, Integer height, Integer quality) {
        return scale(width, height, quality, false);
    }


    public Builder<T> scale(Integer width, Integer height, Integer quality, boolean forceScale) {
        Operate operate = new Operate();
        operate.setOperateType(OperateType.SCALE);
        operate.setWidth(width);
        operate.setHeight(height);
        operate.setQuality(quality);
        operate.setForceScale(forceScale);
        operates.add(operate);
        return this;
    }

    /**
     * 按照比例进行缩放
     *
     * @param radio 1.0 表示不缩放, 0.5 缩放为一半
     * @return
     */
    public Builder<T> scale(Double radio, Integer quality) {
        Operate operate = new Operate();
        operate.setOperateType(OperateType.SCALE);
        operate.setRadio(radio);
        operate.setQuality(quality);
        operates.add(operate);
        return this;
    }


    /**
     * 裁剪
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public Builder<T> crop(int x, int y, int width, int height) {
        Operate operate = new Operate();
        operate.setOperateType(OperateType.CROP);
        operate.setWidth(width);
        operate.setHeight(height);
        operate.setX(x);
        operate.setY(y);
        operates.add(operate);
        return this;
    }


    /**
     * 旋转
     *
     * @param rotate
     * @return
     */
    public Builder<T> rotate(double rotate) {
        Operate operate = new Operate();
        operate.setOperateType(OperateType.ROTATE);
        operate.setRotate(rotate);
        operates.add(operate);
        return this;
    }

    /**
     * 上下翻转
     *
     * @return
     */
    public Builder<T> flip() {
        Operate operate = new Operate();
        operate.setOperateType(OperateType.FLIP);
        operates.add(operate);
        return this;
    }

    /**
     * 左右翻转,即镜像
     *
     * @return
     */
    public Builder<T> flop() {
        Operate operate = new Operate();
        operate.setOperateType(OperateType.FLOP);
        operates.add(operate);
        return this;
    }

    /**
     * 添加边框
     *
     * @param width  边框的宽
     * @param height 边框的高
     * @param color  边框的填充色
     * @return
     */
    public Builder<T> board(Integer width, Integer height, String color) {
        Operate args = new Operate();
        args.setOperateType(OperateType.BOARD);
        args.setWidth(width);
        args.setHeight(height);
        args.setColor(color);
        operates.add(args);
        return this;
    }

    /**
     * 添加水印
     *
     * @param water 水印的源图片 (默认为png格式)
     * @param x     添加到目标图片的x坐标
     * @param y     添加到目标图片的y坐标
     * @param <U>
     * @return
     */
    public <U> Builder<T> water(U water, int x, int y) {
        return water(water, "png", x, y);
    }

    /**
     * 添加水印
     *
     * @param water
     * @param imgType 水印图片的类型; 当传入的为inputStream时, 此参数才有意义
     * @param x
     * @param y
     * @param <U>
     * @return
     */
    public <U> Builder<T> water(U water, String imgType, int x, int y) {
        Operate<U> operate = new Operate<>();
        operate.setOperateType(OperateType.WATER);
        operate.setX(x);
        operate.setY(y);
        operate.setWater(water);
        operate.setWaterImgType(imgType);
        operates.add(operate);
        return this;
    }


    /**
     * 执行图片处理, 并保存文件为: 源文件_out.jpg （类型由输出的图片类型决定）
     *
     * @return 保存的文件名
     * @throws Exception
     */
    public String toFile() throws Exception {
        return toFile(null);
    }


    /**
     * 执行图片处理,并将结果保存为指定文件名的file
     *
     * @param outputFilename 若为null, 则输出文件为 源文件_out.jpg 这种格式
     * @return
     * @throws Exception
     */
    public String toFile(String outputFilename) throws Exception {
        if (CollectionUtils.isEmpty(operates)) {
            throw new ImgOperateException("operates null!");
        }

        /**
         * 获取原始的图片信息， 并构建输出文件名
         *  1. 远程图片，则保存到临时目录下
         *  2. stream， 保存到临时目录下
         *  3. 本地文件
         *
         * 输出文件都放在临时文件夹内，和原文件同名，加一个_out进行区分
         **/
        FileWriteUtil.FileInfo sourceFile = createFile();
        if (outputFilename == null) {
            outputFilename = FileWriteUtil.getTmpPath() + "/"
                    + sourceFile.getFilename() + "_"
                    + System.currentTimeMillis() + "_out." + outputFormat;
        }

        /** 执行图片的操作 */
        if (ImgBaseOperate.operate(operates, sourceFile.getAbsFile(), outputFilename)) {
            return outputFilename;
        } else {
            return null;
        }
    }

    /**
     * 执行图片操作,并输出字节流
     *
     * @return
     * @throws Exception
     */
    public InputStream asStream() throws Exception {
        if (CollectionUtils.isEmpty(operates)) {
            throw new ImgOperateException("operate null!");
        }

        String outputFilename = this.toFile();
        if (StringUtils.isBlank(outputFilename)) {
            return null;
        }

        return new FileInputStream(new File(outputFilename));
    }


    public byte[] asBytes() throws Exception {
        if (CollectionUtils.isEmpty(operates)) {
            throw new ImgOperateException("operate null!");
        }

        String outputFilename = this.toFile();
        if (StringUtils.isBlank(outputFilename)) {
            return null;
        }


        return BytesTool.file2bytes(outputFilename);
    }


    public BufferedImage asImg() throws Exception {
        if (CollectionUtils.isEmpty(operates)) {
            throw new ImgOperateException("operate null!");
        }

        String outputFilename = this.toFile();
        if (StringUtils.isBlank(outputFilename)) {
            return null;
        }

        return ImageIO.read(new File(outputFilename));
    }


    private FileWriteUtil.FileInfo createFile() throws Exception {
        if (this.sourceFile instanceof String) {
            /** 生成的文件在源文件目录下 */
            updateOutputFormat((String) this.sourceFile);
        } else if (this.sourceFile instanceof URI) {
            /** 源文件和生成的文件都保存在临时目录下 */
            String urlPath = ((URI) this.sourceFile).getPath();
            updateOutputFormat(urlPath);
        }

        return FileWriteUtil.saveFile(this.sourceFile, outputFormat);
    }
}
```

参数的设置相关的比较清晰，唯一需要注意的是输出`asFile()`，这个里面实现了一些有意思的东西

- 保存原图片（将网络/二进制的原图，保存到本地）
- 生成临时输出文件
- 命令执行

上面前两个，主要是借助辅助工具 FileWriteUtil实现，与主题的关联不大，但是内部东西还是很有意思的，推荐查看：

- [https://github.com/liuyueyi/quick-media/blob/master/plugins/base-plugin/src/main/java/com/github/hui/quick/plugin/base/FileWriteUtil.java](https://github.com/liuyueyi/quick-media/blob/master/plugins/base-plugin/src/main/java/com/github/hui/quick/plugin/base/FileWriteUtil.java)


命令执行的封装如下(就是解析Operate参数，翻译成对应的IMOperation)

```java
/**
 * 执行图片的复合操作
 *
 * @param operates
 * @param sourceFilename 原始图片名
 * @param outputFilename 生成图片名
 * @return
 * @throws ImgOperateException
 */
public static boolean operate(List<ImgWrapper.Builder.Operate> operates, String sourceFilename, String outputFilename) throws ImgOperateException {
    try {
        IMOperation op = new IMOperation();
        boolean operateTag = false;
        String waterFilename = null;
        for (ImgWrapper.Builder.Operate operate : operates) {
            if (!operate.valid()) {
                continue;
            }

            if (operate.getOperateType() == ImgWrapper.Builder.OperateType.CROP) {
                op.crop(operate.getWidth(), operate.getHeight(), operate.getX(), operate.getY());
//                    if (operate.getRadio() != null && Math.abs(operate.getRadio() - 1.0) > 0.005) {
//                        // 需要对图片进行缩放
//                        op.resize((int) Math.ceil(operate.getWidth() * operate.getRadio()));
//                    }
                operateTag = true;
            } else if (operate.getOperateType() == ImgWrapper.Builder.OperateType.ROTATE) {
                // fixme 180度旋转后裁图,会出现bug, 先这么兼容
                double rotate = operate.getRotate();
                if (Math.abs((rotate % 360) - 180) <= 0.005) {
                    rotate += 0.01;
                }
                op.rotate(rotate);
                operateTag = true;
            } else if (operate.getOperateType() == ImgWrapper.Builder.OperateType.SCALE) {
                if (operate.getRadio() == null) {
                    if (operate.isForceScale()) { // 强制根据给定的参数进行压缩时
                        StringBuilder builder = new StringBuilder();
                        builder.append("!").append(operate.getWidth() == null ? "" : operate.getWidth()).append("x");
                        builder.append(operate.getHeight() == null ? "" : operate.getHeight());
                        op.addRawArgs("-resize", builder.toString());
                    } else {
                        op.resize(operate.getWidth(), operate.getHeight());
                    }
                } else if(Math.abs(operate.getRadio() - 1) > 0.005) {
                    // 对图片进行比例缩放
                    op.addRawArgs("-resize", "%" + (operate.getRadio() * 100));
                }

                if (operate.getQuality() != null && operate.getQuality() > 0) {
                    op.quality(operate.getQuality().doubleValue());
                }
                operateTag = true;
            } else if (operate.getOperateType() == ImgWrapper.Builder.OperateType.FLIP) {
                op.flip();
                operateTag = true;
            } else if (operate.getOperateType() == ImgWrapper.Builder.OperateType.FLOP) {
                op.flop();
                operateTag = true;
            } else if (operate.getOperateType() == ImgWrapper.Builder.OperateType.WATER && waterFilename == null) {
                // 当前只支持添加一次水印
                op.geometry(operate.getWidth(), operate.getHeight(), operate.getX(), operate.getY())
                        .composite();
                waterFilename = operate.getWaterFilename();
                operateTag = true;
            } else if (operate.getOperateType() == ImgWrapper.Builder.OperateType.BOARD) {
                op.border(operate.getWidth(), operate.getHeight()).bordercolor(operate.getColor());
                operateTag = true;
            }
        }

        if (!operateTag) {
            throw new ImgOperateException("operate illegal! operates: " + operates);
        }
        op.addImage(sourceFilename);
        if (waterFilename != null) {
            op.addImage(waterFilename);
        }
        op.addImage(outputFilename);
        /** 传true到构造函数中,则表示使用GraphicMagic, 裁图时,图片大小会变 */
        ConvertCmd convert = new ConvertCmd();
        convert.run(op);
    } catch (IOException e) {
        log.error("file read error!, e: {}", e);
        return false;
    } catch (InterruptedException e) {
        log.error("interrupt exception! e: {}", e);
        return false;
    } catch (IM4JavaException e) {
        log.error("im4java exception! e: {}", e);
        return false;
    }
    return true;
}
```

### 5. 接口封装

包装一个对外使用的方式

```java
public class ImgWrapper {
    /**
     * 根据本地图片进行处理
     *
     * @param file
     * @return
     */
    public static Builder<String> of(String file) {
        checkForNull(file, "Cannot specify null for input file.");
        if (file.startsWith("http")) {
            throw new IllegalArgumentException("file should not be URI resources! file: " + file);
        }
        return Builder.ofString(file);
    }

    public static Builder<URI> of(URI uri) {
        checkForNull(uri, "Cannot specify null for input uri.");
        return Builder.ofUrl(uri);
    }

    public static Builder<InputStream> of(InputStream inputStream) {
        checkForNull(inputStream, "Cannot specify null for InputStream.");
        return Builder.ofStream(inputStream);
    }


    private static void checkForNull(Object o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
    }
}
```

## IV. 测试

上面基本上完成了整个接口的设计与实现，接下来就是接口测试了


给出几个使用姿势演示，更多可以查看：[ImgWrapperTest](https://github.com/liuyueyi/quick-media/blob/master/plugins/imagic-plugin/src/test/java/com/github/hui/quick/plugin/test/ImgWrapperTest.java)

```java
private static final String url = "http://a.hiphotos.baidu.com/image/pic/item/14ce36d3d539b6006a6cc5d0e550352ac65cb733.jpg";
private static final String localFile = "blogInfoV2.png";

@Test
public void testCutImg() {

    try {
        // 保存到本地
        ImgWrapper.of(URI.create(url))
                .crop(10, 20, 500, 500)
                .toFile();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


@Test
public void testRotateImg() {
    try {
        InputStream stream = FileReadUtil.getStreamByFileName(localFile);
        BufferedImage img = ImgWrapper.of(stream).rotate(90).asImg();
        System.out.println("----" + img);
    } catch (Exception e) {
        e.printStackTrace();
    }
}


@Test
public void testWater() {
    BufferedImage img;
    try {
        img = ImgWrapper.of(URI.create(url))
                .board(10, 10, "red")
                .water(localFile, 100, 100)
                .asImg();
        System.out.println("--- " + img);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

## V. 其他

### 项目：

**GitHub:**

- 项目：[Quick-Media](https://github.com/liuyueyi/quick-media)
- 源码：[imagic-plugin](https://github.com/liuyueyi/quick-media/tree/master/plugins/imagic-plugin)

**Gitee:**

- 项目：[Quick-Media](https://gitee.com/liuyueyi/quick-media)
- 源码：[imagic-plugin](https://gitee.com/liuyueyi/quick-media/tree/master/plugins/imagic-plugin)


### 个人博客： [一灰灰Blog](https://liuyueyi.github.io/hexblog)

基于hexo + github pages搭建的个人博客，记录所有学习和工作中的博文，欢迎大家前去逛逛


### 声明

尽信书则不如，已上内容，纯属一家之言，因本人能力一般，见识有限，如发现bug或者有更好的建议，随时欢迎批评指正

- 微博地址: [小灰灰Blog](https://weibo.com/p/1005052169825577/home)
- QQ： 一灰灰/3302797840

### 扫描关注

![QrCode](https://raw.githubusercontent.com/liuyueyi/Source/master/img/info/blogInfoV2.png)
