# markdown 转 image

> 前段时间实现了长图文生成的基本功能，然后想了下能否有个进阶版，直接将markdown生成渲染后的图片呢？

## 思路
> 有不少的库可以将 markdown 转为 html，那么这个需求就可以转为 html转Image了

### 1. markdown 转 html

可以参看之前的博文[《Java 实现 markdown转Html》](https://my.oschina.net/u/566591/blog/1535380)

### 2. html 转 图片

主要的核心问题就在这里了，如何实现html转图片？

- 直接实现html转图片的包没怎么见，看到一个 `html2image`, 还不太好用
- 在 AWT or Swing 的Panel上显示网页，在把Panel输出为 image 文件
- 使用js相关技术实现转换

本篇博文具体实现以 `html2image` 的实现逻辑作为参考，然后定制实现一把（后面有机会写一篇利用js来实现html转图片的博文）

#### html2image 的实现原理

`html2image` 基本上没啥维护了，内部主要是利用了 `xhtmlrender` 实现html渲染为图片

```java
Graphics2DRenderer renderer = new Graphics2DRenderer();
// 设置渲染内容
renderer.setDocument(document, document.getDocumentURI());

// 获取Graphics2D
graphics2D = bufferedImage.createGraphics();
renderer.layout(graphics2D, dimension);

// 内容渲染
renderer.render(graphics2D);
```

#### 说明

1. 为什么并不直接使用 `java-html2image` ?

  - 因为有些定制的场景支持得不太友好，加上源码也比较简单，所以干脆站在前人的基础上进行拓展

2. 设计目标（这里指html转图片的功能）

  - 生成图片的宽可指定
  - 支持对线上网页进行转图片
  - 支持对html中指定的区域进行转换
  - css样式渲染支持


## 实现

> 本篇先会先实现一个基本的功能，即读去`markdown`文档, 并转为一张图片


### 1. markdown 转 html 封装
> 利用之前封装的 `MarkDown2HtmlWrapper` 工具类

具体实现逻辑参考项目工程，和markdown转html博文

### 2. html 转 image

#### 参数配置项 `HtmlRenderOptions`

**注意**

- html 为 Document 属性
- autoW, autoH 用于控制是否自适应html实际的长宽

```java
@Data
public class HtmlRenderOptions {

    /**
     * 输出图片的宽
     */
    private Integer w;


    /**
     * 输出图片的高
     */
    private Integer h;


    /**
     * 是否自适应宽
     */
    private boolean autoW;


    /**
     * 是否自适应高
     */
    private boolean autoH;


    /**
     * 输出图片的格式
     */
    private String outType;

    /**
     * html相关内容
     */
    private Document document;
}
```

### 封装处理类
> 同样采用Builder模式来进行配置项设置

```java
public class Html2ImageWrapper {

    private static DOMParser domParser;

    static {
        domParser = new DOMParser(new HTMLConfiguration());
        try {
            domParser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
        } catch (Exception e) {
            throw new RuntimeException("Can't create HtmlParserImpl", e);
        }
    }


    private HtmlRenderOptions options;


    private Html2ImageWrapper(HtmlRenderOptions options) {
        this.options = options;
    }


    private static Document parseDocument(String content) throws Exception {
        domParser.parse(new InputSource(new StringReader(content)));
        return domParser.getDocument();
    }


    public static Builder of(String html) {
        return new Builder().setHtml(html);
    }


    public static Builder ofMd(MarkdownEntity entity) {
        return new Builder().setHtml(entity);
    }


    public BufferedImage asImage() {
        BufferedImage bf = HtmlRender.parseImage(options);
        return bf;
    }


    public boolean asFile(String absFileName) throws IOException {
        File file = new File(absFileName);
        FileUtil.mkDir(file);

        BufferedImage bufferedImage = asImage();
        if (!ImageIO.write(bufferedImage, options.getOutType(), file)) {
            throw new IOException("save image error!");
        }

        return true;
    }


    public String asString() throws IOException {
        BufferedImage img = asImage();
        return Base64Util.encode(img, options.getOutType());
    }


    @Getter
    public static class Builder {
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
         * 输出图片的格式
         */
        private String outType = "jpg";


        /**
         * 待转换的html内容
         */
        private MarkdownEntity html;


        public Builder setW(Integer w) {
            this.w = w;
            return this;
        }

        public Builder setH(Integer h) {
            this.h = h;
            return this;
        }

        public Builder setAutoW(boolean autoW) {
            this.autoW = autoW;
            return this;
        }

        public Builder setAutoH(boolean autoH) {
            this.autoH = autoH;
            return this;
        }

        public Builder setOutType(String outType) {
            this.outType = outType;
            return this;
        }


        public Builder setHtml(String html) {
            this.html = new MarkdownEntity();
            return this;
        }


        public Builder setHtml(MarkdownEntity html) {
            this.html = html;
            return this;
        }

        public Html2ImageWrapper build() throws Exception {
            HtmlRenderOptions options = new HtmlRenderOptions();
            options.setW(w);
            options.setH(h);
            options.setAutoW(autoW);
            options.setAutoH(autoH);
            options.setOutType(outType);


            if (fontColor != null) {
                html.addDivStyle("style", "color:" + options.getFontColor());
            }
            html.addDivStyle("style", "width:" + w + ";");
            html.addWidthCss("img");
            html.addWidthCss("code");

            options.setDocument(parseDocument(html.toString()));

            return new Html2ImageWrapper(options);
        }

    }
}
```

上面的实现，有个需要注意的地方

**如何将html格式的字符串，转为 Document 对象**

利用了开源工具 `nekohtml`, 可以较好的实现html标签解析，看一下`DOMParse` 的初始化过程


```java
private static DOMParser domParser;

static {
    domParser = new DOMParser(new HTMLConfiguration());
    try {
        domParser.setProperty("http://cyberneko.org/html/properties/names/elems", 
        "lower");
    } catch (Exception e) {
        throw new RuntimeException("Can't create HtmlParserImpl", e);
    }
}
```

try语句块中的内容并不能缺少，否则最终的样式会错乱，关于 `nekohtml` 的使用说明，可以查阅相关教程


上面的封装，主要是`HtmlRenderOptions`的构建，主要的渲染逻辑则在下面

### 渲染

利用 `xhtmlrenderer` 实现html的渲染

- 宽高的自适应
- 图片的布局，内容渲染

```java
public class HtmlRender {

    /**
     * 输出图片
     *
     * @param options
     * @return
     */
    public static BufferedImage parseImage(HtmlRenderOptions options) {
        int width = options.getW();
        int height = options.getH() == null ? 1024 : options.getH();
        Graphics2DRenderer renderer = new Graphics2DRenderer();
        renderer.setDocument(options.getDocument(), options.getDocument().getDocumentURI());


        Dimension dimension = new Dimension(width, height);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = GraphicUtil.getG2d(bufferedImage);

        // 自适应修改生成图片的宽高
        if (options.isAutoH() || options.getH() == null) {
            // do layout with temp buffer
            renderer.layout(graphics2D, new Dimension(width, height));
            graphics2D.dispose();

            Rectangle size = renderer.getMinimumSize();
            final int autoWidth = options.isAutoW() ? (int) size.getWidth() : width;
            final int autoHeight = (int) size.getHeight();
            bufferedImage = new BufferedImage(autoWidth, autoHeight, BufferedImage.TYPE_INT_RGB);
            dimension = new Dimension(autoWidth, autoHeight);

            graphics2D = GraphicUtil.getG2d(bufferedImage);
        }


        renderer.layout(graphics2D, dimension);
        renderer.render(graphics2D);
        graphics2D.dispose();
        return bufferedImage;
    }
}
```

## 测试

```java
@Test
public void testParse() throws Exception {
    String file = "md/tutorial.md";

    MarkdownEntity html = MarkDown2HtmlWrapper.ofFile(file);

    BufferedImage img = Html2ImageWrapper.ofMd(html)
            .setW(600)
            .setAutoW(false)
            .setAutoH(true)
            .setOutType("jpg")
            .build()
            .asImage();

    ImageIO.write(img, "jpg", new File("/Users/yihui/Desktop/md.jpg"));
}
```

输出图片

![out.jpg](https://camo.githubusercontent.com/90dc0e400056650e581439945f82a193d4ae9f17/687474703a2f2f75706c6f61642d696d616765732e6a69616e7368752e696f2f75706c6f61645f696d616765732f313430353933362d336164353064323937386533643465302e6a70673f696d6167654d6f6772322f6175746f2d6f7269656e742f7374726970253743696d61676556696577322f322f772f31323430)

然后演示一个对项目中实际的教程文档输出图片的动态示意图， 因为生成的图片特别特别长，所以就不贴输出的图片了，有兴趣的同学可以下载工程，实际跑一下看看

源markdown文件地址:

[https://github.com/liuyueyi/quick-media/blob/master/doc/images/imgGenV2.md](https://github.com/liuyueyi/quick-media/blob/master/doc/images/imgGenV2.md)


![show.gif](https://camo.githubusercontent.com/113c4e98f0dc2d149d74d23fca154368bfb7f7aa/687474703a2f2f75706c6f61642d696d616765732e6a69616e7368752e696f2f75706c6f61645f696d616765732f313430353933362d613734393739653762303736623335322e6769663f696d6167654d6f6772322f6175746f2d6f7269656e742f7374726970)


## 参考博文

- [Java 实现HTML 页面转成image 图片](https://www.2cto.com/kf/201303/196946.html)