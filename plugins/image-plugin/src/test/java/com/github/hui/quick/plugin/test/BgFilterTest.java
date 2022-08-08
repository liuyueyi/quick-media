package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.jhlabs.image.DoGFilter;
import com.jhlabs.image.ImageUtils;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BgFilterTest {
    private static final String PRE = "D://quick-media/";


    @Test
    public void testRemoveBg() throws IOException {
        BgFilter bgFilter = new BgFilter("d://MobileFile/wx.png");
        bgFilter.saveAsTransparentPng("d://MobileFile/op_wx.png");
        System.out.println("over");
    }

    @Test
    public void testRm() throws IOException {
        String img = "ddm2";
        String path = PRE + img + ".jpg";

        BufferedImage source = autoRemoveBg(path);
        ImageIO.write(source, "png", new File(PRE + "bgf_" + img + ".png"));
    }

    private BufferedImage autoRemoveBg(String path) throws IOException {
        BufferedImage detect = borderDetected(path);
        int w = detect.getWidth(), h = detect.getHeight();
        byte[][] rect = new byte[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int color = detect.getRGB(x, y);
                if (color == 0 || color == 0xFF000000) {
                    rect[x][y] = 0;
                } else {
                    rect[x][y] = 1;
                }
            }
        }

        // List<Integer> 记录的是一个封闭的边界
        List<List<Integer>> rectList = new ArrayList<>();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (rect[x][y] == 1) {

                }
            }
        }

        BufferedImage source = ImageLoadUtil.getImageByPath(path);
        source = ImageUtils.convertImageToARGB(source);
        for (int x = 0; x < detect.getWidth(); x++) {
            for (int y = 0; y < detect.getHeight(); y++) {
                if (!inBorder(x, y, w, h, rect)) {
                    source.setRGB(x, y, 0);
                }
            }
        }
        return source;
    }



    /**
     * 获取图片边框
     *
     * @param path
     * @return
     * @throws IOException
     */
    private BufferedImage borderDetected(String path) throws IOException {
        BufferedImage src = ImageLoadUtil.getImageByPath(path);
        src = ImageUtils.convertImageToARGB(src);

        // 高斯差分边缘检测
        DoGFilter filter = new DoGFilter();
        filter.setRadius1(0);
        filter.setRadius2(10);
        filter.setInvert(false);
        BufferedImage output = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

        return filter.filter(src, output);
    }


    /**
     * 边界判断，如果一个点，向上下左右个方向画一条射线，若每条与边框的交接点个数为奇数个，则表明在边框内
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param rect
     * @return
     */
    private boolean inBorder(int x, int y, int w, int h, byte[][] rect) {
        boolean left = false, right = false;
        for (int i = 0; i < w; i++) {
            if (i < x) {
                if (rect[i][y] == 1) {
                    left = true;
                }
            } else if (i == x) {
                if (rect[i][y] == 1) {
                    left = true;
                    right = true;
                    break;
                }
            } else {
                if (!left) {
                    return false;
                }
                if (rect[i][y] == 1) {
                    right = true;
                }
            }
        }

        if (!left || !right) {
            return false;
        }

        boolean up = false, down = false;
        for (int i = 0; i < h; i++) {
            if (i < y) {
                if (rect[x][i] == 1) {
                    up = true;
                }
            } else if (i == y) {
                if (rect[x][i] == 1) {
                    up = true;
                    down = true;
                    break;
                }
            } else {
                if (!up) {
                    return false;
                }
                if (rect[x][i] == 1) {
                    down = true;
                    break;
                }
            }
        }

        return up && down;
    }

    private void line(int x, int y, byte[][] border, List<Integer> line) {
    }

    private boolean inBorder(int x, int y, byte[][] border) {
        if (border[x][y] == 1) {
            // 直接是边框，返回
            return true;
        }

        int w = border.length;
        int h = border[0].length;

        // 左
        int cnt = intersectCnt(new Point(x, y), border, point -> point.x > 0 ? new Point(point.x - 1, point.y) : null);
        if ((cnt & 1) == 0) {
            // 偶数个交点，表示边框外
            return false;
        }

        // 右
        cnt = intersectCnt(new Point(x, y), border, point -> point.x < w - 1 ? new Point(point.x + 1, point.y) : null);
        if ((cnt & 1) == 0) {
            return false;
        }

        // 上
        cnt = intersectCnt(new Point(x, y), border, point -> point.y > 0 ? new Point(point.x, point.y - 1) : null);
        if ((cnt & 1) == 0) {
            return false;
        }

        // 下
        cnt = intersectCnt(new Point(x, y), border, point -> point.y < h - 1 ? new Point(point.x, point.y + 1) : null);
        if ((cnt & 1) == 0) {
            return false;
        }

        return true;
    }

    private int intersectCnt(Point point, byte[][] border, Function<Point, Point> nextCall) {
        int cnt = 0;
        Point nextPoint = nextCall.apply(point);
        while (nextPoint != null) {
            if (border[nextPoint.x][nextPoint.y] == 1) {
                cnt += 1;
            }
            nextPoint = nextCall.apply(nextPoint);
        }
        return cnt;
    }

    public static int toKey(int x, int y, int w) {
        return x * w + y;
    }
}
