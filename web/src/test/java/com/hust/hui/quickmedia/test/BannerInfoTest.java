package com.hust.hui.quickmedia.test;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.hust.hui.quickmedia.web.web.entity.HeadBannerVO;
import com.hust.hui.quickmedia.web.web.entity.ToolBoxVO;
import com.hust.hui.quickmedia.web.web.entity.base.BaseToolWebVO;
import com.hust.hui.quickmedia.web.web.entity.base.MenuWebVO;
import com.hust.hui.quickmedia.web.web.helper.conf.BannerConfContainer;
import com.hust.hui.quickmedia.web.web.helper.conf.BannerConfLoadHelper;
import com.hust.hui.quickmedia.web.web.helper.conf.ToolboxConfContainer;
import com.hust.hui.quickmedia.web.web.helper.conf.ToolboxConfLoadHelper;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihui on 2017/12/13.
 */
public class BannerInfoTest {

    @Test
    public void loadBanner() throws IOException {
        BannerConfContainer container = BannerConfLoadHelper.getInstance().getContainer();
        String json = JSON.toJSONString(container);
        System.out.println(json);

        ToolboxConfContainer tool = ToolboxConfLoadHelper.getInstance().getContainer();
        String toolJson = JSON.toJSONString(tool);
        System.out.println(toolJson);

    }


    @Test
    public void saveBannerYmal() {
        HeadBannerVO vo = new HeadBannerVO();
        vo.setTitle("Index");
        vo.setSubTitle("图文工具箱 | Image toolbox | 提供图片的各式操作");

        List<MenuWebVO> list = new ArrayList<>();
        list.add(new MenuWebVO("/webs/index", "首页"));
        list.add(new MenuWebVO("/webs/image", "Image"));
        vo.setMenuList(list);

        Yaml yaml = new Yaml();
        String ans = yaml.dump(vo);
        System.out.println(ans);
    }


    @Test
    public void saveToolYmal() {
        ToolBoxVO htmlToolBox = new ToolBoxVO();

        htmlToolBox.setToolName("Html工具箱/Html Tool+");

        List<BaseToolWebVO> list = new ArrayList<>();

        list.add(new BaseToolWebVO("html转图", "有访问不了的html？没关系，我们来帮你生成图片，拿走不谢", "/webs/html/toimg", "fa-html5"));
        list.add(new BaseToolWebVO("Json视窗", "在线编辑json串，实时预览", "/webs/html/jsonpre", "fa-wheelchair"));

        htmlToolBox.setTools(list);


        Yaml yaml = new Yaml();
        String ans = yaml.dump(htmlToolBox);
        System.out.println(ans);
    }


    @Test
    public void testParse() {
        String str = "{\"audio\":{\"toolName\":\"音频工具箱/Audio Tool+\",\"tools\":[{\"desc\":\"我们的目标是：不再有手机播放不了的声音\",\"logo\":\"fa-file-video-o\",\"title\":\"音频转码\",\"url\":\"#\"}]},\"html\":{\"toolName\":\"Html工具箱/Html Tool+\",\"tools\":[{\"desc\":\"有访问不了的html？没关系，我们来帮你生成图片，拿走不谢\",\"logo\":\"fa-html5\",\"title\":\"html转图\",\"url\":\"/webs/views/html/htmlPrintImg\"},{\"desc\":\"在线编辑json串，实时预览\",\"logo\":\"fa-wheelchair\",\"title\":\"Json视窗\",\"url\":\"/webs/views/html/htmlJsonPre\"}]},\"image\":{\"toolName\":\"图片工具箱/Image Tool+\",\"tools\":[{\"desc\":\"支持根据宽高进行压缩，支持根据精度进行压缩\",\"logo\":\"fa-image\",\"title\":\"图片压缩\",\"url\":\"#\"},{\"desc\":\"图片裁剪，根据你的意愿，随意的裁剪\",\"logo\":\"fa-fire-extinguisher\",\"title\":\"图片裁剪\",\"url\":\"#\"},{\"desc\":\"jpg,webp,png...图片格式任意互转\",\"logo\":\"fa-anchor\",\"title\":\"图片转码\",\"url\":\"#\"},{\"desc\":\"转来转去，总有个适合你的旋转角度\",\"logo\":\"fa-shield\",\"title\":\"图片旋转\",\"url\":\"#\"},{\"desc\":\"圆角，圆角，圆角图片也开始支持了\",\"logo\":\"fa-bullseye\",\"title\":\"圆角图片\",\"url\":\"#\"},{\"desc\":\"小边框，背景也是不可缺少的\",\"logo\":\"fa-play-circle\",\"title\":\"添加边框\",\"url\":\"#\"},{\"desc\":\"提供酷炫的模板，来拼装属于你的图片吧\",\"logo\":\"fa-ticket\",\"title\":\"模板合成\",\"url\":\"/webs/views/image/imageMerge\"}]},\"markdown\":{\"toolName\":\"Markdown工具箱/Markdown Tool+\",\"tools\":[{\"desc\":\"最优好的语言MarkDown，我们提供编辑功能，还提供导出图片，pdf!\",\"logo\":\"fa-edit\",\"title\":\"markdown编辑\",\"url\":\"/webs/views/markdown/markdownEdit\"}]},\"qrcode\":{\"toolName\":\"二维码工具箱/Qrcode Tool+\",\"tools\":[{\"desc\":\"生成各种酷炫的二维码\",\"logo\":\"fa-qrcode\",\"title\":\"二维码生成\",\"url\":\"#\"},{\"desc\":\"黑白黑白框，到底是个什么鬼？让我们来解析二维码隐藏的信息\",\"logo\":\"fa-barcode\",\"title\":\"二维码解析\",\"url\":\"#\"}]}}";
        Gson gson = new Gson();
        ToolboxConfContainer confContainer = gson.fromJson(str, ToolboxConfContainer.class);
        System.out.println(confContainer);
    }
}
