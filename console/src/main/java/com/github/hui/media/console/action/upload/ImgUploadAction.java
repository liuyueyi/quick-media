package com.github.hui.media.console.action.upload;

import com.github.hui.media.console.action.BaseAction;
import com.github.hui.media.console.action.BaseResponse;
import com.github.hui.media.console.entity.ResponseWrapper;
import com.github.hui.media.console.entity.Status;
import com.github.hui.media.console.helper.ImgGenHelper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2017/10/9.
 */
@Slf4j
@RestController
@RequestMapping(path = "media/upload")
@CrossOrigin(origins = "*")
public class ImgUploadAction extends BaseAction {

    @RequestMapping(path = {"img"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseWrapper<BaseResponse> upload(HttpServletRequest request) {
        try {
            BufferedImage img = getImg(request);
            BaseResponse response = new BaseResponse();
            response.setImg(ImgGenHelper.saveImg(img));
            return ResponseWrapper.successReturn(response);
        } catch (Exception e) {
            log.error("save upload file error! e: {}", e);
            return ResponseWrapper.errorReturn(Status.StatusEnum.FAIL);
        }
    }
}
