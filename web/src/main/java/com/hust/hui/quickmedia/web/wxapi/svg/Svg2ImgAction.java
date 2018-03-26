package com.hust.hui.quickmedia.web.wxapi.svg;

import com.github.hui.quick.plugin.svg.SvgRenderWrapper;
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
import java.util.Collections;

/**
 * Created by yihui on 2018/1/14.
 */
@RestController
@Slf4j
public class Svg2ImgAction extends WxBaseAction {

    @RequestMapping(path = {"wx/svg2img", "wx/wx/svg2img"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    @ValidateDot
    public ResponseWrapper<WxBaseResponse> parse2img(HttpServletRequest request,
                                                     Svg2ImgRequest svg2ImgRequest) {

        BufferedImage bf;
        try {
            if (svg2ImgRequest.genJpegImg()) {
                bf = SvgRenderWrapper.convertToJpegAsImg(svg2ImgRequest.getSvg(), Collections.emptyMap());
            } else {
                bf = SvgRenderWrapper.convertToPngAsImg(svg2ImgRequest.getSvg(), Collections.emptyMap());
            }
        } catch (Exception e) {
            log.error("parse html2img error! req: {}, e: {}", request, e);
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.FAIL_MIX, "请确认输入的合法url");
        }

        return buildReturn(svg2ImgRequest, bf);
    }

}
