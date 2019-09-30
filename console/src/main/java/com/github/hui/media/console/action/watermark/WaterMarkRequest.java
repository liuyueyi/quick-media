package com.github.hui.media.console.action.watermark;

import com.github.hui.media.console.action.BaseRequest;
import com.github.hui.quick.plugin.image.wrapper.wartermark.WaterMarkOptions;
import lombok.*;

/**
 * Created by yihui on 2017/9/29.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WaterMarkRequest extends BaseRequest {
    private static final long serialVersionUID = -7890778866527419514L;
    /**
     * logo 的相对地址
     */
    private String logo;


    /**
     * 1 表示允许签名， 0 表示无签名
     */
    private int signEnabled;

    /**
     * 水印签名
     */
    private String sign;


    /**
     * 水印样式，主要是绘制的方式
     */
    private String style = WaterMarkOptions.WaterStyle.OVERRIDE_RIGHT_BOTTOM.name();


    private Integer opacity;


    private Integer rotate;


    private Integer logoHeight;


    @Override
    public boolean validate() {
        return true;
    }
}
