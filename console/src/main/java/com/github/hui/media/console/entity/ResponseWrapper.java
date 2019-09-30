package com.github.hui.media.console.entity;

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

    public static <T> ResponseWrapper<T> errorReturnMix(Status.StatusEnum statusEnum, String ... msgs) {
        return errorReturn(statusEnum.getStatus(), msgs);
    }


    @SuppressWarnings("unchecked")
    public static <T> ResponseWrapper<T> errorReturn(Status status, String... msgs) {
        String msg;
        if (msgs.length > 0) {
            msg = String.format(status.getMsg(), msgs);
        } else {
            msg = status.getMsg();
        }
        return new ResponseWrapper<T>(status.getCode(), msg);
    }
}
