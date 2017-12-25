package com.hust.hui.quickmedia.web.web.helper.conf;

import com.alibaba.fastjson.JSON;
import com.hust.hui.quickmedia.common.util.YamlUtil;
import com.hust.hui.quickmedia.web.web.entity.HeadBannerVO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yihui on 2017/12/13.
 */
@Slf4j
public class BannerConfLoadHelper {
    private static final String BANNER_INFO_CONF_PATH = "conf/bannerInfo.yaml";

    @Getter
    private BannerConfContainer container;

    private static BannerConfLoadHelper instance = new BannerConfLoadHelper();

    private BannerConfLoadHelper() {
        try {
            container = YamlUtil.read(BANNER_INFO_CONF_PATH, BannerConfContainer.class);
        } catch (Exception e) {
            log.error("load bannerConf error! e: {}", e);
            container = JSON.parseObject("{\"audio\":{\"menuList\":[{\"menu\":\"首页\",\"url\":\"/webs/views/index\"},{\"menu\":\"Audio\",\"url\":\"/webs/views/audio\"}],\"subTitle\":\"音频工具箱 | Audio toolbox | 提供音频的各式操作\",\"title\":\"Audio\"},\"html\":{\"menuList\":[{\"menu\":\"首页\",\"url\":\"/webs/views/index\"},{\"menu\":\"Html\",\"url\":\"/webs/views/html\"}],\"subTitle\":\"网页工具箱 | Html toolbox | 提供网页相关的各式小工具\",\"title\":\"Html\"},\"htmlJsonPre\":{\"menuList\":[{\"menu\":\"首页\",\"url\":\"/webs/views/index\"},{\"menu\":\"Html\",\"url\":\"/webs/views/html\"},{\"menu\":\"Json\",\"url\":\"/webs/views/html/htmlJsonPre\"}],\"subTitle\":\"Json预览 | Json在线编辑| Json实时预览 | Json串格式化\",\"title\":\"jsonView\"},\"htmlPrintImg\":{\"menuList\":[{\"menu\":\"首页\",\"url\":\"/webs/views/index\"},{\"menu\":\"Html\",\"url\":\"/webs/views/html\"},{\"menu\":\"渲染图\",\"url\":\"/webs/views/html/htmlPrintImg\"}],\"subTitle\":\"网页输出图片 | html >>> image | 指定html，返回图片\",\"title\":\"html2img\"},\"image\":{\"menuList\":[{\"menu\":\"首页\",\"url\":\"/webs/views/index\"},{\"menu\":\"Image\",\"url\":\"/webs/views/image\"}],\"subTitle\":\"图文工具箱 | Image toolbox | 提供图片的各式操作\",\"title\":\"Image\"},\"imageCut\":{\"menuList\":[{\"menu\":\"首页\",\"url\":\"/webs/views/index\"},{\"menu\":\"Image\",\"url\":\"/webs/views/image\"},{\"menu\":\"Cut\",\"url\":\"/webs/views/image/imageCut\"}],\"subTitle\":\"图片裁剪 | Image Cut\",\"title\":\"ImageCut\"},\"imageMerge\":{\"menuList\":[{\"menu\":\"首页\",\"url\":\"/webs/views/index\"},{\"menu\":\"Image\",\"url\":\"/webs/views/image\"},{\"menu\":\"Merge\",\"url\":\"/webs/views/image/imageMerge\"}],\"subTitle\":\"图片合成 | Image Merge\",\"title\":\"ImageMerge\"},\"imageScale\":{\"menuList\":[{\"menu\":\"首页\",\"url\":\"/webs/views/index\"},{\"menu\":\"Image\",\"url\":\"/webs/views/image\"},{\"menu\":\"Scale\",\"url\":\"/webs/views/image/imageScale\"}],\"subTitle\":\"图片压缩 | Image Scale\",\"title\":\"ImageScale\"},\"index\":{\"menuList\":[{\"menu\":\"首页\",\"url\":\"/webs/views/index\"}],\"subTitle\":\"工具箱首页\",\"title\":\"index\"},\"markdown\":{\"menuList\":[{\"menu\":\"首页\",\"url\":\"/webs/views/index\"},{\"menu\":\"markdown\",\"url\":\"/webs/views/markdown\"}],\"subTitle\":\"markdown 工具箱 | markdown toolbox | 在线预览输出\",\"title\":\"Markdown\"},\"markdownEdit\":{\"menuList\":[{\"menu\":\"首页\",\"url\":\"/webs/views/index\"},{\"menu\":\"markdown\",\"url\":\"/webs/views/markdown\"},{\"menu\":\"markdown在线\",\"url\":\"/webs/views/markdown/markdownEdit\"}],\"subTitle\":\"markdown 在线预览 | markdown 输出pdf/image\",\"title\":\"MarkdownEdit\"},\"qrcode\":{\"menuList\":[{\"menu\":\"首页\",\"url\":\"/webs/views/index\"},{\"menu\":\"QrCode\",\"url\":\"/webs/views/qrcode\"}],\"subTitle\":\"二维码工具箱 | Qrcode toolbox | 提供二维码的生成和解码\",\"title\":\"QrCode\"}}",
                    BannerConfContainer.class);
        }
    }

    public static BannerConfLoadHelper getInstance() {
        return instance;
    }


    private Map<String, HeadBannerVO> bannerCache = new ConcurrentHashMap<>();

    public HeadBannerVO getBannerVO(String key) {
        HeadBannerVO vo = bannerCache.get(key);
        if (vo == null) {
            try {
                Field field = container.getClass().getDeclaredField(key);
                field.setAccessible(true);
                vo = (HeadBannerVO) field.get(container);
                bannerCache.put(key, vo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return vo;
    }


    public HeadBannerVO getIndex() {
        return container.getIndex();
    }
}
