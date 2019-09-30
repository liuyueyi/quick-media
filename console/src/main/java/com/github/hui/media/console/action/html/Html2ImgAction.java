package com.github.hui.media.console.action.html;

import com.github.hui.media.console.action.BaseAction;
import com.github.hui.media.console.action.BaseResponse;
import com.github.hui.media.console.annotation.ValidateDot;
import com.github.hui.media.console.entity.ResponseWrapper;
import com.github.hui.media.console.entity.Status;
import com.github.hui.quick.plugin.phantom.Html2ImageByJsWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;

/**
 * 通过phantomjs实现渲染
 * Created by yihui on 2017/12/1.
 */
@Slf4j
@RestController
@RequestMapping(path = "media/html")
@CrossOrigin(origins = "*")
public class Html2ImgAction extends BaseAction {

    @ValidateDot
    @RequestMapping(path = {"phantom"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseWrapper<BaseResponse> parseByJs2img(Html2ImgRequest html2ImgRequest) {
        if (!html2ImgRequest.getContent().startsWith("http")) {
            return ResponseWrapper.errorReturn(Status.StatusEnum.ILLEGAL_PARAMS);
        }

        BufferedImage bf;
        try {
            bf = Html2ImageByJsWrapper.renderHtml2Image(html2ImgRequest.getContent());
        } catch (Exception e) {
            log.error("parse html2img error! req: {}, e: {}", html2ImgRequest, e);
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.FAIL_MIX, "请确认输入的合法url");
        }


        return buildReturn(html2ImgRequest, bf);
    }

    @ValidateDot
    @RequestMapping(path = {"jdk"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseWrapper<BaseResponse> parseByJdk2img(Html2ImgRequest html2ImgRequest) {

        BufferedImage bf;
        try {
            bf = Html2ImageByJsWrapper.renderHtml2Image(html2ImgRequest.getContent());
        } catch (Exception e) {
            log.error("parse html2img error! req: {}, e: {}", html2ImgRequest, e);
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.FAIL_MIX, "请确认输入html文档");
        }

        return buildReturn(html2ImgRequest, bf);
    }
}
