package com.hust.hui.quickmedia.web.web.constants;

import com.hust.hui.quickmedia.web.web.entity.HeadBannerVO;
import com.hust.hui.quickmedia.web.web.entity.base.IWebVO;
import com.hust.hui.quickmedia.web.web.entity.base.MenuWebVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihui on 2017/12/2.
 */
public class HeadBannerConstants {

    private static HeadBannerConstants instance = new HeadBannerConstants();

    public static HeadBannerConstants getInstance() {
        return instance;
    }


    public HeadBannerVO htmlBannerVO = initHtmlBanner();

    public HeadBannerVO markdownVO = initMdBanner();


    private HeadBannerConstants() {
    }

    private HeadBannerVO initHtmlBanner() {
        HeadBannerVO htmlBannerVO = new HeadBannerVO();
        htmlBannerVO.setTitle("HTML");
        htmlBannerVO.setSubTitle("网页输出图片 | html >>> image");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        list.add(new MenuWebVO("/web/html", "html"));
        list.add(new MenuWebVO("/web/html/toimg", "渲染图"));
        htmlBannerVO.setMenuList(list);
        return htmlBannerVO;
    }


    private HeadBannerVO initMdBanner() {
        HeadBannerVO mdBannerVO = new HeadBannerVO();
        mdBannerVO.setTitle("Markdown");
        mdBannerVO.setSubTitle("markdown 编辑预览 | markdown 输出pdf/image");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        list.add(new MenuWebVO("/web/markdown", "markdown"));
        list.add(new MenuWebVO("/web/markdown/edit", "markdown在线"));
        mdBannerVO.setMenuList(list);
        return mdBannerVO;
    }


}
