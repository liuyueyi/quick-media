package com.hust.hui.quickmedia.web.api.image.parse;

import com.hust.hui.quickmedia.common.constants.MediaType;
import com.hust.hui.quickmedia.common.html.Html2ImageWrapper;
import com.hust.hui.quickmedia.common.markdown.MarkDown2HtmlWrapper;
import com.hust.hui.quickmedia.common.markdown.MarkdownEntity;
import com.hust.hui.quickmedia.common.util.Base64Util;
import com.hust.hui.quickmedia.common.util.DomUtil;
import com.hust.hui.quickmedia.web.annotation.ValidateDot;
import com.hust.hui.quickmedia.web.entity.ResponseWrapper;
import com.hust.hui.quickmedia.web.exception.Html2ImageError;
import com.hust.hui.quickmedia.web.exception.Markdown2HtmlError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2017/9/12.
 */
@RestController
@Slf4j
public class ImgParseController {

    private static final int DEFAULT_W = 600;

    private static final MediaType DEFAULT_TYPE = MediaType.ImageJpg;

    @RequestMapping(path = "img/parse", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    @ValidateDot
    public ResponseWrapper<ImgParseResponse> parse(HttpServletRequest httpServletRequest,
                                                   ImgParseRequest imgParseRequest) throws Exception {


        // 1. parse markdown to html
        MarkdownEntity entity = parseEntity(imgParseRequest.getContent());


        // 2. parse html to image
        BufferedImage image = parseImage(entity, imgParseRequest);


        // 3. build result
        ImgParseResponse response = new ImgParseResponse();
        response.setPrefix(DEFAULT_TYPE.getPrefix());
        response.setImg(Base64Util.encode(image, DEFAULT_TYPE.getExt()));
        response.setResult(DomUtil.toDomSrc(response.getImg(), DEFAULT_TYPE));


        // 4. return
        return ResponseWrapper.successReturn(response);
    }


    private MarkdownEntity parseEntity(String content) throws Markdown2HtmlError {
        MarkdownEntity entity = null;
        try {
            if (content.startsWith("http")) {
                entity = MarkDown2HtmlWrapper.ofUrl(content);
            } else {
                entity = MarkDown2HtmlWrapper.ofContent(content);
            }
        } catch (Exception e) {
            log.error("markdown to image error! e: {}", e);
            throw new Markdown2HtmlError();
        }

        return entity;
    }


    private BufferedImage parseImage(MarkdownEntity entity, ImgParseRequest imgParseRequest) throws Html2ImageError {
        boolean autoW = imgParseRequest.getW() == null;

        try {
            return Html2ImageWrapper.ofMd(entity)
                    .setAutoW(autoW)
                    .setW(autoW ? DEFAULT_W : imgParseRequest.getW())
                    .setAutoH(true)
                    .setOutType(DEFAULT_TYPE.getExt())
                    .build()
                    .asImage();
        } catch (Exception e) {
            log.error("parse html to image error! html: {}, e: {}", entity.toString(), e);
            throw new Html2ImageError();
        }
    }


}
