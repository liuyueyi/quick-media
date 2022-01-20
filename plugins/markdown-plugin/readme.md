## markdown-plugin

实现将markdonw渲染输出图片的功能，主要借助开源库 `flexmark` 和 `xhtmlrenderer`来实现，目前支持自定义css样式

这个插件，主要提供了两个功能

- [markdown 转 image](https://github.com/liuyueyi/quick-media/blob/master/doc/md/md2html.md) 
- [html 转图片](https://github.com/liuyueyi/quick-media/blob/master/doc/md/html2image.md)

### 使用说明

#### 1. 依赖

首先第一步添加依赖：

```pom
<repositories>
    <repository>
        <id>yihui-maven-repo</id>
        <url>https://raw.githubusercontent.com/liuyueyi/maven-repository/master/repository</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>markdown-plugin</artifactId>
    <version>1.3</version>
</dependency>
```

#### 2. markdown 转 图片使用示例

然后开始使用，下面演示根据本地md文档，渲染输出图片，然后保存在本地的case，（可以直接查看test下面的 `Md2ImgTest`，然后执行本地测试）

```java
@Test
public void testMd2Img() throws Exception {
    MarkdownEntity entity = MarkDown2HtmlWrapper.ofFile("md/test.md");
    BufferedImage bf = Html2ImageWrapper.ofMd(entity)
            .setW(800)
            .setAutoW(false)
            .setAutoH(true)
            .setOutType("jpg")
            /*
            在其他平台最好设置一个通用字体, 否则可能会出现由于字体字库不全而导致的乱码问题
            如在win下设置为'微软雅黑'
            .setFontFamily("微软雅黑")
             */
            .build()
            .asImage();
    ImageIO.write(bf, "jpg", new File("test_out.jpg"));
    System.out.println("---over---");
}
```

输出结果直接放在项目中，可以点击查看 [md2img输出图片](md_out.jpg)


#### 3. html 转图片使用示例

同样可以在test下面的 `Html2ImgTest` 查看使用case，下面给出几种常见的使用姿势

**html 字符串渲染**

```java
@Test
public void testHtmp2Img() throws Exception {
    String html = "<html>\n" + "<body>\n" + "\n" + "<span> hello world </span>\n" + "<hr/>\n" +
            "<button> 按钮 </button>\n" + "\n" + "</body>\n" + "</html>";
    BufferedImage img =
            Html2ImageWrapper.of(html).setAutoW(false).setAutoH(true).setOutType("jpg").build().asImage();
    System.out.println(img);
}
```

**html文件渲染**

针对整个html文件进行渲染，css也写在html中

```java
/**
 * 完整的html文档, 希望直接进行渲染输出图片，使用 ofDoc 方式
 *
 * @throws Exception
 */
@Test
public void testFullHtml() throws Exception {
    String html = FileReadUtil.readAll("html/full.html");
    BufferedImage img = Html2ImageWrapper.ofDoc(html).setOutType("jpg").build().asImage();
    System.out.println(img);
}
```

**html + css文件渲染**

html和css文件隔离的情况

```java
/**
 * css 文件和html文件分开的情况
 *
 * @throws Exception
 */
@Test
public void testParseHtml2Img() throws Exception {
    String html = FileReadUtil.readAll("html/demo.html");

    String MD_CSS = FileReadUtil.readAll("html/demo.css");
    MD_CSS = "<style type=\"text/css\">\n" + MD_CSS + "\n</style>\n";

    BufferedImage img =
            Html2ImageWrapper.of(html).setCss(MD_CSS).setAutoW(false).setAutoH(false).setOutType("jpg").build()
                    .asImage();
    System.out.println(img);
}
```

#### 3. 请求参数说明

上面的设置参数，主要包含以下几个，查看 `com.github.hui.quick.plugin.md.Html2ImageWrapper.Builder` 这个内部类了解详情

```java
/**
 * 输出图片的宽
 */
private Integer w = 600;

/**
 * 输出图片的高度
 */
private Integer h;

/**
 * true，根据网页的实际宽渲染；
 * false， 则根据指定的宽进行渲染
 */
private boolean autoW = true;

/**
 * true，根据网页的实际高渲染；
 * false， 则根据指定的高进行渲染
 */
private boolean autoH = false;


/**
 * 输出图片的格式, 可以为 jpg, png...
 */
private String outType = "jpg";


/**
 * 待转换的html内容
 */
private MarkdownEntity html;

/**
 * 样式内容
 */
private String css;

/**
 * 字体 
 */
private Font font = new Font("宋体", Font.PLAIN, 18);

/**
 * 字体颜色 
 */
private Integer fontColor;

/**
 * 如果想直接渲染整个html，则直接设置这个即可
 */
private Document document;
```