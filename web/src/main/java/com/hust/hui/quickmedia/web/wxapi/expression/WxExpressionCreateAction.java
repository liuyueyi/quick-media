package com.hust.hui.quickmedia.web.wxapi.expression;

import com.hust.hui.quickmedia.common.emoticon.EmotionWrapper;
import com.hust.hui.quickmedia.common.image.ImgCreateOptions;
import com.hust.hui.quickmedia.common.util.FontUtil;
import com.hust.hui.quickmedia.web.entity.ResponseWrapper;
import com.hust.hui.quickmedia.web.entity.Status;
import com.hust.hui.quickmedia.web.wxapi.WxBaseResponse;
import com.hust.hui.quickmedia.web.wxapi.common.WxEmotionCreateTemplateEnum;
import com.hust.hui.quickmedia.web.wxapi.helper.ImgGenHelper;
import com.hust.hui.quickmedia.web.wxapi.validate.MediaValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yihui on 2017/9/18.
 */
@Slf4j
public class WxExpressionCreateAction {

    private static final int DEFAULT_SIZE = 640;
    private static final int DEFAULT_SMALL_SIZE = 400;

    private static final int LINE_PADDING = 10;
    private static final int TOP_PADDING = 20;
    private static final int BOTTOM_PADDING = 20;
    private static final int LEFT_PADDING = 20;
    private static final int RIGHT_PADDING = 20;

    @ResponseBody
    @RequestMapping(path = "wx/emotions", method = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST})
    public ResponseWrapper<WxBaseResponse> create(HttpServletRequest httpServletRequest,
                                                  WxExpressionCreateRequest wxExpressionCreateRequest) throws Exception {


        String msg = wxExpressionCreateRequest.getContent();
        if (msg == null) {
            msg = "";
        }


        String saveImg = ImgGenHelper.genTmpImg("gif");
        String absFileName = ImgGenHelper.ABS_TMP_PATH + saveImg;

        EmotionWrapper.Builder builder = EmotionWrapper.ofContent(msg)
                .setW(DEFAULT_SIZE)
                .setH(DEFAULT_SIZE)
                .setLeftPadding(LEFT_PADDING)
                .setRightPadding(RIGHT_PADDING)
                .setTopPadding(TOP_PADDING)
                .setBottomPadding(BOTTOM_PADDING)
                .setLinePadding(LINE_PADDING)
                .setFont(FontUtil.DEFAULT_FONT)
                .setFontColor(Color.BLACK)
                .setDrawStyle(ImgCreateOptions.DrawStyle.getStyle(wxExpressionCreateRequest.getStyle()))
                .setImgFirst(wxExpressionCreateRequest.isImgFirst());

        WxEmotionCreateTemplateEnum template = WxEmotionCreateTemplateEnum.getTemplate(wxExpressionCreateRequest.getTemplateId());
        if (template == WxEmotionCreateTemplateEnum.GIF_SFONT) {
            builder.setGif(getGif(httpServletRequest));
        } else if(template == WxEmotionCreateTemplateEnum.MIMG_SFONT) {
            builder.setGif(getImgs(httpServletRequest));
        }


        try {
            builder.build().asFile(absFileName);
        } catch (Exception e) {
            log.error("create gif image error! e:{}", e);
            return ResponseWrapper.errorReturn(Status.StatusEnum.FAIL);
        }

        WxBaseResponse response = new WxBaseResponse();
        response.setImg(saveImg);
        return ResponseWrapper.successReturn(response);
    }


    public InputStream getGif(HttpServletRequest httpServletRequest) throws IOException {
        MultipartFile file = null;
        if (httpServletRequest instanceof MultipartHttpServletRequest) {
            file = ((MultipartHttpServletRequest) httpServletRequest).getFile("image");
        }

        if (file == null) {
            throw new IllegalArgumentException("图片不能为空");
        }


        String content = file.getContentType();

        if (MediaValidate.validateDynamicImg(content)) {
            throw new IllegalArgumentException("不支持的图片类型");
        }


        return file.getInputStream();
    }


    public List<BufferedImage> getImgs(HttpServletRequest httpServletRequest) throws IOException {
        List<MultipartFile> list = null;
        if (httpServletRequest instanceof MultipartHttpServletRequest) {
            list = ((MultipartHttpServletRequest) httpServletRequest).getFiles("image");
        }

        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException("上传图片不能为空");
        }


        list = list.stream().filter(f -> MediaValidate.validateStaticImg(f.getContentType())).collect(Collectors.toList());
        if (list.size() <= 1) {
            throw new IllegalArgumentException("上传jpg/png/webp格式图片数量不能小于1");
        }


        List<BufferedImage> imgList;
        try {
            imgList = list.stream().map(f -> {
                try {
                    return ImageIO.read(f.getInputStream());
                } catch (IOException e) {
                    log.error("create emotion parse upload file to image error!");
                    throw new IllegalArgumentException("不支持的图片类型!");
                }
            }).collect(Collectors.toList());
            return imgList;
        } catch (Exception e) {
            log.error("create emotion parse upload file to image error!");
            throw new IllegalArgumentException("不支持的图片类型!");
        }
    }
}
