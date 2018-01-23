package com.hust.hui.quickmedia.web.wxapi.html;

import com.hust.hui.quickmedia.common.html.Html2ImageByJsWrapper;
import com.hust.hui.quickmedia.web.annotation.ValidateDot;
import com.hust.hui.quickmedia.web.entity.ResponseWrapper;
import com.hust.hui.quickmedia.web.entity.Status;
import com.hust.hui.quickmedia.web.wxapi.WxBaseAction;
import com.hust.hui.quickmedia.web.wxapi.WxBaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2017/12/1.
 */
@RestController
@Slf4j
public class Html2ImgAction extends WxBaseAction {

    @RequestMapping(path = {"wx/html2img", "wx/wx/html2img"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    @ValidateDot
    public ResponseWrapper<WxBaseResponse> parse2img(HttpServletRequest request,
                                                     Html2ImgRequest html2ImgRequest) {

        BufferedImage bf = null;
        try {
            bf = Html2ImageByJsWrapper.renderHtml2Image(html2ImgRequest.getUrl());
        } catch (Exception e) {
            log.error("parse html2img error! req: {}, e: {}", request, e);
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.FAIL_MIX, "请确认输入的合法url");
        }


        return buildReturn(html2ImgRequest, bf);
    }

}
