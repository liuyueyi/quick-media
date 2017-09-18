package com.hust.hui.quickmedia.web.api.image.create;

import com.alibaba.fastjson.JSON;
import com.hust.hui.quickmedia.common.constants.MediaType;
import com.hust.hui.quickmedia.common.image.ImgCreateWrapper;
import com.hust.hui.quickmedia.common.util.Base64Util;
import com.hust.hui.quickmedia.common.util.DomUtil;
import com.hust.hui.quickmedia.common.util.NumUtil;
import com.hust.hui.quickmedia.web.annotation.ValidateDot;
import com.hust.hui.quickmedia.web.entity.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yihui on 2017/8/18.
 */
@RestController
@Slf4j
public class ImgCreateController {


    /**
     * 生成图片
     * <p>
     * 测试case:
     *
     * 上下布局
     *
     * http://localhost:8080/img/create?w=400&contents=%7B%22contents%22%3A%5B%7B%22content%22%3A%22%E6%8B%9B%E9%AD%82%E9%85%B9%E7%BF%81%E5%AE%BE%E6%97%B8%5Cn%E9%83%91%E8%B5%B7%5Cn%5Cn%E5%90%9B%E4%B9%8B%E5%9C%A8%E4%B8%96%E5%B8%9D%E6%95%95%E4%B8%8B%EF%BC%8C%E5%90%9B%E4%B9%8B%E8%B0%A2%E4%B8%96%E5%B8%9D%E6%95%95%E5%9B%9E%E3%80%82%5Cn%E9%AD%82%E4%B9%8B%E4%B8%BA%E5%8F%98%E6%80%A7%E5%8E%9F%E8%BF%94%EF%BC%8C%E6%B0%94%E4%B9%8B%E4%B8%BA%E7%89%A9%E6%83%85%E6%9C%AC%E5%BC%80%E3%80%82%5Cn%E6%96%BC%E6%88%8F%E9%BE%99%E5%85%AE%E5%87%A4%E5%85%AE%E7%A5%9E%E6%B0%94%E7%9B%9B%EF%BC%8C%E5%99%AB%E5%98%BB%E9%AC%BC%E5%85%AE%E5%BD%92%E5%85%AE%E5%A4%A7%E5%9D%97%E5%9F%83%E3%80%82%22%2C%22fontName%22%3A%22%E5%AE%8B%E4%BD%93%22%2C%22fontSize%22%3A18%2C%22order%22%3A0%7D%2C%7B%22content%22%3A%22%E8%BA%AB%E5%8F%AF%E6%9C%BD%E5%90%8D%E4%B8%8D%E5%8F%AF%E6%9C%BD%EF%BC%8C%E9%AA%A8%E5%8F%AF%E7%81%B0%E7%A5%9E%E4%B8%8D%E5%8F%AF%E7%81%B0%E3%80%82%5Cn%E9%87%87%E7%9F%B3%E6%8D%89%E6%9C%88%E6%9D%8E%E7%99%BD%E9%9D%9E%E9%86%89%EF%BC%8C%E8%80%92%E9%98%B3%E9%81%BF%E6%B0%B4%E5%AD%90%E7%BE%8E%E9%9D%9E%E7%81%BE%E3%80%82%5Cn%E9%95%BF%E5%AD%99%E7%8E%8B%E5%90%89%E5%91%BD%E4%B8%8D%E5%A4%AD%EF%BC%8C%E7%8E%89%E5%B7%9D%E8%80%81%E5%AD%90%E8%AF%97%E4%B8%8D%E5%BE%98%E3%80%82%5Cn%E6%96%B0%E5%9F%8E%E7%BD%97%E9%9A%90%E5%9C%A8%E5%A5%87%E7%89%B9%EF%BC%8C%E9%92%B1%E5%A1%98%E6%BD%98%E9%98%86%E7%BB%88%E5%B4%94%E5%B5%AC%E3%80%82%5Cn%E9%98%B4%E5%85%AE%E9%AD%84%E5%85%AE%E6%9B%B7%E5%BE%80%EF%BC%8C%E9%98%B3%E5%85%AE%E9%AD%84%E5%85%AE%E6%9B%B7%E6%9D%A5%E3%80%82%5Cn%E5%90%9B%E5%85%B6%E5%BD%92%E6%9D%A5%EF%BC%8C%E6%95%85%E4%BA%A4%E5%AF%A5%E8%90%BD%E6%9B%B4%E6%95%A3%E6%BC%AB%E3%80%82%5Cn%E5%90%9B%E6%9D%A5%E5%BD%92%E6%9D%A5%EF%BC%8C%E5%B8%9D%E5%9F%8E%E7%BB%9A%E7%83%82%E5%8F%AF%E5%BE%98%E5%BE%8A%E3%80%82%5Cn%E5%90%9B%E5%85%B6%E5%BD%92%E6%9D%A5%EF%BC%8C%E4%B8%9C%E8%A5%BF%E5%8D%97%E5%8C%97%E4%B8%8D%E5%8F%AF%E5%8E%BB%E3%80%82%5Cn%E5%90%9B%E5%85%B6%E5%BD%92%E6%9D%A5%E3%80%82%5Cn%E6%98%A5%E7%A7%8B%E9%9C%9C%E9%9C%B2%E4%BB%A4%E4%BA%BA%E5%93%80%E3%80%82%5Cn%E8%8A%B1%E4%B9%8B%E6%98%8E%E5%90%BE%E6%97%A0%E4%B8%8E%E7%AC%91%EF%BC%8C%E5%8F%B6%E4%B9%8B%E9%99%A8%E5%90%BE%E5%AE%9E%E8%8B%A5%E6%91%A7%E3%80%82%5Cn%E6%99%93%E7%8C%BF%E5%95%B8%E5%90%BE%E9%97%BB%E6%B3%AA%E5%A0%95%EF%BC%8C%E5%AE%B5%E9%B9%A4%E7%AB%8B%E5%90%BE%E8%A7%81%E5%BF%83%E7%8C%9C%E3%80%82%5Cn%E7%8E%89%E6%B3%89%E5%85%B6%E6%B8%85%E5%8F%AF%E9%89%B4%EF%BC%8C%E8%A5%BF%E6%B9%96%E5%85%B6%E7%94%98%E5%8F%AF%E6%9D%AF%E3%80%82%5Cn%E5%AD%A4%E5%B1%B1%E6%9A%96%E6%A2%85%E9%A6%99%E5%8F%AF%E5%97%85%EF%BC%8C%E8%8A%B1%E7%BF%81%E8%91%AC%E8%8D%90%E8%8F%8A%E4%B9%8B%E9%9A%88%E3%80%82%5Cn%E5%90%9B%E5%85%B6%E5%BD%92%E6%9D%A5%EF%BC%8C%E5%8F%AF%E4%BC%B4%E9%80%8B%E4%BB%99%E4%B9%8B%E6%A2%85%EF%BC%8C%E5%8E%BB%E6%AD%A4%E5%8F%88%E5%A5%9A%E4%B9%8B%E5%93%89%E3%80%82%22%2C%22fontName%22%3A%22%E5%AE%8B%E4%BD%93%22%2C%22fontSize%22%3A18%2C%22order%22%3A2%7D%5D%2C%22imgs%22%3A%5B%7B%22img%22%3A%22https%3A%2F%2Fstatic.oschina.net%2Fuploads%2Fimg%2F201708%2F12175633_sOfz.png%22%2C%22order%22%3A1%7D%5D%7D
     *
     * 垂直，从左到右
     *
     * http://localhost:8080/img/create?h=400&topic=VERTICAL_LEFT&contents=%7B%22contents%22%3A%5B%7B%22content%22%3A%22%E6%8B%9B%E9%AD%82%E9%85%B9%E7%BF%81%E5%AE%BE%E6%97%B8%5Cn%E9%83%91%E8%B5%B7%5Cn%5Cn%E5%90%9B%E4%B9%8B%E5%9C%A8%E4%B8%96%E5%B8%9D%E6%95%95%E4%B8%8B%EF%BC%8C%E5%90%9B%E4%B9%8B%E8%B0%A2%E4%B8%96%E5%B8%9D%E6%95%95%E5%9B%9E%E3%80%82%5Cn%E9%AD%82%E4%B9%8B%E4%B8%BA%E5%8F%98%E6%80%A7%E5%8E%9F%E8%BF%94%EF%BC%8C%E6%B0%94%E4%B9%8B%E4%B8%BA%E7%89%A9%E6%83%85%E6%9C%AC%E5%BC%80%E3%80%82%5Cn%E6%96%BC%E6%88%8F%E9%BE%99%E5%85%AE%E5%87%A4%E5%85%AE%E7%A5%9E%E6%B0%94%E7%9B%9B%EF%BC%8C%E5%99%AB%E5%98%BB%E9%AC%BC%E5%85%AE%E5%BD%92%E5%85%AE%E5%A4%A7%E5%9D%97%E5%9F%83%E3%80%82%22%2C%22fontName%22%3A%22%E5%AE%8B%E4%BD%93%22%2C%22fontSize%22%3A18%2C%22order%22%3A0%7D%2C%7B%22content%22%3A%22%E8%BA%AB%E5%8F%AF%E6%9C%BD%E5%90%8D%E4%B8%8D%E5%8F%AF%E6%9C%BD%EF%BC%8C%E9%AA%A8%E5%8F%AF%E7%81%B0%E7%A5%9E%E4%B8%8D%E5%8F%AF%E7%81%B0%E3%80%82%5Cn%E9%87%87%E7%9F%B3%E6%8D%89%E6%9C%88%E6%9D%8E%E7%99%BD%E9%9D%9E%E9%86%89%EF%BC%8C%E8%80%92%E9%98%B3%E9%81%BF%E6%B0%B4%E5%AD%90%E7%BE%8E%E9%9D%9E%E7%81%BE%E3%80%82%5Cn%E9%95%BF%E5%AD%99%E7%8E%8B%E5%90%89%E5%91%BD%E4%B8%8D%E5%A4%AD%EF%BC%8C%E7%8E%89%E5%B7%9D%E8%80%81%E5%AD%90%E8%AF%97%E4%B8%8D%E5%BE%98%E3%80%82%5Cn%E6%96%B0%E5%9F%8E%E7%BD%97%E9%9A%90%E5%9C%A8%E5%A5%87%E7%89%B9%EF%BC%8C%E9%92%B1%E5%A1%98%E6%BD%98%E9%98%86%E7%BB%88%E5%B4%94%E5%B5%AC%E3%80%82%5Cn%E9%98%B4%E5%85%AE%E9%AD%84%E5%85%AE%E6%9B%B7%E5%BE%80%EF%BC%8C%E9%98%B3%E5%85%AE%E9%AD%84%E5%85%AE%E6%9B%B7%E6%9D%A5%E3%80%82%5Cn%E5%90%9B%E5%85%B6%E5%BD%92%E6%9D%A5%EF%BC%8C%E6%95%85%E4%BA%A4%E5%AF%A5%E8%90%BD%E6%9B%B4%E6%95%A3%E6%BC%AB%E3%80%82%5Cn%E5%90%9B%E6%9D%A5%E5%BD%92%E6%9D%A5%EF%BC%8C%E5%B8%9D%E5%9F%8E%E7%BB%9A%E7%83%82%E5%8F%AF%E5%BE%98%E5%BE%8A%E3%80%82%5Cn%E5%90%9B%E5%85%B6%E5%BD%92%E6%9D%A5%EF%BC%8C%E4%B8%9C%E8%A5%BF%E5%8D%97%E5%8C%97%E4%B8%8D%E5%8F%AF%E5%8E%BB%E3%80%82%5Cn%E5%90%9B%E5%85%B6%E5%BD%92%E6%9D%A5%E3%80%82%5Cn%E6%98%A5%E7%A7%8B%E9%9C%9C%E9%9C%B2%E4%BB%A4%E4%BA%BA%E5%93%80%E3%80%82%5Cn%E8%8A%B1%E4%B9%8B%E6%98%8E%E5%90%BE%E6%97%A0%E4%B8%8E%E7%AC%91%EF%BC%8C%E5%8F%B6%E4%B9%8B%E9%99%A8%E5%90%BE%E5%AE%9E%E8%8B%A5%E6%91%A7%E3%80%82%5Cn%E6%99%93%E7%8C%BF%E5%95%B8%E5%90%BE%E9%97%BB%E6%B3%AA%E5%A0%95%EF%BC%8C%E5%AE%B5%E9%B9%A4%E7%AB%8B%E5%90%BE%E8%A7%81%E5%BF%83%E7%8C%9C%E3%80%82%5Cn%E7%8E%89%E6%B3%89%E5%85%B6%E6%B8%85%E5%8F%AF%E9%89%B4%EF%BC%8C%E8%A5%BF%E6%B9%96%E5%85%B6%E7%94%98%E5%8F%AF%E6%9D%AF%E3%80%82%5Cn%E5%AD%A4%E5%B1%B1%E6%9A%96%E6%A2%85%E9%A6%99%E5%8F%AF%E5%97%85%EF%BC%8C%E8%8A%B1%E7%BF%81%E8%91%AC%E8%8D%90%E8%8F%8A%E4%B9%8B%E9%9A%88%E3%80%82%5Cn%E5%90%9B%E5%85%B6%E5%BD%92%E6%9D%A5%EF%BC%8C%E5%8F%AF%E4%BC%B4%E9%80%8B%E4%BB%99%E4%B9%8B%E6%A2%85%EF%BC%8C%E5%8E%BB%E6%AD%A4%E5%8F%88%E5%A5%9A%E4%B9%8B%E5%93%89%E3%80%82%22%2C%22fontName%22%3A%22%E5%AE%8B%E4%BD%93%22%2C%22fontSize%22%3A18%2C%22order%22%3A2%7D%5D%2C%22imgs%22%3A%5B%7B%22img%22%3A%22https%3A%2F%2Fstatic.oschina.net%2Fuploads%2Fimg%2F201708%2F12175633_sOfz.png%22%2C%22order%22%3A1%7D%5D%7D
     *
     *
     * 垂直，从右到左
     *
     * http://localhost:8080/img/create?h=400&topic=VERTICAL_RIGHT&contents=%7B%22contents%22%3A%5B%7B%22content%22%3A%22%E6%8B%9B%E9%AD%82%E9%85%B9%E7%BF%81%E5%AE%BE%E6%97%B8%5Cn%E9%83%91%E8%B5%B7%5Cn%5Cn%E5%90%9B%E4%B9%8B%E5%9C%A8%E4%B8%96%E5%B8%9D%E6%95%95%E4%B8%8B%EF%BC%8C%E5%90%9B%E4%B9%8B%E8%B0%A2%E4%B8%96%E5%B8%9D%E6%95%95%E5%9B%9E%E3%80%82%5Cn%E9%AD%82%E4%B9%8B%E4%B8%BA%E5%8F%98%E6%80%A7%E5%8E%9F%E8%BF%94%EF%BC%8C%E6%B0%94%E4%B9%8B%E4%B8%BA%E7%89%A9%E6%83%85%E6%9C%AC%E5%BC%80%E3%80%82%5Cn%E6%96%BC%E6%88%8F%E9%BE%99%E5%85%AE%E5%87%A4%E5%85%AE%E7%A5%9E%E6%B0%94%E7%9B%9B%EF%BC%8C%E5%99%AB%E5%98%BB%E9%AC%BC%E5%85%AE%E5%BD%92%E5%85%AE%E5%A4%A7%E5%9D%97%E5%9F%83%E3%80%82%22%2C%22fontName%22%3A%22%E5%AE%8B%E4%BD%93%22%2C%22fontSize%22%3A18%2C%22order%22%3A0%7D%2C%7B%22content%22%3A%22%E8%BA%AB%E5%8F%AF%E6%9C%BD%E5%90%8D%E4%B8%8D%E5%8F%AF%E6%9C%BD%EF%BC%8C%E9%AA%A8%E5%8F%AF%E7%81%B0%E7%A5%9E%E4%B8%8D%E5%8F%AF%E7%81%B0%E3%80%82%5Cn%E9%87%87%E7%9F%B3%E6%8D%89%E6%9C%88%E6%9D%8E%E7%99%BD%E9%9D%9E%E9%86%89%EF%BC%8C%E8%80%92%E9%98%B3%E9%81%BF%E6%B0%B4%E5%AD%90%E7%BE%8E%E9%9D%9E%E7%81%BE%E3%80%82%5Cn%E9%95%BF%E5%AD%99%E7%8E%8B%E5%90%89%E5%91%BD%E4%B8%8D%E5%A4%AD%EF%BC%8C%E7%8E%89%E5%B7%9D%E8%80%81%E5%AD%90%E8%AF%97%E4%B8%8D%E5%BE%98%E3%80%82%5Cn%E6%96%B0%E5%9F%8E%E7%BD%97%E9%9A%90%E5%9C%A8%E5%A5%87%E7%89%B9%EF%BC%8C%E9%92%B1%E5%A1%98%E6%BD%98%E9%98%86%E7%BB%88%E5%B4%94%E5%B5%AC%E3%80%82%5Cn%E9%98%B4%E5%85%AE%E9%AD%84%E5%85%AE%E6%9B%B7%E5%BE%80%EF%BC%8C%E9%98%B3%E5%85%AE%E9%AD%84%E5%85%AE%E6%9B%B7%E6%9D%A5%E3%80%82%5Cn%E5%90%9B%E5%85%B6%E5%BD%92%E6%9D%A5%EF%BC%8C%E6%95%85%E4%BA%A4%E5%AF%A5%E8%90%BD%E6%9B%B4%E6%95%A3%E6%BC%AB%E3%80%82%5Cn%E5%90%9B%E6%9D%A5%E5%BD%92%E6%9D%A5%EF%BC%8C%E5%B8%9D%E5%9F%8E%E7%BB%9A%E7%83%82%E5%8F%AF%E5%BE%98%E5%BE%8A%E3%80%82%5Cn%E5%90%9B%E5%85%B6%E5%BD%92%E6%9D%A5%EF%BC%8C%E4%B8%9C%E8%A5%BF%E5%8D%97%E5%8C%97%E4%B8%8D%E5%8F%AF%E5%8E%BB%E3%80%82%5Cn%E5%90%9B%E5%85%B6%E5%BD%92%E6%9D%A5%E3%80%82%5Cn%E6%98%A5%E7%A7%8B%E9%9C%9C%E9%9C%B2%E4%BB%A4%E4%BA%BA%E5%93%80%E3%80%82%5Cn%E8%8A%B1%E4%B9%8B%E6%98%8E%E5%90%BE%E6%97%A0%E4%B8%8E%E7%AC%91%EF%BC%8C%E5%8F%B6%E4%B9%8B%E9%99%A8%E5%90%BE%E5%AE%9E%E8%8B%A5%E6%91%A7%E3%80%82%5Cn%E6%99%93%E7%8C%BF%E5%95%B8%E5%90%BE%E9%97%BB%E6%B3%AA%E5%A0%95%EF%BC%8C%E5%AE%B5%E9%B9%A4%E7%AB%8B%E5%90%BE%E8%A7%81%E5%BF%83%E7%8C%9C%E3%80%82%5Cn%E7%8E%89%E6%B3%89%E5%85%B6%E6%B8%85%E5%8F%AF%E9%89%B4%EF%BC%8C%E8%A5%BF%E6%B9%96%E5%85%B6%E7%94%98%E5%8F%AF%E6%9D%AF%E3%80%82%5Cn%E5%AD%A4%E5%B1%B1%E6%9A%96%E6%A2%85%E9%A6%99%E5%8F%AF%E5%97%85%EF%BC%8C%E8%8A%B1%E7%BF%81%E8%91%AC%E8%8D%90%E8%8F%8A%E4%B9%8B%E9%9A%88%E3%80%82%5Cn%E5%90%9B%E5%85%B6%E5%BD%92%E6%9D%A5%EF%BC%8C%E5%8F%AF%E4%BC%B4%E9%80%8B%E4%BB%99%E4%B9%8B%E6%A2%85%EF%BC%8C%E5%8E%BB%E6%AD%A4%E5%8F%88%E5%A5%9A%E4%B9%8B%E5%93%89%E3%80%82%22%2C%22fontName%22%3A%22%E5%AE%8B%E4%BD%93%22%2C%22fontSize%22%3A18%2C%22order%22%3A2%7D%5D%2C%22imgs%22%3A%5B%7B%22img%22%3A%22https%3A%2F%2Fstatic.oschina.net%2Fuploads%2Fimg%2F201708%2F12175633_sOfz.png%22%2C%22order%22%3A1%7D%5D%7D
     *
     *
     * @param httpServletRequest
     * @param imgCreateRequest
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "img/create", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    @ValidateDot
    public ResponseWrapper<ImgCreateResponse> create(HttpServletRequest httpServletRequest, ImgCreateRequest imgCreateRequest) throws Exception {

        ImgCreateWrapper.Builder build = ImgCreateWrapper.build()
                .setDrawStyle(imgCreateRequest.getTopic())
                .setImgW(imgCreateRequest.getW() == null ? 0 : imgCreateRequest.getW())
                .setImgH(imgCreateRequest.getH() == null ? 0 : imgCreateRequest.getH())
                .setBgColor(NumUtil.decode2int(imgCreateRequest.getBgColor(), 0xffffffff))
                .setFontColor(NumUtil.decode2int(imgCreateRequest.getFontColor(), 0xff000000))
                .setFontSize(imgCreateRequest.getFontSize())
                .setFontName(imgCreateRequest.getFontName())
                .setLeftPadding(imgCreateRequest.getLeftPadding())
                .setRightPadding(imgCreateRequest.getRightPadding())
                .setTopPadding(imgCreateRequest.getTopPadding())
                .setBottomPadding(imgCreateRequest.getBottomPadding())
                .setLinePadding(imgCreateRequest.getLinePadding());


        String content = imgCreateRequest.getContents();
        ImgCreateRequest.Infos infos = null;
        try {
            infos = JSON.parseObject(content, ImgCreateRequest.Infos.class);
        } catch (Exception e) {
            log.error("parse content to Infos error! content: {} e: {}", content, e);
            throw new IllegalArgumentException("illegal contents!");
        }


        List<ImgCreateRequest.ContentInfo> contentList = infos.getContents();
        List<ImgCreateRequest.ImgInfo> imgList = infos.getImgs();

        List<ImgCreateRequest.IInfo> list = new ArrayList<>();
        list.addAll(contentList);
        list.addAll(imgList);

        list.sort(Comparator.comparingInt(ImgCreateRequest.IInfo::getOrder));

        for (ImgCreateRequest.IInfo info : list) {
            if (info instanceof ImgCreateRequest.ContentInfo) {
                build.drawContent(((ImgCreateRequest.ContentInfo) info).getContent());
            } else if (info instanceof ImgCreateRequest.ImgInfo) {
                build.drawImage(((ImgCreateRequest.ImgInfo) info).getImg());
            }
        }

        BufferedImage img = build.asImage();

        String ans = Base64Util.encode(img, "png");
        ImgCreateResponse res = new ImgCreateResponse();
        res.setImg(DomUtil.toDomSrc(ans, MediaType.ImagePng));
        return new ResponseWrapper<>(res);
    }

}
