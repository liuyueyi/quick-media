package com.hust.hui.quickmedia.web.wxapi.qrcode;

import com.hust.hui.quickmedia.web.wxapi.WxBaseRequest;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yihui on 2017/10/15.
 */
@Data
public class WxQrCodeEncRequest extends WxBaseRequest {

    private static final long serialVersionUID = -5451353741058229910L;

    private String content;

    private int size;

    private int errorLevel;

    private int padding;

    private String preColor;

    private String bgColor;

    private String detectColor;

    private String style;

    private Boolean scale;

    @Override
    public boolean validate() {
        return super.validate() && StringUtils.isNotBlank(content);
    }
}
