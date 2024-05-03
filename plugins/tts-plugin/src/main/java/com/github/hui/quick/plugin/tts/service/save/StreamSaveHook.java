package com.github.hui.quick.plugin.tts.service.save;

import com.github.hui.quick.plugin.tts.exceptions.TtsException;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 流的方式保存
 *
 * @author YiHui
 * @date 2024/4/30
 */
public class StreamSaveHook {
    private static Logger log = LoggerFactory.getLogger(StreamSaveHook.class);

    public static InputStream save(ByteString data) {
        try {
            byte[] audioBuffer = data.toByteArray();
            return new ByteArrayInputStream(audioBuffer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw TtsException.of("音频文件保存异常，" + e.getMessage());
        }
    }
}
