package com.hust.hui.quickmedia.web.wxapi.html;

import com.hust.hui.quickmedia.common.constants.MediaType;
import com.hust.hui.quickmedia.common.html.Html2ImageByJsWrapper;
import com.hust.hui.quickmedia.common.util.Base64Util;
import com.hust.hui.quickmedia.web.annotation.ValidateDot;
import com.hust.hui.quickmedia.web.entity.ResponseWrapper;
import com.hust.hui.quickmedia.web.entity.Status;
import com.hust.hui.quickmedia.web.wxapi.WxBaseResponse;
import com.hust.hui.quickmedia.web.wxapi.helper.ImgGenHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by yihui on 2017/12/1.
 */
@RestController
@Slf4j
public class Html2ImgAction {

    @RequestMapping(path = "wx/html2img", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
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


        String ans;
        WxBaseResponse response = new WxBaseResponse();
        if (html2ImgRequest.urlReturn()) {
            ans = ImgGenHelper.saveImg(bf);
            response.setImg(ans);
            response.setUrl("https://zbang.online/" + ans);
        } else {
            try {
                ans = Base64Util.encode(bf, "png");
                response.setBase64result(ans);
                response.setPrefix(MediaType.ImagePng.getPrefix());
            } catch (IOException e) {
                log.error("parse img to base64 error! req: {}, e:{}", html2ImgRequest, e);
                ans = ImgGenHelper.saveImg(bf);
                response.setImg(ans);
                response.setUrl("https://zbang.online/" + ans);
            }
        }
        response.setImg(ans);

        return ResponseWrapper.successReturn(response);
    }

}
