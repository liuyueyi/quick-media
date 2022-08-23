package com.github.hui.quick.plugin.qrcode.v3.req;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.QrType;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.tpl.ImgTplParse;
import com.github.hui.quick.plugin.qrcode.v3.tpl.SvgTplParse;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.BooleanUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码配置参数
 *
 * @author YiHui
 * @date 2022/7/20
 */
public class QrCodeV3Options {
    private final QrCodeGenV3 gen;

    /**
     * 塞入二维码的信息
     */
    private String msg;

    /**
     * 生成二维码的宽
     */
    private Integer w;


    /**
     * 生成二维码的高
     */
    private Integer h;

    /**
     * error level, default H
     */
    private ErrorCorrectionLevel errorCorrection = ErrorCorrectionLevel.H;

    /**
     * qrcode message's code, default UTF-8
     */
    private String code = "utf-8";


    /**
     * 0 - 4
     */
    private Integer padding = 1;

    private Map<EncodeHintType, Object> hints;


    /**
     * 生成二维码图片的格式 png, jpg
     */
    private String picType;

    /**
     * 输出二维码的类型
     */
    private QrType qrType;


    /**
     * 二维码信息(即传统二维码中的黑色方块) 绘制选项
     */
    private DrawOptions drawOptions;

    /**
     * 前置图选项
     */
    private FrontOptions frontOptions;

    /**
     * 背景图样式选项
     */
    private BgOptions bgOptions;

    /**
     * logo 样式选项
     */
    private LogoOptions logoOptions;

    /**
     * 三个探测图形的样式选项
     */
    private DetectOptions detectOptions;

    public QrCodeV3Options(QrCodeGenV3 gen) {
        this.gen = gen;
    }

    public String getMsg() {
        return msg;
    }

    public QrCodeV3Options setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Integer getW() {
        return w;
    }

    public QrCodeV3Options setW(Integer w) {
        this.w = w;
        return this;
    }

    public QrCodeV3Options setSize(Integer size) {
        this.w = size;
        this.h = size;
        return this;
    }

    public Integer getH() {
        return h;
    }

    public QrCodeV3Options setH(Integer h) {
        this.h = h;
        return this;
    }

    public QrType getQrType() {
        if (qrType == null) {
            // 未指定时，尝试根据设置的资源进行智能猜测需要输出的是啥
            if (drawOptions.getDrawStyle() == DrawStyle.SVG) {
                qrType = QrType.SVG;
            } else if (bgOptions.getBg() != null && bgOptions.getBg().getGif() != null || (frontOptions.getFt() != null && frontOptions.getFt().getGif() != null)) {
                qrType = QrType.GIF;
                picType = qrType.getSuffix();
            }
        }
        return qrType;
    }

    public QrCodeV3Options setQrType(QrType qrType) {
        this.qrType = qrType;
        return this;
    }

    public Map<EncodeHintType, Object> getHints() {
        return hints;
    }

    public QrCodeV3Options setHints(Map<EncodeHintType, Object> hints) {
        this.hints = hints;
        return this;
    }

    public String getPicType() {
        return picType;
    }

    public QrCodeV3Options setPicType(String picType) {
        this.picType = picType;
        return this;
    }

    public DrawOptions getDrawOptions() {
        return drawOptions;
    }

    public DrawOptions newDrawOptions() {
        if (this.drawOptions == null) this.drawOptions = new DrawOptions(this);
        return drawOptions;
    }

    public FrontOptions getFrontOptions() {
        return frontOptions;
    }

    public FrontOptions newFrontOptions() {
        if (this.frontOptions == null) this.frontOptions = new FrontOptions(this);
        return frontOptions;
    }

    public BgOptions getBgOptions() {
        return bgOptions;
    }

    public BgOptions newBgOptions() {
        if (bgOptions == null) this.bgOptions = new BgOptions(this);
        return bgOptions;
    }

    public LogoOptions getLogoOptions() {
        return logoOptions;
    }

    public LogoOptions newLogoOptions() {
        if (logoOptions == null) this.logoOptions = new LogoOptions(this);
        return logoOptions;
    }

    public DetectOptions getDetectOptions() {
        return detectOptions;
    }

    public DetectOptions newDetectOptions() {
        if (detectOptions == null) this.detectOptions = new DetectOptions(this);
        return detectOptions;
    }

    public ErrorCorrectionLevel getErrorCorrection() {
        return errorCorrection;
    }

    public QrCodeV3Options setErrorCorrection(ErrorCorrectionLevel errorCorrection) {
        this.errorCorrection = errorCorrection;
        return this;
    }

    public String getCode() {
        return code;
    }

    public QrCodeV3Options setCode(String code) {
        this.code = code;
        return this;
    }

    public Integer getPadding() {
        return padding;
    }

    public QrCodeV3Options setPadding(Integer padding) {
        this.padding = padding;
        return this;
    }

    // ===========================
    // 简化参数配置的复杂性，对于一些简单的场景，直接支持设置图片样式
    // ===========================


    // ----------- 二维码绘制相关参数 -----------
    public QrCodeV3Options setPreColor(Color color) {
        newDrawOptions().setPreColor(color);
        return this;
    }

    public QrCodeV3Options setBgColor(Color color) {
        newDrawOptions().setBgColor(color);
        return this;
    }

    public QrCodeV3Options setDrawStyle(DrawStyle style) {
        newDrawOptions().setDrawStyle(style);
        return this;
    }

    public QrCodeV3Options setDrawResource(QrResource resource) {
        newDrawOptions().setRenderResource(resource);
        return this;
    }

    /**
     * 设置全局的资源信息
     *
     * @param resource
     * @return
     */
    public QrCodeV3Options setDrawGlobalResource(QrResource resource) {
        newDrawOptions().setGlobalResource(resource);
        return this;
    }

    /**
     * 输入svg模板，主要分两块 defs + symbol；用于减轻一个一个设置svg symbol的复杂性
     * svg 模板规则，一个实例demo如下：
     * - defs 标签：内部为预定义的标签，供其他的symbol使用
     * - symbol 标签组：每个symbol要求必须存在 viewBox，读取其中的width, height；若存在width, height属性，则直接取width，height值
     * - 取所有的symbol中的 width, height的最小值作为 1个qrdot 的基本单位
     * - 每个symbol的占位  col  = width / qrdot,   row = height / qrdot
     * <defs>
     * <g id="def_HA62">
     * <path d="M5.74,0.06c4.18-0.11,8.34-0.03,12.53,0c0.83-0.02,1.64,0.13,2.43,0.31c0.07,0.96,0.33,1.92,0.28,2.89c-0.06,1.24,0.18,2.5-0.16,3.72C13.95,7,7.08,7.05,0.26,6.67c-0.36-2.1-0.31-4.23-0.07-6.35C1.98-0.05,3.88,0.11,5.74,0.06" style="fill: #8C4410" />
     * </g>
     * <g id="def_pTxe">
     * <path d="M5.74,0.06c4.18-0.11,8.34-0.03,12.53,0c0.83-0.02,1.64,0.13,2.43,0.31c0.07,0.96,0.33,1.92,0.28,2.89c-0.06,1.24,0.18,2.5-0.16,3.72C13.95,7,7.08,7.05,0.26,6.67c-0.36-2.1-0.31-4.23-0.07-6.35C1.98-0.05,3.88,0.11,5.74,0.06" style="fill: #8C4410" />
     * </g>
     * </defs>
     * <symbol id="symbol_1z" viewBox="0 0 49 49">
     * <use x="0.4" y="0" xlink:href="#def_HA62" />
     * <use x="27.6" y="42" xlink:href="#def_HA62" />
     * <use transform="rotate(90)" x="0.4" y="-49" xlink:href="#def_HA62" />
     * <use transform="rotate(90)" x="27.6" y="-7" xlink:href="#def_HA62" />
     * <path d="M17.68,15.8c4.12-0.29,8.25-0.32,12.39-0.26c1.08,0.1,2.51-0.15,3.12,1c0.71,1.36,0.8,2.96,1.08,4.45  c0.08,1.92,0,3.83,0.01,5.75c-0.12,2.45-0.03,5.03-1.1,7.29c-5.11,0.55-10.27,0.64-15.38,0.22c-2.45-5.39-2.48-11.52-1.55-17.25  C16.29,16.3,16.98,15.81,17.68,15.8" style="fill: #8C4410" />
     * <path d="M16.65,29.41c0.58,0.12,1.16,0.26,1.75,0.4c0.12,0.96,0.25,1.92,0.37,2.88c-0.3,0.1-0.91,0.3-1.21,0.39  C17.31,31.85,16.97,30.62,16.65,29.41z" style="fill: #E29126" />
     * </symbol>
     * <symbol id="symbol_1A" viewBox="0 0 49 49">
     * <use x="0.4" y="0" xlink:href="#def_pTxe" />
     * <use x="27.6" y="42" xlink:href="#def_pTxe" />
     * <use transform="rotate(90)" x="0.4" y="-49" xlink:href="#def_pTxe" />
     * <use transform="rotate(90)" x="27.6" y="-7" xlink:href="#def_pTxe" />
     * <path d="M17.68,15.8c4.12-0.29,8.25-0.32,12.39-0.26c1.08,0.1,2.51-0.15,3.12,1c0.71,1.36,0.8,2.96,1.08,4.45  c0.08,1.92,0,3.83,0.01,5.75c-0.12,2.45-0.03,5.03-1.1,7.29c-5.11,0.55-10.27,0.64-15.38,0.22c-2.45-5.39-2.48-11.52-1.55-17.25  C16.29,16.3,16.98,15.81,17.68,15.8" style="fill: #8C4410" />
     * <path d="M16.65,29.41c0.58,0.12,1.16,0.26,1.75,0.4c0.12,0.96,0.25,1.92,0.37,2.88c-0.3,0.1-0.91,0.3-1.21,0.39  C17.31,31.85,16.97,30.62,16.65,29.41z" style="fill: #E29126" />
     * </symbol>
     *
     * @param svg
     * @return
     */
    public QrCodeV3Options setSvgTemplate(String svg) {
        SvgTplParse.svgTemplateParseAndInit(this, svg);
        return setDrawStyle(DrawStyle.SVG).setQrType(QrType.SVG);
    }

    public QrCodeV3Options setImgTemplate(String img) {
        ImgTplParse.imgTemplateParseAndInit(this, img);
        return setDrawStyle(DrawStyle.IMAGE).setQrType(QrType.IMG);
    }

    // ----------- logo 相关 ----------

    public QrCodeV3Options setLogo(QrResource resource) {
        newLogoOptions().setLogo(resource);
        return this;
    }

    /**
     * 设置 二维码 / logo的比例，值越大，则logo越小
     *
     * @param rate
     * @return
     */
    public QrCodeV3Options setLogoRate(int rate) {
        newLogoOptions().setRate(rate);
        return this;
    }

    public QrCodeV3Options clearLogoArea(boolean clear) {
        newLogoOptions().setClearLogoArea(clear);
        return this;
    }

    public QrCodeV3Options setBorderColor(Color color) {
        newLogoOptions().setBorderColor(color);
        return this;
    }

    public QrCodeV3Options setOutBorderColo(Color color) {
        newLogoOptions().setOuterBorderColor(color);
        return this;
    }

    // ------------- 探测图像相关 ---------

    public QrCodeV3Options setDetect(QrResource resource) {
        newDetectOptions().setResource(resource);
        return this;
    }

    /**
     * 设置探测图形资源是否为一整个
     *
     * @param whole
     * @return
     */
    public QrCodeV3Options setDetectWhole(boolean whole) {
        newDetectOptions().setWhole(whole);
        return this;
    }

    public QrCodeV3Options setDetectSpecial(Boolean special) {
        newDetectOptions().setSpecial(special);
        return this;
    }

    // ------------- 前置图 ---------

    public QrCodeV3Options setFtResource(QrResource resource) {
        newFrontOptions().setFt(resource);
        return this;
    }

    public QrCodeV3Options setFtStartX(int x) {
        newFrontOptions().setStartX(x);
        return this;
    }

    public QrCodeV3Options setFtStartY(int y) {
        newFrontOptions().setStartY(y);
        return this;
    }

    /**
     * 若指定了gif资源，则返回true
     *
     * @return
     */
    public boolean gifEnable() {
        return (frontOptions != null && frontOptions.getFt() != null && frontOptions.getFt().getGif() != null && frontOptions.getFt().getGif().getFrameCount() > 0)
                || (bgOptions != null && bgOptions.getBg() != null && bgOptions.getBg().getGif() != null && bgOptions.getBg().getGif().getFrameCount() > 0);

    }

    public QrCodeGenV3 build() {
        if (w == null) w = h == null ? Integer.valueOf(200) : h;
        if (h == null) h = w;
        if (picType == null) picType = "png";

        if (drawOptions == null) newDrawOptions().complete();
        else drawOptions.complete();
        if (detectOptions == null) newDetectOptions().complete();
        else detectOptions.complete();
        if (frontOptions == null) newFrontOptions().complete();
        else frontOptions.complete();
        if (bgOptions == null) newBgOptions().complete();
        else bgOptions.complete();
        if (logoOptions == null) newLogoOptions().complete();
        else logoOptions.complete();


        if (this.hints == null) {
            this.hints = new HashMap<>(8);
            hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrection);
            hints.put(EncodeHintType.CHARACTER_SET, code);
            hints.put(EncodeHintType.MARGIN, this.getPadding());
        } else {
            if (!hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrection);
            }
            if (!hints.containsKey(EncodeHintType.CHARACTER_SET)) {
                hints.put(EncodeHintType.CHARACTER_SET, code);
            }
            if (!hints.containsKey(EncodeHintType.MARGIN)) {
                hints.put(EncodeHintType.MARGIN, this.getPadding());
            }
        }

        if (bgOptions.getBgStyle() == BgStyle.PENETRATE) {
            // 透传，用背景图颜色进行绘制时
            drawOptions.setTransparencyBgFill(true);
            drawOptions.setPreColor(ColorUtil.OPACITY);
            bgOptions.setOpacity(1);
            if (!BooleanUtils.isTrue(detectOptions.getSpecial())) {
                // 对于穿透的场景，若探测图形没有特殊处理，则设置颜色为透明
                detectOptions.setInColor(ColorUtil.OPACITY);
                detectOptions.setOutColor(ColorUtil.OPACITY);
            }
        }

        if (!BooleanUtils.isTrue(detectOptions.getSpecial())) {
            // 探测图形非特殊处理时，直接使用preColor
            if (detectOptions.getInColor() == null) detectOptions.setInColor(drawOptions.getPreColor());
            if (detectOptions.getOutColor() == null) detectOptions.setOutColor(drawOptions.getPreColor());
        }

        // 当指定了svg资源时，输出二维码为svg
        if (qrType == null) {
            if (drawOptions.getDrawStyle() == DrawStyle.SVG) {
                qrType = QrType.SVG;
            } else if (gifEnable()) {
                qrType = QrType.GIF;
            }
        }
        return gen;
    }

    // -------------- 下面是输出结果的直接调用方式

    public String asSvg() throws Exception {
        return build().asSvg();
    }

    public BufferedImage asImg() throws Exception {
        return build().asImg();
    }

    public ByteArrayOutputStream asGif() throws Exception {
        return build().asGif();
    }

    public String asStr() throws Exception {
        return build().asStr();
    }

    public boolean asFile(String absFileName) throws Exception {
        return build().asFile(absFileName);
    }
}
