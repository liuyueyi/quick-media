package com.hust.hui.quickmedia.web.wxapi.watermark;

import com.hust.hui.quickmedia.common.img.wartermark.WaterMarkOptions;
import com.hust.hui.quickmedia.web.entity.IRequest;
import lombok.Data;

/**
 * Created by yihui on 2017/9/29.
 */
@Data
public class WxWaterMarkRequest implements IRequest {

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
