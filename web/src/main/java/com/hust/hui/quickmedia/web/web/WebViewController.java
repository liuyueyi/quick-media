package com.hust.hui.quickmedia.web.web;

import com.hust.hui.quickmedia.web.web.helper.conf.BannerConfLoadHelper;
import com.hust.hui.quickmedia.web.web.helper.conf.ToolboxConfLoadHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by yihui on 2017/12/18.
 */
@Controller
public class WebViewController {

    public static final String TOOL_BOX_KEY = "toolBox";
    public static final String HEAD_BANNER_KEY = "banner";


    private static final String URL_PREFIX = "/webs/views/";


    @RequestMapping(value = {"webs", "webs/", "webs/views/index", "webs/index"}, method = RequestMethod.GET)
    public String index(Map<String, Object> map) {
        map.put(TOOL_BOX_KEY, ToolboxConfLoadHelper.getInstance().getAll());
        map.put(HEAD_BANNER_KEY, BannerConfLoadHelper.getInstance().getIndex());
        return "index";
    }


    /**
     * path 路径规则定义:
     *
     *  /web/view/{toolKey}/{funcKey}
     *
     *  如果只有三层，即只有a，表示进入的是box页（工具箱页面）
     *  如果有四层，即有a,有b，表示进入的是功能页
     *
     *  比如html页面的
     *
     *  /web/view/html/htmlPrintImg
     *
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(path = URL_PREFIX + "**")
    public String view(HttpServletRequest request, Map<String, Object> map) {
        String path = request.getRequestURI().substring(URL_PREFIX.length());

        String paths[] = StringUtils.split(path, "/");
        if (paths.length >= 2) {
            map.put(HEAD_BANNER_KEY, BannerConfLoadHelper.getInstance().getBannerVO(paths[1]));

            return "/views/" + path;
        } else {
            // toolbox页面
            map.put(TOOL_BOX_KEY, ToolboxConfLoadHelper.getInstance().getToolBox(paths[0]));
            map.put(HEAD_BANNER_KEY, BannerConfLoadHelper.getInstance().getBannerVO(paths[0]));

            return "/views/" + paths[0];
        }
    }
}
