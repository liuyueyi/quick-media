package com.hust.hui.quickmedia.web.wxapi.holder;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by yihui on 2017/9/24.
 */
@Component
public class ContextHolder implements ApplicationContextAware {

    private static ApplicationContext apc;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        apc = applicationContext;
    }



}
