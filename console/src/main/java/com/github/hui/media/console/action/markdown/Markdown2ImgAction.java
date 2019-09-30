package com.github.hui.media.console.action.markdown;


import com.github.hui.media.console.action.BaseAction;
import com.github.hui.media.console.action.BaseResponse;
import com.github.hui.media.console.annotation.ValidateDot;
import com.github.hui.media.console.entity.ResponseWrapper;
import com.github.hui.media.console.entity.Status;
import com.github.hui.media.console.util.DrawUtil;
import com.github.hui.quick.plugin.base.FileReadUtil;
import com.github.hui.quick.plugin.md.Html2ImageWrapper;
import com.github.hui.quick.plugin.md.MarkDown2HtmlWrapper;
import com.github.hui.quick.plugin.md.entity.MarkdownEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2017/10/15.
 */
@Slf4j
@RestController
@RequestMapping(path = "media/md")
@CrossOrigin(origins = "*")
public class Markdown2ImgAction extends BaseAction {

    public static String MD_CSS = null;

    static {
        try {
            MD_CSS = FileReadUtil.readAll("md/huimarkdown_code.css");
            MD_CSS = "<style type=\"text/css\">\n" + MD_CSS + "\n</style>\n";
        } catch (Exception e) {
            MD_CSS = "";
        }
    }


    @ValidateDot
    @RequestMapping(path = {"render"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseWrapper<BaseResponse> parse(MarkdownRequest downRequest) {
        String content = downRequest.getContent();

        if (downRequest.isNoborder()) {
            MarkDown2HtmlWrapper.MD_CSS = MD_CSS;
        }

        MarkdownEntity html = MarkDown2HtmlWrapper.ofContent(content);

        try {
            BufferedImage img =
                    Html2ImageWrapper.ofMd(html).setW(800).setAutoW(false).setAutoH(true).setOutType("jpg").build()
                            .asImage();
            // 添加签名
            DrawUtil.drawSign(img);
            return buildReturn(downRequest, img);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.FAIL_MIX, "转换失败!");
        }
    }

}
