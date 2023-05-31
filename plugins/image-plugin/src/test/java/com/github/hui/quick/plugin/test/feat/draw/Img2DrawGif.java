package com.github.hui.quick.plugin.test.feat.draw;

import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.base.gif.GifHelper;
import com.github.hui.quick.plugin.test.SketchTest;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Img2DrawGif {
    private static final String PRE = "D://quick-media/";

    @Test
    public void testImg2gif() throws IOException {
        String fileName = "jj";
        String path = PRE + fileName + ".jpg";
        List<BufferedImage> imgs = splitImg(path);
        int delay;
        if (imgs.size() < 5) {
            delay = 500;
        } else if (imgs.size() < 10) {
            delay = 300;
        } else {
            delay = 200;
        }
        GifHelper.saveGif(imgs, delay, PRE + "out_g_" + fileName + ".gif");
        System.out.println("img size: " + imgs.size());
    }

    public List<BufferedImage> splitImg(String path) throws IOException {
        BufferedImage img = SketchTest.toSketch(path);
//        BufferedImage img = ImageLoadUtil.getImageByPath(path);
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage last = null;
        List<BufferedImage> result = new ArrayList<>();
        result.add(GraphicUtil.createImg(w, h, img));
        while (true) {
            last = outImg(last, img, w, h);
            if (last == null) {
                break;
            }

            result.add(last);
        }
        return result;
    }

    private BufferedImage outImg(BufferedImage last, BufferedImage source, int w, int h) {
        BufferedImage newImg = GraphicUtil.createImg(w, h, last);
        Graphics2D g2d = GraphicUtil.getG2d(newImg);
        if (last == null) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, w, h);
            return newImg;
        }

        int drawDot = 0;
        for (int y = 0; y < h; y++) {
            int leftX = 0, rightX = w - 1;
            int reverseCnt = 0;
            while (leftX <= rightX) {
                if (reverseCnt <= 1) {
                    Color rgbColor = new Color(source.getRGB(leftX, y), true);
                    if (!bgColor(rgbColor)) {
                        g2d.setColor(rgbColor);
                        g2d.fillRect(leftX, y, 1, 1);

                        source.setRGB(leftX, y, 0);
                        drawDot++;

                        if (reverseCnt == 0) {
                            reverseCnt = 1;
                        }
                    } else if (reverseCnt == 1) {
                        reverseCnt = 2;
                    }
                    leftX += 1;
                } else {
                    // 注释下面的，实现从左到右渲染
//                    break;
                    Color rgbColor = new Color(source.getRGB(rightX, y), true);
                    if (!bgColor(rgbColor)) {
                        g2d.setColor(rgbColor);
                        g2d.fillRect(rightX, y, 1, 1);

                        source.setRGB(rightX, y, 0);
                        drawDot++;

                        if (reverseCnt == 2) {
                            reverseCnt = 3;
                        }
                    } else if (reverseCnt == 3) {
                        break;
                    }
                    rightX -= 1;
                }
            }
        }
        g2d.dispose();
        if (drawDot == 0) {
            return null;
        }
        return newImg;
    }

    private boolean bgColor(Color rgbColor) {
        return rgbColor.getAlpha() == 0 || (rgbColor.getRed() > 250 && rgbColor.getGreen() > 250 && rgbColor.getBlue() > 250);
    }
}
