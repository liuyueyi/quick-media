package com.hust.hui.quickmedia.web.wxapi.svg;

import com.hust.hui.quickmedia.web.wxapi.WxBaseRequest;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yihui on 2018/1/14.
 */
@Data
@ToString(callSuper = true)
public class Svg2ImgRequest extends WxBaseRequest {
    private static final long serialVersionUID = -3830377123484165376L;

    /**
     * svg的内容
     * <p>
     * http 开头，则表示引用url格式的svg
     * 否则，则表示传的是纯svg文本
     */
    private String svg;


    public boolean validate() {
        if (!super.validate()) {
            return false;
        }

        if (StringUtils.isBlank(svg) ||
                !(svg.startsWith("http") || svg.startsWith("<svg"))) {
            return false;
        }

        return true;
    }
}
