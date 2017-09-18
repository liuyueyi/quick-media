package com.hust.hui.quickmedia.common.util;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yihui on 2017/9/6.
 */
public class FontUtil {

    public static Font DEFAULT_FONT;

    static {
        try {
            DEFAULT_FONT = getFont("font/txlove.ttf", Font.PLAIN, 18);
        } catch (Exception e) {
            DEFAULT_FONT = new Font("宋体", Font.PLAIN, 18);
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

}
