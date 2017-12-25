package com.hust.hui.quickmedia.web.web.helper.conf;

import com.hust.hui.quickmedia.common.util.YamlUtil;
import com.hust.hui.quickmedia.web.web.entity.ToolBoxVO;
import lombok.Getter;

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
public class ToolboxConfLoadHelper {
    private static final String BANNER_INFO_CONF_PATH = "conf/Toolbox.yaml";

    @Getter
    private ToolboxConfContainer container;

    private static ToolboxConfLoadHelper instance = new ToolboxConfLoadHelper();

    private  ToolboxConfLoadHelper() {
        try {
            container = YamlUtil.read(BANNER_INFO_CONF_PATH, ToolboxConfContainer.class);
        } catch (IOException e) {
            e.printStackTrace();
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
