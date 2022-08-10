package com.github.hui.test;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author YiHui
 * @date 2022/8/10
 */
public class ColorUtilTest {

    /**
     * <a href="https://github.com/liuyueyi/quick-media/issues/104">ColorUtil 支持html格式颜色与jdk颜色互转</a>
     */
    @Test
    public void testHtml2Color() {
        List<String> inputs = Arrays.asList(
                "#111", "#aaa", "#a10", "#eee",
                "#111a", "#aaaa", "#a10f", "#eee0",
                "#111111", "#aaaaaa", "#aa1100", "#eeeeee",
                "#111111aa", "#aaaaaaaa", "#aa1100ff", "#eeeeee00"
        );

        List<Color> outputs = Arrays.asList(
                new Color(0x11, 0x11, 0x11), new Color(0xaa, 0xaa, 0xaa), new Color(0xaa, 0x11, 0x00), new Color(0xee, 0xee, 0xee),
                new Color(0x11, 0x11, 0x11, 0xaa), new Color(0xaa, 0xaa, 0xaa, 0xaa), new Color(0xaa, 0x11, 0x00, 0xff), new Color(0xee, 0xee, 0xee, 0x00),
                new Color(0x11, 0x11, 0x11), new Color(0xaa, 0xaa, 0xaa), new Color(0xaa, 0x11, 0x00), new Color(0xee, 0xee, 0xee),
                new Color(0x11, 0x11, 0x11, 0xaa), new Color(0xaa, 0xaa, 0xaa, 0xaa), new Color(0xaa, 0x11, 0x00, 0xff), new Color(0xee, 0xee, 0xee, 0x00)
        );

        for (int i = 0; i < inputs.size(); i++) {
            Color o = ColorUtil.html2color(inputs.get(i));
            System.out.println(o.equals(outputs.get(i)));
        }
    }

}
