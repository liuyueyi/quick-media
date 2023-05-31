package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.svg.SvgRenderWrapper;
import com.github.hui.quick.plugin.svg.model.RenderType;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 2018/1/14.
 */
public class SvgRenderTest {

    public Map<String, Object> newMap(String k, Object v, Object... kv) {
        Map<String, Object> map = new HashMap<>();
        map.put(k, v);
        for (int i = 0; i < kv.length; i += 2) {
            map.put((String) kv[i], kv[i + 1]);
        }
        return map;
    }

    /**
     * 模板参数替换
     */
    @Test
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
                .asFile("/tmp/i1.jpg");
        System.out.println("渲染完成");

        SvgRenderWrapper.of(svg)
                .addParams("svg_2", "金额： ￥520.00")
                // svgContent 表示替换标签内容值； 后面的 fill: #ff0000 表示替换标签的属性值
                .addParams("svg_3", newMap("svgContent", "当前时间: " + LocalDateTime.now(), "fill", "#ff0000"))
                // 对于image标签，直接传图片地址即可
                .addParams("svg_1", "https://spring.hhui.top/spring-blog/imgs/221026/logo.jpg")
                .asFile("/tmp/i2.jpg");
        System.out.println("替换属性渲染完成");
    }

    @Test
    public void testPng() {
        BufferedImage img = SvgRenderWrapper.of("test.svg").setType(RenderType.TIFF).asImg();
        System.out.println("---");
    }

    @Test
    public void testRender() {
        String svg = "<svg width=\"480\" height=\"240\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n" +
                "    <rect fill=\"#ffffff\" style=\"stroke:#e6e6e6;stroke-width:2\" id=\"canvas_background\" height=\"100%\" width=\"100%\" y=\"0\" x=\"0\"/>\n" +
                "    <svg width=\"300\" height=\"230\">\n" +
                "        <image y=\"40\" width=\"100%\" height=\"80\" text-anchor=\"middle\" xlink:href=\"http://s3.mogucdn.com/mlcdn/c45406/171105_4keid4g9cbh13eeg3l78e6fae325j_640x960.jpg_220x330.jpg\"/>\n" +
                "        <text y=\"150\" x=\"50%\" font-size=\"18\" text-anchor=\"middle\" style=\"display: block;\">小灰灰Blog</text>\n" +
                "        <text y=\"170\" x=\"50%\" font-size=\"14\" text-anchor=\"middle\" fill='#bbb'>\n" +
                "            <tspan y=\"180\" x=\"50%\">\n" +
                "                码农界新人，Java搬运工一枚\n" +
                "            </tspan>\n" +
                "            <tspan y=\"200\" x=\"50%\">\n" +
                "                关注不定时分享各种边缘or好用知识点\n" +
                "            </tspan>\n" +
                "        </text>\n" +
                "    </svg>\n" +
                "    <line x1=\"300\" y1=\"20\" x2=\"270\" y2=\"220\" style=\"stroke:#e6e6e6;stroke-width:1\"/>\n" +
                "    <svg width=\"170\" height=\"230\" x=\"300\">\n" +
                "        <!--<rect fill=\"#ccabcd\" id=\"canvas_background\" height=\"100%\" width=\"100%\" y=\"10\" x=\"10\"></rect>-->\n" +
                "        <image y=\"30\" width=\"100%\" height=\"160\" text-anchor=\"middle\" xlink:href=\"https://static.oschina.net/uploads/img/201707/09205944_0PzS.jpg\">\n" +
                "        </image>\n" +
                "        <text y=\"200\" x=\"50%\" font-size=\"12\" text-anchor=\"middle\" fill=\"#999\">\n" +
                "            长按或扫一扫关注\n" +
                "        </text>\n" +
                "    </svg>\n" +
                "</svg>";
        SvgRenderWrapper.of(svg).asFile("/tmp/svg/save.jpg");
        System.out.println("渲染图片完成!");
    }
}
