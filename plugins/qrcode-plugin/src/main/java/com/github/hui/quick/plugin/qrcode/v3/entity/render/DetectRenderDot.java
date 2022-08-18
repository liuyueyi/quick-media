package com.github.hui.quick.plugin.qrcode.v3.entity.render;

import com.github.hui.quick.plugin.qrcode.helper.QrCodeRenderHelper;
import com.github.hui.quick.plugin.qrcode.v3.constants.RenderDotType;
import com.github.hui.quick.plugin.qrcode.v3.constants.RenderFunc;
import com.github.hui.quick.plugin.qrcode.v3.entity.svg.SvgTemplate;

import java.awt.*;

/**
 * 二维矩阵渲染
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class DetectRenderDot extends RenderDot {
    private QrCodeRenderHelper.DetectLocation location;
    private int dotNum;
    /**
     * 当size == 1时，用这个来区分当前这个是在码眼外层的还是内层的框上
     */
    private Boolean outBorder;

    public DetectRenderDot() {
        this.type = RenderDotType.DETECT.getType();
    }

    public QrCodeRenderHelper.DetectLocation getLocation() {
        return location;
    }

    public DetectRenderDot setLocation(QrCodeRenderHelper.DetectLocation location) {
        this.location = location;
        return this;
    }

    public int getDotNum() {
        return dotNum;
    }

    public DetectRenderDot setDotNum(int dotNum) {
        this.dotNum = dotNum;
        return this;
    }

    public Boolean getOutBorder() {
        return outBorder;
    }

    public DetectRenderDot setOutBorder(Boolean outBorder) {
        this.outBorder = outBorder;
        return this;
    }

    @Override
    public void renderGeometry(Graphics2D g2d, RenderFunc.GeometryDrawFunc imgFunc) {
        // 多个探测图形
        imgFunc.draw(g2d, x, y, dotNum * size, dotNum * size);
    }

    @Override
    public void renderImg(Graphics2D g2d, RenderFunc.ImgDrawFunc drawFunc) {
        drawFunc.draw(g2d, resource == null ? null : resource.getImg(), x, y, size * dotNum, size * dotNum);
    }

    @Override
    public void renderTxt(Graphics2D g2d, RenderFunc.TxtImgDrawFunc drawFunc) {
        Font oldFont = g2d.getFont();
        if (oldFont.getSize() != size) g2d.setFont(resource.getFont(size));
        drawFunc.draw(g2d, resource == null ? null : resource.getText(), x, y, size * dotNum);
        g2d.setFont(oldFont);
    }

    @Override
    public void renderSvg(SvgTemplate svg, RenderFunc.SvgDrawFunc drawFunc) {
        drawFunc.draw(svg, resource == null ? null : resource.getSvgInfo(), x, y, dotNum * size, dotNum * size);
    }

    @Override
    public String toString() {
        return "DetectRenderDot{" +
                "location=" + location +
                ", dotNum=" + dotNum +
                ", outBorder=" + outBorder +
                ", x=" + x +
                ", y=" + y +
                ", size=" + size +
                ", type=" + type +
                ", resource=" + resource +
                '}';
    }
}