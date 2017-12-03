package com.hust.hui.quickmedia.web.filter;

import javax.servlet.*;
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
        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {

    }
}
