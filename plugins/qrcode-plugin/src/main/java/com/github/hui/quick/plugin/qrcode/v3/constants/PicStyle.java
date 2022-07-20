package com.github.hui.quick.plugin.qrcode.v3.constants;

import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;

/**
 * @author yihui
 * @date 22/7/20
 */
public enum  PicStyle {
    ROUND, NORMAL, CIRCLE;

    public static PicStyle getStyle(String name) {
        return PicStyle.valueOf(name.toUpperCase());
    }
}
