package com.github.hui.quick.plugin.imagic.exception;


/**
 * Created by yihui on 18/04/17.
 */
public class ImgOperateException extends Exception {

    public ImgOperateException(String message) {
        super(message);
    }


    @Override
    public ImgOperateException fillInStackTrace() {
        return this;
    }
}
