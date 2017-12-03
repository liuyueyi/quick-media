package com.hust.hui.quickmedia.web.wxapi;

import com.hust.hui.quickmedia.web.entity.IRequest;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

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
     */
    private String type;


    private String token;


    @Override
    public boolean validate() {
        return true;
//        return RuleConstants.ACCESS_TOKEN.equalsIgnoreCase(token);
    }


    public boolean urlReturn() {
        return StringUtils.isBlank(type) || "URL".equalsIgnoreCase(type);
    }
}
