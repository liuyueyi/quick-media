package com.hust.hui.quickmedia.web.wxapi.templates;

import com.hust.hui.quickmedia.common.util.FileReadUtil;
import com.hust.hui.quickmedia.web.entity.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yihui on 2017/9/18.
 */
@RestController
@Slf4j
public class WxImgCreateTemplateAction {

    private final static String TEMP_FILE = "/tmp/wx/templates.data";

    private List<WxImgCreateTemplateCell> cellList = new CopyOnWriteArrayList<>();

    private volatile long lastModify;

    @PostConstruct
    public void init() {

        try {
            init(TEMP_FILE);
        } catch (IOException e) {
            try {
                init("templates.data");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        // 每分钟清零一把报警计数
        ScheduledExecutorService scheduleExecutorService = Executors.newScheduledThreadPool(1);
        scheduleExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                File file = new File(TEMP_FILE);
                if (lastModify >= file.lastModified()) {
                    return;
                }
                try {
                    init(TEMP_FILE);
                } catch (IOException e) {
                    log.error("reload templates error!");
                    try {
                        init("templates.data");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                lastModify = file.lastModified();
            }
        }, 0, 5, TimeUnit.MINUTES);
    }


    private void init(String fileName) throws IOException {
        List<WxImgCreateTemplateCell> list = new CopyOnWriteArrayList<>();
        BufferedReader reader = FileReadUtil.createLineRead(fileName);
        String line = reader.readLine();
        String[] temps;
        while (line != null) {
            temps = StringUtils.split(line, "|");
            list.add(new WxImgCreateTemplateCell(temps[0], temps[1]));
            line = reader.readLine();
        }
        cellList = list;
    }


    @RequestMapping(value = "wx/list", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseWrapper<WxImgCreateTemplateResponse> templates(HttpServletRequest httpServletRequest) {
        WxImgCreateTemplateResponse response = new WxImgCreateTemplateResponse();
        response.setNum(cellList.size());
        response.setList(cellList);
        return ResponseWrapper.successReturn(response);
    }
}
