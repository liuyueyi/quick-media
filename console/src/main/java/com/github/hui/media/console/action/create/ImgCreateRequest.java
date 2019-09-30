package com.github.hui.media.console.action.create;

import com.github.hui.media.console.action.BaseRequest;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yihui on 2017/9/18.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ImgCreateRequest extends BaseRequest {
    private static final long serialVersionUID = 4245509869837054939L;
    /**
     * 文字内容
     */
    private String msg;


    /**
     * 模板ID
     */
    private String templateId;


    private String sign;


    private Integer signStatus;


    @Override
    public boolean validate() {
        return !(StringUtils.isBlank(msg) || StringUtils.isBlank(templateId));
    }
}
