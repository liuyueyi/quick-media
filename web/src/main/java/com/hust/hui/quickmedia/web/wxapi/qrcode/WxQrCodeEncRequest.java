package com.hust.hui.quickmedia.web.wxapi.qrcode;

import com.hust.hui.quickmedia.web.entity.IRequest;
import lombok.Data;

/**
 * Created by yihui on 2017/10/15.
 */
@Data
public class WxQrCodeEncRequest implements IRequest {

    private String content;

    private int size;

    private int errorLevel;

    private int padding;

    @Override
    public boolean validate() {
        return true;
    }
}
