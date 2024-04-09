package com.github.hui.quick.plugin.qrcode.v3.req;

import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * 背景图的配置信息
 *
 * @author YiHui
 */
public class BgOptions {
    private static Logger log = LoggerFactory.getLogger(BgOptions.class);
    private final QrCodeV3Options options;

    /**
     * 内部变量，用于标记现有的参数是否已经自动缩放过
     */
    private boolean scaledTag;

    /**
     * 背景图
     */
    private QrResource bg;

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
     * 背景图宽
     */
    private int bgW;

    /**
     * 背景图高
     */
    private int bgH;

    /**
     * 背景图样式，默认为 OVERRIDE
     */
    private BgStyle bgStyle;

    /**
     * if {@link #bgStyle} ==  BgStyle.OVERRIDE，
     * 用于设置二维码的透明度 0 - 1，越小则透明度越高，
     */
    private float opacity;


    /**
     * if {@link #bgStyle} ==  BgStyle.FILL
     * <p>
     * 用于设置二维码的绘制在背景图上的x坐标
     */
    private int startX;


    /**
     * if {@link #bgStyle} ==  BgStyle.FILL
     * <p>
     * 用于设置二维码的绘制在背景图上的y坐标
     */
    private int startY;

    public BgOptions(QrCodeV3Options options) {
        this.options = options;
    }

    public QrResource getBg() {
        return bg;
    }

    public BgOptions setBg(QrResource bg) {
        this.bg = bg;
        return this;
    }

    public BgOptions setBg(String bg) {
        return setBg(new QrResource(bg));
    }

    public int getBgW() {
        return bgW;
    }

    public BgOptions setBgW(int bgW) {
        this.bgW = bgW;
        return this;
    }

    public int getBgH() {
        return bgH;
    }

    public BgOptions setBgH(int bgH) {
        this.bgH = bgH;
        return this;
    }

    public BgStyle getBgStyle() {
        return bgStyle;
    }

    public BgOptions setBgStyle(BgStyle bgStyle) {
        this.bgStyle = bgStyle;
        return this;
    }

    public float getOpacity() {
        return opacity;
    }

    public BgOptions setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }

    public int getStartX() {
        return startX;
    }

    public BgOptions setStartX(int startX) {
        this.startX = startX;
        return this;
    }

    public int getStartY() {
        return startY;
    }

    public BgOptions setStartY(int startY) {
        this.startY = startY;
        return this;
    }

    public boolean isAutoScale() {
        return autoScale;
    }

    public BgOptions setAutoScale(boolean autoScale) {
        this.autoScale = autoScale;
        return this;
    }

    public Integer getBasicQrSize() {
        return basicQrSize;
    }

    public BgOptions setBasicQrSize(Integer basicQrSize) {
        this.basicQrSize = basicQrSize;
        this.autoScale = true;
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
        this.bgW = (int) Math.floor(rate * this.bgW);
        this.bgH = (int) Math.floor(rate * this.bgH);
        this.startX = (int) Math.floor(rate * this.startX);
        this.startY = (int) Math.floor(rate * this.startY);
    }

    public QrCodeV3Options complete() {
        // 背景图宽高
        if (bg != null) {
            BufferedImage img = Optional.ofNullable(bg.getImg()).orElse(bg.getGif() != null ? bg.getGif().getImage() : null);
            Optional.ofNullable(img).ifPresent(i -> {
                if (bgW <= 0) bgW = img.getWidth();
                if (bgH <= 0) bgH = img.getHeight();
            });
        }

        // 默认采用全覆盖方式
        if (bgStyle == null) bgStyle = BgStyle.OVERRIDE;

        if (bgStyle == BgStyle.OVERRIDE && opacity <= 0) {
            // 默认将覆盖方式的二维码透明设置为0.8
            opacity = 0.85F;
        }

        if (opacity > 1) opacity = 1.0F;

        return options;
    }

    /**
     * 判断是否需要绘制背景
     *
     * @return
     */
    public boolean needDrawBg() {
        if (bg == null) {
            return false;
        }

        if (bg.getImg() != null || bg.getGif() != null) {
            return true;
        }

        if (bg.getDrawStyle() != null) {
            return true;
        }

        if (bg.getSvg() != null) {
            return true;
        }
        return false;
    }
}
