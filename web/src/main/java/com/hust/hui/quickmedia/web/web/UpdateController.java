package com.hust.hui.quickmedia.web.web;

import com.github.hui.quick.plugin.base.ProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yihui on 2017/12/28.
 */
@Controller
@Slf4j
public class UpdateController {

    @RequestMapping(path = "/webs/xxx/yyy/up")
    @ResponseBody
    public String update(HttpServletRequest request) {
        String method = request.getParameter("method");
        if ("book".equalsIgnoreCase(method)) {
            new Thread(() -> {
                try {
                    ProcessUtil.instance().process("cd /mydata; sh /mydata/build.sh");
                } catch (Exception e) {
                    log.error("update error! e: {}", e);
                }
            }).start();
        }


        return "over";
    }
}
