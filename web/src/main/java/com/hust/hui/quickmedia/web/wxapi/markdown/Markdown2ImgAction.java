package com.hust.hui.quickmedia.web.wxapi.markdown;

import com.hust.hui.quickmedia.common.html.Html2ImageWrapper;
import com.hust.hui.quickmedia.common.markdown.MarkDown2HtmlWrapper;
import com.hust.hui.quickmedia.common.markdown.MarkdownEntity;
import com.hust.hui.quickmedia.common.util.DrawUtil;
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
import java.io.UnsupportedEncodingException;

/**
 * Created by yihui on 2017/10/15.
 */
@RestController
@Slf4j
public class Markdown2ImgAction extends WxBaseAction {

    @RequestMapping(path = {"wx/md2img", "wx/wx/md2img"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    @ValidateDot
    public ResponseWrapper<WxBaseResponse> parse(HttpServletRequest request, MarkdownRequest downRequest) throws UnsupportedEncodingException {
        String content = downRequest.getContent();


        MarkdownEntity html = MarkDown2HtmlWrapper.ofContent(content);

        BufferedImage img = null;
        try {
            img = Html2ImageWrapper.ofMd(html)
                    .setW(800)
                    .setAutoW(false)
                    .setAutoH(true)
                    .setOutType("jpg")
                    .build()
                    .asImage();
            // 添加签名
            DrawUtil.drawSign(img);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.FAIL_MIX, "转换失败!");
        }

        return buildReturn(downRequest, img);
    }

}
