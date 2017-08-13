package com.hust.hui.quickmedia.web.api.qrcode;

import com.hust.hui.quickmedia.common.qrcode.QrCodeOptions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by yihui on 2017/7/17.
 */
@Getter
@Setter
@ToString
public class QrCodeRequest {

    private String content;

    private Integer size = 200;

    /**
     * 绘制二维码的背景色
     */
    private String bgColor = "0xffffffff";

    /**
     * 绘制二维码的前置色
     */
    private String preColor = "0xff000000";

    /**
     * 绘制二维码的图片
     */
    private String drawImg;

    private String drawRow2Img;

    private String drawCol2Img;

    private String drawSize4Img;

    /**
     * 绘制二维码的样式
     */
    private String drawStyle = QrCodeOptions.DrawStyle.RECT.name();

    /**
     * 二维码边框留白， 取值 [0, 4]
     */
    private Integer padding = 1;


    /**
     * 二维码文本信息的编码格式
     */
    private String charset = "UTF-8";



    // 探测图形

    /**
     * 探测图形外边框颜色
     */
    private String detectOutColor;


    /**
     * 探测图形内边框颜色
     */
    private String detectInColor;


    /**
     * 位置探测图形图片
     */
    private String detectImg;


    // logo 相关

    /**
     * logo的http格式地址
     */
    private String logo;

    /**
     * logo 占二维码大小的比例
     */
    private Integer logoRate;


    /**
     * logo的样式: ROUND & NORMAL
     */
    private String logoStyle = QrCodeOptions.LogoStyle.NORMAL.name();

    /**
     * logo边框是否存在
     */
    private boolean logoBorder = false;

    /**
     * logo边框颜色
     */
    private String logoBorderColor;



    // 背景相关

    /**
     * 背景图
     */
    private String bgImg;

    /**
     * 背景图宽
     */
    private Integer bgW;

    /**
     * 背景图高
     */
    private Integer bgH;

    /**
     * 填充模式时，二维码在背景上的起始x坐标
     */
    private Integer bgX;

    /**
     * 填充模式时，二维码在背景上的起始y坐标
     */
    private Integer bgY;

    /**
     * 全覆盖模式时，二维码的透明度
     */
    private Float bgOpacity;


    /**
     * 背景样式，默认为全覆盖模式
     */
    private String bgStyle = QrCodeOptions.BgImgStyle.OVERRIDE.name();
}
