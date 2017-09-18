package com.hust.hui.quickmedia.web.api.image.parse;

import com.hust.hui.quickmedia.web.entity.IResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by yihui on 2017/9/12.
 */
@Getter
@Setter
@ToString
public class ImgParseResponse implements IResponse {

    private static final long serialVersionUID = 8894925118168275564L;


    /**
     * base64 格式图片的前缀
     */
    private String prefix;

    /**
     * base64 格式的图片
     */
    private String img;


    private String result;
}
