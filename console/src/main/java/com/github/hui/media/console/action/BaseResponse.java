package com.github.hui.media.console.action;

import com.github.hui.media.console.entity.IResponse;
import lombok.Data;

/**
 * Created by yihui on 2017/9/18.
 */
@Data
public class BaseResponse implements IResponse {
    private static final long serialVersionUID = -696503402977921464L;
    /**
     * base64格式的图片信息
     */
    private String base64result;

    /**
     * base64图片时，对应img标签图片格式头
     */
    private String prefix;

    /**
     * 相对路径
     */
    private String img;

    /**
     * 带http头的路径
     */
    private String url;
}
