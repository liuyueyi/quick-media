package com.github.hui.media.console.action.watermark;


import com.github.hui.media.console.action.BaseAction;
import com.github.hui.media.console.action.BaseResponse;
import com.github.hui.media.console.annotation.ValidateDot;
import com.github.hui.media.console.entity.ResponseWrapper;
import com.github.hui.media.console.entity.Status;
import com.github.hui.media.console.helper.ImgGenHelper;
import com.github.hui.quick.plugin.base.ImageLoadUtil;
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
 * 水印服务
 * Created by yihui on 2017/9/29.
 */
@Slf4j
@RestController
@RequestMapping(path = "media/water")
@CrossOrigin(origins = "*")
public class WxWaterMarkAction extends BaseAction {

    @ValidateDot
    @RequestMapping(path = {"render"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseWrapper<BaseResponse> markWater(HttpServletRequest httpServletRequest,
            WaterMarkRequest wxWaterMarkRequest) {
        BufferedImage img = getImg(httpServletRequest);
        if (img == null) {
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.ILLEGAL_PARAMS, "主图不能为空");
        }


        BufferedImage logo = null;
        try {
            if (StringUtils.isNotBlank(wxWaterMarkRequest.getLogo())) {
                logo = ImageLoadUtil.getImageByPath(ImgGenHelper.TMP_UPLOAD_PATH + "/" + wxWaterMarkRequest.getLogo());
            }
        } catch (Exception e) {
            log.error("read logo from local disk error! path: {}, e", wxWaterMarkRequest.getLogo(), e);
        }


        String sign = wxWaterMarkRequest.getSign();
        if (wxWaterMarkRequest.getSignEnabled() == 0) {
            sign = " ";
        } else if (StringUtils.isBlank(sign)) {
            sign = " 图文小工具";
        }


        BufferedImage water =
                WaterMarkWrapper.of(img).setStyle(wxWaterMarkRequest.getStyle()).setWaterLogo(logo).setWaterInfo(sign)
                        .setWaterColor(Color.LIGHT_GRAY).setPaddingY(40).setPaddingX(40).setInline(true)
                        .setWaterOpacity(wxWaterMarkRequest.getOpacity() == null ? 0.85f :
                                wxWaterMarkRequest.getOpacity() / 100.f).setWaterLogoHeight(
                        wxWaterMarkRequest.getLogoHeight() == null ? 0 : wxWaterMarkRequest.getLogoHeight())
                        .setRotate(wxWaterMarkRequest.getRotate() == null ? 0 : wxWaterMarkRequest.getRotate()).build()
                        .asImage();
        return buildReturn(wxWaterMarkRequest, water);
    }


}
