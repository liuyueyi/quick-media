package com.github.hui.quick.plugin.image.util;

import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yihui on 2017/9/6.
 */
public class FontUtil {
    public static final String DEFAULT_FONT_NAME = "宋体";
    public static String SELF_DEFINE_FONT;

    /**
     * 对于自定义的字体文件，一般是 ttf/otf文件名后缀，文件名中必然包含 点号，基于此来判断传入的是自定义字体的文件名，还是字体名
     */
    public static final String FONT_PATH_SYMBOL = ".";

    public static Font DEFAULT_FONT;

    public static Font BIG_DEFAULT_FONT;

    public static Font BIG_BOLD_DEFAULT_FONT;

    public static Font BIG_ITALIC_DEFAULT_FONT;

    public static Font SMALLER_DEFAULT_FONT;

    public static Font SMALLER_DEFAULT_ITALIC_FONT;

    static {
        // 从系统参数中获取默认的字体文件
        SELF_DEFINE_FONT = System.getProperty("quick.media.font.default");
        DEFAULT_FONT = getFontOrDefault(SELF_DEFINE_FONT, Font.PLAIN, 18);

        BIG_DEFAULT_FONT = getFontOrDefault(SELF_DEFINE_FONT, Font.PLAIN, 22);
        BIG_BOLD_DEFAULT_FONT = getFontOrDefault(SELF_DEFINE_FONT, Font.BOLD, 22);
        BIG_ITALIC_DEFAULT_FONT = getFontOrDefault(SELF_DEFINE_FONT, Font.ITALIC, 22);

        SMALLER_DEFAULT_FONT = getFontOrDefault(SELF_DEFINE_FONT, Font.PLAIN, 16);
        SMALLER_DEFAULT_ITALIC_FONT = getFontOrDefault(SELF_DEFINE_FONT, Font.ITALIC, 16);
    }

    /**
     * 创建字体，当不存在时使用默认的宋体
     *
     * @param fontName
     * @param style
     * @param size
     * @return
     */
    public static Font getFontOrDefault(String fontName, int style, int size) {
        try {
            if (StringUtils.isBlank(fontName)) {
                fontName = DEFAULT_FONT_NAME;
            }

            return fontName.contains(FONT_PATH_SYMBOL) ? getFont(fontName, style, size) : new Font(fontName, style, size);
        } catch (Exception e) {
            return new Font(DEFAULT_FONT_NAME, style, size);
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
        try (InputStream inputStream = FileReadUtil.getStreamByFileName(fontPath)) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            return font.deriveFont(style, size);
        }
    }


    public static FontMetrics getFontMetric(Font font) {
        BufferedImage bf = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = GraphicUtil.getG2d(bf);
        g2d.setFont(font);
        return g2d.getFontMetrics();
    }

}
