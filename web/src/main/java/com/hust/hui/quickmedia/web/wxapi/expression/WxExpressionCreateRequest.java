package com.hust.hui.quickmedia.web.wxapi.expression;

import com.hust.hui.quickmedia.common.img.create.ImgCreateOptions;
import com.hust.hui.quickmedia.web.entity.IRequest;
import lombok.Data;

/**
 * Created by yihui on 2017/9/19.
 */
@Data
public class WxExpressionCreateRequest implements IRequest {
    private static final long serialVersionUID = -3233706891250396030L;


    /**
     * 文字内容
     */
    private String content;


    /**
     * 模式
     * <p>
     * up_down, left_right, right_left
     * <p>
     * {@link ImgCreateOptions.DrawStyle#name}
     */
    private String style;


    /**
     * 模板id
     * <p>
     * 逐渐显示， 按行显示， 按标点显示，静态图
     * 文字静态，动图
     */
    private String templateId;


    /**
     * 图放在前面
     */
    private boolean imgFirst;


    @Override
    public boolean validate() {
        return true;
    }
}
