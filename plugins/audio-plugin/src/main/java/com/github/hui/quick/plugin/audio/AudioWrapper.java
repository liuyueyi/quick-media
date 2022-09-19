package com.github.hui.quick.plugin.audio;

import com.github.hui.quick.plugin.base.ProcessUtil;
import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装工具类
 * <p>
 * Created by yihui on 2017/7/13.
 */
public class AudioWrapper {

    private static final Logger log = LoggerFactory.getLogger(AudioWrapper.class);

    public static Builder<String> of(String str) {
        return new Builder<String>().setSource(str);
    }


    public static Builder<URI> of(URI uri) {
        return new Builder<URI>().setSource(uri);
    }


    public static Builder<InputStream> of(InputStream inputStream) {
        return new Builder<InputStream>().setSource(inputStream);
    }


    private static void checkNotNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalStateException(msg);
        }
    }

    private static boolean run(String cmd) {
        try {
            return ProcessUtil.instance().process(cmd);
        } catch (Exception e) {
            log.error("operate audio error! cmd: {}, e: {}", cmd, e);
            return false;
        }
    }


    public static class Builder<T> {
        /**
         * 输入源
         */
        private T source;


        /**
         * 源音频格式
         */
        private String inputType;


        /**
         * 输出音频格式
         */
        private String outputType;


        /**
         * 命令行参数
         */
        private Map<String, Object> options;


        /**
         * 临时文件信息
         */
        private FileWriteUtil.FileInfo tempFileInfo;


        private String tempOutputFile;

        public Builder() {
            options = new HashMap<>(8);

            // 添加兜底的配置信息

            // 覆盖写
            addOption("y", "")
                    // 解决mac/ios 显示音频时间不对的问题
                    .addOption("write_xing", 0)
                    // 不输出日志
                    .addOption("loglevel", "quiet");
        }

        public Builder<T> setSource(T source) {
            this.source = source;
            return this;
        }

        public Builder<T> setInputType(String inputType) {
            this.inputType = inputType;
            return this;
        }

        public Builder<T> setOutputType(String outputType) {
            this.outputType = outputType;
            return this;
        }

        public Builder<T> addOption(String conf, Object val) {
            this.options.put(conf, val);
            return this;
        }


        private String builder() throws Exception {

            checkNotNull(source, "src file should not be null!");

            checkNotNull(outputType, "output Audio type should not be null!");


            tempFileInfo = FileWriteUtil.saveFile(source, inputType);

            tempOutputFile = tempFileInfo.getPath() + "/" + tempFileInfo.getFilename() + "_out." + outputType;

            return new AudioOptions().setSrc(tempFileInfo.getAbsFile()).setDest(tempOutputFile).build();
        }


        public InputStream asStream() throws Exception {
            String output = asFile();
            return output == null ? null : FileReadUtil.getStreamByFileName(output);
        }

        public String asFile() throws Exception {
            String cmd = builder();
            return run(cmd) ? tempOutputFile : null;
        }
    }

}
