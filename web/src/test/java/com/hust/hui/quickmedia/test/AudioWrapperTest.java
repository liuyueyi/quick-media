package com.hust.hui.quickmedia.test;

import com.hust.hui.quickmedia.common.AudioWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by yihui on 2017/7/13.
 */
@Slf4j
public class AudioWrapperTest {


    @Test
    public void testAudioParse() {
        String src = "test.amr";

        try {
            String output = AudioWrapper.of(src)
                    .setInputType("amr")
                    .setOutputType("mp3")
                    .asFile();
            log.info("the parse audio abs path: {}", output);
        } catch (Exception e) {
            log.error("parse audio error! e: {}", e);
        }
    }

}
