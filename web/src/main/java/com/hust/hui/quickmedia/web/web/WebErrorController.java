package com.hust.hui.quickmedia.web.web;

import com.hust.hui.quickmedia.web.web.constants.HeadBannerConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import static com.hust.hui.quickmedia.web.web.WebIndexController.HEAD_BANNER_KEY;

/**
 * Created by yihui on 2017/12/3.
 */
@Controller
public class WebErrorController {


    @RequestMapping(value = {"/404", "/web/404"})
    public String notFound404(Map<String, Object> map) {
        map.put(HEAD_BANNER_KEY, HeadBannerConstants.getInstance()._404VO);
        return "404";
    }


    @RequestMapping(value = {"/500", "/web/500"})
    public String interError500(Map<String, Object> map) {
        map.put(HEAD_BANNER_KEY, HeadBannerConstants.getInstance()._500VO);
        return "500";
    }
}
