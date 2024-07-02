package com.github.hui.quick.plugin.tts.service.save;

import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import com.github.hui.quick.plugin.tts.exceptions.TtsException;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author YiHui
 * @date 2024/4/30
 */
public class FileSaveHook {
    private static Logger log = LoggerFactory.getLogger(FileSaveHook.class);

    public static String save(ByteString data, String saveFileName, String suffix) {
        try {
            int fileNameIndex = saveFileName.lastIndexOf("/");
            if (fileNameIndex > 0) {
                // 文件夹若不存在，则创建
                String path = saveFileName.substring(0, fileNameIndex);
                FileWriteUtil.mkDir(new File(path));
            }

            String outputFileName = saveFileName + "." + suffix;
            File outputAudioFile = new File(outputFileName);
            if (outputAudioFile.exists()) {
                // 覆盖调旧的文件
                outputAudioFile.delete();
            }
            try (FileOutputStream fstream = new FileOutputStream(outputAudioFile)) {
                byte[] audioBuffer = data.toByteArray();
                fstream.write(audioBuffer);
                fstream.flush();
            }

            return outputAudioFile.getAbsolutePath();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw TtsException.of("音频文件写出异常，" + e.getMessage());
        }
    }

}
