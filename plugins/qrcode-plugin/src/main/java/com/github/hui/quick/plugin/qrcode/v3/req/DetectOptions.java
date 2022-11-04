package com.github.hui.quick.plugin.qrcode.v3.req;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.QrArea;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;

import java.awt.*;

/**
 * 探测图形的配置信息
 *
 * @author YiHui
 */
public class DetectOptions {
    private final QrCodeV3Options options;

    /**
     * 外层颜色
     */
    private Color outColor;

    /**
     * 内层颜色
     */
    private Color inColor;
    /**
     * 兜底的资源
     */
    private QrResource resource;
    /**
     * 左上角的探测图形
     */
    private QrResource lt;
    /**
     * 右上角的探测图形
     */
    private QrResource rt;
    /**
     * 左下角的探测图形
     */
    private QrResource ld;
    /**
     * 定位点的资源图形
     */
    private QrResource checkPoint;
    /**
     * true 表示探测图形单独处理
     * false 表示探测图形的样式更随二维码的主样式
     */
    private Boolean special;

    /**
     * true 表示一个探测图形只用一个资源进行填充；适用于指定探测图形图片、svg节点的场景
     */
    private Boolean whole;

    public DetectOptions(QrCodeV3Options options) {
        this.options = options;
    }

    public Color getOutColor() {
        return outColor;
    }

    public DetectOptions setOutColor(Color outColor) {
        this.outColor = outColor;
        return this;
    }

    public DetectOptions setOutColor(int color) {
        return setOutColor(ColorUtil.int2color(color));
    }

    public Color getInColor() {
        return inColor;
    }

    public DetectOptions setInColor(Color inColor) {
        this.inColor = inColor;
        return this;
    }

    public DetectOptions setInColor(int color) {
        return setInColor(ColorUtil.int2color(color));
    }

    public DetectOptions setColor(Color color) {
        this.inColor = color;
        this.outColor = color;
        return this;
    }

    public DetectOptions setColor(int color) {
        return setColor(ColorUtil.int2color(color));
    }

    public QrResource getLt() {
        return lt;
    }

    public DetectOptions setLt(QrResource lt) {
        initDetectResource(lt);
        this.lt = lt;
        this.resource = lt;
        return this;
    }

    public DetectOptions setLt(String lt) {
        return setLt(new QrResource(lt));
    }

    public QrResource getRt() {
        return rt;
    }

    public DetectOptions setRt(QrResource rt) {
        initDetectResource(rt);
        this.rt = rt;
        this.resource = rt;
        return this;
    }

    public DetectOptions setRt(String rt) {
        return setRt(new QrResource(rt));
    }

    public QrResource getLd() {
        return ld;
    }

    public DetectOptions setLd(QrResource ld) {
        initDetectResource(ld);
        this.ld = ld;
        this.resource = ld;
        return this;
    }

    public DetectOptions setLd(String ld) {
        return setLd(new QrResource(ld));
    }

    public DetectOptions setCheckPoint(QrResource checkPoint) {
        this.checkPoint = checkPoint;
        return this;
    }

    public DetectOptions setCheckPoint(String cp) {
        return setCheckPoint(new QrResource(cp));
    }

    public QrResource getCheckPoint() {
        return checkPoint;
    }

    /**
     * 内部初始化时使用，不对外提供
     *
     * @param resource
     * @return
     */
    DetectOptions initResource(QrResource resource) {
        this.resource = resource;
        return this;
    }

    public QrResource getResource() {
        return resource;
    }

    public DetectOptions setResource(QrResource resource) {
        initDetectResource(resource);
        this.resource = resource;
        return this;
    }

    public DetectOptions setResource(String resource) {
        return setResource(new QrResource(resource));
    }

    /**
     * 当指定探测图形资源信息之后，我们需要设置其对应的绘制方式，覆盖全局的信息点的DrawStyle
     *
     * @param resource
     */
    private void initDetectResource(QrResource resource) {
        if (resource == null || resource.getDrawStyle() != null) return;
        // 避免出现探测点的绘制被全局的绘制样式覆盖，导致无法正确处理
        if (resource.txtResource()) resource.setDrawStyle(DrawStyle.TXT);
        else if (resource.getSvgInfo() != null) resource.setDrawStyle(DrawStyle.SVG);
        else if (resource.getImg() != null) resource.setDrawStyle(DrawStyle.IMAGE);
    }

    public Boolean getSpecial() {
        return special;
    }

    /**
     * true 表示探测图形单独处理；若制定了探测图形的图片，则这个值必然是true；若没有指定探测图形资源，则探测图形采用标准的黑色回字
     *
     * @param special
     * @return
     */
    public DetectOptions setSpecial(Boolean special) {
        this.special = special;
        return this;
    }

    public Boolean getWhole() {
        return whole;
    }

    public DetectOptions setWhole(Boolean whole) {
        this.whole = whole;
        return this;
    }

    public QrResource chooseDetectResource(QrArea detectLocation) {
        switch (detectLocation) {
            case DETECT_LD:
                return ld == null ? resource : ld;
            case DETECT_LT:
                return lt == null ? resource : lt;
            case DETECT_RT:
                return rt == null ? resource : rt;
            case CHECK_POINT:
                return checkPoint == null ? resource : checkPoint;
            default:
                return null;
        }
    }

    public QrCodeV3Options complete() {
        if (special == null) special = false;

        if (resource == null) {
            if (ld != null) resource = ld;
            else if (lt != null) resource = lt;
            else if (rt != null) resource = rt;
            else if (checkPoint != null) resource = checkPoint;
        }
        // 只要一个有资源，则表明探测图形全指定
        if (resource != null) {
            if (whole == null) whole = true;
            special = true;
        }

        return options;
    }
}