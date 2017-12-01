package com.hust.hui.quickmedia.web.wxapi.html;

import com.hust.hui.quickmedia.web.entity.IValidate;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * Created by yihui on 2017/12/1.
 */
@Data
public class Html2ImgRequest implements IValidate {

    private String url;

    /**
     * 返回图片的样式, 默认为url格式
     *
     * url: 表示图片的url
     * img: 表示base64格式的图片
     *
     */
    private String type;


    private String token;


    private static final String ACCESS_TOKEN = "0xdahdljk3u8eqhrjqwer90e";


    @Override
    public boolean validate() {
        return StringUtils.isNotBlank(url)
                && StringUtils.startsWith(url, "http")
                && ACCESS_TOKEN.equalsIgnoreCase(token);
    }


    public boolean urlReturn() {
        if (StringUtils.isBlank(type)) {
            return true;
        }

        return "URL".equalsIgnoreCase(url);
    }
}
