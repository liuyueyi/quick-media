package com.github.hui.quick.plugin.image.helper;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.image.util.StrUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

/**
 * 图片像素画处理
 *
 * @author yihui
 * @data 2021/11/7
 */
public class ImgPixelHelper {
    public static String SVG_START = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + "<svg xmlns=\"http://www.w3.org/2000/svg\"\n" + "        viewBox=\"0 0 {width} {height}\"\n" + "        style=\"width: 100%; height: 100%; overflow: auto; fill: {BG_COLOR}\">\n" + "    <script type=\"text/javascript\"><![CDATA[\n" + "window.addEventListener('load',function() {\n" + "    var bounding_rect = document.getElementById(\"bounding-rect\");\n" + "    var text = document.getElementById(\"ascii\");\n" + "    var bb_text = text.getBBox();\n" + "    // Change the font size so that the height and width match up\n" + "    var font_size = Math.round(1e3 * bb_text.height / bb_text.width) / 1e3;\n" + "    text.setAttribute(\"font-size\", font_size + \"px\");\n" + "    // Adjust size of bounding rectangle\n" + "    bb_text = text.getBBox();\n" + "    bounding_rect.setAttribute(\"width\", bb_text.width);\n" + "    bounding_rect.setAttribute(\"height\", bb_text.height);\n" + "}, false);\n" + "    ]]></script>\n" + "    <style type=\"text/css\">\n" + "    text.ascii-art {\n" + "        user-select: none;\n" + "        whiteSpace: \"pre\";\n" + "        fill: {FONT_COLOR};\n" + "        -webkit-user-select:none;\n" + "        -khtml-user-select:none;\n" + "        -moz-user-select:none;\n" + "        -ms-user-select:none;\n" + "    }\n" + "    </style>\n" + "    <rect x=\"0\" y=\"0\" height=\"100%\" width=\"100%\" id=\"bounding-rect\"/>\n" + "    <text x=\"0\" y=\"0\" id=\"ascii\" font-family=\"monospace, courier\" text-anchor=\"start\"\n" + "        font-size=\"1px\" class=\"ascii-art\">";
    public static String SVG_END = "\n  </text></svg>";

    public static String SIMPLE_SVG_START = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 {width} {height}\" style=\"width: 100%; height: 100%; overflow: auto; fill: {BG_COLOR}\">\n";
    public static String SIMPLE_SVG_END = "\n</svg>";

    /**
     * 基于颜色的灰度值，获取对应的字符
     *
     * <a href='https://blog.csdn.net/cuixiping/article/details/40291389'/>
     *
     * @param g
     * @return
     */
    public static char toChar(String ascii, Color g) {
        // 原始灰度值公式如下
        // double gray = 0.299 * g.getRed() + 0.587 * g.getGreen() + 0.114 * g.getBlue();
        // 使用下面这种用于提高计算效率
        int gray = (19595 * g.getRed() + 38469 * g.getGreen() + 7472 * g.getBlue()) >> 16;
        return ascii.charAt(gray * (ascii.length() - 1) / 255);
    }

    /**
     * 获取某一块的所有像素的颜色
     *
     * @param image
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public static int[] getPixels(BufferedImage image, int x, int y, int w, int h) {
        int[] colors = new int[w * h];
        int idx = 0;
        for (int i = y; (i < h + y) && (i < image.getHeight()); i++) {
            for (int j = x; (j < w + x) && (j < image.getWidth()); j++) {
                int color = image.getRGB(j, i);
                colors[idx++] = color;
            }
        }
        return colors;
    }

    /**
     * 字符转svg矢量图
     *
     * @param lines
     * @param bgColor
     * @param fontColor
     * @return
     */
    public static String ascii2svg(List<String> lines, String bgColor, String fontColor) {
        StringBuilder builder = new StringBuilder();
        int height = lines.size();
        int width = lines.stream().max(Comparator.comparingInt(String::length)).get().length();
        builder.append(StrUtil.replace(SVG_START, "{width}", String.valueOf(width), "{height}", String.valueOf(height), "{BG_COLOR}", bgColor, "{FONT_COLOR}", fontColor));

        float dy = 100.0f / height;
        String start = String.format("<tspan x=\"0\" dy=\"%.3f%%\" textLength=\"100%%\" xml:space=\"preserve\">", dy);
        String end = "</tspan>";
        for (String line : lines) {
            builder.append(start).append(StrUtil.replace(line, "&", "&amp;", "\"", "&quot;", "<", "&lt;", ">", "&gt;")).append(end).append("\n");
        }

        builder.append(SVG_END);
        return builder.toString();
    }

    public static Color getAverageColor(int[] colors) {
        int a = 0, r = 0, g = 0, b = 0;
        for (int color : colors) {
            Color c = new Color(color, true);
            a += c.getAlpha();
            r += c.getRed();
            g += c.getGreen();
            b += c.getBlue();
        }
        int size = colors.length;
        a = Math.round(a / (float) size);
        r = Math.round(r / (float) size);
        g = Math.round(g / (float) size);
        b = Math.round(b / (float) size);
        return new Color(r, g, b, a);
    }

    public static String getSvgCell(Color color, int x, int y, int size) {
        String htmlColor = ColorUtil.color2htmlColor(color);
        return "<rect" + " fill=\"" + htmlColor + "\" height=\"" + size + "\" width=\"" + size + "\" y=\"" + y + "\" x=\"" + x + "\"/>";
    }
}
