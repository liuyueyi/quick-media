package com.hust.hui.quickmedia.web.exception;

import com.hust.hui.quickmedia.web.entity.Status;

/**
 * Created by yihui on 2017/9/12.
 */
public abstract class IError extends Exception {

    public abstract Status.StatusEnum getStatus();

}
