package com.hust.hui.quickmedia.web.wxapi.markdown;

import com.hust.hui.quickmedia.web.wxapi.WxBaseRequest;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * Created by yihui on 2017/10/15.
 */
@Data
public class MarkdownRequest extends WxBaseRequest {
    private static final long serialVersionUID = 5034814880113324217L;

    private String content;

    @Override
    public boolean validate() {
        return super.validate() && StringUtils.isNotBlank(content);
    }
}
