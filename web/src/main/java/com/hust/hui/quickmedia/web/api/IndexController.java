package com.hust.hui.quickmedia.web.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yihui on 2017/7/12.
 */
@RestController
public class IndexController {

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public String index(HttpServletRequest request) {
        return "hello " + request.getParameter("name");
    }

}
