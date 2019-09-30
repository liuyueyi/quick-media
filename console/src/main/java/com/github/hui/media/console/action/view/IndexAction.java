package com.github.hui.media.console.action.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by @author yihui in 17:54 19/8/15.
 */
@Controller
public class IndexAction {
    @RequestMapping(path = {"/", "/index", "/media/", "media/index"})
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}
