package com.hust.hui.quickmedia.web.wxapi;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * Created by yihui on 2018/1/18.
 */
@ControllerAdvice
public class JsonpAdvice  extends AbstractJsonpResponseBodyAdvice {
    public JsonpAdvice() {
        super("callback");
    }
}