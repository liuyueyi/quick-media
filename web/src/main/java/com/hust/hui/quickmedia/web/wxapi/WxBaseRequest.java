package com.hust.hui.quickmedia.web.wxapi;

import com.hust.hui.quickmedia.web.entity.IRequest;
import com.hust.hui.quickmedia.web.entity.OutputEnum;
import lombok.Data;

/**
 * Created by yihui on 2017/12/3.
 */
@Data
public class WxBaseRequest implements IRequest {

    private static final long serialVersionUID = -5824276840657942205L;

    /**
     * 返回图片的样式, 默认为url格式
     * <p>
     * url: 表示图片的url
     * img: 表示base64格式的图片
     * stream : 表示直接返回图片
     */
    private String type;


    private String token;


    @Override
    public boolean validate() {
        return true;
//        return RuleConstants.ACCESS_TOKEN.equalsIgnoreCase(token);
    }


    public boolean urlReturn() {
        return OutputEnum.ofStr(type) == OutputEnum.URL;
    }

    public boolean base64Return() {
        return OutputEnum.ofStr(type) == OutputEnum.IMG;
    }

    public boolean streamReturn() {
        return OutputEnum.ofStr(type) == OutputEnum.STREAM;
    }
}
