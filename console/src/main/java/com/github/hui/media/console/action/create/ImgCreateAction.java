package com.github.hui.media.console.action.create;


import com.github.hui.media.console.action.BaseAction;
import com.github.hui.media.console.action.BaseResponse;
import com.github.hui.media.console.action.create.common.ImgCreateTemplateEnum;
import com.github.hui.media.console.entity.ResponseWrapper;
import com.github.hui.media.console.entity.Status;
import com.github.hui.quick.plugin.date.ChineseDateExtendTool;
import com.github.hui.quick.plugin.image.util.FontUtil;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateWrapper;
import com.github.hui.quick.plugin.image.wrapper.wartermark.WaterMarkOptions;
import com.github.hui.quick.plugin.image.wrapper.wartermark.WaterMarkWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2017/9/18.
 */
@Slf4j
@RestController
@RequestMapping(path = "media/create")
@CrossOrigin(origins = "*")
public class ImgCreateAction extends BaseAction {

    private static final int DEFAULT_SIZE = 640;
    private static final int DEFAULT_SMALL_SIZE = 400;

    private static final int LINE_PADDING = 10;
    private static final int TOP_PADDING = 20;
    private static final int BOTTOM_PADDING = 20;
    private static final int LEFT_PADDING = 20;
    private static final int RIGHT_PADDING = 20;


    @RequestMapping(value = {"create"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseWrapper<BaseResponse> create(HttpServletRequest httpServletRequest,
            ImgCreateRequest imgCreateRequest) {

        BufferedImage bfImg = getImg(httpServletRequest);

        ImgCreateTemplateEnum cenum = ImgCreateTemplateEnum.getEnum(imgCreateRequest.getTemplateId());
        if (cenum == null) {
            throw new IllegalArgumentException("非法的模板id!");
        }


        int paddingSize, imgSize;
        if (cenum.getDrawStyle() == ImgCreateOptions.DrawStyle.HORIZONTAL) {
            paddingSize = LEFT_PADDING + RIGHT_PADDING;
            imgSize = bfImg.getWidth();
        } else {
            paddingSize = TOP_PADDING + BOTTOM_PADDING;
            imgSize = bfImg.getHeight();
        }
        int size = calculateSize(imgCreateRequest.getMsg(), paddingSize, imgSize);
        ImgCreateWrapper.Builder builder =
                ImgCreateWrapper.build().setImgW(size).setImgH(size).setDrawStyle(cenum.getDrawStyle())
                        .setFont(FontUtil.BIG_DEFAULT_FONT).setLeftPadding(LEFT_PADDING).setRightPadding(RIGHT_PADDING)
                        .setTopPadding(TOP_PADDING).setBottomPadding(BOTTOM_PADDING).setLinePadding(LINE_PADDING)
                        .setBorderLeftPadding(18).setBorderTopPadding(20).setBorderBottomPadding(20).setBorder(false);

        try {
            BufferedImage ans;
            if (cenum.imgEnd()) {
                ans = builder.setAlignStyle(cenum.getAlignStyle()).drawContent(imgCreateRequest.getMsg())
                        .setFont(FontUtil.SMALLER_DEFAULT_ITALIC_FONT).setAlignStyle(
                                cenum.getDrawStyle() == ImgCreateOptions.DrawStyle.HORIZONTAL ?
                                        ImgCreateOptions.AlignStyle.RIGHT : ImgCreateOptions.AlignStyle.BOTTOM)
                        .drawContent(getSign(imgCreateRequest)).drawContent(" ")
                        .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER).drawImage(bfImg).asImage();
            } else {
                ans = builder.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER).drawImage(bfImg).drawContent(" ")
                        .setAlignStyle(cenum.getAlignStyle()).drawContent(imgCreateRequest.getMsg())
                        .setFont(FontUtil.SMALLER_DEFAULT_ITALIC_FONT).setAlignStyle(
                                cenum.getDrawStyle() == ImgCreateOptions.DrawStyle.HORIZONTAL ?
                                        ImgCreateOptions.AlignStyle.RIGHT : ImgCreateOptions.AlignStyle.BOTTOM)
                        .drawContent(getSign(imgCreateRequest)).asImage();
            }

            ans = WaterMarkWrapper.of(ans).setStyle(WaterMarkOptions.WaterStyle.OVERRIDE_RIGHT_BOTTOM)
                    .setWaterFont(FontUtil.SMALLER_DEFAULT_ITALIC_FONT)
                    .setWaterInfo("--" + ChineseDateExtendTool.getNowLunarDate()).setWaterColor(Color.BLACK)
                    .setInline(true).setWaterOpacity(1).build().asImage();


            return buildReturn(imgCreateRequest, ans);
        } catch (Exception e) {
            log.error("WxImgCreateAction!Create image error! e: {}", e);
            return ResponseWrapper.errorReturn(Status.StatusEnum.FAIL);
        }
    }


    private String getSign(ImgCreateRequest request) {
        if (request.getSignStatus() == null || request.getSignStatus() <= 0) {
            return null;
        }


        if (StringUtils.isBlank(request.getSign())) {
            return ChineseDateExtendTool.getNowLunarDate();
        }

        return request.getSign();
    }


    private int calculateSize(String msg, int paddingSize, int imgSize) {
        String[] strs = StringUtils.split(msg, "\n");
        int maxLen = 0;
        for (String str : strs) {
            if (str.length() > maxLen) {
                maxLen = str.length();
            }
        }


        // 最长的一行文字长度
        int size = FontUtil.BIG_DEFAULT_FONT.getSize() * (maxLen + 3) + paddingSize;


        // 不能超过默认的长度
        size = Math.min(size, DEFAULT_SIZE);


        // 不能比最小的还要小
        int tmpSize = Math.max(size, DEFAULT_SMALL_SIZE);

        imgSize += paddingSize;
        if (imgSize > size || imgSize < tmpSize) {
            return tmpSize;
        } else {
            return imgSize;
        }
    }

}
