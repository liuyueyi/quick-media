package com.hust.hui.quickmedia.common.util;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.URI;

/**
 * Created by yihui on 2017/7/13.
 */
@Slf4j
public class FileUtil {

    public static String TEMP_PATH = "/tmp/audio";

    public static <T> FileInfo saveFile(T src, String inputType) throws Exception {
        if (src instanceof String) { // 给的文件路径，区分三中，本地绝对路径，相对路径，网络地址
            return saveFileByPath((String) src);
        } else if (src instanceof URI) { // 网络资源文件时，需要下载到本地临时目录下
            return saveFileByURI((URI) src);
        } else if (src instanceof InputStream) { // 输入流保存在到临时目录
            return saveFileByStream((InputStream) src, inputType);
        } else {
            throw new IllegalStateException("save file parameter only support String/URI/InputStream type! but input type is: " + (src == null ? null : src.getClass()));
        }
    }


    /**
     * 根据path路径 生成源文件信息
     *
     * @param path
     * @return
     * @throws Exception
     */
    private static FileInfo saveFileByPath(String path) throws Exception {
        if (path.startsWith("http")) {
            return saveFileByURI(URI.create(path));
        }


        String tmpAbsFile;
        if (path.startsWith("/")) { // 绝对路径
            tmpAbsFile = path;
        } else { // 相对路径
            tmpAbsFile = FileUtil.class.getClassLoader().getResource(path).getFile();
        }


        return parseAbsFileToFileInfo(tmpAbsFile);
    }


    /**
     * 下载远程文件， 保存到临时目录, 病生成文件信息
     *
     * @param uri
     * @return
     * @throws Exception
     */
    private static FileInfo saveFileByURI(URI uri) throws Exception {
        String path = uri.getPath();
        if (path.endsWith("/")) {
            throw new IllegalArgumentException("a select uri should be choosed! but input path is: " + path);
        }

        int index = path.lastIndexOf("/");
        String filename = path.substring(index + 1);

        FileInfo fileInfo = new FileInfo();
        extraFileName(filename, fileInfo);
        fileInfo.setPath(TEMP_PATH);

        try {
            InputStream inputStream = HttpUtil.downFile(uri);
            return saveFileByStream(inputStream, fileInfo);

        } catch (Exception e) {
            log.error("down file from url: {} error! e: {}", uri, e);
            throw e;
        }
    }


    public static FileInfo saveFileByStream(InputStream inputStream, String fileType) throws Exception {
        // 临时文件生成规则  当前时间戳 + 随机数 + 后缀
        return saveFileByStream(inputStream, TEMP_PATH, genTempFileName(), fileType);
    }


    /**
     * 将字节流保存到文件中
     *
     * @param stream
     * @param filename
     * @return
     */
    public static FileInfo saveFileByStream(InputStream stream, String path, String filename, String fileType) throws FileNotFoundException {
        return saveFileByStream(stream, new FileInfo(path, filename, fileType));
    }


    public static FileInfo saveFileByStream(InputStream stream, FileInfo fileInfo) throws FileNotFoundException {
        if (!StringUtils.isBlank(fileInfo.getPath())) {
            mkDir(new File(fileInfo.getPath()));
        }

        String tempAbsFile = fileInfo.getPath() + "/" + fileInfo.getFilename() + "." + fileInfo.getFileType();
        BufferedOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(stream);
            outputStream = new BufferedOutputStream(new FileOutputStream(tempAbsFile));
            int len = inputStream.available();
            //判断长度是否大于4k
            if (len <= 4096) {
                byte[] bytes = new byte[len];
                inputStream.read(bytes);
                outputStream.write(bytes);
            } else {
                int byteCount = 0;
                //1M逐个读取
                byte[] bytes = new byte[4096];
                while ((byteCount = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, byteCount);
                }
            }

            return fileInfo;
        } catch (Exception e) {
            log.error("save stream into file error! filename: {} e: {}", tempAbsFile, e);
            return null;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("close stream error! e: {}", e);
            }
        }
    }


    /**
     * 临时文件名生成： 时间戳 + 0-1000随机数
     *
     * @return
     */
    private static String genTempFileName() {
        return System.currentTimeMillis() + "_" + ((int) (Math.random() * 1000));
    }


    /**
     * 递归创建文件夹
     *
     * @param file 由目录创建的file对象
     * @throws FileNotFoundException
     */
    public static void mkDir(File file) throws FileNotFoundException {
        if (file.getParentFile() == null) {
            file = file.getAbsoluteFile();
        }

        boolean ans;
        if (file.getParentFile().exists()) {
            modifyFileAuth(file);
            if (!file.exists() && !file.mkdir()) {
                throw new FileNotFoundException();
            }
        } else {
            mkDir(file.getParentFile());
            modifyFileAuth(file);
            if (!file.exists() && !file.mkdir()) {
                throw new FileNotFoundException();
            }
        }
    }

    private static void modifyFileAuth(File file) {
        boolean ans = file.setExecutable(true);
        ans = file.setReadable(true) && ans;
        ans = file.setWritable(true) && ans;
        if(log.isDebugEnabled()) {
            log.debug("create file auth : {}", ans);
        }
    }


    /**
     * 根据绝对路径解析出 目录 + 文件名 + 文件后缀
     *
     * @param absFile 全路径文件名
     * @return
     */
    public static FileInfo parseAbsFileToFileInfo(String absFile) {
        FileInfo fileInfo = new FileInfo();
        extraFilePath(absFile, fileInfo);
        extraFileName(fileInfo.getFilename(), fileInfo);
        return fileInfo;
    }


    /**
     * 根据绝对路径解析 目录 + 文件名（带后缀）
     *
     * @param absFilename
     * @param fileInfo
     */
    private static void extraFilePath(String absFilename, FileInfo fileInfo) {
        int index = absFilename.lastIndexOf("/");
        if (index < 0) {
            fileInfo.setPath(TEMP_PATH);
            fileInfo.setFilename(absFilename);
        } else {
            fileInfo.setPath(absFilename.substring(0, index));
            fileInfo.setFilename(index + 1 == absFilename.length() ? "" : absFilename.substring(index + 1));
        }
    }


    /**
     * 根据带后缀文件名解析 文件名 + 后缀
     *
     * @param fileName
     * @param fileInfo
     */
    private static void extraFileName(String fileName, FileInfo fileInfo) {
        int index = fileName.lastIndexOf(".");
        if (index < 0) {
            fileInfo.setFilename(fileName);
            fileInfo.setFileType("");
        } else {
            fileInfo.setFilename(fileName.substring(0, index));
            fileInfo.setFileType(index + 1 == fileName.length() ? "" : fileName.substring(index + 1));
        }
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        /**
         * 文件所在的目录
         */
        private String path;


        /**
         * 文件名 （不包含后缀）
         */
        private String filename;


        /**
         * 文件类型
         */
        private String fileType;


        public String getAbsFile() {
            return path + "/" + filename + "." + fileType;
        }
    }


}
