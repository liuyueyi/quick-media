package com.github.hui.quick.plugin.base.file;

import com.github.hui.quick.plugin.base.http.HttpUtil;
import com.google.common.base.Joiner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yihui on 2018/3/23.
 */
public class FileReadUtil {

    public static String readAll(String fileName) throws IOException {
        try(BufferedReader reader = createLineRead(fileName)) {
            List<String> lines = reader.lines().collect(Collectors.toList());
            return Joiner.on("\n").join(lines);
        }
    }


    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     *
     * @param fileName 文件的名
     */
    public static InputStream createByteRead(String fileName) throws IOException {
        return getStreamByFileName(fileName);
    }


    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     *
     * @param fileName 文件名
     */
    public static Reader createCharRead(String fileName) throws IOException {
        return new InputStreamReader(getStreamByFileName(fileName), StandardCharsets.UTF_8);
    }


    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     *
     * @param fileName 文件名
     */
    public static BufferedReader createLineRead(String fileName) throws IOException {
        return new BufferedReader(new InputStreamReader(getStreamByFileName(fileName), StandardCharsets.UTF_8));
    }


    public static InputStream getStreamByFileName(String fileName) throws IOException {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName should not be null!");
        }

        if (fileName.startsWith("http")) {
            // 网络地址
            return HttpUtil.downFile(fileName);
        } else if (BasicFileUtil.isAbsFile(fileName)) {
            // 绝对路径
            Path path = Paths.get(fileName);
            return Files.newInputStream(path);
        } else if (fileName.startsWith("~")) {
            // 用户目录下的绝对路径文件
            fileName = BasicFileUtil.parseHomeDir2AbsDir(fileName);
            return Files.newInputStream(Paths.get(fileName));
        } else { // 相对路径
            return FileReadUtil.class.getClassLoader().getResourceAsStream(fileName);
        }
    }

    /**
     * 将字节数组转换成16进制字符串
     */
    private static String bytesToHex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }


        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * 获取文件对应的魔数
     *
     * @param file
     * @return
     */
    public static String getMagicNum(String file) {
        try (InputStream stream = FileReadUtil.getStreamByFileName(file)) {

            byte[] b = new byte[28];
            stream.read(b, 0, 28);

            return bytesToHex(b);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取流文件对应的魔数
     * @param inputStream
     * @return
     */
    public static String getMagicNum(ByteArrayInputStream inputStream) {
        byte[] bytes = new byte[28];
        inputStream.read(bytes, 0, 28);
        inputStream.reset();
        return bytesToHex(bytes);
    }
}
