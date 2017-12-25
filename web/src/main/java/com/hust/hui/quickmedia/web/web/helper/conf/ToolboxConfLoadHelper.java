package com.hust.hui.quickmedia.web.web.helper.conf;

import com.alibaba.fastjson.JSON;
import com.hust.hui.quickmedia.common.util.YamlUtil;
import com.hust.hui.quickmedia.web.web.entity.ToolBoxVO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by yihui on 2017/12/13.
 */
@Slf4j
public class ToolboxConfLoadHelper {
    private static final String BANNER_INFO_CONF_PATH = "conf/Toolbox.yaml";

    @Getter
    private ToolboxConfContainer container;

    private static ToolboxConfLoadHelper instance = new ToolboxConfLoadHelper();

    private  ToolboxConfLoadHelper() {
        try {
            container = YamlUtil.read(BANNER_INFO_CONF_PATH, ToolboxConfContainer.class);
        } catch (IOException e) {
            log.error("init ToolboxConfLoadHelper error! e: {}", e);
            container = JSON.parseObject("{\"audio\":{\"toolName\":\"音频工具箱/Audio Tool+\",\"tools\":[{\"desc\":\"我们的目标是：不再有手机播放不了的声音\",\"logo\":\"fa-file-video-o\",\"title\":\"音频转码\",\"url\":\"#\"}]},\"html\":{\"toolName\":\"Html工具箱/Html Tool+\",\"tools\":[{\"desc\":\"有访问不了的html？没关系，我们来帮你生成图片，拿走不谢\",\"logo\":\"fa-html5\",\"title\":\"html转图\",\"url\":\"/webs/views/html/htmlPrintImg\"},{\"desc\":\"在线编辑json串，实时预览\",\"logo\":\"fa-wheelchair\",\"title\":\"Json视窗\",\"url\":\"/webs/views/html/htmlJsonPre\"}]},\"image\":{\"toolName\":\"图片工具箱/Image Tool+\",\"tools\":[{\"desc\":\"支持根据宽高进行压缩，支持根据精度进行压缩\",\"logo\":\"fa-image\",\"title\":\"图片压缩\",\"url\":\"#\"},{\"desc\":\"图片裁剪，根据你的意愿，随意的裁剪\",\"logo\":\"fa-fire-extinguisher\",\"title\":\"图片裁剪\",\"url\":\"#\"},{\"desc\":\"jpg,webp,png...图片格式任意互转\",\"logo\":\"fa-anchor\",\"title\":\"图片转码\",\"url\":\"#\"},{\"desc\":\"转来转去，总有个适合你的旋转角度\",\"logo\":\"fa-shield\",\"title\":\"图片旋转\",\"url\":\"#\"},{\"desc\":\"圆角，圆角，圆角图片也开始支持了\",\"logo\":\"fa-bullseye\",\"title\":\"圆角图片\",\"url\":\"#\"},{\"desc\":\"小边框，背景也是不可缺少的\",\"logo\":\"fa-play-circle\",\"title\":\"添加边框\",\"url\":\"#\"},{\"desc\":\"提供酷炫的模板，来拼装属于你的图片吧\",\"logo\":\"fa-ticket\",\"title\":\"模板合成\",\"url\":\"/webs/views/image/imageMerge\"}]},\"markdown\":{\"toolName\":\"Markdown工具箱/Markdown Tool+\",\"tools\":[{\"desc\":\"最优好的语言MarkDown，我们提供编辑功能，还提供导出图片，pdf!\",\"logo\":\"fa-edit\",\"title\":\"markdown编辑\",\"url\":\"/webs/views/markdown/markdownEdit\"}]},\"qrcode\":{\"toolName\":\"二维码工具箱/Qrcode Tool+\",\"tools\":[{\"desc\":\"生成各种酷炫的二维码\",\"logo\":\"fa-qrcode\",\"title\":\"二维码生成\",\"url\":\"#\"},{\"desc\":\"黑白黑白框，到底是个什么鬼？让我们来解析二维码隐藏的信息\",\"logo\":\"fa-barcode\",\"title\":\"二维码解析\",\"url\":\"#\"}]}}",
                    ToolboxConfContainer.class);
        }
    }

    public static ToolboxConfLoadHelper getInstance() {
        return instance;
    }


    private Map<String, List<ToolBoxVO>> cacheMap = new ConcurrentHashMap<>();

    public List<ToolBoxVO> getToolBox(String key) {
        List<ToolBoxVO> list = cacheMap.get(key);
        if (list == null) {
            try {
                Field field = container.getClass().getDeclaredField(key);
                field.setAccessible(true);
                list = new ArrayList<>();
                list.add((ToolBoxVO) field.get(container));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }


    private List<ToolBoxVO> list = new CopyOnWriteArrayList<>();
    public List<ToolBoxVO> getAll() {
         if(list.size() == 0) {
             list.add(container.getImage());
             list.add(container.getAudio());
             list.add(container.getQrcode());
             list.add(container.getMarkdown());
             list.add(container.getHtml());
         }

         return list;
    }
}
