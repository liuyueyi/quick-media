package com.github.hui.media.console.action.html;

import com.github.hui.media.console.action.BaseRequest;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yihui on 2017/12/1.
 */
@Data@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Html2ImgRequest extends BaseRequest {

    private static final long serialVersionUID = 6721926611725589167L;

    private String content;

    @Override
    public boolean validate() {
        return super.validate() && StringUtils.isNotBlank(content);
    }

}
