package com.github.hui.quick.plugin.qrcode.v3.templates.svg;

/**
 * @author
 * @date 2022/6/10
 */
public class ImgSvgTag extends SvgTag {

    protected String img;

    public ImgSvgTag setImg(String img) {
        this.img = img;
        return this;
    }

    @Override
    public String toSvgTag() {
        return "<image" + " href=\"" + img + "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\"/>";
    }
}
