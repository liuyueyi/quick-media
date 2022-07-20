package com.github.hui.quick.plugin.qrcode.v3.constants;

import com.github.hui.quick.plugin.qrcode.constants.QuickQrUtil;
import com.github.hui.quick.plugin.qrcode.entity.DotSize;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * 绘制二维码信息的样式
 */
public enum DrawStyle {
    RECT { // 矩形

        @Override
        public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
            g2d.fillRect(x, y, w, h);
        }

        @Override
        public boolean expand(DotSize dotSize) {
            return dotSize.getRow() == dotSize.getCol();
        }
    }, MINI_RECT {
        @Override
        public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
            int offsetX = w / 5, offsetY = h / 5;
            int width = w - offsetX * 2, height = h - offsetY * 2;
            g2d.fillRect(x + offsetX, y + offsetY, width, height);
        }

        @Override
        public boolean expand(DotSize dotSize) {
            return false;
        }
    }, CIRCLE { // 圆点

        @Override
        public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
            g2d.fill(new Ellipse2D.Float(x, y, w, h));
        }

        @Override
        public boolean expand(DotSize dotSize) {
            return dotSize.getRow() == dotSize.getCol();
        }
    }, TRIANGLE { // 三角形

        @Override
        public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
            int px[] = {x, x + (w >> 1), x + w};
            int py[] = {y + w, y, y + w};
            g2d.fillPolygon(px, py, 3);
        }

        @Override
        public boolean expand(DotSize expandType) {
            return false;
        }
    }, DIAMOND { // 五边形-钻石

        @Override
        public void draw(Graphics2D g2d, int x, int y, int size, int h, BufferedImage img, String txt) {
            int cell4 = size >> 2;
            int cell2 = size >> 1;
            int px[] = {x + cell4, x + size - cell4, x + size, x + cell2, x};
            int py[] = {y, y, y + cell2, y + size, y + cell2};
            g2d.fillPolygon(px, py, 5);
        }

        @Override
        public boolean expand(DotSize dotSize) {
            return dotSize.getRow() == dotSize.getCol();
        }
    }, SEXANGLE { // 六边形

        @Override
        public void draw(Graphics2D g2d, int x, int y, int size, int h, BufferedImage img, String txt) {
            int add = size >> 2;
            int px[] = {x + add, x + size - add, x + size, x + size - add, x + add, x};
            int py[] = {y, y, y + add + add, y + size, y + size, y + add + add};
            g2d.fillPolygon(px, py, 6);
        }

        @Override
        public boolean expand(DotSize dotSize) {
            return dotSize.getRow() == dotSize.getCol();
        }
    }, OCTAGON { // 八边形

        @Override
        public void draw(Graphics2D g2d, int x, int y, int size, int h, BufferedImage img, String txt) {
            int add = size / 3;
            int px[] = {x + add, x + size - add, x + size, x + size, x + size - add, x + add, x, x};
            int py[] = {y, y, y + add, y + size - add, y + size, y + size, y + size - add, y + add};
            g2d.fillPolygon(px, py, 8);
        }

        @Override
        public boolean expand(DotSize dotSize) {
            return dotSize.getRow() == dotSize.getCol();
        }
    }, IMAGE { // 自定义图片

        @Override
        public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
            g2d.drawImage(img.getScaledInstance(w, h, Image.SCALE_SMOOTH), x, y, null);
        }

        @Override
        public boolean expand(DotSize expandType) {
            return true;
        }
    },

    TXT { // 文字绘制

        @Override
        public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
            Font oldFont = g2d.getFont();
            if (oldFont.getSize() != w) {
                Font newFont = QuickQrUtil.font(oldFont.getName(), oldFont.getStyle(), w);
                g2d.setFont(newFont);
            }
            g2d.drawString(txt, x, y + w);
            g2d.setFont(oldFont);
        }

        @Override
        public boolean expand(DotSize dotSize) {
            return dotSize.getRow() == dotSize.getCol();
        }
    },

    IMAGE_V2 {
        @Override
        public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
            g2d.drawImage(img.getScaledInstance(w, h, Image.SCALE_SMOOTH), x, y, null);
        }

        @Override
        public boolean expand(DotSize dotSize) {
            return true;
        }
    };

    private static Map<String, DrawStyle> map;

    static {
        map = new HashMap<>(10);
        for (DrawStyle style : DrawStyle.values()) {
            map.put(style.name(), style);
        }
    }

    public static DrawStyle getDrawStyle(String name) {
        if (name == null || name.isEmpty()) { // 默认返回矩形
            return RECT;
        }


        DrawStyle style = map.get(name.toUpperCase());
        return style == null ? RECT : style;
    }


    public abstract void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt);


    /**
     * 返回是否支持绘制自定义图形的扩展
     *
     * @param dotSize
     * @return
     */
    public abstract boolean expand(DotSize dotSize);
}
