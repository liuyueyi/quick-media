package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

/**
 * 图片标签
 *
 * @author YiHui
 * @date 2022/10/21
 */
public class ImgSvgTag extends SvgTag {
    protected String href;

    public ImgSvgTag setHref(String href) {
        this.href = href;
        return this;
    }

    @Override
    public String toString() {
        return "<image xlink:href=\"" + href + "\" " + super.toString() + "></image>";
    }
}
