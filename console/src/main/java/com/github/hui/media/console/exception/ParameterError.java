package com.github.hui.media.console.exception;


import com.github.hui.media.console.entity.Status;

/**
 * Created by yihui on 2017/9/12.
 */
public class ParameterError extends IError {

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public Status.StatusEnum getStatus() {
        return Status.StatusEnum.ILLEGAL_PARAMS;
    }
}
