# Java实现长图文生成
> 很久很久以前，就觉得微博的长图文实现得非常有意思，将排版直接以最终的图片输出，收藏查看分享都很方便，现在则自己动手实现一个简单版本的

## 目标

首先定义下我们预期达到的目标：根据文字 + 图片生成长图文

### 目标拆解

- 支持大段文字生成图片
- 支持插入图片
- 支持上下左右边距设置
- 支持字体选择
- 支持字体颜色
- 支持左对齐，居中，右对齐


### 预期结果

我们将通过spring-boot搭建一个生成长图文的http接口，通过传入参数来指定各种配置信息，下面是一个最终调用的示意图

![演示图](https://static.oschina.net/uploads/img/201708/18180654_y9iv.gif "演示图")


## 设计&实现

> 长图文的生成，采用awt进行文字绘制和图片绘制


### 1. 参数选项 `ImgCreateOptions`

根据我们的预期目标，设定配置参数，基本上会包含以下参数

```java
@Getter
@Setter
@ToString
public class ImgCreateOptions {

    /**
     * 绘制的背景图
     */
    private BufferedImage bgImg;


    /**
     * 生成图片的宽
     */
    private Integer imgW;


    private Font font = new Font("宋体", Font.PLAIN, 18);

    /**
     * 字体色
     */
    private Color fontColor = Color.BLACK;


    /**
     * 两边边距
     */
    private int leftPadding;

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


    private AlignStyle alignStyle;

    /**
     * 对齐方式
     */
    public enum AlignStyle {
        LEFT,
        CENTER,
        RIGHT;


        private static Map<String, AlignStyle> map = new HashMap<>();

        static {
            for(AlignStyle style: AlignStyle.values()) {
                map.put(style.name(), style);
            }
        }


        public static AlignStyle getStyle(String name) {
            name = name.toUpperCase();
            if (map.containsKey(name)) {
                return map.get(name);
            }

            return LEFT;
        }
    }
}
```


### 2. 封装类 `ImageCreateWrapper`

封装配置参数的设置，绘制文本，绘制图片的操作方式，输出样式等接口

```java
public class ImgCreateWrapper {


    public static Builder build() {
        return new Builder();
    }


    public static class Builder {
        /**
         * 生成的图片创建参数
         */
        private ImgCreateOptions options = new ImgCreateOptions();


        /**
         * 输出的结果
         */
        private BufferedImage result;


        private final int addH = 1000;


        /**
         * 实际填充的内容高度
         */
        private int contentH;


        private Color bgColor;

        public Builder setBgColor(int color) {
            return setBgColor(ColorUtil.int2color(color));
        }

        /**
         * 设置背景图
         *
         * @param bgColor
         * @return
         */
        public Builder setBgColor(Color bgColor) {
            this.bgColor = bgColor;
            return this;
        }


        public Builder setBgImg(BufferedImage bgImg) {
            options.setBgImg(bgImg);
            return this;
        }


        public Builder setImgW(int w) {
            options.setImgW(w);
            return this;
        }

        public Builder setFont(Font font) {
            options.setFont(font);
            return this;
        }

        public Builder setFontName(String fontName) {
            Font font = options.getFont();
            options.setFont(new Font(fontName, font.getStyle(), font.getSize()));
            return this;
        }


        public Builder setFontColor(int fontColor) {
            return setFontColor(ColorUtil.int2color(fontColor));
        }

        public Builder setFontColor(Color fontColor) {
            options.setFontColor(fontColor);
            return this;
        }

        public Builder setFontSize(Integer fontSize) {
            Font font = options.getFont();
            options.setFont(new Font(font.getName(), font.getStyle(), fontSize));
            return this;
        }

        public Builder setLeftPadding(int leftPadding) {
            options.setLeftPadding(leftPadding);
            return this;
        }

        public Builder setTopPadding(int topPadding) {
            options.setTopPadding(topPadding);
            contentH = topPadding;
            return this;
        }

        public Builder setBottomPadding(int bottomPadding) {
            options.setBottomPadding(bottomPadding);
            return this;
        }

        public Builder setLinePadding(int linePadding) {
            options.setLinePadding(linePadding);
            return this;
        }

        public Builder setAlignStyle(String style) {
            return setAlignStyle(ImgCreateOptions.AlignStyle.getStyle(style));
        }

        public Builder setAlignStyle(ImgCreateOptions.AlignStyle alignStyle) {
            options.setAlignStyle(alignStyle);
            return this;
        }


        public Builder drawContent(String content) {
            // xxx
            return this;
        }


        public Builder drawImage(String img) {
            BufferedImage bfImg;
            try {
                 bfImg = ImageUtil.getImageByPath(img);
            } catch (IOException e) {
                log.error("load draw img error! img: {}, e:{}", img, e);
                throw new IllegalStateException("load draw img error! img: " + img, e);
            }

            return drawImage(bfImg);
        }


        public Builder drawImage(BufferedImage bufferedImage) {

           // xxx
           return this;
        }


        public BufferedImage asImage() {
            int realH = contentH + options.getBottomPadding();

            BufferedImage bf = new BufferedImage(options.getImgW(), realH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bf.createGraphics();

            if (options.getBgImg() == null) {
                g2d.setColor(bgColor == null ? Color.WHITE : bgColor);
                g2d.fillRect(0, 0, options.getImgW(), realH);
            } else {
                g2d.drawImage(options.getBgImg(), 0, 0, options.getImgW(), realH, null);
            }

            g2d.drawImage(result, 0, 0, null);
            g2d.dispose();
            return bf;
        }


        public String asString() throws IOException {
            BufferedImage img = asImage();
            return Base64Util.encode(img, "png");
        }
}
```


上面具体的文本和图片绘制实现没有，后面详细讲解，这里主要关注的是一个参数 `contentH`， 表示实际绘制的内容高度（包括上边距），因此最终生成图片的高度应该是

`int realH = contentH + options.getBottomPadding();`


其次简单说一下上面的图片输出方法：`com.hust.hui.quickmedia.common.image.ImgCreateWrapper.Builder#asImage`

- 计算最终生成图片的高度（宽度由输入参数指定）
- 绘制背景（如果没有背景图片，则用纯色填充）
- 绘制实体内容（即绘制的文本，图片）


### 3. 内容填充 `GraphicUtil`
> 具体的内容填充，区分为文本绘制和图片绘制

#### 设计

1. 考虑到在填充的过程中，可以自由设置字体，颜色等，所以在我们的绘制方法中，直接实现掉内容的绘制填充，即 `drawXXX` 方法真正的实现了内容填充，执行完之后，内容已经填充到画布上了

2. 图片绘制，考虑到图片本身大小和最终结果的大小可能有冲突，采用下面的规则
  - 绘制图片宽度 <=（指定生成图片宽 - 边距），全部填充
  - 绘制图片宽度 >（指定生成图片宽 - 边距），等比例缩放绘制图片

3. 文本绘制，换行的问题
  - 每一行允许的文本长度有限，超过时，需要自动换行处理


#### 文本绘制

考虑基本的文本绘制，流程如下

- 创建`BufferImage`对象
- 获取`Graphic2d`对象，操作绘制
- 设置基本配置信息
- 文本按换行进行拆分为字符串数组, 循环绘制单行内容
  - 计算当行字符串，实际绘制的行数，然后进行拆分
  - 依次绘制文本（需要注意y坐标的变化）



下面是具体的实现

```java
public static int drawContent(Graphics2D g2d,
                                  String content,
                                  int y,
                                  ImgCreateOptions options) {

    int w = options.getImgW();
    int leftPadding = options.getLeftPadding();
    int linePadding = options.getLinePadding();
    Font font = options.getFont();


    // 一行容纳的字符个数
    int lineNum = (int) Math.floor((w - (leftPadding << 1)) / (double) font.getSize());

    // 对长串字符串进行分割成多行进行绘制
    String[] strs = splitStr(content, lineNum);

    g2d.setFont(font);

    g2d.setColor(options.getFontColor());
    int index = 0;
    int x;
    for (String tmp : strs) {
        x = calOffsetX(leftPadding, w, tmp.length() * font.getSize(), options.getAlignStyle());
        g2d.drawString(tmp, x, y + (linePadding + font.getSize()) * index);
        index++;
    }


    return y + (linePadding + font.getSize()) * (index);
}

/**
 * 计算不同对其方式时，对应的x坐标
 *
 * @param padding 左右边距
 * @param width   图片总宽
 * @param strSize 字符串总长
 * @param style   对其方式
 * @return 返回计算后的x坐标
 */
private static int calOffsetX(int padding,
                              int width,
                              int strSize,
                              ImgCreateOptions.AlignStyle style) {
    if (style == ImgCreateOptions.AlignStyle.LEFT) {
        return padding;
    } else if (style == ImgCreateOptions.AlignStyle.RIGHT) {
        return width - padding - strSize;
    } else {
        return (width - strSize) >> 1;
    }
}


/**
 * 按照长度对字符串进行分割
 * <p>
 * fixme 包含emoj表情时，兼容一把
 *
 * @param str      原始字符串
 * @param splitLen 分割的长度
 * @return
 */
public static String[] splitStr(String str, int splitLen) {
    int len = str.length();
    int size = (int) Math.ceil(len / (float) splitLen);

    String[] ans = new String[size];
    int start = 0;
    int end = splitLen;
    for (int i = 0; i < size; i++) {
        ans[i] = str.substring(start, end > len ? len : end);
        start = end;
        end += splitLen;
    }

    return ans;
}
```


上面的实现比较清晰了，图片的绘制则更加简单

#### 图片绘制

只需要重新计算下待绘制图片的宽高即可，具体实现如下


```java
/**
 * 在原图上绘制图片
 *
 * @param source  原图
 * @param dest    待绘制图片
 * @param y       待绘制的y坐标
 * @param options
 * @return 绘制图片的高度
 */
public static int drawImage(BufferedImage source,
                            BufferedImage dest,
                            int y,
                            ImgCreateOptions options) {
    Graphics2D g2d = getG2d(source);
    int w = Math.min(dest.getWidth(), options.getImgW() - (options.getLeftPadding() << 1));
    int h = w * dest.getHeight() / dest.getWidth();

    int x = calOffsetX(options.getLeftPadding(),
            options.getImgW(), w, options.getAlignStyle());

    // 绘制图片
    g2d.drawImage(dest,
            x,
            y + options.getLinePadding(),
            w,
            h,
            null);
    g2d.dispose();

    return h;
}

public static Graphics2D getG2d(BufferedImage bf) {
        Graphics2D g2d = bf.createGraphics();

    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

    return g2d;
}
```


### 4. 内容渲染
前面只是给出了单块内容（如一段文字，一张图片）的渲染，存在一些问题

- 绘制的内容超过画布的高度如何处理
- 文本绘制要求传入的文本没有换行符，否则换行不生效
- 交叉绘制的场景，如何重新计算y坐标

---

解决这些问题则是在 `ImgCreateWrapper` 的具体绘制中进行了实现，先看文本的绘制

- 根据换行符对字符串进行拆分
- 计算绘制内容最终转换为图片时，所占用的高度
- 重新生成画布 `BufferedImage result`
  - 如果result为空，则直接生成
  - 如果最终生成的高度，超过已有画布的高度，则生成一个更高的画布，并将原来的内容绘制上去
- 迭代绘制单行内容

```java
public Builder drawContent(String content) {
    String[] strs = StringUtils.split(content, "\n");
    if (strs.length == 0) { // empty line
        strs = new String[1];
        strs[0] = " ";
    }

    int fontSize = options.getFont().getSize();
    int lineNum = calLineNum(strs, options.getImgW(), options.getLeftPadding(), fontSize);
    // 填写内容需要占用的高度
    int height = lineNum * (fontSize + options.getLinePadding());

    if (result == null) {
        result = GraphicUtil.createImg(options.getImgW(),
                Math.max(height + options.getTopPadding() + options.getBottomPadding(), BASE_ADD_H),
                null);
    } else if (result.getHeight() < contentH + height + options.getBottomPadding()) {
        // 超过原来图片高度的上限, 则需要扩充图片长度
        result = GraphicUtil.createImg(options.getImgW(),
                result.getHeight() + Math.max(height + options.getBottomPadding(), BASE_ADD_H),
                result);
    }


    // 绘制文字
    Graphics2D g2d = GraphicUtil.getG2d(result);
    int index = 0;
    for (String str : strs) {
        GraphicUtil.drawContent(g2d, str,
                contentH + (fontSize + options.getLinePadding()) * (++index)
                , options);
    }
    g2d.dispose();

    contentH += height;
    return this;
}


/**
 * 计算总行数
 *
 * @param strs     字符串列表
 * @param w        生成图片的宽
 * @param padding  渲染内容的左右边距
 * @param fontSize 字体大小
 * @return
 */
private int calLineNum(String[] strs, int w, int padding, int fontSize) {
    // 每行的字符数
    double lineFontLen = Math.floor((w - (padding << 1)) / (double) fontSize);


    int totalLine = 0;
    for (String str : strs) {
        totalLine += Math.ceil(str.length() / lineFontLen);
    }

    return totalLine;
}
```


上面需要注意的是画布的生成规则，特别是高度超过上限之后，重新计算图片高度时，需要额外注意新增的高度，应该为基本的增量与（绘制内容高度+下边距）的较大值

```
int realAddH = Math.max(bufferedImage.getHeight() + options.getBottomPadding() + options.getTopPadding(), BASE_ADD_H)
```



重新生成画布实现 `com.hust.hui.quickmedia.common.util.GraphicUtil#createImg`

```java
public static BufferedImage createImg(int w, int h, BufferedImage img) {
    BufferedImage bf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = bf.createGraphics();

    if (img != null) {
        g2d.setComposite(AlphaComposite.Src);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(img, 0, 0, null);
    }
    g2d.dispose();
    return bf;
}
```


上面理解之后，绘制图片就比较简单了，基本上行没什么差别


```java
public Builder drawImage(String img) {
    BufferedImage bfImg;
    try {
        bfImg = ImageUtil.getImageByPath(img);
    } catch (IOException e) {
        log.error("load draw img error! img: {}, e:{}", img, e);
        throw new IllegalStateException("load draw img error! img: " + img, e);
    }

    return drawImage(bfImg);
}


public Builder drawImage(BufferedImage bufferedImage) {

    if (result == null) {
        result = GraphicUtil.createImg(options.getImgW(),
                Math.max(bufferedImage.getHeight() + options.getBottomPadding() + options.getTopPadding(), BASE_ADD_H),
                null);
    } else if (result.getHeight() < contentH + bufferedImage.getHeight() + options.getBottomPadding()) {
        // 超过阀值
        result = GraphicUtil.createImg(options.getImgW(),
                result.getHeight() + Math.max(bufferedImage.getHeight() + options.getBottomPadding() + options.getTopPadding(), BASE_ADD_H),
                result);
    }

    // 更新实际高度
    int h = GraphicUtil.drawImage(result,
            bufferedImage,
            contentH,
            options);
    contentH += h + options.getLinePadding();
    return this;
}
```

### 5. http接口

上面实现的生成图片的公共方法，在 `quick-media` 工程中，利用spring-boot搭建了一个web服务，提供了一个http接口，用于生成长图文，最终的成果就是我们开头的那个gif图的效果，相关代码就没啥好说的，有兴趣的可以直接查看工程源码，链接看最后

## 测试验证

上面基本上完成了我们预期的目标，接下来则是进行验证，测试代码比较简单，先准备一段文本，这里拉了一首诗

```text
招魂酹翁宾旸
郑起

君之在世帝敕下，君之谢世帝敕回。
魂之为变性原返，气之为物情本开。
於戏龙兮凤兮神气盛，噫嘻鬼兮归兮大块埃。
身可朽名不可朽，骨可灰神不可灰。
采石捉月李白非醉，耒阳避水子美非灾。
长孙王吉命不夭，玉川老子诗不徘。
新城罗隐在奇特，钱塘潘阆终崔嵬。
阴兮魄兮曷往，阳兮魄兮曷来。
君其归来，故交寥落更散漫。
君来归来，帝城绚烂可徘徊。
君其归来，东西南北不可去。
君其归来。
春秋霜露令人哀。
花之明吾无与笑，叶之陨吾实若摧。
晓猿啸吾闻泪堕，宵鹤立吾见心猜。
玉泉其清可鉴，西湖其甘可杯。
孤山暖梅香可嗅，花翁葬荐菊之隈。
君其归来，可伴逋仙之梅，去此又奚之哉。
```

测试代码

```java
@Test
public void testGenImg() throws IOException {
    int w = 400;
    int leftPadding = 10;
    int topPadding = 40;
    int bottomPadding = 40;
    int linePadding = 10;
    Font font = new Font("宋体", Font.PLAIN, 18);

    ImgCreateWrapper.Builder build = ImgCreateWrapper.build()
            .setImgW(w)
            .setLeftPadding(leftPadding)
            .setTopPadding(topPadding)
            .setBottomPadding(bottomPadding)
            .setLinePadding(linePadding)
            .setFont(font)
            .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
//                .setBgImg(ImageUtil.getImageByPath("qrbg.jpg"))
            .setBgColor(0xFFF7EED6)
            ;


    BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt");
    String line;
    int index = 0;
    while ((line = reader.readLine()) != null) {
        build.drawContent(line);

        if (++index == 5) {
            build.drawImage(ImageUtil.getImageByPath("https://static.oschina.net/uploads/img/201708/12175633_sOfz.png"));
        }

        if (index == 7) {
            build.setFontSize(25);
        }

        if (index == 10) {
            build.setFontSize(20);
            build.setFontColor(Color.RED);
        }
    }

    BufferedImage img = build.asImage();
    String out = Base64Util.encode(img, "png");
    System.out.println("<img src=\"data:image/png;base64," + out + "\" />");
}
```


输出图片


![测试结果图](https://static.oschina.net/uploads/img/201708/18180717_MrRM.png "测试结果图")

## 其他


项目地址: [https://github.com/liuyueyi/quick-media](https://github.com/liuyueyi/quick-media)


个人博客：[一灰的个人博客](http://blog.zbang.online:8080)

公众号获取更多:

![个人信息](https://static.oschina.net/uploads/img/201708/12175649_wn2r.png "个人信息")



