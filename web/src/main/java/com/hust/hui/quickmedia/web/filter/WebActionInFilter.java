package com.hust.hui.quickmedia.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yihui on 2017/8/3.
 */
public class WebActionInFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        servletRequest.setAttribute("toolBox", ToolBoxVOConstants.getInstance().getToolBoxList());

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        java.util.Date date = new java.util.Date();
        response.setDateHeader("Last-Modified", date.getTime());
        response.setDateHeader("Expires", date.getTime() + 60 * 1000 * 60); // 60分钟的缓存
        response.setHeader("Cache-Control", "public");
        response.setHeader("Pragma", "Pragma");
        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {

    }
}
