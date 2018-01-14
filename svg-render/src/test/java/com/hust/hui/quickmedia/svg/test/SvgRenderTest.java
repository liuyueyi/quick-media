package com.hust.hui.quickmedia.svg.test;

import com.hust.hui.quickmedia.svg.SvgRenderWrapper;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 2018/1/14.
 */
public class SvgRenderTest {

    @Test
    public void testGenPng() {
        Map<String, Object> map = new HashMap();
        map.put("svg_2", "当前金额: ￥1314.00");
        map.put("svg_3", "当前时间: 2017-01-01 11:00:00");
        try {
            BufferedImage img = SvgRenderWrapper.convertToJpegAsImg(
                    "test.svg",
                    map);
            System.out.println("---");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
