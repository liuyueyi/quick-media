package com.github.hui.quick.plugin.test.feat.svg;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.image.util.StrUtil;
import com.github.hui.quick.plugin.image.wrapper.pixel.ImgPixelWrapper;
import com.github.hui.quick.plugin.image.wrapper.pixel.model.PixelStyleEnum;
import com.github.hui.quick.plugin.image.wrapper.svg.SvgParserWrapper;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * png/jpeg -> svg
 *
 * @author YiHui
 * @date 2023/1/16
 */
public class SvgParseTest {

    public static String SVG_START = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<svg xmlns=\"http://www.w3.org/2000/svg\"\n" +
            "        viewBox=\"0 0 {width} {height}\"\n" +
            "        style=\"width: 100%; height: 100%; overflow: auto; fill: {BG_COLOR}\">\n";
    public static String SVG_END = "\n</svg>";

    @Test
    public void test2svg() {
        int size = 4;
        BufferedImage pixelImg = ImgPixelWrapper.build()
                .setSourceImg("https://spring.hhui.top/spring-blog/css/images/avatar.jpg")
                .setBlockSize(size)
                .setPixelType(PixelStyleEnum.PIXEL_COLOR_AVG)
                .build().asBufferedImg();

        int w = pixelImg.getWidth(), h = pixelImg.getHeight();
        StringBuilder builder = new StringBuilder();
        String s = StrUtil.replace(SVG_START, "{width}", String.valueOf(w), "{height}", String.valueOf(h), "{BG_COLOR}", "white", "{FONT_COLOR}", "black");
        builder.append(s).append("\n");
        for (int i = 0; i < w; i += size) {
            for (int j = 0; j < h; j += size) {
                int color = pixelImg.getRGB(i, j);
                Color c = new Color(color, true);
                if (c.getRed() >= 250 && c.getGreen() >= 250 && c.getBlue() >= 250) {
                    continue;
                }
                String htmlColor = ColorUtil.int2htmlColor(color);
                builder.append(toRect(htmlColor, size, size, i, j)).append("\n");
            }
        }
        builder.append(SVG_END);
        System.out.println(builder);
        System.out.println("over");
    }

    public String toRect(String color, int w, int h, int x, int y) {
        return "<rect" + " fill=\"" + color + "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\"/>";
    }

    @Test
    public void png2svg() throws IOException {
        // 图片转svg
        String png = "https://ci.xiaohongshu.com/d5137769-1836-cc20-c1eb-20af1109dc7a?imageView2/2/w/1080/format/jpg";
        SvgParserWrapper
                .of(png)
                .setScaleRate(0.3f)
                .setBlockSize(1)
                .setBgPredicate(c -> {
                    if (c.getRed() >= 50 && c.getRed() <= 60
                            && c.getGreen() >= 55 && c.getGreen() <= 62
                            && c.getBlue() >= 60 && c.getBlue() <= 65) {
                        return true;
                    }
                    if (c.getRed() >= 0xd7 && c.getGreen() >= 0x5f && c.getBlue() >= 0xf0) {
                        return true;
                    }
                    return false;
                })
                .build()
                .asFile("d:/quick-media/am2.svg");
        System.out.println("----over----");
    }

    @Test
    public void parsePhoto() throws Exception {
        String photo = "d:/wzb.png";
        SvgParserWrapper.of(photo).setScaleRate(1).setBlockSize(1)
                .setBgPredicate(c -> {
                    return c.getAlpha() < 10;
                })
                .build().asFile("d:/quick-media/w.svg");
        System.out.println("---over---");
    }
}
