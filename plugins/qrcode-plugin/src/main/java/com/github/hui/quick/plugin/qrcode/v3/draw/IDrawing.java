package com.github.hui.quick.plugin.qrcode.v3.draw;

import com.github.hui.quick.plugin.qrcode.v3.entity.render.RenderDot;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.SvgTemplate;

import java.awt.*;

/**
 * 具体的渲染实现类，可以定义各种绘制方式
 *
 * @author YiHui
 * @date 2022/8/8
 */
public interface IDrawing {
    /**
     * 绘制为图片
     *
     * @param g2d
     * @param renderDot
     */
    void drawAsImg(Graphics2D g2d, RenderDot renderDot);

    /**
     * 渲染为svg
     *
     * @param svg
     * @param renderDot
     */
    void drawAsSvg(SvgTemplate svg, RenderDot renderDot);
}
