package com.github.hui.quick.plugin.test.v3.sample;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicTypeEnum;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author YiHui
 * @date 2022/10/14
 */
public class BgSameDrawSample extends BasicGenTest {

    /**
     * 输出实例图如
     * bgSameDrawSample.jpg
     *
     * @throws Exception
     */
    @Test
    public void testGenV3() throws Exception {
        // 创建一个与二维码样式相同资源的背景
        int resultSize = 1200;
        int starSize = 16;
        BufferedImage bg = GraphicUtil.createImg(resultSize, resultSize, 0, 0, null, Color.WHITE);
        Graphics2D g2d = GraphicUtil.getG2d(bg);
        g2d.setColor(Color.RED);
        Random random = new Random();
        for (int x = 0; x < resultSize - starSize; x += starSize) {
            for (int y = 0; y < resultSize - starSize; y += starSize) {
                if (random.nextInt(6) == 3) {
                    DrawStyle.MINI_RECT.geometryDrawFunc(g2d, x, y, starSize, starSize);
                }
            }
        }
        g2d.dispose();

        // 前置图，将中间抠出一个透明的圆圈，保留一宽度为10的蓝色圆
        int bgSize = 900;
        BufferedImage pre = GraphicUtil.createImg(resultSize, resultSize, 0, 0, null, Color.WHITE);
        g2d = GraphicUtil.getG2d(pre);
        g2d.setColor(Color.RED);
        int circleSize = 20;
        g2d.fill(new Ellipse2D.Float((resultSize - bgSize) / 2.0f - circleSize, (resultSize - bgSize) / 2.0f - circleSize,
                bgSize + circleSize * 2, bgSize + circleSize * 2));
        g2d.setColor(ColorUtil.OPACITY);
        g2d.setComposite(AlphaComposite.Src);
        g2d.fill(new Ellipse2D.Float((resultSize - bgSize) / 2.0f, (resultSize - bgSize) / 2.0f, bgSize, bgSize));
        g2d.dispose();


        // 二维码
        int qrSize = 600;
        QrCodeGenV3.of(msg)
                .setPadding(0)
                .setSize(qrSize)
                .newDrawOptions()
                .setDrawStyle(DrawStyle.MINI_RECT)
                .setPreColor(Color.RED)
                .complete()
                .setDetectSpecial(true)
                .newBgOptions()
                .setStartX((resultSize - qrSize) / 2)
                .setStartY((resultSize - qrSize) / 2)
                .setBg(new QrResource().setImg(bg))
                .setBgStyle(BgStyle.FILL)
                .complete()
                .newFrontOptions()
                .setFt(new QrResource().setImg(pre))
                .complete()
                .setPicType(PicTypeEnum.JPG)
                .asFile(prefix + "/BgSameDrawSample.jpg");

        System.out.println("---over---");

    }
}
