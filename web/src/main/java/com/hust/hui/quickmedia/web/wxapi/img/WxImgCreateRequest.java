package com.hust.hui.quickmedia.web.wxapi.img;

import com.hust.hui.quickmedia.web.entity.IRequest;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * Created by yihui on 2017/9/18.
 */
@Data
public class WxImgCreateRequest implements IRequest {


    /**
     * 文字内容
     */
    private String msg;


    /**
     * 模板ID
     */
    private String templateId;


    @Override
    public boolean validate() {
        return !(StringUtils.isBlank(msg) || StringUtils.isBlank(templateId));
    }
}
