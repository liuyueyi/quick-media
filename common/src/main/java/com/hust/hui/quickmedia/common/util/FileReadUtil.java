package com.hust.hui.quickmedia.common.util;

import com.google.common.base.Joiner;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yihui on 2017/8/16.
 */
public class FileReadUtil {

    public static String readAll(String fileName) throws IOException {
        BufferedReader reader = createLineRead(fileName);
        List<String> lines = reader.lines().collect(Collectors.toList());
        String content = Joiner.on("\n").join(lines);
        return content;
    }


    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     *
     * @param fileName 文件的名
     */
    public static InputStream createByteRead(String fileName) throws IOException {

//        File file = new File(fileName);
//
//        return new FileInputStream(file);
        return getStreamByFileName(fileName);
    }


    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     *
     * @param fileName 文件名
     */
    public static Reader createCharRead(String fileName) throws IOException {
//        File file = new File(fileName);
//        return new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));

        return new InputStreamReader(getStreamByFileName(fileName), Charset.forName("UTF-8"));
    }


    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     *
     * @param fileName 文件名
     */
    public static BufferedReader createLineRead(String fileName) throws IOException {
//        File file = new File(fileName);
////        return new BufferedReader(new FileReader(file));
//        return new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));

        return new BufferedReader(new InputStreamReader(getStreamByFileName(fileName), Charset.forName("UTF-8")));
    }



    public static InputStream getStreamByFileName(String fileName) throws IOException {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName should not be null!");
        }

        if (fileName.startsWith("http")) { // 网络地址
            return HttpUtil.downFile(fileName);
        } else if (fileName.startsWith("/")) { // 绝对路径
            Path path = Paths.get(fileName);
            return Files.newInputStream(path);
        } else  { // 相对路径
            return FileReadUtil.class.getClassLoader().getResourceAsStream(fileName);
        }
    }



    /** 将字节数组转换成16进制字符串 */
    private static String bytesToHex(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
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
}
