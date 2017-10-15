package com.hust.hui.quickmedia.web.wxapi.markdown;

import com.hust.hui.quickmedia.common.html.Html2ImageWrapper;
import com.hust.hui.quickmedia.common.markdown.MarkDown2HtmlWrapper;
import com.hust.hui.quickmedia.common.markdown.MarkdownEntity;
import com.hust.hui.quickmedia.common.util.DrawUtil;
import com.hust.hui.quickmedia.web.entity.ResponseWrapper;
import com.hust.hui.quickmedia.web.entity.Status;
import com.hust.hui.quickmedia.web.wxapi.WxBaseResponse;
import com.hust.hui.quickmedia.web.wxapi.helper.ImgGenHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2017/10/15.
 */
@RestController
public class Markdown2ImgAction {

    @RequestMapping(path = "wx/md2img")
    public ResponseWrapper<WxBaseResponse> parse(HttpServletRequest request) {
        String content = request.getParameter("content");


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

            String ans = ImgGenHelper.saveImg(img);
            WxBaseResponse response = new WxBaseResponse();
            response.setImg(ans);
            return ResponseWrapper.successReturn(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.FAIL_MIX, "转换失败!");
        }
    }

}
