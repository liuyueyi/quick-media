//package com.hust.hui.quickmedia.web.web;
//
//import com.hust.hui.quickmedia.web.web.constants.HeadBannerConstants;
//import com.hust.hui.quickmedia.web.web.constants.ToolBoxVOConstants;
//import com.hust.hui.quickmedia.web.web.helper.conf.BannerConfLoadHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import java.util.Map;
//
///**
// * Created by yihui on 2017/12/2.
// */
//@Controller
//@Slf4j
//public class WebIndexController {
//
//    public static final String TOOL_BOX_KEY = "toolBox";
//    public static final String HEAD_BANNER_KEY = "banner";
//
//
//    @RequestMapping(value = {"web", "web/", "web/views/index", "web/index"}, method = RequestMethod.GET)
//    public String index(Map<String, Object> map) {
//        map.put(TOOL_BOX_KEY, ToolBoxVOConstants.getInstance().getToolBoxList());
//        map.put(HEAD_BANNER_KEY, BannerConfLoadHelper.getInstance().getIndex());
//        return "index";
//    }
//
//
//    @RequestMapping(value = "web/image")
//    public String imageView(Map<String, Object> map) {
//        map.put(TOOL_BOX_KEY, ToolBoxVOConstants.getInstance().imgToolBox);
//        map.put(HEAD_BANNER_KEY, HeadBannerConstants.getInstance().imageVO);
//        return "views/image";
//    }
//
//
//    @RequestMapping(value = "web/image/templatemerge")
//    public String imageTemplateMergeView(Map<String, Object> map) {
//        return null;
//    }
//
//
//
//    @RequestMapping(value = "web/qrcode")
//    public String qrcodeView(Map<String, Object> map) {
//        map.put(TOOL_BOX_KEY, ToolBoxVOConstants.getInstance().qrcodeToolBox);
//        map.put(HEAD_BANNER_KEY, HeadBannerConstants.getInstance().qrcodeVO);
//        return "views/qrcode";
//    }
//
//
//    @RequestMapping(value = "web/audio")
//    public String audioView(Map<String, Object> map) {
//        map.put(TOOL_BOX_KEY, ToolBoxVOConstants.getInstance().audioToolBox);
//        map.put(HEAD_BANNER_KEY, HeadBannerConstants.getInstance().audioVO);
//        return "views/audio";
//    }
//
//
//    @RequestMapping(value = "web/markdown")
//    public String markdownView(Map<String, Object> map) {
//        map.put(TOOL_BOX_KEY, ToolBoxVOConstants.getInstance().markdownToolBox);
//        map.put(HEAD_BANNER_KEY, HeadBannerConstants.getInstance().markdownVO);
//        return "views/markdown";
//    }
//
//
//    @RequestMapping(value = "web/markdown/edit")
//    public String markdown2imgView(Map<String, Object> map) {
//        map.put(HEAD_BANNER_KEY, HeadBannerConstants.getInstance().markdownSubEditVO);
//        return "views/process/mdedit";
//    }
//
//
//    @RequestMapping(value = "web/html")
//    public String htmlView(Map<String, Object> map) {
//        map.put(TOOL_BOX_KEY, ToolBoxVOConstants.getInstance().htmlToolBox);
//        map.put(HEAD_BANNER_KEY, HeadBannerConstants.getInstance().htmlBannerVO);
//        return "views/html";
//    }
//
//
//    @RequestMapping(value = "web/html/toimg")
//    public String html2imgView(Map<String, Object> map) {
//        map.put(HEAD_BANNER_KEY, HeadBannerConstants.getInstance().htmlSubPrintImgBannerVO);
//        return "views/process/html2img";
//    }
//
//
//    @RequestMapping(value = "web/html/jsonpre")
//    public String htmlJsonPreView(Map<String, Object> map) {
//        map.put(HEAD_BANNER_KEY, HeadBannerConstants.getInstance().htmlSubJsonPreBannerVO);
//        return "views/process/jsonpre";
//    }
//}
