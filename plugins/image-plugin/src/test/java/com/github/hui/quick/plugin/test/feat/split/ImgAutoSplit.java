package com.github.hui.quick.plugin.test.feat.split;

import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.image.wrapper.split.ImgSplitWrapper;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动拆分图
 *
 * @author YiHui
 * @date 2023/1/9
 */
public class ImgAutoSplit {

    public List<BufferedImage> split(String img) throws IOException {
        BufferedImage origin = ImageLoadUtil.getImageByPath(img);
        List<BufferedImage> ans = new ArrayList<>();
        while (true) {
            BufferedImage o = pickOneImg(origin);
            if (o != null) {
                ans.add(o);
            } else {
                break;
            }
        }
        return ans;
    }

    private BufferedImage pickOneImg(BufferedImage img) {
        int pointX = 0, pointY = 0;
        boolean pointChoose = false;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (!bgColor(img.getRGB(x, y))) {
                    pointX = x;
                    pointY = y;
                    pointChoose = true;
                    break;
                }
            }
            if (pointChoose) {
                break;
            }
        }

        if (!pointChoose) {
            return null;
        }

        int upY = pointY, downY = pointY, leftX = pointX, rightX = pointX;
        // 采用边框识别的方式，获取最高点、最低点、最左点、最右点
        List<Point> scanPoints = new ArrayList<>();
        Point current = new Point(pointX, pointY);
        int scanIndex = 0;
        while (true) {
            if (!scanPoints.contains(current)) {
                scanPoints.add(current);
            }

            // 上
            Point next = new Point(current.x, current.y - 1);
            if (!scanPoints.contains(next) && isBorderPoint(img, next.x, next.y)) {
                current = next;
                upY = Math.min(upY, next.y);
                scanIndex = 0;
                continue;
            }

            // 下
            next.y = current.y + 1;
            if (!scanPoints.contains(next) && isBorderPoint(img, next.x, next.y)) {
                current = next;
                downY = Math.max(downY, next.y);
                scanIndex = 0;
                continue;
            }

            // 左
            next.y = current.y;
            next.x = current.x - 1;
            if (!scanPoints.contains(next) && isBorderPoint(img, next.x, next.y)) {
                current = next;
                leftX = Math.min(leftX, next.x);
                scanIndex = 0;
                continue;
            }

            // 右
            next.x = current.x + 1;
            if (!scanPoints.contains(next) && isBorderPoint(img, next.x, next.y)) {
                current = next;
                rightX = Math.max(rightX, next.x);
                scanIndex = 0;
                continue;
            }

            next.x = current.x - 1;
            next.y = current.y - 1;
            if (!scanPoints.contains(next) && isBorderPoint(img, next.x, next.y)) {
                // 左上
                current = next;
                leftX = Math.min(leftX, next.x);
                upY = Math.min(upY, next.y);
                scanIndex = 0;
                continue;
            }

            next.x = current.x + 1;
            next.y = current.y - 1;
            if (!scanPoints.contains(next) && isBorderPoint(img, next.x, next.y)) {
                // 右上
                current = next;
                rightX = Math.max(rightX, next.x);
                upY = Math.min(upY, next.y);
                scanIndex = 0;
                continue;
            }


            next.x = current.x - 1;
            next.y = current.y + 1;
            if (!scanPoints.contains(next) && isBorderPoint(img, next.x, next.y)) {
                // 左下
                current = next;
                leftX = Math.min(leftX, next.x);
                downY = Math.max(downY, next.y);
                scanIndex = 0;
                continue;
            }

            next.x = current.x + 1;
            next.y = current.y + 1;
            if (!scanPoints.contains(next) && isBorderPoint(img, next.x, next.y)) {
                // 右下
                current = next;
                rightX = Math.max(rightX, next.x);
                downY = Math.max(downY, next.y);
                scanIndex = 0;
                continue;
            }

            if (scanIndex >= scanPoints.size()) {
                break;
            }
            current = scanPoints.get(scanIndex);
            scanIndex++;
        }

        // 上下左右已找到，直接切图
        int w = rightX - leftX, h = downY - upY;
        BufferedImage out = GraphicUtil.createImg(w, h, 0, 0, null);
        Graphics2D g2d = GraphicUtil.getG2d(out);
        for (int x = leftX; x <= rightX; x++) {
            for (int y = upY; y <= downY; y++) {
                g2d.setColor(new Color(img.getRGB(x, y), true));
                g2d.drawRect(x - leftX, y - upY, 1, 1);
                // 将裁剪出来的区域全部设置为空
                img.setRGB(x, y, 0);
            }
        }
        g2d.dispose();
        return out;
    }

    private boolean isBorderPoint(BufferedImage img, int x, int y) {
        if (bgColor(img.getRGB(x, y))) {
            return false;
        }

        if (y > 0) {
            // 若上面的一个点，是背景点，则是边界
            if (bgColor(img.getRGB(x, y - 1))) {
                return true;
            }
        }

        if (x > 0) {
            if (bgColor(img.getRGB(x - 1, y))) {
                return true;
            }
        }

        if (x < img.getWidth() - 1) {
            if (bgColor(img.getRGB(x + 1, y))) {
                return true;
            }
        }

        if (y < img.getHeight() - 1) {
            if (bgColor(img.getRGB(x, y + 1))) {
                return true;
            }
        }

        if (y > 0 && x > 0 && bgColor(img.getRGB(x - 1, y - 1))) {
            // 左上
            return true;
        }

        if (y > 0 && x < img.getWidth() - 1 && bgColor(img.getRGB(x + 1, y - 1))) {
            // 右上
            return true;
        }

        if (x > 0 && y < img.getHeight() - 1 && bgColor(img.getRGB(x - 1, y + 1))) {
            // 左下
            return true;
        }

        if (x < img.getWidth() - 1 && y < img.getHeight() - 1 && bgColor(img.getRGB(x + 1, y + 1))) {
            // right down
            return true;
        }

        return false;
    }

    private boolean bgColor(int rgbColor) {
        return new Color(rgbColor, true).getAlpha() == 0;
    }

    public void splitAndSave(String img, String prefix) throws Exception {
        List<BufferedImage> list = split(img);
        int i = 1;
        for (BufferedImage bi : list) {
            ImageIO.write(bi, "png", new File("d:/quick-media/out/" + prefix + "_" + i + ".png"));
            i++;
        }
    }


    @Test
    public void testSplit() throws Exception {
//        splitAndSave("D:\\quick-media\\resource\\assets\\bundle_item_icon\\img\\fe543675-0383-4438-a5d0-257176768173.64845.png", "fish");
        ImgSplitWrapper.build().setImg("D:\\quick-media\\resource\\assets\\bundle_item_icon\\img\\fe543675-0383-4438-a5d0-257176768173.64845.png")
                .setOutputPath("d:/quick-media/out")
                .setOutputImgName("ff")
                .setOutputIncrIndex(1).build().splitAndSave();
        System.out.println("over");
    }
}
