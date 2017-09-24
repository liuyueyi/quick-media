package com.hust.hui.quickmedia.web.wxapi.helper;

import com.hust.hui.quickmedia.common.util.FileUtil;
import com.hust.hui.quickmedia.common.util.ProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

/**
 * Created by yihui on 2017/9/24.
 */
@Slf4j
public class ImgGenHelper {

    public static final String WEB_TMP_PATH = "/mydata/html/story/public/";

    public static final String TMP_IMG_PATH = "ximg/genimg/";


    private static String getTime() {
        return DateFormatUtils.format(new Date(), "yyyyMMdd_HHmm");
    }

    public static String genTmpImg(String type) {
        String time = System.currentTimeMillis() + "_" + ((int) (Math.random() * 100));
        return TMP_IMG_PATH + getTime() + "/" + time + "." + type;
    }


    public static String saveImg(BufferedImage bf) {
        String path = ImgGenHelper.genTmpImg("png");
        try {
            File file = new File(ImgGenHelper.WEB_TMP_PATH + path);
            FileUtil.mkDir(file);
            ImageIO.write(bf, "png", file);

            ProcessUtil.instance().process("chomd -R 755 " + WEB_TMP_PATH + TMP_IMG_PATH);
        } catch (Exception e) {
            log.error("save file error!");
        }
        return path;
    }
}
