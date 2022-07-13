package com.github.hui.quick.plugin.test.water;

import com.github.hui.quick.plugin.image.wrapper.wartermark.remove.WaterMarkRemoveWrapper;
import com.github.hui.quick.plugin.image.wrapper.wartermark.remove.operator.WaterMarkRemoveTypeEnum;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author YiHui
 * @date 2022/7/8
 */
public class WaterRemoveTest {
    @Test
    public void testRemove() throws IOException {
        String url = "https://mmbiz.qpic.cn/mmbiz_png/dYV9cAW65kYJ2uVS43GPVqNQcAtJqVCWhBmISJXF9KpNib7zicjIX7VFYnNccafC7LomzqIZKQe4A54RNicic9HTvw/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1";

        BufferedImage img = WaterMarkRemoveWrapper.of(url)
                .setType(WaterMarkRemoveTypeEnum.FILL.getType())
                .setWaterMarkH(47)
                .setWaterMarkW(189)
                .setWaterMarkX(782)
                .setWaterMarkY(352)
                .setFillColor("0xff557ec6")
                .build()
                .asImage();
        System.out.println("---");
    }

    @Test
    public void testRemove2() throws IOException {
        String url = "https://mmbiz.qpic.cn/mmbiz_png/dYV9cAW65kYJ2uVS43GPVqNQcAtJqVCWhBmISJXF9KpNib7zicjIX7VFYnNccafC7LomzqIZKQe4A54RNicic9HTvw/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1";

        BufferedImage img = WaterMarkRemoveWrapper.of(url)
                .setType(WaterMarkRemoveTypeEnum.PIXEL.getType())
                .setWaterMarkH(47)
                .setWaterMarkW(189)
                .setWaterMarkX(782)
                .setWaterMarkY(352)
                .setPixelSize(8)
                .build()
                .asImage();
        System.out.println("---");
    }

    @Test
    public void testRemove3() throws IOException {
        String url = "https://mmbiz.qpic.cn/mmbiz_png/dYV9cAW65kYJ2uVS43GPVqNQcAtJqVCWhBmISJXF9KpNib7zicjIX7VFYnNccafC7LomzqIZKQe4A54RNicic9HTvw/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1";

        BufferedImage img = WaterMarkRemoveWrapper.of(url)
                .setType(WaterMarkRemoveTypeEnum.BG.getType())
                .setWaterMarkH(47)
                .setWaterMarkW(189)
                .setWaterMarkX(782)
                .setWaterMarkY(352)
                .setUpDownRate(0.2F)
                .setDownRange(0.2f)
                .build()
                .asImage();
        System.out.println("---");
    }
}
