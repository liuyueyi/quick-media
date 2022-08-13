## qrcode-plugin

> 更丰富的使用教程，请参考: [二维码插件使用手册](https://liuyueyi.github.io/quick-media/#/%E6%8F%92%E4%BB%B6/%E4%BA%8C%E7%BB%B4%E7%A0%81/%E4%BA%8C%E7%BB%B4%E7%A0%81%E6%8F%92%E4%BB%B6%E4%BD%BF%E7%94%A8%E6%89%8B%E5%86%8C)

封装了二维码的生成和解析，深度定制了二维码的生成，支持各种酷炫二维码的产生

- [x] 二维码生成
- [x] 个性二维码生成
    - 支持logo
    - 支持logo样式 （圆角logo， 边框）
    - 支持二维码颜色设置
    - 支持探测图形颜色设置
    - 支持背景图
    - 支持base64格式的二维码图片
    - 支持二维码定制绘制信息样式
     - 三角形
     - 矩形
     - 五边形 （五角星待支持）
     - 六边形
     - 八边形
     - 圆
     - 自定义图片
- [x] 二维码信息解析

### 1. 依赖

直接从中央仓库引入最新的jar；请注意使用最新的版本

```xml
<dependency>
    <groupId>com.github.hui.media</groupId>
    <artifactId>qrcode-plugin</artifactId>
</dependency>
```

### 2. v3.0使用说明

3.0版本相比较于2.0有了较大的变更，使用姿势上大致上差不多，具体的详情可以查看示例教程文档，这里给出几个体验demo

- 指定图片资源输出酷炫的二维码 [QrV3GenTest](src/test/java/com/github/hui/quick/plugin/test/v3/QrV3GenTest.java)

```java
// 传入不同位置的图片资源，渲染输出个性二维码
public void testImgRender() {
    int size = 500;
    try {
        String bg = "http://img11.hc360.cn/11/busin/109/955/b/11-109955021.jpg";
        BufferedImage img = QrCodeGenV3.of(msg).setW(size)
                .setErrorCorrection(ErrorCorrectionLevel.H)
                .newDrawOptions()
                .setDrawStyle(DrawStyle.IMAGE)
                .newRenderResource(new QrResource().setImg("jihe/a.png")).build()
                .addSource(1, 3, new QrResource().setImg("jihe/b.png")).build()
                .addSource(3, 1, new QrResource().setImg("jihe/c.png")).build()
                .addSource(2, 3, new QrResource().setImg("jihe/e.png")).build()
                .addSource(3, 2, new QrResource().setImg("jihe/f.png")).build()
                .addSource(2, 2, new QrResource().setImg("jihe/g.png")).build()
                .addSource(4, 3, new QrResource().setImg("jihe/h.png")).build().over()
                .complete()
                .newDetectOptions() // 设置三个探测点的图形
                .setResource(new QrResource().setImg("jihe/PDP.png")).complete()
                .newBgOptions() // 设置背景图
                .setBgStyle(BgStyle.PENETRATE)
                .setBg(new QrResource().setImg(bg))
                .setStartX(10).setStartY(100)
                .complete()
                .build().asImg();
        System.out.println("over");
        ImageIO.write(img, "png", new File(prefix + "/q1.png"));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

- 输出svg格式的二维码

```java
public void symbolCdr() throws Exception {
    boolean svg = QrCodeGenV3.of(msg).setW(500)
    .newDrawOptions()
    .setDrawStyle(DrawStyle.SVG)
    .newRenderResource(new QrResource().setSvg(" <symbol id=\"symbol_1R\" viewBox=\"0 0 50 50\">\n" +
    "        <circle cx=\"25\" cy=\"25\" r=\"11.5\" style=\"fill: #F98E00\"/>\n" +
    "    </symbol>")).addResource(new QrResource().setSvg("<symbol id=\"symbol_1X\" viewBox=\"0 0 50 50\">\n" +
    "        <path d=\"M42.82,21.81C42.82,11.98,34.84,4,25,4S7.19,11.98,7.19,21.81L5.5,44l8.03-4.37L19.3,46l5.7-6.37L30.7,46  l5.78-6.37L44.5,44L42.82,21.81z M18.46,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03s5.76,2.7,5.76,6.03  C24.22,24.48,21.64,27.18,18.46,27.18z M31.54,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03  c3.18,0,5.76,2.7,5.76,6.03C37.3,24.48,34.72,27.18,31.54,27.18z\"\n" +
    "              style=\"fill: #F98E00\"/>\n" +
    "        <ellipse cx=\"20.76\" cy=\"22.13\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
    "        <ellipse cx=\"33.51\" cy=\"21.8\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
    "    </symbol>")).addResource(new QrResource().setSvg("<symbol id=\"symbol_1W\" viewBox=\"0 0 50 50\">\n" +
    "        <path d=\"M42.82,21.81C42.82,11.98,34.84,4,25,4S7.19,11.98,7.19,21.81L5.5,44l8.03-4.37L19.3,46l5.7-6.37L30.7,46  l5.78-6.37L44.5,44L42.82,21.81z M18.46,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03s5.76,2.7,5.76,6.03  C24.22,24.48,21.64,27.18,18.46,27.18z M31.54,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03  c3.18,0,5.76,2.7,5.76,6.03C37.3,24.48,34.72,27.18,31.54,27.18z\"\n" +
    "              style=\"fill: #40C4ED\"/>\n" +
    "        <ellipse cx=\"18.63\" cy=\"22.66\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
    "        <ellipse cx=\"31.37\" cy=\"22.34\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
    "    </symbol>")).addResource(new QrResource().setSvg("<symbol id=\"symbol_1V\" viewBox=\"0 0 50 50\">\n" +
    "        <path d=\"M42.82,21.81C42.82,11.98,34.84,4,25,4S7.19,11.98,7.19,21.81L5.5,44l8.03-4.37L19.3,46l5.7-6.37L30.7,46  l5.78-6.37L44.5,44L42.82,21.81z M18.46,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03s5.76,2.7,5.76,6.03  C24.22,24.48,21.64,27.18,18.46,27.18z M31.54,27.18c-3.18,0-5.76-2.7-5.76-6.03c0-3.33,2.58-6.03,5.76-6.03  c3.18,0,5.76,2.7,5.76,6.03C37.3,24.48,34.72,27.18,31.54,27.18z\"\n" +
    "              style=\"fill: #7BB72E\"/>\n" +
    "        <ellipse cx=\"16.63\" cy=\"22.06\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
    "        <ellipse cx=\"29.37\" cy=\"21.74\" rx=\"2.02\" ry=\"2.4\" style=\"fill: #555555\"/>\n" +
    "    </symbol>")).build()
    .addSource(2, 1, new QrResource().setSvg("<symbol id=\"symbol_1T\" viewBox=\"0 0 100 50\">\n" +
    "        <line x1=\"17\" y1=\"25\" x2=\"83\" y2=\"25\"\n" +
    "              style=\"fill: none; stroke: #333333; stroke-width: 20; stroke-linecap: round; stroke-miterlimit: 10\"/>\n" +
    "    </symbol>")).addResource(new QrResource().setSvg(" <symbol id=\"symbol_1U\" viewBox=\"0 0 100 50\">\n" +
    "        <circle cx=\"25\" cy=\"25\" r=\"11.5\" style=\"fill: #F98E00\"/>\n" +
    "        <circle cx=\"75\" cy=\"25\" r=\"11.5\" style=\"fill: #F98E00\"/>\n" +
    "    </symbol>")).build()
    .addSource(1, 3, new QrResource().setSvg("<symbol id=\"symbol_1S\" viewBox=\"0 0 50 150\">\n" +
    "    <line x1=\"25\" y1=\"20\" x2=\"25\" y2=\"130\"\n" +
    "              style=\"fill: none; stroke: #333333; stroke-width: 20; stroke-linecap: round; stroke-miterlimit: 10\"/>\n" +
    "    </symbol>")).build().over()
    .complete()
    .newDetectOptions()
    .setLt(new QrResource().setSvg("<symbol id=\"detect_lt\" viewBox=\"0 0 49 49\">\n" +
    "        <path d=\"M45.5,42.34c0,1.75-1.38,3.16-3.08,3.16H6.59c-1.7,0-3.09-1.42-3.09-3.16V6.66c0-1.75,1.38-3.16,3.09-3.16h35.83c1.7,0,3.08,1.42,3.08,3.16V42.34z\"\n" +
    "              style=\"fill: none; stroke: #333333; stroke-width: 7; stroke-linecap: round; stroke-miterlimit: 10\"/>\n" +
    "        <path d=\"M35,31c0,2.21-1.79,4-4,4H18c-2.21,0-4-1.79-4-4V18c0-2.21,1.79-4,4-4h13c2.21,0,4,1.79,4,4V31z\"\n" +
    "              style=\"fill: #333333\"/>\n" +
    "    </symbol>"))
    .setLd(new QrResource().setSvg("<symbol id=\"detect_ld\" viewBox=\"0 0 49 49\">\n" +
    "        <path d=\"M45.5,42.34c0,1.75-1.38,3.16-3.08,3.16H6.59c-1.7,0-3.09-1.42-3.09-3.16V6.66c0-1.75,1.38-3.16,3.09-3.16h35.83c1.7,0,3.08,1.42,3.08,3.16V42.34z\"\n" +
    "              style=\"fill: none; stroke: #333333; stroke-width: 7; stroke-linecap: round; stroke-miterlimit: 10\"/>\n" +
    "        <path d=\"M35,31c0,2.21-1.79,4-4,4H18c-2.21,0-4-1.79-4-4V18c0-2.21,1.79-4,4-4h13c2.21,0,4,1.79,4,4V31z\"\n" +
    "              style=\"fill: #333333\"/>\n" +
    "    </symbol>"))
    .setRt(new QrResource().setSvg(" <symbol id=\"detect_rt\" viewBox=\"0 0 49 49\">\n" +
    "        <path d=\"M45.5,42.34c0,1.75-1.38,3.16-3.08,3.16H6.59c-1.7,0-3.09-1.42-3.09-3.16V6.66c0-1.75,1.38-3.16,3.09-3.16h35.83c1.7,0,3.08,1.42,3.08,3.16V42.34z\"\n" +
    "              style=\"fill: none; stroke: #333333; stroke-width: 7; stroke-linecap: round; stroke-miterlimit: 10\"/>\n" +
    "        <path d=\"M35,31c0,2.21-1.79,4-4,4H18c-2.21,0-4-1.79-4-4V18c0-2.21,1.79-4,4-4h13c2.21,0,4,1.79,4,4V31z\"\n" +
    "              style=\"fill: #333333\"/>\n" +
    "    </symbol>")).complete()
    .build()
    .asFile(prefix + "/吃豆人.svg");
    System.out.println(svg);
}
```

上面这种指定每个资源位的svg貌似不太方便，因此特意支持了模板设置方式

```java
@Test
public void xhrTemplate() throws Exception {
    String svgTemplate = FileReadUtil.readAll("svg/小黄人.template");
    boolean ans = QrCodeGenV3.of(msg).setW(500).setSvgTemplate(svgTemplate)
            .setLogoRate(20)
            .setErrorCorrection(ErrorCorrectionLevel.H)
            .build()
            .asFile(prefix + "/小黄人.svg");
    System.out.println(ans);
}
```



### 3. v2.0使用说明


对二维码的生成，进行了深度定制，可以对二维码中的所有元素进行修改，包括是哪个探测定位点，二维码内部矩阵，背景，logo等

想了解如何使用，推荐参考下测试类里面的各种使用姿势: [QrCodeWrapperTest](https://github.com/liuyueyi/quick-media/blob/master/plugins/qrcode-plugin/src/test/java/com/github/hui/quick/plugin/test/QrCodeWrapperTest.java)

一个简单的生成二维码的case，指定了颜色，指定二维码矩阵改成小圆点，支持了logo

```java
private String msg = "https://liuyueyi.github.io/hexblog/2018/03/23/mysql之锁与事务详解/";

@Test
public void testGenWxQrcode() {
    String logo = "logo.jpg";

    int size = 500;
    try {
        BufferedImage img = QrCodeGenWrapper.of(msg)
                .setW(size)
                .setH(size)
//                    .setDetectImg("detect.png")
                .setDrawPreColor(0xff008e59)
//                    .setDrawPreColor(0xff002fa7)
                .setErrorCorrection(ErrorCorrectionLevel.M)
                .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                .setLogoBgColor(Color.LIGHT_GRAY)
                .setLogo(logo)
                .setLogoRate(10)
                .setDrawStyle(QrCodeOptions.DrawStyle.CIRCLE)
                .setDrawEnableScale(true)
                .asBufferedImage();
        ImageIO.write(img, "png", new File("src/test/qrcode/style.png"));
    } catch (IOException e) {
        e.printStackTrace();
    } catch (WriterException e) {
        e.printStackTrace();
    }
}
```

输出图片如: 

![qrcode](https://raw.githubusercontent.com/liuyueyi/quick-media/master/plugins/qrcode-plugin/src/test/qrcode/style.png)
