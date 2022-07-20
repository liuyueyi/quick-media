package com.github.hui.quick.plugin.photo.options;

import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.photo.operator.PhotoOperator;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * @author yihui
 * @date 2022/6/14
 */
public abstract class OperateOptions<T> implements Serializable {
    protected BufferedImage img;

    protected String imgType = "png";

    private T delegate;

    public OperateOptions(T delegate) {
        this.delegate = delegate;
    }

    public T build() {
        return delegate;
    }

    public OperateOptions<T> setImg(BufferedImage img) {
        this.img = img;
        return this;
    }

    public OperateOptions<T> setImg(String img) {
        try {
            return setImg(ImageLoadUtil.getImageByPath(img));
        } catch (Exception e) {
            throw new IllegalArgumentException("not image uri:" + img);
        }
    }

    public BufferedImage getImg() {
        return img;
    }

    public String getImgType() {
        return imgType;
    }

    public OperateOptions<T> setImgType(String imgType) {
        this.imgType = imgType;
        return this;
    }

    public T getDelegate() {
        return delegate;
    }

    public OperateOptions<T> setDelegate(T delegate) {
        this.delegate = delegate;
        return this;
    }

    /**
     * create photo operator to process input image as you want
     *
     * @return
     */
    public abstract PhotoOperator operator();

}
