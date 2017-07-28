package com.hust.hui.quickmedia.common.qrcode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import lombok.Data;

import java.awt.*;
import java.util.Map;

/**
 * Created by yihui on 2017/7/17.
 */
@Data
public class QrCodeOptions {
    /**
     * 塞入二维码的信息
     */
    private String msg;


    /**
     * 二维码的背景图
     */
    private String background;

    /**
     * 背景图宽
     */
    private Integer bgW;


    /**
     * 背景图高
     */
    private Integer bgH;


    /**
     * 二维码中间的logo
     */
    private String logo;


    /**
     * logo的样式， 目前支持圆角+普通
     */
    private LogoStyle logoStyle;

    /**
     * logo 的边框背景色
     */
    private Color logoBgColor;


    /**
     * 生成二维码的宽
     */
    private Integer w;


    /**
     * 生成二维码的高
     */
    private Integer h;


    /**
     * 生成二维码的颜色
     */
    private MatrixToImageConfig matrixToImageConfig;


    /**
     * 三个位置探测图形的背景色和前置色
     */
    private MatrixToImageConfig detectCornerColor;


    private Map<EncodeHintType, Object> hints;


    /**
     * 生成二维码图片的格式 png, jpg
     */
    private String picType;


    public enum LogoStyle {
        ROUND,
        NORMAL;


        public static LogoStyle getStyle(String name) {
            if ("ROUND".equalsIgnoreCase(name)) {
                return ROUND;
            } else {
                return NORMAL;
            }
        }
    }
}
