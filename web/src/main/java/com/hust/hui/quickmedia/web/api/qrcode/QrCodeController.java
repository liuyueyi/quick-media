package com.hust.hui.quickmedia.web.api.qrcode;

import com.hust.hui.quickmedia.common.qrcode.QrCodeDeWrapper;
import com.hust.hui.quickmedia.common.qrcode.QrCodeGenWrapper;
import com.hust.hui.quickmedia.common.qrcode.QrCodeOptions;
import com.hust.hui.quickmedia.common.util.NumUtil;
import com.hust.hui.quickmedia.web.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by yihui on 2017/7/17.
 */
@RestController
@Slf4j
public class QrCodeController {

    /**
     * 二维码内容识别
     * 测试case:  http://localhost:8080/qrcode/decode?path=https://static.oschina.net/uploads/img/201707/09205944_0PzS.jpg
     *
     * @param httpServletRequest
     * @param path
     * @return
     */
    @RequestMapping(value = "/qrcode/decode", method = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST})
    public Response<QrCodeResponse> decode(HttpServletRequest httpServletRequest,
                                           @RequestParam("path") String path) {

        String ans;
        try {
            if (httpServletRequest instanceof MultipartHttpServletRequest) {
                MultipartFile file = ((MultipartHttpServletRequest) httpServletRequest).getFile("file");
                ans = QrCodeDeWrapper.decode(ImageIO.read(file.getInputStream()));
                return new Response<QrCodeResponse>(new QrCodeResponse(ans));
            } else if (StringUtils.isNotBlank(path) && path.startsWith("http")) {
                ans = QrCodeDeWrapper.decode(path);
                return new Response<QrCodeResponse>(new QrCodeResponse(ans));
            }
        } catch (Exception e) {
            log.error("reader info from qrcode error! e: {}", e);
            return new Response<QrCodeResponse>(5001, "read info from qrcode error!");
        }


        return new Response<QrCodeResponse>(5002, "请上传二维码图片or指定二维码的网络地址");
    }


    /**
     * 生成二维码
     * 测试case:  http://localhost:8080/qrcode/gen?content=https://my.oschina.net/u/566591/blog/1359432
     * <p>
     * 返回base64 编码的二维码图片， 前端可以直接使用  <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAIAAAAiOjnJAAAEIklEQVR42u3bMXbbQAwEUN3/0k6fIs+xMANQ/igtWuQuPosx1q8vpQL1sgUKLAWWAkspsBRYCqy/f9qq/7rvvy9+Zwlv7eDcigaXsNVBsMACCyywwAILLLC2YU2GhdjO5gTnfndwvdc6CBZYYIEFFlhggQXWDVi1BQ9GuZyG2sXv9KjfQbDAAgsssMACCyywPh1WbUqT811Lo7X4CRZYYIEFFlhggQUWWOGRTu6ramFtyzdYYIEFFlhggQUWWB8Eq3Ca59Ro5RFR7loHwQILLLDAAgsssMBagpWrrUj12z6tdRAssMDyKVhggQUWWHVYT6ytHFSbO9XWO7NpYIEFFlhggQUWWGCdgFU765PryuDgJXeWq+YssVdggQUWWGCBBRZYYN2AtbWkrQYPPkaOe+41WxvpgAUWWGCBBRZYYIH1LVivG5V7qlxXjkxpcqDBAgsssMACCyywwDoGayskDs6Oti6uTcO2Rljf7R1YYIEFFlhggQUWWDuwblq56aw28NkacE2mQrDAAgsssMACCyyw5mFt5aBazwY/rUmqvWZggQUWWGCBBRZYYN2G9fHTksHY+4rV1lgGLLDAAgsssMACC6wPgpULXLmuPGK0khth9W8EFlhggQUWWGCBBdbJ81jH48nuSa+tp6oFarDAAgsssMACCyywtmE9YvKw5TtnpRY/nzHSAQsssMACCyywwALrJ6lwMFPU5j9b/a4FzCNjN7DAAgsssMACCyywbsM6Iik3HjmyG1uTtN6sECywwAILLLDAAgus+WMzN519LVUuYOaCXn/bwQILLLDAAgsssMBqwRp0thVeBh8jd9QpR/YZ57HAAgsssMACCyywwDoNKxdPcrBqqTBntD8rAwsssMACCyywwALrBqzcsaFaeMk981YovjbCAgsssMACCyywwALrJKzavh9p/yD3wQNYg0F+5GKwwAILLLDAAgsssJZgbT30E4Pe4GPcDKdggQUWWGCBBRZYYB2DlQtruaNOuY4eyW5HXgawwAILLLDAAgsssI7Bqm30O0vamnjcvFGN7HcfACywwAILLLDAAgusEqyttJKLgY84CPWI6Lr25wawwAILLLDAAgsssK78u+2pgU9tAJL7dF0/WGCBBRZYYIEFFlhLqXArYT1ispRr4U2yYIEFFlhggQUWWGDdhrWV+2rRpvZuDFqppcIfLg0ssMACCyywwAILrBOwtvJX/1BRGsfnvSpggQUWWGCBBRZYYP0aWLnwkhsl5W6U2/ZCGgULLLDAAgsssMAC69Nh1YzW8mYO9FZGBgsssMACCyywwALrUbC20lluPJKz8gFTmp99FVhggQUWWGCBBRZYS7COLPhmDKxNaba6EBzpgAUWWGCBBRZYYIE1AEupd+O/LVBgKbAUWEqBpcBSYCk1W38ApvoJhIelIpkAAAAASUVORK5CYII="/>
     *
     * @param httpServletRequest
     * @param qrCodeRequest
     * @return
     */
    @RequestMapping(value = "/qrcode/gen", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public Response<QrCodeResponse> parse(HttpServletRequest httpServletRequest,
                                          QrCodeRequest qrCodeRequest) {

        if (qrCodeRequest.getContent() == null || qrCodeRequest.getContent().length() > 300) {
            return new Response<>(5002, "生成二维码的内容在0-300字符内");
        }


        try {
            String qr = this.build(qrCodeRequest).asString();
            qr = "<img src=\"data:image/png;base64," + qr + "\"/>";
            return new Response<>(new QrCodeResponse(qr));
        } catch (Exception e) {
            log.error("create qrcode error! request:{}, e: {}", qrCodeRequest, e);
            return new Response<>(5001, "gen qrcode error!");
        }
    }


    private QrCodeGenWrapper.Builder build(QrCodeRequest request) throws IOException {
        QrCodeGenWrapper.Builder builder = QrCodeGenWrapper.of(request.getContent())
                .setW(request.getSize())
                .setH(request.getSize())
                .setPadding(request.getPadding())
                .setCode(request.getCharset());

        QrCodeOptions.DrawStyle drawStyle = QrCodeOptions.DrawStyle.getDrawStyle(request.getDrawStyle());
        builder.setDrawStyle(drawStyle);
        if (drawStyle == QrCodeOptions.DrawStyle.IMAGE) {
            builder.setDrawImg(request.getDrawImg())
                    .setDrawSize4Img(request.getDrawSize4Img())
                    .setDrawRow2Img(request.getDrawRow2Img())
                    .setDrawCol2Img(request.getDrawCol2Img());
        } else {
            builder.setDrawBgColor(NumUtil.decode2int(request.getBgColor(), 0xFFFFFFFF))
                    .setDrawPreColor(NumUtil.decode2int(request.getPreColor(), 0xFF000000));
        }


        buildLogoConfig(builder, request);

        buildDetectConfig(builder, request);

        buildBgConfig(builder, request);

        return builder;
    }


    private void buildLogoConfig(QrCodeGenWrapper.Builder builder, QrCodeRequest request) throws IOException {
        if (StringUtils.isBlank(request.getLogo())) {
            return;
        }


        builder.setLogo(request.getLogo())
                .setLogoRate(request.getLogoRate() == null ? 12 : request.getLogoRate())
                .setLogoBorder(request.isLogoBorder())
                .setLogoStyle(QrCodeOptions.LogoStyle.getStyle(request.getLogo()))
                .setLogoBgColor(NumUtil.decode2int(request.getLogoBorderColor(), null));
    }


    private void buildDetectConfig(QrCodeGenWrapper.Builder builder, QrCodeRequest request) throws IOException {
        if (StringUtils.isNotBlank(request.getDetectImg())) {
            builder.setDetectImg(request.getDetectImg());
            return;
        }

        if (StringUtils.isNotBlank(request.getDetectInColor())) {
            builder.setDetectInColor(NumUtil.decode2int(request.getDetectInColor(), null));
        }

        if (StringUtils.isNotBlank(request.getDetectOutColor())) {
            builder.setDetectOutColor(NumUtil.decode2int(request.getDetectOutColor(), null));
        }
    }


    private void buildBgConfig(QrCodeGenWrapper.Builder builder, QrCodeRequest request) throws IOException {
        if (StringUtils.isBlank(request.getBgImg())) {
            return;
        }


        builder.setBgImg(request.getBgImg())
                .setBgW(request.getBgW() == null ? 0 : request.getBgW())
                .setBgH(request.getBgH() == null ? 0 : request.getBgH());

        QrCodeOptions.BgImgStyle style = QrCodeOptions.BgImgStyle.getStyle(request.getBgStyle());
        if (style == QrCodeOptions.BgImgStyle.FILL) {
            builder.setBgStartY(request.getBgY() == null ? 0 : request.getBgY())
                    .setBgStartX(request.getBgX() == null ? 0 : request.getBgX());
            return;
        }


        builder.setBgOpacity(request.getBgOpacity() == null ? 0.85f : request.getBgOpacity());
    }
}
