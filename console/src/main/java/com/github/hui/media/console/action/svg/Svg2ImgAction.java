package com.github.hui.media.console.action.svg;

import com.github.hui.media.console.action.BaseAction;
import com.github.hui.media.console.action.BaseResponse;
import com.github.hui.media.console.annotation.ValidateDot;
import com.github.hui.media.console.entity.ResponseWrapper;
import com.github.hui.media.console.entity.Status;
import com.github.hui.quick.plugin.svg.SvgRenderWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.util.Collections;

/**
 * Created by yihui on 2018/1/14.
 */
@Slf4j
@RestController
@RequestMapping(path = "media/svg")
@CrossOrigin(origins = "*")
public class Svg2ImgAction extends BaseAction {

    @ValidateDot
    @RequestMapping(path = {"render"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseWrapper<BaseResponse> parse2img(Svg2ImgRequest svg2ImgRequest) {
        try {
            BufferedImage bf;
            if (svg2ImgRequest.genJpegImg()) {
                bf = SvgRenderWrapper.convertToJpegAsImg(svg2ImgRequest.getSvg(), Collections.emptyMap());
            } else {
                bf = SvgRenderWrapper.convertToPngAsImg(svg2ImgRequest.getSvg(), Collections.emptyMap());
            }
            return buildReturn(svg2ImgRequest, bf);
        } catch (Exception e) {
            log.error("parse html2img error! req: {}, e: {}", svg2ImgRequest, e);
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.FAIL_MIX, "请确认输入的合法url");
        }
    }

}

