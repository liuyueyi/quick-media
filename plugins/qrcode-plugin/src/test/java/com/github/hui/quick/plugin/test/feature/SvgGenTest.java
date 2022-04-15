package com.github.hui.quick.plugin.test.feature;

import com.github.hui.quick.plugin.qrcode.helper.QrCodeGenerateHelper;
import com.github.hui.quick.plugin.qrcode.util.NumUtil;
import com.github.hui.quick.plugin.qrcode.wrapper.BitMatrixEx;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yihui
 * @date 2022/4/11
 */
public class SvgGenTest {

    public static class SvgTemplate {
        private int width;
        private int height;

        private List<SvgTag> tagList;

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("<svg width=\"");
            builder.append(width)
                    .append("\" height=\"")
                    .append(height)
                    .append("\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">")
                    .append("\n")
            ;
            for (SvgTag tag : tagList) {
                builder.append("\t").append(tag.toString()).append("\n");
            }
            builder.append("\n</svg>");
            return builder.toString();
        }
    }

    public static class SvgTag {
        protected String color;
        protected int x;
        protected int y;
        protected int w;
        protected int h;
    }

    public static class CircleSvgTag extends SvgTag {
        @Override
        public String toString() {
            int r = Math.floorDiv(w, 2);
            x += r;
            y += r;
            return "<circle cx=\"" + x + "\" cy=\"" + y + "\" r=\"" + r + "\" fill=\"" + color + "\"/>";
        }
    }

    /**
     * 普通的二维码
     */
    public static class RectSvgTag extends SvgTag {
        @Override
        public String toString() {
            return "<rect" + " fill=\"" + color + "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\"/>";
        }
    }

    /**
     * 矩形旋转45°
     */
    public static class RotateRectSvgTag extends SvgTag {
        @Override
        public String toString() {
            int size = w / 2;
            StringBuilder points = new StringBuilder();
            points.append(x + size).append(",").append(y).append(" ")
                    .append(x + size * 2).append(",").append(y + size).append(" ")
                    .append(x + size).append(",").append(y + size * 2).append(" ")
                    .append(x).append(",").append(y + size).append(" ");

            return "<polygon points=\"" + points.toString() + "\"  style=\"fill:" + color + ";fill-rule:nonzero;\" />";
        }
    }

    /**
     * 圆角矩形
     */
    public static class RoundRectSvgTag extends SvgTag {
        /**
         * 圆角比例, 默认 = 4
         */
        protected int roundRate = 4;

        @Override
        public String toString() {
            int round = Math.floorDiv(w, roundRate);
            return "<rect" + " fill=\"" + color + "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\" rx=\"" + round + "\" ry=\"" + round + "\" />";
        }
    }

    /**
     * 留一点空隙的矩形框
     */
    public static class MiniRectSvgTag extends SvgTag {
        @Override
        public String toString() {
            int offsetX = w / 6, offsetY = h / 6;
            w -= offsetX * 2;
            h -= offsetY * 2;
            x += offsetX;
            y += offsetY;
            return "<rect" + " fill=\"" + color + "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\"/>";
        }
    }

    /**
     * 五角星
     */
    public static class StarSvgTag extends SvgTag {
        @Override
        public String toString() {
            float rate = NumUtil.divWithScaleFloor(w, 20, 2);
            StringBuilder points = new StringBuilder();
            points.append(x + NumUtil.multiplyWithScaleFloor(10, rate, 2)).append(",").append(y + rate).append(" ")
                    .append(x + NumUtil.multiplyWithScaleFloor(4, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(19.8f, rate, 2)).append(" ")
                    .append(x + NumUtil.multiplyWithScaleFloor(19, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(7.8f, rate, 2)).append(" ")
                    .append(x + rate).append(",").append(y + NumUtil.multiplyWithScaleFloor(7.8f, rate, 2)).append(" ")
                    .append(x + NumUtil.multiplyWithScaleFloor(16, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(19.8f, rate, 2)).append(" ");

            return "<polygon points=\"" + points.toString() + "\"  style=\"fill:" + color + ";fill-rule:nonzero;\" />";
        }
    }

    /**
     * 五边形
     */
    public static class PentagonSvgTag extends SvgTag {
        @Override
        public String toString() {
            float rate = NumUtil.divWithScaleFloor(w, 30, 2);
            StringBuilder points = new StringBuilder();
            points.append(x + NumUtil.multiplyWithScaleFloor(15, rate, 2)).append(",").append(y).append(" ")
                    .append(x + NumUtil.multiplyWithScaleFloor(30, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(15, rate, 2)).append(" ")
                    .append(x + NumUtil.multiplyWithScaleFloor(20, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(30, rate, 2)).append(" ")
                    .append(x + NumUtil.multiplyWithScaleFloor(10, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(30, rate, 2)).append(" ")
                    .append(x).append(",").append(y + NumUtil.multiplyWithScaleFloor(15, rate, 2)).append(" ");

            return "<polygon points=\"" + points.toString() + "\"  style=\"fill:" + color + ";fill-rule:nonzero;\" />";
        }
    }

    /**
     * 六边形器
     */
    public static class HexagonSvgTag extends SvgTag {
        @Override
        public String toString() {
            float rate = NumUtil.divWithScaleFloor(w, 30, 2);
            StringBuilder points = new StringBuilder();
            points.append(x + NumUtil.multiplyWithScaleFloor(10, rate, 2)).append(",").append(y).append(" ")
                    .append(x + NumUtil.multiplyWithScaleFloor(20, rate, 2)).append(",").append(y).append(" ")
                    .append(x + NumUtil.multiplyWithScaleFloor(30, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(15, rate, 2)).append(" ")
                    .append(x + NumUtil.multiplyWithScaleFloor(20, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(30, rate, 2)).append(" ")
                    .append(x + NumUtil.multiplyWithScaleFloor(10, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(30, rate, 2)).append(" ")
                    .append(x).append(",").append(y + NumUtil.multiplyWithScaleFloor(15, rate, 2)).append(" ");

            return "<polygon points=\"" + points.toString() + "\"  style=\"fill:" + color + ";fill-rule:nonzero;\" />";
        }
    }

    @Test
    public void testRenderToSvg() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            int size = 400;
            QrCodeOptions qrCodeOptions = QrCodeGenWrapper.of(msg)
                    .setW(size)
                    // 定位点(探测图形)外边颜色
                    .setDetectOutColor(Color.CYAN)
                    // 定位点内部颜色
                    .setDetectInColor(Color.RED)
                    // 二维码着色点
                    .setDrawPreColor(Color.BLUE)
                    // 探测图形特殊处理
                    .setDetectSpecial()
                    // 二维码背景图
                    .setDrawBgColor(0xffffffff).build();


            BitMatrixEx bitMatrix = QrCodeGenerateHelper.encode(qrCodeOptions);
            int matrixSize = bitMatrix.getByteMatrix().getWidth();
            int cellSize = Math.floorDiv(size, matrixSize);

            List<SvgTag> list = new ArrayList<>();
            for (int x = 0; x < bitMatrix.getByteMatrix().getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getByteMatrix().getHeight(); y++) {
                    if (bitMatrix.getByteMatrix().get(x, y) == 1) {
                        SvgTag tag = new HexagonSvgTag();
                        tag.x = x * cellSize;
                        tag.y = y * cellSize;
                        tag.w = cellSize;
                        tag.h = cellSize;
                        tag.color = "#000000";
                        list.add(tag);
                    }
                }
            }

            SvgTemplate svgTemplate = new SvgTemplate();
            svgTemplate.width = size;
            svgTemplate.height = size;
            svgTemplate.tagList = list;

            System.out.println(svgTemplate);

        } catch (Exception e) {
        }
    }

}
