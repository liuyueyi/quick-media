package com.hust.hui.quickmedia.web.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Created by yihui on 2017/7/13.
 */
@Getter
@Setter
@ToString
public class ResponseWrapper<T> {

    private Status status;

    private T result;


    public ResponseWrapper(int code, String msg) {
        status = new Status(code, msg);
    }


    public ResponseWrapper(T t) {
        status = new Status(200, "success");
        this.result = t;
    }


    public static <T> ResponseWrapper<T> successReturn(T t) {
        return new ResponseWrapper(t);
    }


    @SuppressWarnings("unchecked")
    public static <T> ResponseWrapper<T> errorReturn(Status.StatusEnum statusEnum) {
        return errorReturn(statusEnum.getStatus());
    }


    @SuppressWarnings("unchecked")
    public static <T> ResponseWrapper<T> errorReturn(Status status) {
        return new ResponseWrapper<T>(status.getCode(), status.getMsg());
    }
}
