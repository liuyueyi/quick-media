package com.github.hui.media.console.exception;


import com.github.hui.media.console.entity.Status;

/**
 * Created by yihui on 2017/9/12.
 */
public abstract class IError extends Exception {

    public abstract Status.StatusEnum getStatus();

}
