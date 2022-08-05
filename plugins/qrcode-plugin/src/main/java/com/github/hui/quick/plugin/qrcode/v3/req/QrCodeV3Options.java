package com.github.hui.quick.plugin.qrcode.v3.req;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.QrType;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.BooleanUtils;

import java.awt.*;
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

    public Integer getH() {
        return h;
    }

    public QrCodeV3Options setH(Integer h) {
        this.h = h;
        return this;
    }

    public QrType getQrType() {
        if (qrType != null) {
            return qrType;
        }

        if (drawOptions.getDrawStyle() == DrawStyle.SVG) {
            qrType = QrType.SVG;
        } else if (bgOptions.getBg() != null && bgOptions.getBg().getGif() != null || (frontOptions.getFt() != null && frontOptions.getFt().getGif() != null)) {
            picType = "gif";
            qrType = QrType.GIF;
        } else {
            qrType = QrType.IMG;
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

    // ----------- logo 相关 ----------

    public QrCodeV3Options setLogo(QrResource resource) {
        newLogoOptions().setLogo(resource);
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
            detectOptions.setInColor(ColorUtil.OPACITY);
            detectOptions.setOutColor(ColorUtil.OPACITY);
        } else if (!BooleanUtils.isTrue(detectOptions.getSpecial())) {
            // 探测图形非特殊处理时，直接使用preColor
            detectOptions.setInColor(drawOptions.getPreColor());
            detectOptions.setOutColor(drawOptions.getPreColor());
        }

        // 当指定了svg资源时，输出二维码为svg
        if (qrType == null && drawOptions.getDrawStyle().isSvg()) qrType = QrType.SVG;
        return gen;
    }
}
