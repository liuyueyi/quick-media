# svg-plugin

借助batik实现svg的渲染，并输出jpg/png图片，通过实际测试这个接口的性能不是特别好，但常规的使用还ok

## 1. 依赖

jar包引入，请注意使用最新的版本

```xml
<!-- https://mvnrepository.com/artifact/com.github.liuyueyi.media/svg-plugin -->
<dependency>
    <groupId>com.github.liuyueyi.media</groupId>
    <artifactId>svg-core</artifactId>
</dependency>
```

## 2. 使用说明

### 3.0.1及之后版本

#### 基本使用

直接将svg文件渲染为图片

```java
public void testPng(){
        BufferedImage img=SvgRenderWrapper.of("test.svg")
        .setType(RenderType.JPG) // 默认输出图片为png格式，因此当输出png时，可以省略这个参数设置
        .asImg();
        System.out.println("---");
        }
```

**svg传参**

of的传参可以是svg的文件地址（本地的绝对路径、相对路径或者网络url均可）, 也可以是svg文本

**支持四种输出方式**

- 输出图片: asImg()
- 输出流: asStream()
- 输出字节: asBytes()
- 输出文件: asFile(fileName)
    - 输出文件时，会自动根据文件名后缀，来设置渲染图片类型，如 save.jpg 表示将svg渲染为jpg格式图片
    - 输出文件时，若指定文件的父目录不存在，会递归的创建父目录，无需担心文件不存在的异常

**渲染类型**

当前支持将svg渲染为jpg, png, tiff 3种格式图片

- png: `.setType(RenderType.PNG)`
- jpg: `.setType(RenderType.JPG)`
- tiff: `.setType(RenderType.TIFF)`

说明，当希望渲染图片为tiff格式时，对于jdk8及之下的版本，由于jdk本身不支持tiff图片，因此需要使用第三方插件来适配，所以请在pom中额外添加依赖

```xml
<!-- https://mvnrepository.com/artifact/com.twelvemonkeys.imageio/imageio-tiff -->
<dependency>
  <groupId>com.twelvemonkeys.imageio</groupId>
  <artifactId>imageio-tiff</artifactId>
  <version>3.9.4</version>
</dependency>
```

#### 模板渲染

模板渲染主要是指svg定义了一套样式模板，其中的部分元素可以根据传参进行替换，从而实现复用的效果

下面是一个模板的使用case

```java
public void testRenderSvgTemplate() {
    String svg = "<svg width=\"480\" height=\"855\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n" +
            "    <!-- Created with Method Draw - http://github.com/duopixel/Method-Draw/ -->\n" +
            "    <defs>\n" +
            "        <filter id=\"svg_2_blur\">\n" +
            "            <feGaussianBlur stdDeviation=\"0.1\" in=\"SourceGraphic\"/>\n" +
            "        </filter>\n" +
            "    </defs>\n" +
            "    <g>\n" +
            "        <title>background</title>\n" +
            "        <rect fill=\"#fff\" id=\"canvas_background\" height=\"857\" width=\"482\" y=\"-1\" x=\"-1\"/>\n" +
            "        <g display=\"none\" overflow=\"visible\" y=\"0\" x=\"0\" height=\"100%\" width=\"100%\" id=\"canvasGrid\">\n" +
            "            <rect fill=\"url(#gridpattern)\" stroke-width=\"0\" y=\"0\" x=\"0\" height=\"100%\" width=\"100%\"/>\n" +
            "        </g>\n" +
            "    </g>\n" +
            "    <g>\n" +
            "    <text x=\"100\" y=\"55\" fill=\"red\" style=\"outline: 10px solid blue; font-size:2em; overflow; visible\">I love SVG</text>\n" +
            "    <text x=\"22\" y=\"40\">Text Behind Shape</text>\n" +
            "\n" +
            "    <circle cx=\"50\" cy=\"50\" r=\"25\"\n" +
            "            style=\"stroke: none; fill: #0000ff;\n" +
            "           fill-opacity: 0.3;  \" />\n" +
            "    <circle cx=\"120\" cy=\"50\" r=\"25\"\n" +
            "            style=\"stroke: none; fill: #0000ff;\n" +
            "           fill-opacity: 0.7;  \" />\n" +
            "\n" +
            "\n" +
            "    <title>Layer 1</title>\n" +
            "    <image xlink:href=\"http://s2.mogucdn.com/mlcdn/c45406/170418_68lkjddg3bll08h9c9bk0d8ihkffi_800x1200.jpg_468x468.jpg\" id=\"svg_1\" height=\"855\" width=\"480\" y=\"-1\" x=\"1\"/>\n" +
            "    <text style=\"cursor: move;\" filter=\"url(#svg_2_blur)\" opacity=\"0.75\" stroke=\"#000\" xml:space=\"preserve\" text-anchor=\"start\" font-family=\"Euphoria, sans-serif\" font-size=\"35\" id=\"svg_2\" y=\"375.33555\" x=\"160.49442\" stroke-width=\"0\" fill=\"#000000\">￥1314.00</text>\n" +
            "    <text xml:space=\"preserve\" text-anchor=\"start\" font-family=\"Euphoria, sans-serif\" font-size=\"18\" id=\"svg_3\" y=\"827\" x=\"208.5\" stroke-width=\"0\" fill=\"#999999\">2017-01-28 11:02:01</text>\n" +
            "    </g>\n" +
            "</svg>";

    SvgRenderWrapper.of(svg)
            .addParams("svg_2", "当前金额: ￥1314.00")
            .addParams("svg_3", "当前时间: " + LocalDateTime.now())
            .setCacheEnable(true) // 表示开启缓存，对于模板渲染的case，可以有效提高渲染效率
            .asFile("/tmp/i1.tiff");
    System.out.println("渲染完成");

    SvgRenderWrapper.of(svg)
            .addParams("svg_2", "金额： ￥520.00")
            // svgContent 表示替换标签内容值； 后面的 fill: #ff0000 表示替换标签的属性值
            .addParams("svg_3", newMap("svgContent", "当前时间: " + LocalDateTime.now(), "fill", "#ff0000"))
            // 对于image标签，直接传图片地址即可
            .addParams("svg_1", "https://spring.hhui.top/spring-blog/imgs/221026/logo.jpg")
            .asFile("/tmp/i2.tiff");
    System.out.println("替换属性渲染完成");
}
```

**模板传参**

当前仅支持根据标签id来实现参数替换，对于使用name的传参替换有bug，因此暂不提供，详情参看 * [图片合成支持的前世今生 - 一灰灰Blog](https://blog.hhui.top/hexblog/2017/12/17/%E5%9B%BE%E7%89%87%E5%90%88%E6%88%90%E6%94%AF%E6%8C%81%E7%9A%84%E5%89%8D%E4%B8%96%E4%BB%8A%E7%94%9F/#4-svg-%E8%BD%AC-%E5%9B%BE%E7%89%87)

两种类型的参数替换

一个实例传参如下，表示需要替换 id=svg_3 标签的正文内容和fill属性

```json
{
  "svg_3": {
    "svgContent": "替换后的内容",
    "fill": "#ff0000"
  }
}
```

- 标签正文内容替换：主要替换的是 `<text id="svg_3">[这里是被替换内容]</tag>` 中两个标签内的文本，更新为 `替换后的内容`
- 标签属性替换：替换的标签的属性，如 `<text fill="#999999">正文</text>` 替换这个元素中的颜色属性,更新为 `fill="#ff0000`

<bold color="red">
- 需要注意的是，如果标签为image，它的正文内容实际上是 `xlink:href` 这个属性的值，而不是两个标签中间的文本
- 当需要替换标签属性时，传参value必然是map，其中key为属性名，`svgContent`为保留关键字，表示这个传参为正文内容，如上面的 `.addParams("svg_3", newMap("svgContent", "当前时间: " + LocalDateTime.now(), "fill", "#ff0000"))`
- 当只需要替换正文时，传参可以简化为 `{"tag_id": "替换后的内容"}`， 如传参设置 `.addParams("svg_2", "金额： ￥520.00")`
</bold>

渲染之后输出图片为:

| 正文替换  | 正文 + 属性替换                                             |
|-------|-------------------------------------------------------|
| ![](http://cdn.hhui.top/quick/quick-media/svg/i1.jpg) | ![](http://cdn.hhui.top/quick/quick-media/svg/i2.jpg) |

请注意上面两张图的差别

- 价格
- 图片
- 时间及颜色

**缓存提效**

对于模板渲染的方式，可以搭配`cacheEnable`参数来使用；这个表示是否需要缓存svg模板转换后的Document结构

默认规则：

- 当param传参非空时，开启缓存
- 当param传参为空时，关闭缓存

主动设置的cacheEnable的优先级最高，会覆盖默认的规则

默认最多只缓存100个模板对象；可以通过 `setCacheSize()` 来更新缓存大小

### 3.0.0及之前版本

以下是 3.0.0 及之前的版本使用姿势，最新版本做了较大改动，两者使用姿势并不通用，强烈推荐使用最新版本

对外暴露的一个封装类: `SvgRenderWrapper`，一般的使用姿势是接受两个参数，这里需要额外说明

```java
/**
 * 将SVG转换成PNG
 *
 * @param path     可以是http开头的SVG文件路径；可以是<svg开头的纯svg内容
 * @param paramMap 变更参数键值对，key为svg元素Id value为替换内容
 * @throws TranscoderException
 * @throws IOException
 */
public static BufferedImage convertToJpegAsImg(String path,Map<String, Object> paramMap);
```

- path: svg的文本内容；或者http地址对应的svg模板
- paramMap: 则表示替换的参数

**说明**

- 考虑到某些svg模板是通用的，只是部分图片或文字有些微的区别，所有通过paramMap来实现对svg内容的替换，目前只支持根据id进行替换

- 为了提升性能，内部对svg的模板进行了缓存，提升将svg模板解析为document对象的性能开销；而允许这么做的原因，就是document对象提供了深拷贝的接口

这个的具体使用比较简单，主要是svg模板的使用，友情贡献一个个人名片生成的svg模板

```html
<svg width="610" height="240" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
    <style type="text/css">
        text {
            text-anchor: middle;
        }

        #title {
            font-size: 20px;
            stroke-width: 2px;
            stroke: #aaa;
            fill: #abc;
        }

        #title2 {
            font-size: 15px;
            stroke: #345;
            fill: #abc;
        }

        #name {
            font-size: 18px;
            fill: #333;
            display: block;
        }

        #ownDesc {
            fill: #bbb;
            font-size: 14px;
        }

        #qrDesc {
            fill: #999;
            font-size: 12px;
        }

        .bgcolor {
            fill: #fff;
        }
    </style>
    <rect class='bgcolor' x='0' y='0' height='100%' width='100%'></rect>
    <rect class='bgcolor' style="stroke:#e6e6e6;stroke-width:2" id="canvas_background" height="220" width="96%" y="10"
          x="2%"></rect>
    <rect class='bgcolor' x='33%' y='0' height='30' width='33%'/>
    <text y="16" x="50%" id="title" lengthAdjust='spacing' rotate="5" textLength='33%'>一灰灰Blog</text>
    <svg width="270" height="230">
        <image y="45" height="90" width="62%"
               xlink:href="http://image.uc.cn/o/wemedia/s/upload/2017/39c53604fe3587a4876396cf3785b801x200x200x13.png"
               id="logo">
        </image>
        <text x="205" y="72" id='name'>一灰灰Blog</text>
        <text>
            <tspan y="100" x="200" font-size="13px" fill="#567">
                qq: 3302797840
            </tspan>
            <tspan y="120" x="198" font-size="13px" fill="#567">
                sina: 一灰灰blog
            </tspan>
            <tspan y="140" x="198" font-size="13px" fill="#567">
                osc: 小灰灰Blog
            </tspan>
        </text>
        <text y="170" x="50%" id='ownDesc'>
            <tspan y="170" x="50%">
                码农界新人，Java搬运工一枚
            </tspan>
            <tspan y="190" x="50%">
                不定时分享个人学习收获
            </tspan>
        </text>
    </svg>
    <line x1="260" y1="40" x2="240" y2="200" style="stroke:#e6e6e6;stroke-width:1"/>
    <svg width="150" height="230" x="270">
        <image y="30" width="100%" height="160"
               xlink:href="https://static.oschina.net/uploads/img/201707/09205944_0PzS.jpg" id="qrCode">
        </image>
        <text y="200" x="50%" id="qrDesc">
            小灰灰Blog微信公众号
        </text>
    </svg>
    <line x1="425" y1="40" x2="425" y2="200" style="stroke:#e6e600;stroke-width:1"/>
    <svg width="150" height="230" x="430">
        <image y="30" width="100%" height="160"
               xlink:href="http://s17.mogucdn.com/mlcdn/c45406/180209_3i75g6a8fbb9i9j6ked54ka8ggikh_500x500.png"
               id="qrCode">
        </image>
        <text y="200" x="50%" style="font-size:12px;fill:#E02222">
            一灰灰Blog 个人博客
        </text>
    </svg>
    <rect class='bgcolor' x='22%' y='220' height='30' width='56%'/>
    <text y="234" x="146" fill="#aaa"> [ -</text>
    <text y="234" x="464" fill="#aaa"> - ]</text>
    <text y="235" x="50%" id="title2" lengthAdjust='spacing' textLength='60%'>获取技术干货，共同学习成长</text>
</svg>
```

输出结果如下：

![card](https://spring.hhui.top/spring-blog/imgs/info/info.png)

