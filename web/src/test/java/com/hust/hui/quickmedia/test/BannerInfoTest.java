package com.hust.hui.quickmedia.test;

import com.alibaba.fastjson.JSON;
import com.hust.hui.quickmedia.web.web.entity.HeadBannerVO;
import com.hust.hui.quickmedia.web.web.entity.ToolBoxVO;
import com.hust.hui.quickmedia.web.web.entity.base.BaseToolWebVO;
import com.hust.hui.quickmedia.web.web.entity.base.IWebVO;
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

        List<IWebVO> list = new ArrayList<>();
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

        List<IWebVO> list = new ArrayList<>();

        list.add(new BaseToolWebVO("html转图", "有访问不了的html？没关系，我们来帮你生成图片，拿走不谢", "/webs/html/toimg", "fa-html5"));
        list.add(new BaseToolWebVO("Json视窗", "在线编辑json串，实时预览", "/webs/html/jsonpre", "fa-wheelchair"));

        htmlToolBox.setTools(list);


        Yaml yaml = new Yaml();
        String ans = yaml.dump(htmlToolBox);
        System.out.println(ans);
    }
}
