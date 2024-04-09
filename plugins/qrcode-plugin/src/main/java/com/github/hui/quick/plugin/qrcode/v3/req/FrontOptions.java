package com.github.hui.quick.plugin.qrcode.v3.req;

import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 前置图的配置信息
 *
 * @author YiHui
 */
public class FrontOptions {
    private static Logger log = LoggerFactory.getLogger(BgOptions.class);
    private final QrCodeV3Options options;

    /**
     * 内部变量，用于标记现有的参数是否已经自动缩放过
     */
    private boolean scaledTag;

    /**
     * 前置图
     */
    private QrResource ft;

    /**
     * 背景图自动缩放策略，配合 basicQrSize 参数共同使用
     * - 如 basicQrSize = 500, 表示这个背景是为了 500x500 大小的二维码设计的，当我们最终输出的二维码是 250x250时，那么背景图的 bgW, bgH, startX, startY 都减小一半
     *
     * <p>
     * - 默认false, 表示不进行缩放，而是根据给定的图片宽高、startX/startY 来进行渲染
     */
    private boolean autoScale;

    /**
     * 这个背景适用的基准二维码大小，当小于50时，自适应功能不生效
     */
    private Integer basicQrSize;

    /**
     * 前置图宽
     */
    private int ftW;

    /**
     * 前置图高
     */
    private int ftH;

    /**
     * 这里需要重点注意：
     * - 这个坐标是指前置图在最终的底图(即第一张背景图)上的坐标
     * - 如果qrCode没有背景图，则startX表示前置图在二维码上的偏移量；当前置图大于二维码时，作为二维码的外部边框装饰场景，此时 startX 应该小于0
     */
    private int startX;


    /**
     * 用于设置前置图在最终的底图(即第一张背景图)上的坐标
     */
    private int startY;

    /**
     * 二维码大小比最终输出的图片小时，用来指定二维码周边的填充色，不存在时，默认透明
     */
    private Color fillColor;

    public FrontOptions(QrCodeV3Options options) {
        this.options = options;
    }

    public QrResource getFt() {
        return ft;
    }

    public FrontOptions setFt(QrResource ft) {
        this.ft = ft;
        return this;
    }

    public FrontOptions setFt(String ft) {
        this.ft = new QrResource(ft);
        return this;
    }

    public int getFtW() {
        return ftW;
    }

    public FrontOptions setFtW(int ftW) {
        this.ftW = ftW;
        return this;
    }

    public int getFtH() {
        return ftH;
    }

    public FrontOptions setFtH(int ftH) {
        this.ftH = ftH;
        return this;
    }

    public int getStartX() {
        return startX;
    }

    public FrontOptions setStartX(int startX) {
        this.startX = startX;
        return this;
    }

    public int getStartY() {
        return startY;
    }

    public FrontOptions setStartY(int startY) {
        this.startY = startY;
        return this;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public FrontOptions setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public boolean isAutoScale() {
        return autoScale;
    }

    public FrontOptions setAutoScale(boolean autoScale) {
        this.autoScale = autoScale;
        return this;
    }

    public Integer getBasicQrSize() {
        return basicQrSize;
    }

    public FrontOptions setBasicQrSize(Integer basicQrSize) {
        this.basicQrSize = basicQrSize;
        if (basicQrSize != null && basicQrSize <= 50 && log.isDebugEnabled()) {
            log.debug("BgOptions#basicQrSize should bigger than 50 but you set {}, autoScale will not effect!", basicQrSize);
        }
        return this;
    }

    /**
     * 自适应调整背景相关参数，当 autoScale = true 时生效
     *
     * @param qrSize
     */
    public void autoApplyBgOptions(Integer qrSize) {
        if (scaledTag || !autoScale || basicQrSize == null || basicQrSize <= 50) {
            return;
        }

        scaledTag = true;
        float rate = qrSize / (float) basicQrSize;
        this.ftW = (int) Math.floor(rate * this.ftW);
        this.ftH = (int) Math.floor(rate * this.ftH);
        this.startX = (int) Math.floor(rate * this.startX);
        this.startY = (int) Math.floor(rate * this.startY);
    }

    public QrCodeV3Options complete() {
        if (ft == null) {
            return options;
        }

        BufferedImage rImg = ft.getImg() != null ? ft.getImg() : (ft.getGif() != null ? ft.getGif().getImage() : null);
        if (rImg != null) {
            if (ftW <= 0) ftW = rImg.getWidth();
            if (ftH <= 0) ftH = rImg.getHeight();
        }

        return options;
    }
}
