## svg-plugin

借助batik实现svg的渲染，并输出jpg/png图片，通过实际测试这个接口的性能不是特别好，但常规的使用还ok

### 1. 依赖

jar包引入，请注意使用最新的版本

```xml
<!-- https://mvnrepository.com/artifact/com.github.liuyueyi.media/svg-plugin -->
<dependency>
    <groupId>com.github.liuyueyi.media</groupId>
    <artifactId>svg-core</artifactId>
</dependency>
```

### 2. 使用说明

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
public static BufferedImage convertToJpegAsImg(String path, Map<String, Object> paramMap);
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
    <rect class='bgcolor' style="stroke:#e6e6e6;stroke-width:2" id="canvas_background" height="220" width="96%" y="10" x="2%"></rect>
    <rect class='bgcolor' x='33%' y='0' height='30' width='33%'/>
    <text y="16" x="50%" id="title"  lengthAdjust='spacing' rotate="5" textLength='33%'>一灰灰Blog</text>
    <svg width="270" height="230">
        <image  y="45" height="90" width="62%" xlink:href="http://image.uc.cn/o/wemedia/s/upload/2017/39c53604fe3587a4876396cf3785b801x200x200x13.png" id="logo">
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
      <image y="30" width="100%" height="160" xlink:href="https://static.oschina.net/uploads/img/201707/09205944_0PzS.jpg" id="qrCode">
      </image>
      <text y="200" x="50%" id="qrDesc">
         小灰灰Blog微信公众号
      </text>
    </svg>
    <line x1="425" y1="40" x2="425" y2="200" style="stroke:#e6e600;stroke-width:1"/>
    <svg width="150" height="230" x="430">
      <image y="30" width="100%" height="160" xlink:href="http://s17.mogucdn.com/mlcdn/c45406/180209_3i75g6a8fbb9i9j6ked54ka8ggikh_500x500.png" id="qrCode">
      </image>
      <text y="200" x="50%" style="font-size:12px;fill:#E02222">
         一灰灰Blog 个人博客
      </text>
    </svg>
    <rect class='bgcolor' x='22%' y='220' height='30' width='56%'/>
    <text y="234" x="146" fill="#aaa"> [ - </text>
    <text y="234" x="464" fill="#aaa"> - ] </text>
    <text y="235" x="50%" id="title2"  lengthAdjust='spacing' textLength='60%'>获取技术干货，共同学习成长 </text>
</svg>
```

输出结果如下：

![card](https://spring.hhui.top/spring-blog/imgs/info/info.png)

