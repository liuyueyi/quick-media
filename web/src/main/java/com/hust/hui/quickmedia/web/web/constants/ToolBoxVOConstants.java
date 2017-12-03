package com.hust.hui.quickmedia.web.web.constants;

import com.hust.hui.quickmedia.web.web.entity.base.BaseToolWebVO;
import com.hust.hui.quickmedia.web.web.entity.base.IWebVO;
import com.hust.hui.quickmedia.web.web.entity.ToolBoxVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihui on 2017/12/2.
 */
public class ToolBoxVOConstants {

    public List<ToolBoxVO> imgToolBox = new ArrayList<>();

    public List<ToolBoxVO> qrcodeToolBox = new ArrayList<>();

    public List<ToolBoxVO> audioToolBox = new ArrayList<>();

    public List<ToolBoxVO> markdownToolBox = new ArrayList<>();

    public List<ToolBoxVO> htmlToolBox = new ArrayList<>();

    public List<ToolBoxVO> list = new ArrayList<>();

    private static ToolBoxVOConstants instance = new ToolBoxVOConstants();

    public static ToolBoxVOConstants getInstance() {
        return instance;
    }

    public List<ToolBoxVO> getToolBoxList() {
        return list;
    }

    private ToolBoxVOConstants() {
        initImgToolBox();
        initHtmlToolBox();
        initAudioToolBox();
        initQrcodeToolBox();
        initMarkdownToolBox();

        list.addAll(imgToolBox);
        list.addAll(qrcodeToolBox);
        list.addAll(audioToolBox);
        list.addAll(markdownToolBox);
        list.addAll(htmlToolBox);
    }


    private void initImgToolBox() {
        ToolBoxVO imgToolBox = new ToolBoxVO();

        imgToolBox.setToolName("图片工具箱/Image Tool+");

        List<IWebVO> list = new ArrayList<>();

        list.add(new BaseToolWebVO("图片压缩", "支持根据宽高进行压缩，支持根据精度进行压缩", "#", "fa-image"));
        list.add(new BaseToolWebVO("图片裁剪", "图片裁剪，根据你的意愿，随意的裁剪", "#", "fa-fire-extinguisher"));
        list.add(new BaseToolWebVO("图片转码", "jpg,webp,png...图片格式任意互转", "#", "fa-anchor"));
        list.add(new BaseToolWebVO("图片旋转", "转来转去，总有个适合你的旋转角度", "#", "fa-shield"));
        list.add(new BaseToolWebVO("圆角图片", "圆角，圆角，圆角图片也开始支持了", "#", "fa-bullseye"));
        list.add(new BaseToolWebVO("添加边框", "小边框，背景也是不可缺少的", "#", "fa-play-circle"));
        list.add(new BaseToolWebVO("模板合成", "提供酷炫的模板，来拼装属于你的图片吧", "#", "fa-ticket"));
        imgToolBox.setTools(list);

        this.imgToolBox.add(imgToolBox);
    }


    private void initQrcodeToolBox() {
        ToolBoxVO qrcodeToolBox = new ToolBoxVO();

        qrcodeToolBox.setToolName("二维码工具箱/Qrcode Tool+");

        List<IWebVO> list = new ArrayList<>();

        list.add(new BaseToolWebVO("二维码生成", "生成各种酷炫的二维码", "#", "fa-qrcode"));
        list.add(new BaseToolWebVO("二维码解析", "黑白黑白框，到底是个什么鬼？让我们来解析二维码隐藏的信息", "#", "fa-barcode"));

        qrcodeToolBox.setTools(list);

        this.qrcodeToolBox.add(qrcodeToolBox);
    }


    private void initAudioToolBox() {
        ToolBoxVO audioToolBox = new ToolBoxVO();

        audioToolBox.setToolName("音频工具箱/Audio Tool+");

        List<IWebVO> list = new ArrayList<>();

        list.add(new BaseToolWebVO("音频转码", "我们的目标是：不再有手机播放不了的声音", "#", "fa-file-video-o"));

        audioToolBox.setTools(list);

        this.audioToolBox.add(audioToolBox);
    }


    private void initMarkdownToolBox() {
        ToolBoxVO markdownToolBox = new ToolBoxVO();

        markdownToolBox.setToolName("Markdown工具箱/Markdown Tool+");

        List<IWebVO> list = new ArrayList<>();

        list.add(new BaseToolWebVO("markdown编辑", "最优好的语言MarkDown，我们提供编辑功能，还提供导出图片，pdf!", "/web/markdown/edit", "fa-file"));

        markdownToolBox.setTools(list);

        this.markdownToolBox.add(markdownToolBox);
    }


    private void initHtmlToolBox() {
        ToolBoxVO htmlToolBox = new ToolBoxVO();

        htmlToolBox.setToolName("Html工具箱/Html Tool+");

        List<IWebVO> list = new ArrayList<>();

        list.add(new BaseToolWebVO("html转图", "有访问不了的html？没关系，我们来帮你生成图片，拿走不谢", "/web/html/toimg", "fa-html5"));

        htmlToolBox.setTools(list);

        this.htmlToolBox.add(htmlToolBox);
    }
}
