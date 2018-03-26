package com.github.hui.quick.plugin.image.util;

import com.github.hui.quick.plugin.base.FileReadUtil;
import com.github.hui.quick.plugin.base.GraphicUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yihui on 2017/9/6.
 */
public class FontUtil {

    public static Font DEFAULT_FONT;

    public static Font BIG_DEFAULT_FONT;

    public static Font BIG_BOLD_DEFAULT_FONT;

    public static Font BIG_ITALIC_DEFAULT_FONT;

    public static Font SMALLER_DEFAULT_FONT;

    public static Font SMALLER_DEFAULT_ITALIC_FONT;

    static {
        try {
            DEFAULT_FONT = getFont("font/txlove.ttf", Font.PLAIN, 18);

            BIG_DEFAULT_FONT = getFont("font/txlove.ttf", Font.PLAIN, 22);
            BIG_BOLD_DEFAULT_FONT = getFont("font/txlove.ttf", Font.BOLD, 22);
            BIG_ITALIC_DEFAULT_FONT = getFont("font/txlove.ttf", Font.ITALIC, 22);

            SMALLER_DEFAULT_FONT = getFont("font/txlove.ttf", Font.PLAIN, 16);
            SMALLER_DEFAULT_ITALIC_FONT = getFont("font/txlove.ttf", Font.ITALIC, 16);
        } catch (Exception e) {
            DEFAULT_FONT = new Font("宋体", Font.PLAIN, 18);

            BIG_DEFAULT_FONT = new Font("宋体", Font.PLAIN, 22);
            BIG_BOLD_DEFAULT_FONT = new Font("宋体", Font.BOLD, 22);
            BIG_ITALIC_DEFAULT_FONT = new Font("宋体", Font.ITALIC, 22);

            SMALLER_DEFAULT_FONT = new Font("宋体", Font.PLAIN, 16);
            SMALLER_DEFAULT_ITALIC_FONT = new Font("宋体", Font.ITALIC, 16);
        }
    }


    /**
     * 根据字体文件来生成Font类
     *
     * @param fontPath 字体路径
     * @param style    样式
     * @param size     大小
     * @return
     * @throws IOException
     * @throws FontFormatException
     */
    public static Font getFont(String fontPath, int style, int size) throws IOException, FontFormatException {
        InputStream inputStream = FileReadUtil.getStreamByFileName(fontPath);
        Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        return font.deriveFont(style, size);
    }


    public static FontMetrics getFontMetric(Font font) {
        BufferedImage bf = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = GraphicUtil.getG2d(bf);
        g2d.setFont(font);
        return g2d.getFontMetrics();
    }

}
