package com.hust.hui.quickmedia.web.wxapi.helper;

import com.hust.hui.quickmedia.common.util.FileUtil;
import com.hust.hui.quickmedia.common.util.ProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by yihui on 2017/9/24.
 */
@Slf4j
public class ImgGenHelper {

    public static final String ABS_TMP_PATH = "/mydata/html/story/public/";

    public static final String WEB_IMG_PATH = "ximg/genimg/";


    // 上传文件的临时存储目录
    public static final String TMP_UPLOAD_PATH = "/tmp/wx/";


    private static String getTime() {
        return DateFormatUtils.format(new Date(), "yyyyMMdd_HHmm");
    }

    public static String genTmpImg(String type) {
        String time = System.currentTimeMillis() + "_" + ((int) (Math.random() * 100));
        return WEB_IMG_PATH + getTime() + "/" + time + "." + type;
    }


    public static String saveImg(BufferedImage bf) {
        String path = ImgGenHelper.genTmpImg("png");
        try {
            File file = new File(ImgGenHelper.ABS_TMP_PATH + path);
            FileUtil.mkDir(file);
            ImageIO.write(bf, "png", file);

            ProcessUtil.instance().process("chmod -R 755 " + ABS_TMP_PATH + WEB_IMG_PATH);
        } catch (Exception e) {
            log.error("save file error!");
        }
        return path;
    }



    public static String saveTmpUploadFile(InputStream stream, String fileExt) throws FileNotFoundException {
        String fileName = System.currentTimeMillis() + "_" + ((int) (Math.random() * 100));
        String tmpPath = getTime();

        FileUtil.saveFileByStream(stream, TMP_UPLOAD_PATH + "/" + tmpPath, fileName, fileExt);
        return  tmpPath + "/" + fileName + "." + fileExt;
    }
}
