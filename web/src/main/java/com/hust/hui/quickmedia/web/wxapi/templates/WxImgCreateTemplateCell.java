package com.hust.hui.quickmedia.web.wxapi.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yihui on 2017/9/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxImgCreateTemplateCell {
    /**
     * 模板名
     */
    private String name;

    /**
     * 模板id
     */
    private String id;


    /**
     * 模板图片
     */
    private String img;


    /**
     * 热度
     */
    private int total;
}
