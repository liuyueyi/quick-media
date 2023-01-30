package com.github.hui.quick.plugin.svg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by yihui on 2018/1/14.
 */
public class UriUtil {
    private static final Logger log = LoggerFactory.getLogger(UriUtil.class);

    public static String getAbsUri(String url) throws URISyntaxException {
        URI uri;
        if (url.startsWith("http")) {
            // 网络路径
            uri = URI.create(url);
        } else if (url.startsWith("/")) {
            // 绝对路径
            uri = URI.create(url);
        } else { // 相对路径
            uri = UriUtil.class.getClassLoader().getResource(url).toURI();
        }
        return uri.toString();
    }

    /**
     * 初始化文件，文件的父目录不存在，则创建
     *
     * @param fileUri
     * @return
     */
    public static File initFile(String fileUri) throws FileNotFoundException {
        File file = new File(fileUri);
        mkDir(file.getParentFile());
        return file;
    }

    /**
     * 递归创建文件夹
     *
     * @param path 由目录创建的file对象
     * @throws FileNotFoundException
     */
    public static void mkDir(File path) throws FileNotFoundException {
        if (path.getParentFile() == null) {
            path = path.getAbsoluteFile();
        }

        if (path.getParentFile() == null) {
            // windows 操作系统下，如果直接到最上层的分区，这里依然可能是null，所以直接返回
            return;
        }

        if (path.getParentFile().exists()) {
            modifyFileAuth(path);
            if (!path.exists() && !path.mkdir()) {
                throw new FileNotFoundException();
            }
        } else {
            mkDir(path.getParentFile());
            modifyFileAuth(path);
            if (!path.exists() && !path.mkdir()) {
                throw new FileNotFoundException();
            }
        }
    }

    /**
     * 修改文件权限，设置为可读写
     *
     * @param file
     */
    private static void modifyFileAuth(File file) {
        boolean ans = file.setExecutable(true, false);
        ans = file.setReadable(true, false) && ans;
        ans = file.setWritable(true, false) && ans;
        if (log.isDebugEnabled()) {
            log.debug("create file auth : {}", ans);
        }
    }

}
