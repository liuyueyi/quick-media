package com.github.hui.media.console.action;

import com.github.hui.media.console.entity.IRequest;
import com.github.hui.media.console.entity.OutputEnum;
import com.github.hui.quick.plugin.base.constants.MediaType;
import lombok.Data;

/**
 * Created by yihui on 2017/12/3.
 */
@Data
public class BaseRequest implements IRequest {

    private static final long serialVersionUID = -5824276840657942205L;

    /**
     * 返回图片的样式, 默认为url格式
     * <p>
     * url: 表示图片的url
     * img: 表示base64格式的图片
     * stream : 表示直接返回图片
     */
    private String type;

    /**
     * 输出图片类型 ，目前只支持 jpeg, png
     */
    private String imgType;


    @Override
    public boolean validate() {
        return true;
    }


    public boolean urlReturn() {
        return OutputEnum.ofStr(type) == OutputEnum.URL;
    }

    public boolean base64Return() {
        return OutputEnum.ofStr(type) == OutputEnum.IMG;
    }

    public boolean streamReturn() {
        return OutputEnum.ofStr(type) == OutputEnum.STREAM;
    }


    /**
     * 删除jpeg图片
     *
     * @return
     */
    public boolean genJpegImg() {
        return !"png".equalsIgnoreCase(imgType);
    }


    public MediaType genMediaType() {
        if (genJpegImg()) {
            return MediaType.ImageJpg;
        } else {
            return MediaType.ImagePng;
        }
    }

}
