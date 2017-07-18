package com.hust.hui.quickmedia.web.api.qrcode;

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

    private String bgColor = "0xFF000000";

    private String preColor = "0xFFFFFFFF";

    private String logo;


    /**
     * 二维码文本信息的编码格式
     */
    private String charset = "UTF-8";

    /**
     * 二维码白边大小, 取值 0 - 4
     */
    private Integer padding = 0;
}
