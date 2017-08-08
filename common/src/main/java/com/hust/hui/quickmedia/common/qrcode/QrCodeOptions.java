package com.hust.hui.quickmedia.common.qrcode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 2017/7/17.
 */
@Data
public class QrCodeOptions {
    /**
     * 塞入二维码的信息
     */
    private String msg;


    /**
     * 二维码的背景图
     */
    private String background;

    /**
     * 背景图宽
     */
    private Integer bgW;


    /**
     * 背景图高
     */
    private Integer bgH;


    /**
     * 二维码中间的logo
     */
    private String logo;


    /**
     * logo的样式， 目前支持圆角+普通
     */
    private LogoStyle logoStyle;

    /**
     * logo 的边框背景色
     */
    private Color logoBgColor;


    /**
     * 生成二维码的宽
     */
    private Integer w;


    /**
     * 生成二维码的高
     */
    private Integer h;


    /**
     * 生成二维码的颜色
     */
    private MatrixToImageConfig matrixToImageConfig;


    /**
     * 三个位置探测图形的外框色和内框色
     */
    private MatrixToImageConfig detectCornerColor;


    private Map<EncodeHintType, Object> hints;


    /**
     * 生成二维码图片的格式 png, jpg
     */
    private String picType;


    /**
     * 绘制二维码的样式
     */
    private DrawStyle drawStyle;


    /**
     * 绘制二维码的图片链接
     */
    private String drawImg;


    public enum LogoStyle {
        ROUND,
        NORMAL;


        public static LogoStyle getStyle(String name) {
            if ("ROUND".equalsIgnoreCase(name)) {
                return ROUND;
            } else {
                return NORMAL;
            }
        }
    }


    public enum DrawStyle {
        RECT { // 矩形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, BufferedImage img) {
                g2d.fillRect(x, y, size, size);
            }
        },
        CIRCLE {
            // 圆点
            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, BufferedImage img) {
                g2d.fill(new Ellipse2D.Float(x, y, size, size));
            }
        },
        TRIANGLE {
            // 三角形
            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, BufferedImage img) {
                int px[] = {x, x + (size >> 1), x + size};
                int py[] = {y + size, y, y + size};
                g2d.fillPolygon(px, py, 3);
            }
        },
        DIAMOND {
            // 五边形-钻石
            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, BufferedImage img) {
                int cell4 = size >> 2;
                int cell2 = size >> 1;
                int px[] = {x + cell4, x + size - cell4, x + size, x + cell2, x};
                int py[] = {y, y, y + cell2, y + size, y + cell2};
                g2d.fillPolygon(px, py, 5);
            }
        },
        SEXANGLE {
            // 六边形
            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, BufferedImage img) {
                int add = size >> 2;
                int px[] = {x + add, x + size - add, x + size, x + size - add, x + add, x};
                int py[] = {y, y, y + add + add, y + size, y + size, y + add + add};
                g2d.fillPolygon(px, py, 6);
            }
        },
        OCTAGON {
            // 八边形
            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, BufferedImage img) {
                int add = size / 3;
                int px[] = {x + add, x + size - add, x + size, x + size, x + size - add, x + add, x, x};
                int py[] = {y, y, y + add, y + size - add, y + size, y + size, y + size - add, y + add};
                g2d.fillPolygon(px, py, 8);
            }
        },
        IMAGE {
            // 自定义图片
            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, BufferedImage img) {
                g2d.drawImage(img, x, y, size, size, null);
            }
        },;

        private static Map<String, DrawStyle> map;

        static {
            map = new HashMap<>(6);
            for (DrawStyle style : DrawStyle.values()) {
                map.put(style.name(), style);
            }
        }

        public static DrawStyle getDrawStyle(String name) {
            if (StringUtils.isBlank(name)) { // 默认返回矩形
                return RECT;
            }


            DrawStyle style = map.get(name.toUpperCase());
            return style == null ? RECT : style;
        }


        public abstract void draw(Graphics2D g2d, int x, int y, int size, BufferedImage img);
    }
}
