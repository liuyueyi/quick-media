package com.github.hui.quick.plugin.qrcode.v3.core.render;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.QrType;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.BgRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.DetectRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.PreRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.RenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.BorderSvgTag;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.RectSvgTag;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.SvgTemplate;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.SymbolSvgTag;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.List;
import java.util.Optional;

/**
 * @author YiHui
 * @date 2022/8/5
 */
public class QrSvgRender {

    public static SvgTemplate drawQrInfo(List<RenderDot> dotList, QrCodeV3Options options) {
        options.setQrType(QrType.SVG);
        SvgTemplate svgTemplate = new SvgTemplate(options.getW(), options.getH());
        Optional.ofNullable(options.getDrawOptions().getResourcePool().getGlobalResource()).ifPresent(r -> svgTemplate.setDefs(r.getSvg()));

        // 若不存在特殊处理的0点，则使用背景色进行填充
        if (dotList.stream().noneMatch(s -> s instanceof BgRenderDot)) {
            svgTemplate.addTag(new RectSvgTag().setX(0).setY(0).setW(options.getW()).setH(options.getH()).setColor(ColorUtil.color2htmlColor(options.getDrawOptions().getBgColor())));
        }

        // 设置渲染
        dotList.forEach(dot -> {
            Optional.ofNullable(dot.getResource()).ifPresent(s -> svgTemplate.addSymbol(dot.getResource().getSvg()));
            if (dot instanceof PreRenderDot) {
                svgTemplate.setCurrentColor(options.getDrawOptions().getPreColor());
            } else if (dot instanceof BgRenderDot) {
                svgTemplate.setCurrentColor(options.getDrawOptions().getBgColor());
            } else if (dot instanceof DetectRenderDot) {
                // 探测图形
                DetectRenderDot dDot = (DetectRenderDot) dot;
                Color color = BooleanUtils.isTrue(dDot.getOutBorder()) ? options.getDetectOptions().getOutColor() : options.getDetectOptions().getInColor();
                if (dot.getResource() != null && dot.getResource().getDrawColor() != null) {
                    // 若指定绘制资源的颜色，则使用它覆盖外部的outBorderColor, inColor
                    color = dot.getResource().getDrawColor();
                }
                svgTemplate.setCurrentColor(color);
                if (BooleanUtils.isTrue(options.getDetectOptions().getSpecial())
                        && (dot.getResource() == null || dot.getResource().getSvgInfo() == null)) {
                    svgTemplate.setCurrentColor(color == null ? Color.BLACK: color);
                    if (dot.getResource() == null || dot.getResource().getDrawStyle() == null) {
                        // 当探测图形特殊处理，即不与指定前置图样式相同时；首先判断是否有指定特殊的探测图形资源，没有时，则走默认的黑色矩形框设置
                        dot.setResource(new QrResource().setDrawStyle(DrawStyle.RECT));
                    }
                }
            }
            // 当特殊指定了绘制颜色时，使用其覆盖默认的颜色
            if (dot.getResource() != null && dot.getResource().getDrawColor() != null) {
                svgTemplate.setCurrentColor(dot.getResource().getDrawColor());
            }
            options.getDrawOptions().getDrawStyle().drawAsSvg(svgTemplate, dot);
        });

        return svgTemplate;
    }


    /**
     * 绘制logo
     *
     * @param svgTemplate
     * @param options
     */
    public static void drawLogo(SvgTemplate svgTemplate, QrCodeV3Options options) {
        QrResource logo = options.getLogoOptions().getLogo();
        if (logo == null || (StringUtils.isBlank(logo.getSvg()) && logo.getDrawStyle() == null)) {
            // 无svg格式的logo，直接返回
            return;
        }

        // logo的宽高，避免长图的变形，这里采用等比例缩放的策略
        int qrWidth = svgTemplate.getWidth();
        int qrHeight = svgTemplate.getHeight();

        int logoRate = options.getLogoOptions().getRate();
        int logoWidth = (qrWidth << 1) / logoRate;
        int logoHeight = (qrHeight << 1) / logoRate;
        int logoOffsetX = (qrWidth - logoWidth) >> 1;
        int logoOffsetY = (qrHeight - logoHeight) >> 1;

        if (options.getLogoOptions().getBorderColor() != null) {
            // 绘制圆角边框
            svgTemplate.addTag(new BorderSvgTag()
                    .setFillColor(ColorUtil.color2htmlColor(options.getDrawOptions().getBgColor()))
                    .setW(logoWidth).setH(logoHeight).setX(logoOffsetX).setY(logoOffsetY)
                    .setColor(ColorUtil.color2htmlColor(options.getLogoOptions().getBorderColor())));
        }

        if (logo.getDrawStyle() != null && logo.getDrawStyle() != DrawStyle.SVG) {
            // 支持绘制样式的logo渲染
            if (logo.getDrawColor() != null) {
                svgTemplate.setCurrentColor(logo.getDrawColor());
            }
            RenderDot logoDot = new PreRenderDot()
                    .setW(logoWidth)
                    .setH(logoHeight)
                    .setX(logoOffsetX)
                    .setY(logoOffsetY)
                    .setSize(1)
                    .setResource(logo);
            logo.getDrawStyle().drawAsSvg(svgTemplate, logoDot);
        } else {
            svgTemplate.addSymbol(logo.getSvg());
            svgTemplate.addTag(new SymbolSvgTag().setSvgId(logo.getSvgId())
                    .setX(logoOffsetX)
                    .setY(logoOffsetY)
                    .setW(logoWidth)
                    .setH(logoHeight)
            );
        }
    }
}
