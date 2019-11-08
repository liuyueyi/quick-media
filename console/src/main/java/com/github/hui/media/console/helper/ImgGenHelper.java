package com.github.hui.media.console.helper;

import com.github.hui.media.console.util.LocalDateTimeUtil;
import com.github.hui.quick.plugin.base.FileWriteUtil;
import com.github.hui.quick.plugin.base.ProcessUtil;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by yihui on 2017/9/24.
 */
@Slf4j
public class ImgGenHelper {
    /**
     * 绝对存储路径
     */
    public static final String ABS_TMP_PATH = "/mydata/html/story/public/";

    public static final String WEB_IMG_PATH = "ximg/genimg/";

    /**
     * 上传文件的临时存储目录
     */
    public static final String TMP_UPLOAD_PATH = "/tmp/wx/";

    private static Random random = new Random();

    private static String genTmpFileName() {
        return System.currentTimeMillis() + "_" + random.nextInt(100);
    }

    public static String genTmpImg(String type) {
        String time = genTmpFileName();
        return WEB_IMG_PATH + LocalDateTimeUtil.getCurrentDateTime() + "/" + time + "." + type;
    }

    /**
     * 图片本地保存
     *
     * @param bf
     * @return
     */
    public static String saveImg(BufferedImage bf) {
        try {
            String path = ImgGenHelper.genTmpImg("png");
            File file = new File(ImgGenHelper.ABS_TMP_PATH + path);
            FileWriteUtil.mkDir(file.getParentFile());
            ImageIO.write(bf, "png", file);

            ProcessUtil.instance().process("chmod -R 755 " + ABS_TMP_PATH + WEB_IMG_PATH);
            return path;
        } catch (Exception e) {
            log.error("save file error!");
            return null;
        }
    }


    public static String saveTmpUploadFile(InputStream stream, String fileExt) throws FileNotFoundException {
        String fileName = genTmpFileName();
        String tmpPath = LocalDateTimeUtil.getCurrentDateTime();

        FileWriteUtil.saveFileByStream(stream, TMP_UPLOAD_PATH + "/" + tmpPath, fileName, fileExt);
        return tmpPath + "/" + fileName + "." + fileExt;
    }
}
