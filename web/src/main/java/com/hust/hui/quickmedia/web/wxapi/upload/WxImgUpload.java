package com.hust.hui.quickmedia.web.wxapi.upload;

import com.hust.hui.quickmedia.common.constants.MediaType;
import com.hust.hui.quickmedia.web.entity.ResponseWrapper;
import com.hust.hui.quickmedia.web.entity.Status;
import com.hust.hui.quickmedia.web.wxapi.WxBaseAction;
import com.hust.hui.quickmedia.web.wxapi.WxBaseResponse;
import com.hust.hui.quickmedia.web.wxapi.helper.ImgGenHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yihui on 2017/10/9.
 */
@Slf4j
@RestController
public class WxImgUpload extends WxBaseAction {

    @RequestMapping(path = {"wx/upload", "wx/wx/upload"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseWrapper<WxBaseResponse> upload(HttpServletRequest request) {
        MultipartFile file = null;
        if (request instanceof MultipartHttpServletRequest) {
            file = ((MultipartHttpServletRequest) request).getFile("image");
        }

        if (file == null) {
            throw new IllegalArgumentException("图片不能为空!");
        }


        // 目前只支持 jpg, png, webp 等静态图片格式
        String contentType = file.getContentType();
        String ext = MediaType.getExtByMime(contentType);

        try {
            String fileName = ImgGenHelper.saveTmpUploadFile(file.getInputStream(), ext);
            WxBaseResponse response = new WxBaseResponse();
            response.setImg(fileName);
            return ResponseWrapper.successReturn(response);
        } catch (Exception e) {
            log.error("save upload file error! e: {}", e);
            return ResponseWrapper.errorReturn(Status.StatusEnum.FAIL);
        }
    }
}
