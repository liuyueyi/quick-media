## image-plugin

提供图片的各种操作封装类，目前支持功能：

- [x] 长图文生成
   - [水平文字，上下布局长图文生成](doc/images/imgGenV1.md)
   - [垂直文字，左右布局长图文生成](doc/images/imgGenV2.md)
   - 第三方字体支持
- [x] gif图生成
- [x] 合成
    - [图片合成支持](doc/images/imgMerge.md)
- [x] 水印
- [x] svg渲染
- [ ] 裁剪
- [ ] 压缩
- [ ] 旋转
- [ ] 缩放
- [ ] 格式转换


### 1. 依赖

外部如需使用，请引入下面的源

```xml
<repositories>
    <repository>
        <id>yihui-maven-repo</id>
        <url>https://raw.githubusercontent.com/liuyueyi/maven-repository/master/repository</url>
    </repository>
</repositories>
```

jar包引入，请注意使用最新的版本

```xml
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>image-plugin</artifactId>
    <version>2.5.2</version>
</dependency>
```

### 2. 使用说明

目前提供了都中图文生成方式，可以通过测试包下的case来查看使用说明


#### a. ImgCreateWrapper
 
 图文生成封装类，提供了多重生成功能的方式，生成条件参数
 
```java
/**
 * 绘制的背景图
 */
private BufferedImage bgImg;


/**
 * 生成图片的宽
 */
private Integer imgW;


/**
 * 生成图片的高
 */
private Integer imgH;


/**
 * 文字对应的字体
 */
private Font font = DEFAULT_FONT;


/**
 * 字体色
 */
private Color fontColor = Color.BLACK;


/**
 * 左边距
 */
private int leftPadding;

/**
 * 右边距
 */
private int rightPadding;

/**
 * 上边距
 */
private int topPadding;

/**
 * 底边距
 */
private int bottomPadding;

/**
 * 行距
 */
private int linePadding;


/**
 * 对齐方式
 *
 * 水平绘制时: 左对齐，居中， 右对齐
 * 垂直绘制时: 上对齐，居中，下对齐
 *
 */
private AlignStyle alignStyle;


/**
 * 文本绘制方式， 水平or垂直
 */
private DrawStyle drawStyle;
```

一个简单的使用姿势，详情查看: [ImgCreateWrapperTest.java](https://github.com/liuyueyi/quick-media/blob/master/plugins/image-plugin/src/test/java/com/github/hui/quick/plugin/test/ImgCreateWrapperTest.java)

```java
public void testLocalGenImg() throws IOException {
    int w = 400;
    int leftPadding = 10;
    int topPadding = 20;
    int bottomPadding = 10;
    int linePadding = 10;
    Font font = new Font("手札体", Font.PLAIN, 18);

    ImgCreateWrapper.Builder build = ImgCreateWrapper.build()
            .setImgW(w)
            .setLeftPadding(leftPadding)
            .setRightPadding(leftPadding)
            .setTopPadding(topPadding)
            .setBottomPadding(bottomPadding)
            .setLinePadding(linePadding)
            .setFont(font)
            .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
            .setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL)
            .setBgColor(Color.WHITE)
            .setBorder(true)
            .setBorderColor(0xFFF7EED6);


    BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt");
    String line;
    while ((line = reader.readLine()) != null) {
        build.drawContent(line);
    }

    build.setAlignStyle(ImgCreateOptions.AlignStyle.RIGHT)
            .drawImage("https://gitee.com/liuyueyi/Source/raw/master/img/info/blogInfoV2.png");

    BufferedImage img = build.asImage();
    ImageIO.write(img, "png", new File("/Users/yihui/Desktop/2out.png"));
}
```


#### b. LineGifCreateWrapper

逐行打印文本，主要参数同上，使用姿势可以参考: [LineCreateWrapperTest](https://github.com/liuyueyi/quick-media/blob/master/plugins/image-plugin/src/test/java/com/github/hui/quick/plugin/test/LineCreateWrapperTest.java)

一个使用示意方式

```java
private static final String sign = "https://gitee.com/liuyueyi/Source/raw/master/img/info/blogInfoV2.png";

@Test
public void genVerticalImg() throws IOException, FontFormatException {
    int h = 500;
    int leftPadding = 10;
    int topPadding = 10;
    int bottomPadding = 10;
    int linePadding = 10;

    LineGifCreateWrapper.Builder build = (LineGifCreateWrapper.Builder) LineGifCreateWrapper.build()
            .setImgH(h)
            .setDrawStyle(ImgCreateOptions.DrawStyle.VERTICAL_RIGHT)
            .setLeftPadding(leftPadding)
            .setTopPadding(topPadding)
            .setBottomPadding(bottomPadding)
            .setLinePadding(linePadding)
            .setFont(FontUtil.DEFAULT_FONT)
            .setFontColor(Color.BLUE)
            .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
            .setBgColor(Color.WHITE)
            .setBorder(true)
            .setBorderColor(0xFFF7EED6);


    BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt");
    String line;
    while ((line = reader.readLine()) != null) {
        build.drawContent(line);
    }

    build.setAlignStyle(ImgCreateOptions.AlignStyle.BOTTOM)
            .drawImage(sign);

    build.asGif("/Users/yihui/Desktop/out.gif");
//        String str = build.asString();
//
//        String dom = "<img src=\"" + DomUtil.toDomSrc(str, MediaType.ImageGif) + "\"/>";
//        System.out.println(dom);
}
```

#### c. WordGifCreateWrapper

逐字输出，生成gif动画，详细使用参考： [LineCreateWrapperTest](https://github.com/liuyueyi/quick-media/blob/master/plugins/image-plugin/src/test/java/com/github/hui/quick/plugin/test/LineCreateWrapperTest.java)

一个简单的case如下:

```java
@Test
public void testWordGif() throws IOException {
    int h = 300;
    int leftPadding = 10;
    int topPadding = 10;
    int bottomPadding = 10;
    int linePadding = 10;

    WordGifCreateWrapper.Builder build = (WordGifCreateWrapper.Builder) WordGifCreateWrapper.build()
            .setDelay(100)
            .setImgH(h)
            .setImgW(h)
            .setDrawStyle(ImgCreateOptions.DrawStyle.VERTICAL_RIGHT)
            .setLeftPadding(leftPadding)
            .setTopPadding(topPadding)
            .setBottomPadding(bottomPadding)
            .setLinePadding(linePadding)
            .setFont(FontUtil.DEFAULT_FONT)
            .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
            .setBgColor(Color.WHITE)
            .setBorder(true)
            .setBorderColor(0xFFF7EED6)
            ;


    BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt");
    String line;
    while ((line = reader.readLine()) != null) {
        build.drawContent(line);
    }

    build.drawContent(" ");

    build.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
            .drawImage(sign);

    build.asGif("/Users/yihui/Desktop/out_word.gif");
}
```

#### d. ImgMergeWrapper

图片合成，目前提供一个生成个人签名的模板，生成如下格式的图

![img](https://raw.githubusercontent.com/liuyueyi/quick-media/master/doc/img/qrcode/wxBlog.jpg)


使用姿势就比较简单了，详细使用可以参考: [ImgMergeWrapperTest](https://github.com/liuyueyi/quick-media/blob/master/plugins/image-plugin/src/test/java/com/github/hui/quick/plugin/test/ImgMergeWrapperTest.java)

```java
@Test
public void testTemplate() throws IOException {
    BufferedImage logo = ImageLoadUtil.getImageByPath("logo.jpg");
    BufferedImage qrCode = ImageLoadUtil.getImageByPath("QrCode.jpg");
    String name = "小灰灰Blog";
    List<String> desc = Arrays.asList("我是一灰灰，一匹不吃羊的狼", "专注码农技术分享");

    int w = QrCodeCardTemplate.w, h = QrCodeCardTemplate.h;
    List<IMergeCell> list = QrCodeCardTemplateBuilder.build(logo, name, desc, qrCode, "微 信 公 众 号");

    BufferedImage bg = ImgMergeWrapper.merge(list, w, h);

    try {
        ImageIO.write(bg, "jpg", new File("/Users/yihui/Desktop/merge.jpg"));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```


#### e. WaterMarkWrapper

水印封装类，详细使用可以参考: [WaterMarkWrapperTest](https://github.com/liuyueyi/quick-media/blob/master/plugins/image-plugin/src/test/java/com/github/hui/quick/plugin/test/WaterMarkWrapperTest.java)

一个简单的演示case

```java
@Test
public void testWaterMark() {
        try {
            BufferedImage img = WaterMarkWrapper.of("bg.png")
                    .setInline(true)
                    .setWaterLogo("xcx.jpg")
                    .setWaterLogoHeight(50)
                    .setWaterInfo(" 图文小工具\n By 小灰灰Blog")
                    .setStyle(WaterMarkOptions.WaterStyle.FILL_BG)
                    .setWaterColor(Color.LIGHT_GRAY)
                    .setWaterOpacity(0.8f)
                    .setRotate(45)
                    .setPaddingX(80)
                    .setPaddingX(80)
                    .build()
                    .asImage();

            ImageIO.write(img, "jpg", new File("/Users/yihui/Desktop/FILL_BG.jpg"));
            System.out.println(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
}
```
