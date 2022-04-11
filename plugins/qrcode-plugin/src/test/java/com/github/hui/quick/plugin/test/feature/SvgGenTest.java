package com.github.hui.quick.plugin.test.feature;

import com.github.hui.quick.plugin.qrcode.helper.QrCodeGenerateHelper;
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
            y += y;
            return "<circle cx=\"" + x + "\" cy=\"" + y + "\" r=\"" + w + "\" fill=\"" + color + "\"/>";
        }
    }

    public static class RectSvgTag extends SvgTag {
        @Override
        public String toString() {
            return "<rect" + " fill=\"" + color + "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\"/>";
        }
    }

    @Test
    public void testRenderToSvg() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            int size = 300;
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
                        SvgTag tag = new SvgTag();
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
