package com.github.hui.quick.plugin.test.artline;

import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.image.util.ExtractLineUtil;
import com.github.hui.quick.plugin.image.util.FilterUtil;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 线图提取测试工具
 *
 * @author YiHui
 * @date 2025/6/5
 */
public class ArtLineTest {

    @Test
    public void testGetImgLineArt() throws Exception {
        String path = "D://quick-media/pixel-in/tt/";
        String img = "cxy.png";
//        String img = "gir.png";
        BufferedImage org = ImageLoadUtil.getImageByPath(path + img);
        long start = System.currentTimeMillis();


        BufferedImage out = ExtractLineUtil.extractLineDrawing(org);
        long now = System.currentTimeMillis();
        System.out.println("线图提取成功： " + (now - start));
        start = now;


        BufferedImage o = ExtractLineUtil.extractLineBySobelDetect(org);
        // 将边缘颜色设置为黑色
        BufferedImage finalImage = applyEdgesToLineArt(org, o);
        now = System.currentTimeMillis();
        System.out.println("边缘检测 --> 重绘 -> " + (now - start));

        // 再来一次
        BufferedImage finalImage2 = applyEdgesToLineArt(out, finalImage);
        System.out.println("在处理一次降噪");

        BufferedImage finalImage3 = applyEdgesToLineArt(out, finalImage2);
        System.out.println("第三次");
    }

    // 将边缘颜色设置为黑色
    public static BufferedImage applyEdgesToLineArt(BufferedImage lineArtImage, BufferedImage edgeImage) {
        int width = lineArtImage.getWidth();
        int height = lineArtImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color edge = new Color(edgeImage.getRGB(x, y));
                if (isEdge(edge) && !isSinglePoint(edgeImage, x, y)) {
                    // 边框
                    outputImage.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    outputImage.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }
        return outputImage;
    }

    private static boolean isEdge(Color edge) {
        int threshold = 10;
        return edge.getRed() < threshold && edge.getBlue() < threshold && edge.getGreen() < threshold;
    }

    private static boolean isSinglePoint(BufferedImage bufferedImage, int x, int y) {
        int pad = 5;
        for (int i = 1; i <= pad; i++) {
            if (isSinglePoint(bufferedImage, x, y, i)) {
                System.out.println("孤立点： " + i + "=>" + x + " " + y );
                return true;
            }
        }
        return false;
    }

    private static boolean isSinglePoint(BufferedImage bufferedImage, int x, int y, int pad) {
        // 如果这个点的四周都不是边缘点，则干掉它
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        // 以x,y为中心，如果间隔pad之外的四周都是空白点，则表示这里为孤立噪点数据
        int lX = x - pad, lY = y - pad;
        int rX = x + pad, rY = y + pad;
        if (lX >= 0) {
            // 左边距判断
            int tmpY = Math.max(0, lY);
            int targetY = Math.min(height - 1, rY);
            while (tmpY <= targetY) {
                if (isEdge(new Color(bufferedImage.getRGB(lX, tmpY)))) {
                    return false;
                }
                tmpY++;
            }
        }

        if (rX < width) {
            // 右边框判断
            int tmpY = Math.max(0, lY);
            int targetY = Math.min(height - 1, rY);
            while (tmpY <= targetY) {
                if (isEdge(new Color(bufferedImage.getRGB(rX, tmpY)))) {
                    return false;
                }
                tmpY++;
            }
        }

        if (lY >= 0) {
            // 上边框判断
            int tmpX = Math.max(0, lX);
            int targetX = Math.min(width - 1, rX);
            while (tmpX <= targetX) {
                if (isEdge(new Color(bufferedImage.getRGB(tmpX, lY)))) {
                    return false;
                }
                tmpX++;
            }
        }

        if (rY < height) {
            // 下边框判断
            int tmpX = Math.max(0, lX);
            int targetX = Math.min(width - 1, rX);
            while (tmpX <= targetX) {
                if (isEdge(new Color(bufferedImage.getRGB(tmpX, rY)))) {
                    return false;
                }
                tmpX++;
            }
        }

        return true;
    }
}
