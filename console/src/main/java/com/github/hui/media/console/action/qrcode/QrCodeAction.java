package com.github.hui.media.console.action.qrcode;

import com.github.hui.media.console.action.BaseAction;
import com.github.hui.media.console.action.BaseResponse;
import com.github.hui.media.console.entity.ResponseWrapper;
import com.github.hui.media.console.entity.Status;
import com.github.hui.quick.plugin.base.NumUtil;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeDeWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * Created by yihui on 2017/10/15.
 */
@Slf4j
@RequestMapping(path = "media/qrcode")
@RestController
@CrossOrigin(origins = "*")
public class QrCodeAction extends BaseAction {

    @RequestMapping(path = "encode", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseWrapper<BaseResponse> parse(QrCodeEncRequest codeReq) {
        BufferedImage logo;
        try {
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            logo = getImg(request);
        } catch (Exception e) {
            logo = null;
        }

        try {
            BufferedImage ans =
                    QrCodeGenWrapper.of(codeReq.getContent()).setW(codeReq.getSize()).setH(codeReq.getSize())
                            .setLogo(logo).setLogoStyle(QrCodeOptions.LogoStyle.ROUND).setLogoBorder(true)
                            .setLogoBgColor(Color.WHITE).setErrorCorrection(getError(codeReq.getErrorLevel()))
                            .setPadding(codeReq.getPadding())
                            .setDrawPreColor(NumUtil.decode2int(codeReq.getPreColor(), 0xFF000000))
                            .setDrawBgColor(NumUtil.decode2int(codeReq.getBgColor(), 0xFFFFFFFF))
                            .setDetectInColor(NumUtil.decode2int(codeReq.getDetectColor(), 0xFF000000))
                            .setDrawStyle(codeReq.getStyle())
                            .setDrawEnableScale(Optional.ofNullable(codeReq.getScale()).orElse(false))
                            .asBufferedImage();

            return buildReturn(codeReq, ans);
        } catch (Exception e) {
            log.error("create qrcode error!: {}", e);
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.FAIL_MIX, "生成失败!");
        }
    }

    private ErrorCorrectionLevel getError(int level) {
        switch (level) {
            case 0:
                return ErrorCorrectionLevel.L;
            case 1:
                return ErrorCorrectionLevel.M;
            case 2:
                return ErrorCorrectionLevel.Q;
            default:
                return ErrorCorrectionLevel.H;
        }
    }

    /**
     * 二维码解析，测试demo: http://localhost:8089/media/qrcode/decode?image=https://blog.hhui.top/hexblog/imgs/info/wx.jpg
     *
     * @param request
     * @return
     */
    @RequestMapping(path = "decode", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseWrapper<String> decode(HttpServletRequest request) {
        BufferedImage img = getImg(request);
        if (img == null) {
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.ILLEGAL_PARAMS_MIX, "请选择包含二维码的图片!");
        }

        try {
            String ans = QrCodeDeWrapper.decode(img);
            return ResponseWrapper.successReturn(ans);
        } catch (Exception e) {
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.FAIL_MIX, "解析失败!");
        }
    }

}
