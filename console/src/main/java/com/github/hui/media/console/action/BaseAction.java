package com.github.hui.media.console.action;

import com.github.hui.media.console.entity.OutputEnum;
import com.github.hui.media.console.entity.ResponseWrapper;
import com.github.hui.media.console.entity.Status;
import com.github.hui.media.console.helper.ImgGenHelper;
import com.github.hui.media.console.validate.MediaValidate;
import com.github.hui.quick.plugin.base.Base64Util;
import com.github.hui.quick.plugin.base.ImageLoadUtil;
import com.github.hui.quick.plugin.base.constants.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by yihui on 2017/10/9.
 */
@Slf4j
public class BaseAction {

    private static final String HTTP_PREFIX = "https://zbang.online/";

    protected BufferedImage getImg(HttpServletRequest request) {
        MultipartFile file = null;
        if (request instanceof MultipartHttpServletRequest) {
            file = ((MultipartHttpServletRequest) request).getFile("image");
        }


        if (file == null) {
            try {
                String image = request.getParameter("image");
                if (StringUtils.isNotBlank(image) && !image.startsWith("/") && !image.startsWith("http")) {
                    image = ImgGenHelper.TMP_UPLOAD_PATH + image;
                }
                return ImageLoadUtil.getImageByPath(image);
            } catch (IOException e) {
                log.error("load upload image error! e: {}", e);
                throw new IllegalArgumentException("图片不能为空!");
            }
        }


        // 目前只支持 jpg, png, webp 等静态图片格式
        String contentType = file.getContentType();
        if (!MediaValidate.validateStaticImg(contentType)) {
            throw new IllegalArgumentException("不支持的图片类型");
        }

        // 获取BufferedImage对象
        try {
            return ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            log.error("WxImgCreateAction!Parse img from httpRequest to BuferedImage error! e: {}", e);
            throw new IllegalArgumentException("不支持的图片类型!");
        }
    }


    /**
     * 根据传入的参数，确定返回url格式图片还是base64格式的图片
     *
     * @param request
     * @param bf
     * @return
     */
    protected ResponseWrapper<BaseResponse> buildReturn(BaseRequest request, BufferedImage bf) {
        if (bf == null) {
            return ResponseWrapper.errorReturnMix(Status.StatusEnum.FAIL);
        }

        String ans;
        BaseResponse response = new BaseResponse();

        // 输出图片类型
        MediaType mediaType = request.genMediaType();

        if (request.urlReturn()) { // 直接返回url
            ans = ImgGenHelper.saveImg(bf);
            response.setImg(ans);
            response.setUrl(HTTP_PREFIX + ans);
        } else if (request.streamReturn()) {
            // 直接返回图片
            try {
                HttpServletResponse servletResponse =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                servletResponse.setContentType(mediaType.getMime());
                OutputStream os = servletResponse.getOutputStream();
                ImageIO.write(bf, mediaType.getExt(), os);
                os.flush();
                os.close();
                return ResponseWrapper.successReturn(response);
            } catch (Exception e) {
                log.error("general return stream img error! req: {}, e:{}", request, e);
                ans = ImgGenHelper.saveImg(bf);
                response.setImg(ans);
                response.setUrl(HTTP_PREFIX + ans);
            }
        } else { // base64的图片返回
            try {
                ans = Base64Util.encode(bf, "png");
                response.setBase64result(ans);
                response.setPrefix(mediaType.getPrefix());
            } catch (IOException e) {
                log.error("parse img to base64 error! req: {}, e:{}", request, e);
                ans = ImgGenHelper.saveImg(bf);
                response.setImg(ans);
                response.setUrl(HTTP_PREFIX + ans);
            }
        }
        return ResponseWrapper.successReturn(response);
    }
}
