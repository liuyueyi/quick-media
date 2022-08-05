package com.github.hui.quick.plugin.qrcode.v3.core.render;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.QrType;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.BgRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.DetectRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.PreRenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.render.RenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.RectSvgTag;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.SvgTemplate;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import org.apache.commons.lang3.BooleanUtils;

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
                if (BooleanUtils.isTrue(((DetectRenderDot) dot).getOutBorder())) {
                    svgTemplate.setCurrentColor(options.getDetectOptions().getOutColor());
                } else {
                    svgTemplate.setCurrentColor(options.getDetectOptions().getInColor());
                }
            }
            options.getDrawOptions().getDrawStyle().drawAsSvg(svgTemplate, dot);
        });

        return svgTemplate;
    }

}
