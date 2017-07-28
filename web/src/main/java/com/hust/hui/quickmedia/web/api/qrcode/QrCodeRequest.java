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

    private String bgColor;

    private String preColor;

    /**
     * 位置探测图形的颜色
     */
    private String detectedPreColor;


    /**
     * logo的http格式地址
     */
    private String logo;


    /**
     * logo的样式: ROUND & NORMAL
     */
    private String logoStyle = QrCodeOptions.LogoStyle.NORMAL.name();


    private String logoBorderColor = "0xffffff00";


    /**
     * 背景图
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
     * 二维码文本信息的编码格式
     */
    private String charset = "UTF-8";

    /**
     * 二维码白边大小, 取值 0 - 4
     */
    private Integer padding = 0;
}
