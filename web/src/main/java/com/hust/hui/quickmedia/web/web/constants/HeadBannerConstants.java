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
    private HeadBannerConstants() {
    }


    private static HeadBannerConstants instance = new HeadBannerConstants();

    public static HeadBannerConstants getInstance() {
        return instance;
    }


    public HeadBannerVO indexVO = initIndexBanner();
    private HeadBannerVO initIndexBanner() {
        HeadBannerVO htmlBannerVO = new HeadBannerVO();
        htmlBannerVO.setTitle("Index");
        htmlBannerVO.setSubTitle("工具箱首页");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        htmlBannerVO.setMenuList(list);
        return htmlBannerVO;
    }



    public HeadBannerVO imageVO = initImageVO();
    private HeadBannerVO initImageVO() {
        HeadBannerVO vo = new HeadBannerVO();
        vo.setTitle("Index");
        vo.setSubTitle("图文工具箱 | Image toolbox | 提供图片的各式操作");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        list.add(new MenuWebVO("/web/image", "Image"));
        vo.setMenuList(list);
        return vo;
    }



    public HeadBannerVO audioVO = initAudioVO();
    private HeadBannerVO initAudioVO() {
        HeadBannerVO vo = new HeadBannerVO();
        vo.setTitle("Index");
        vo.setSubTitle("音频工具箱 | Audio toolbox | 提供音频的各式操作");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        list.add(new MenuWebVO("/web/audio", "Image"));
        vo.setMenuList(list);
        return vo;
    }


    public HeadBannerVO  qrcodeVO= initQrcodeVO();
    private HeadBannerVO initQrcodeVO() {
        HeadBannerVO vo = new HeadBannerVO();
        vo.setTitle("Index");
        vo.setSubTitle("二维码工具箱 | Qrcode toolbox | 提供二维码的生成和解码");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        list.add(new MenuWebVO("/web/qrcode", "Image"));
        vo.setMenuList(list);
        return vo;
    }


//    html
    public HeadBannerVO htmlBannerVO = initHtmlBanner();
    private HeadBannerVO initHtmlBanner() {
        HeadBannerVO htmlBannerVO = new HeadBannerVO();
        htmlBannerVO.setTitle("HTML");
        htmlBannerVO.setSubTitle("网页工具箱 | Html toolbox | 提供网页的各式操作");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        list.add(new MenuWebVO("/web/html", "html"));
        htmlBannerVO.setMenuList(list);
        return htmlBannerVO;
    }

    public HeadBannerVO htmlSubPrintImgBannerVO = initHtmlSubPrintImgBanner();
    private HeadBannerVO initHtmlSubPrintImgBanner() {
        HeadBannerVO htmlBannerVO = new HeadBannerVO();
        htmlBannerVO.setTitle("HTML");
        htmlBannerVO.setSubTitle("网页输出图片 | html >>> image | 指定html，返回图片");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        list.add(new MenuWebVO("/web/html", "html"));
        list.add(new MenuWebVO("/web/html/toimg", "渲染图"));
        htmlBannerVO.setMenuList(list);
        return htmlBannerVO;
    }

    public HeadBannerVO htmlSubJsonPreBannerVO = initHtmlSubJsonPreBanner();
    private HeadBannerVO initHtmlSubJsonPreBanner() {
        HeadBannerVO htmlBannerVO = new HeadBannerVO();
        htmlBannerVO.setTitle("HTML");
        htmlBannerVO.setSubTitle("Json预览 | Json在线编辑| Json实时预览 | Json串格式化");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        list.add(new MenuWebVO("/web/html", "html"));
        list.add(new MenuWebVO("/web/html/jsonpre", "json"));
        htmlBannerVO.setMenuList(list);
        return htmlBannerVO;
    }



//     markdown
    public HeadBannerVO markdownVO = initMdBanner();
    private HeadBannerVO initMdBanner() {
        HeadBannerVO mdBannerVO = new HeadBannerVO();
        mdBannerVO.setTitle("Markdown");
        mdBannerVO.setSubTitle("markdown 工具箱 | markdown toolbox | 在线预览输出");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        list.add(new MenuWebVO("/web/markdown", "markdown"));
        mdBannerVO.setMenuList(list);
        return mdBannerVO;
    }

    public HeadBannerVO markdownSubEditVO = initMdSubEditBanner();
    private HeadBannerVO initMdSubEditBanner() {
        HeadBannerVO mdBannerVO = new HeadBannerVO();
        mdBannerVO.setTitle("Markdown");
        mdBannerVO.setSubTitle("markdown 在线预览 | markdown 输出pdf/image");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        list.add(new MenuWebVO("/web/markdown", "markdown"));
        list.add(new MenuWebVO("/web/markdown/edit", "markdown在线"));
        mdBannerVO.setMenuList(list);
        return mdBannerVO;
    }



    public HeadBannerVO _404VO = init404Banner();
    private HeadBannerVO init404Banner() {
        HeadBannerVO mdBannerVO = new HeadBannerVO();
        mdBannerVO.setTitle("404");
        mdBannerVO.setSubTitle("Page Not Found");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        mdBannerVO.setMenuList(list);
        return mdBannerVO;
    }


    public HeadBannerVO _500VO = init500Banner();
    private HeadBannerVO init500Banner() {
        HeadBannerVO bannerVO = new HeadBannerVO();
        bannerVO.setTitle("500");
        bannerVO.setSubTitle("Inter Service Error");

        List<IWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/web/index", "首页"));
        bannerVO.setMenuList(list);
        return bannerVO;
    }
}
