package com.github.hui.quick.plugin.base.file;

import com.github.hui.quick.plugin.base.OSUtil;
import com.github.hui.quick.plugin.base.constants.MediaType;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by @author yihui in 19:53 18/9/10.
 */
public class BasicFileUtil {

    public static boolean isAbsFile(String fileName) {
        if (OSUtil.isWinOS()) {
            // windows 操作系统时，绝对地址形如  c:\descktop
            return fileName.contains(":") || fileName.startsWith("\\");
        } else {
            // mac or linux
            return fileName.startsWith("/");
        }
    }

    /**
     * 将用户目录下地址~/xxx 转换为绝对地址
     *
     * @param path
     * @return
     */
    public static String parseHomeDir2AbsDir(String path) {
        String homeDir = System.getProperties().getProperty("user.home");
        return StringUtils.replace(path, "~", homeDir);
    }

    /**
     * 根据文件的mime获取文件类型
     *
     * @return
     */
    public static MediaType getMediaType(String path) {
        String magicNum = FileReadUtil.getMagicNum(path);
        if (magicNum == null) {
            return null;
        }

        return MediaType.typeOfMagicNum(magicNum);
    }
}
