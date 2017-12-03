package com.hust.hui.quickmedia.web.wxapi;

import com.hust.hui.quickmedia.web.entity.IResponse;
import lombok.Data;

/**
 * Created by yihui on 2017/9/18.
 */
@Data
public class WxBaseResponse implements IResponse {

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
