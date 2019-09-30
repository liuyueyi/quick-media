package com.github.hui.media.console.filter;


import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.filters.CorsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by @author yihui in 11:18 19/9/30.
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "selfProcessBeforeFilter")
public class SelfProcessBeforeFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger("req");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        this.buildCors((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        this.buildRequestLog((HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void buildCors(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
    }

    private void buildRequestLog(HttpServletRequest request) {
        if (request.getRequestURI().endsWith("css") || request.getRequestURI().endsWith("js") ||
                request.getRequestURI().endsWith("png")) {
            return;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("remoteIp=").append(getClientIP(request)).append("; ");
        msg.append("method=").append(request.getMethod()).append("; ");
        msg.append("uri=").append(request.getRequestURI());
        if (request.getQueryString() != null) {
            msg.append('?').append(request.getQueryString());
        }

        logger.info("{}", msg);
    }

    private static String getClientIP(HttpServletRequest request) {
        try {
            String ip = request.getHeader("X-Forwarded-For");
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                if (ip.length() > 15) {
                    String[] ips = ip.split(",");

                    for (int index = 0; index < ips.length; ++index) {
                        String strIp = ips[index];
                        if (!"unknown".equalsIgnoreCase(strIp)) {
                            ip = strIp;
                            break;
                        }
                    }
                }
            } else {
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }

                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }

                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                }

                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                }

                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
            }

            ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
            return ip;
        } catch (Exception e) {
            log.error("get remote ip error! e: {}", e);
            return "x.0.0.1";
        }
    }
}
