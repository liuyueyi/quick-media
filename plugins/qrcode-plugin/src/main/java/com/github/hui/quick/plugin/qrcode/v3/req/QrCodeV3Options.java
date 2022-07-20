package com.github.hui.quick.plugin.qrcode.v3.req;

import com.google.zxing.EncodeHintType;

import java.util.Map;

/**
 * Created by yihui on 2017/7/17.
 */
public class QrCodeV3Options {
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

    private Map<EncodeHintType, Object> hints;


    /**
     * 生成二维码图片的格式 png, jpg
     */
    private String picType;


    /**
     * 二维码信息(即传统二维码中的黑色方块) 绘制选项
     */
    private DrawOptions drawOptions;

    /**
     * 前置图选项
     */
    private FrontOptions ftImgOptions;

    /**
     * 背景图样式选项
     */
    private BgOptions bgImgOptions;

    /**
     * logo 样式选项
     */
    private LogoOptions logoOptions;

    /**
     * todo 后续可以考虑三个都可以自配置
     * <p>
     * 三个探测图形的样式选项
     */
    private DetectOptions detectOptions;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public DrawOptions getDrawOptions() {
        return drawOptions;
    }

    public void setDrawOptions(DrawOptions drawOptions) {
        this.drawOptions = drawOptions;
    }

    public BgOptions getBgImgOptions() {
        return bgImgOptions;
    }

    public void setBgImgOptions(BgOptions bgImgOptions) {
        this.bgImgOptions = bgImgOptions;
    }

    public FrontOptions getFtImgOptions() {
        return ftImgOptions;
    }

    public void setFtImgOptions(FrontOptions ftImgOptions) {
        this.ftImgOptions = ftImgOptions;
    }

    public LogoOptions getLogoOptions() {
        return logoOptions;
    }

    public void setLogoOptions(LogoOptions logoOptions) {
        this.logoOptions = logoOptions;
    }

    public DetectOptions getDetectOptions() {
        return detectOptions;
    }

    public void setDetectOptions(DetectOptions detectOptions) {
        this.detectOptions = detectOptions;
    }

    public Map<EncodeHintType, Object> getHints() {
        return hints;
    }

    public void setHints(Map<EncodeHintType, Object> hints) {
        this.hints = hints;
    }

    public String getPicType() {
        return picType;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }
}
