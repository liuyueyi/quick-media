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
public class Response<T> {

    private Status status;

    private T result;


    public Response(int code, String msg) {
        status = new Status(code, msg);
    }


    public Response(T t) {
        status = new Status(200, "success");
        this.result = t;
    }
}
