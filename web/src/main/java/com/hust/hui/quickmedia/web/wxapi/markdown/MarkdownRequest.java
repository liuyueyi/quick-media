package com.hust.hui.quickmedia.web.wxapi.markdown;

import com.hust.hui.quickmedia.web.entity.IRequest;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * Created by yihui on 2017/10/15.
 */
@Data
public class MarkdownRequest implements IRequest {

    private String content;

    @Override
    public boolean validate() {
        return StringUtils.isNotBlank(content);
    }
}
