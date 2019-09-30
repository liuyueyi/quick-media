package com.github.hui.media.console.action.svg;

import com.github.hui.media.console.action.BaseRequest;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yihui on 2018/1/14.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Svg2ImgRequest extends BaseRequest {
    private static final long serialVersionUID = -3830377123484165376L;

    /**
     * svg的内容
     * <p>
     * http 开头，则表示引用url格式的svg
     * 否则，则表示传的是纯svg文本
     */
    private String svg;


    @Override
    public boolean validate() {
        if (!super.validate()) {
            return false;
        }

        if (StringUtils.isBlank(svg) || !(svg.startsWith("http") || svg.startsWith("<svg"))) {
            return false;
        }

        return true;
    }
}
