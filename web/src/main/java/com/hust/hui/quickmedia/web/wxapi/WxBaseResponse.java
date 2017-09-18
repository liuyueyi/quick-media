package com.hust.hui.quickmedia.web.wxapi;

import com.hust.hui.quickmedia.web.entity.IResponse;
import lombok.Data;

/**
 * Created by yihui on 2017/9/18.
 */
@Data
public class WxBaseResponse implements IResponse {

    private String base64result;

    private String prefix;
}
