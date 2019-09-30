package com.github.hui.media.console.action.markdown;

import com.github.hui.media.console.action.BaseRequest;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yihui on 2017/10/15.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MarkdownRequest extends BaseRequest {
    private static final long serialVersionUID = 5034814880113324217L;

    private String content;

    private boolean noborder;

    @Override
    public boolean validate() {
        return super.validate() && StringUtils.isNotBlank(content);
    }
}
