package com.hust.hui.quickmedia.web.web.helper.conf;

import com.hust.hui.quickmedia.web.web.entity.HeadBannerVO;
import lombok.Data;

/**
 * Created by yihui on 2017/12/13.
 */
@Data
public class BannerConfContainer {

    private HeadBannerVO index;


    // image 相关
    private HeadBannerVO image;
    private HeadBannerVO imageCut;
    private HeadBannerVO imageScale;
    private HeadBannerVO imageMerge;


    // 音频
    private HeadBannerVO audio;


    // 二维码
    private HeadBannerVO qrcode;


    // html
    private HeadBannerVO html;
    private HeadBannerVO htmlPrintImg;
    private HeadBannerVO htmlJsonPre;
    private HeadBannerVO svgPrintImg;


    // markdown
    private HeadBannerVO markdown;
    private HeadBannerVO markdownEdit;
}
