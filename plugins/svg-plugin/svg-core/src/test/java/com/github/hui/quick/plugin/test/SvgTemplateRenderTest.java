package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.svg.SvgRenderWrapper;
import com.github.hui.quick.plugin.svg.helper.SvgDocumentHelper;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * svg模板替换测试
 * Created by yihui on 2023/05/31.
 */
public class SvgTemplateRenderTest {
    private static final String TPL = "<svg width=\"480\" height=\"855\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n" +
            "\t<defs>\n" +
            "\t    <filter id=\"svg_2_blur\">\n" +
            "\t        <feGaussianBlur stdDeviation=\"0.1\" in=\"SourceGraphic\"/>\n" +
            "\t    </filter>\n" +
            "\t</defs>" +
            "  <g>\n" +
            "    <image xlink:href=\"https://spring.hhui.top/spring-blog/css/images/avatar.jpg\" id=\"img\" height=\"480\" width=\"480\" y=\"-1\" x=\"1\"></image>\n" +
            "    <text style=\"cursor: move;\" filter=\"url(#svg_2_blur)\" opacity=\"0.75\" stroke=\"#000\" xml:space=\"preserve\" text-anchor=\"start\" font-family=\"Euphoria, sans-serif\" font-size=\"35\" id=\"price\" y=\"500\" x=\"160.49442\" stroke-width=\"0\" fill=\"#000000\">￥1314.00</text>\n" +
            "    <text xml:space=\"preserve\" text-anchor=\"start\" font-family=\"Euphoria, sans-serif\" font-size=\"18\" id=\"time\" y=\"525\" x=\"208.5\" stroke-width=\"0\" fill=\"#999999\">2023-05-31 08:08:08</text>\n" +
            "    <a xlink:href=\"https://github.com/liuyueyi/quick-media\" target=\"_blank\" id=\"source_link\">\n" +
            "    \t<text font-family=\"Euphoria, sans-serif\" font-size=\"18\" y=\"525\" x=\"28.5\" stroke-width=\"0\" fill=\"#dd0000\" id=\"source\">项目源码地址</text>\n" +
            "    </a>\n" +
            "  </g>\n" +
            "</svg>";

    @Test
    public void testGenImg() {
        // 传参直接为String，表示替换默认标签内的content
        // 对于image, a标签，表示替换的时 xlink:href 跳转地址
        Map<String, Object> map = new HashMap<>();
        map.put("price", "当前金额: ￥1314.00");
        map.put("time", "当前时间: " + LocalDateTime.now());
        map.put("source", "QuickMedia源码地址");
        map.put("img", "https://spring.hhui.top/spring-blog/imgs/info/wx.jpg");
        try {
            String img = SvgRenderWrapper.of(TPL).setParams(map).asSvg();
            System.out.println("---");
            System.out.println(img);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGenImgV2() {
        Map<String, Object> map = new HashMap<>();
        // 直接替换id=time标签的content
        map.put("time", "当前事件:" + LocalDateTime.now());

        // 下面这个表示将标签的内容替换为指定值
        Map<String, Object> priceMap = new HashMap<>();
        priceMap.put(SvgDocumentHelper.SVG_CONTENT_REPLACE_KEY, "当前金额：￥1314.00");
        // 下面这个表示属性修改，将颜色替换为蓝色
        priceMap.put("fill", "#0000ff");
        map.put("price", priceMap);


        // 替换id=img的标签内容
        Map<String, Object> imgMap = new HashMap<>();
        imgMap.put("xlink:href", "https://spring.hhui.top/spring-blog/imgs/info/wx.jpg");
        imgMap.put("height", "300");
        map.put("img", imgMap);

        try {
            String svg = SvgRenderWrapper.of(TPL).setParams(map).asSvg();
            System.out.println("---\n\n");
            System.out.println(svg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
