package com.hust.hui.quickmedia.web.api.qrcode;

import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.hust.hui.quickmedia.common.qrcode.QrCodeDeWrapper;
import com.hust.hui.quickmedia.common.qrcode.QrCodeGenWrapper;
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

/**
 * Created by yihui on 2017/7/17.
 */
@RestController
@Slf4j
public class QrCodeController {

    /**
     * 生成二维码
     * 测试case:  http://localhost:8080/qrcode/gen?content=https://my.oschina.net/u/566591/blog/1359432
     *
     * 返回base64 编码的二维码图片， 前端可以直接使用  <img src="data:image/png;base54,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAIAAAAiOjnJAAAEIklEQVR42u3bMXbbQAwEUN3/0k6fIs+xMANQ/igtWuQuPosx1q8vpQL1sgUKLAWWAkspsBRYCqy/f9qq/7rvvy9+Zwlv7eDcigaXsNVBsMACCyywwAILLLC2YU2GhdjO5gTnfndwvdc6CBZYYIEFFlhggQXWDVi1BQ9GuZyG2sXv9KjfQbDAAgsssMACCyywPh1WbUqT811Lo7X4CRZYYIEFFlhggQUWWOGRTu6ramFtyzdYYIEFFlhggQUWWB8Eq3Ca59Ro5RFR7loHwQILLLDAAgsssMBagpWrrUj12z6tdRAssMDyKVhggQUWWHVYT6ytHFSbO9XWO7NpYIEFFlhggQUWWGCdgFU765PryuDgJXeWq+YssVdggQUWWGCBBRZYYN2AtbWkrQYPPkaOe+41WxvpgAUWWGCBBRZYYIH1LVivG5V7qlxXjkxpcqDBAgsssMACCyywwDoGayskDs6Oti6uTcO2Rljf7R1YYIEFFlhggQUWWDuwblq56aw28NkacE2mQrDAAgsssMACCyyw5mFt5aBazwY/rUmqvWZggQUWWGCBBRZYYN2G9fHTksHY+4rV1lgGLLDAAgsssMACC6wPgpULXLmuPGK0khth9W8EFlhggQUWWGCBBdbJ81jH48nuSa+tp6oFarDAAgsssMACCyywtmE9YvKw5TtnpRY/nzHSAQsssMACCyywwALrJ6lwMFPU5j9b/a4FzCNjN7DAAgsssMACCyywbsM6Iik3HjmyG1uTtN6sECywwAILLLDAAgus+WMzN519LVUuYOaCXn/bwQILLLDAAgsssMBqwRp0thVeBh8jd9QpR/YZ57HAAgsssMACCyywwDoNKxdPcrBqqTBntD8rAwsssMACCyywwALrBqzcsaFaeMk981YovjbCAgsssMACCyywwALrJKzavh9p/yD3wQNYg0F+5GKwwAILLLDAAgsssJZgbT30E4Pe4GPcDKdggQUWWGCBBRZYYB2DlQtruaNOuY4eyW5HXgawwAILLLDAAgsssI7Bqm30O0vamnjcvFGN7HcfACywwAILLLDAAgusEqyttJKLgY84CPWI6Lr25wawwAILLLDAAgsssK78u+2pgU9tAJL7dF0/WGCBBRZYYIEFFlhLqXArYT1ispRr4U2yYIEFFlhggQUWWGDdhrWV+2rRpvZuDFqppcIfLg0ssMACCyywwAILrBOwtvJX/1BRGsfnvSpggQUWWGCBBRZYYP0aWLnwkhsl5W6U2/ZCGgULLLDAAgsssMAC69Nh1YzW8mYO9FZGBgsssMACCyywwALrUbC20lluPJKz8gFTmp99FVhggQUWWGCBBRZYS7COLPhmDKxNaba6EBzpgAUWWGCBBRZYYIE1AEupd+O/LVBgKbAUWEqBpcBSYCk1W38ApvoJhIelIpkAAAAASUVORK5CYII="/>
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
            String qr = QrCodeGenWrapper.of(qrCodeRequest.getContent())
                    .setLogo(qrCodeRequest.getLogo())
                    .setCode(qrCodeRequest.getCharset())
                    .setW(qrCodeRequest.getSize())
                    .setH(qrCodeRequest.getSize())
                    .setBgColor(NumUtil.decode2int(qrCodeRequest.getBgColor(), MatrixToImageConfig.WHITE))
                    .setPreColor(NumUtil.decode2int(qrCodeRequest.getPreColor(), MatrixToImageConfig.BLACK))
                    .setPadding(qrCodeRequest.getPadding())
                    .asString();
            return new Response<>(new QrCodeResponse(qr));
        } catch (Exception e) {
            log.error("create qrcode error! request:{}, e: {}", qrCodeRequest, e);
            return new Response<>(5001, "gen qrcode error!");
        }
    }


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
}
