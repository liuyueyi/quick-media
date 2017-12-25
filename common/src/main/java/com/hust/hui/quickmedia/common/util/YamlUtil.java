package com.hust.hui.quickmedia.common.util;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yihui on 2017/5/8.
 */
public class YamlUtil {


    /**
     * yaml文件读取
     *
     * @param fileName 文件名
     * @param clz      格式化的对象实例
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T read(String fileName, Class<T> clz) throws IOException {
        InputStream inputStream = FileReadUtil.getStreamByFileName(fileName);
        Yaml yaml = new Yaml();
        T t = yaml.loadAs(inputStream, clz);
        inputStream.close();
        return t;
    }

    public static Object read(String fileName) throws IOException {
        try (InputStream inputStream = FileReadUtil.getStreamByFileName(fileName)) {
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        }
    }

}
