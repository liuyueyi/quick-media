package com.hust.hui.quickmedia.common.img.merge.template;

import com.hust.hui.quickmedia.common.util.FontUtil;

import java.awt.*;

/**
 * Created by yihui on 2017/10/13.
 */
public interface QrCodeCardTemplate {



    // space
    int border_space = 20;
    int double_border_space = border_space << 1;
    int h = 260 + double_border_space;
    int w = 560 + double_border_space;



    // text info
    int text_size = (w - double_border_space) * 3 / 5 - double_border_space;
    int text_x = border_space;
    int text_y = double_border_space;
    int text_logo_size = 100;
    int text_line_space = 10;
    Font text_nameFont = FontUtil.BIG_BOLD_DEFAULT_FONT;
    Color text_nameFont_color = Color.BLACK;
    Font text_descFont = FontUtil.DEFAULT_FONT;
    Color text_descFont_color = Color.GRAY;


    // line info
    int line_w = 40;
    int line_h = h - double_border_space - double_border_space;
    int line_x = text_x + text_size;
    int line_y = double_border_space;
    Color line_color = Color.LIGHT_GRAY;



    // qrcode info
    int qrcode_info_w = (w - double_border_space) * 2 / 5;
    int qrcode_size = 220;
    int qrcode_x = line_x + line_w + border_space / 2;
    int qrcode_y = border_space;
    int qrcode_info_padding = 5;
    Font qrcode_info_font = FontUtil.DEFAULT_FONT;
    Color qrcode_info_color = Color.GRAY;

}
