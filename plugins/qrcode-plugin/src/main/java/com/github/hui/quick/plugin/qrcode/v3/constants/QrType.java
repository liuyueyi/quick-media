package com.github.hui.quick.plugin.qrcode.v3.constants;

/**
 * @author YiHui
 * @date 2022/8/5
 */
public enum QrType {
    /**
     * 图
     */
    IMG("png,jpg,jpeg,webp"),
    /**
     * 动图
     */
    GIF("gif"),
    /**
     * svg格式文本
     */
    SVG("svg"),
    /**
     * 二维数组文本
     */
    STR("txt"),
    ;

    private String suffix;

    QrType(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
