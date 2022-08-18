package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

/**
 * 普通的二维码
 */
public class TextSvgTag extends SvgTag {

    private String txt;

    public String getTxt() {
        return txt;
    }

    public TextSvgTag setTxt(String txt) {
        this.txt = txt;
        return this;
    }

    @Override
    public String toString() {
        return "<text" + " fill=\"" + color + "\" font-size=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\">" + txt + "</text>";
    }
}
