package com.hust.hui.quickmedia.web.web.helper.conf;

import com.hust.hui.quickmedia.common.util.YamlUtil;
import com.hust.hui.quickmedia.web.web.entity.HeadBannerVO;
import lombok.Getter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yihui on 2017/12/13.
 */
public class BannerConfLoadHelper {
    private static final String BANNER_INFO_CONF_PATH = "conf/bannerInfo.yaml";

    @Getter
    private BannerConfContainer container;

    private static BannerConfLoadHelper instance = new BannerConfLoadHelper();

    private BannerConfLoadHelper() {
        try {
            container = YamlUtil.read(BANNER_INFO_CONF_PATH, BannerConfContainer.class);
        } catch (IOException e) {
            e.printStackTrace();
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
