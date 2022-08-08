package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

/**
 * 资源引用类型svg
 *
 * @author YiHui
 * @date 2022/8/5
 */
public class SymbolSvgTag extends SvgTag {

    private String svgId;

    public SymbolSvgTag setSvgId(String svgId) {
        this.svgId = svgId;
        return this;
    }

    @Override
    public String toString() {
        return "<use xlink:href=\"#" + svgId + "\" x=\"" + x + "\" y=\"" + y + "\" width=\"" + w + "\" height=\"" + h + "\"/>";
    }
}
