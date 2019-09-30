package com.github.hui.media.console.filter;


import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.filters.CorsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by @author yihui in 11:18 19/9/30.
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "selfProcessBeforeFilter")
public class SelfProcessBeforeFilter extends CorsFilter {
    private static final long serialVersionUID = 8758617650082254209L;
    private static Logger logger = LoggerFactory.getLogger("req");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        this.buildRequestLog((HttpServletRequest) servletRequest);
        super.doFilter(servletRequest, servletResponse, filterChain);

    }

    private void buildRequestLog(HttpServletRequest request) {
        if (request.getRequestURI().endsWith("css") || request.getRequestURI().endsWith("js") ||
                request.getRequestURI().endsWith("png")) {
            return;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("method=").append(request.getMethod()).append("; ");
        msg.append("uri=").append(request.getRequestURI());
        msg.append("remoteIp=").append(getClientIP(request));
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
