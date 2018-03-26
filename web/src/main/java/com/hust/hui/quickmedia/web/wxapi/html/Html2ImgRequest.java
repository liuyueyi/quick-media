package com.hust.hui.quickmedia.web.wxapi.html;

import com.hust.hui.quickmedia.web.wxapi.WxBaseRequest;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yihui on 2017/12/1.
 */
@Data
public class Html2ImgRequest extends WxBaseRequest {

    private static final long serialVersionUID = 6721926611725589167L;

    private String url;


    @Override
    public boolean validate() {
        return super.validate() &&
                StringUtils.isNotBlank(url)
                && StringUtils.startsWith(url, "http");
    }

}
