package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.audio.AudioWrapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yihui on 2017/7/13.
 */
public class AudioWrapperTest {

    private static final Logger log = LoggerFactory.getLogger(AudioWrapperTest.class);


    @Test
    public void testAudioParse() {
        try {
            String[] arys = new String[]{"test.amr",
                    "http://s11.mogucdn.com/mlcdn/c45406/170713_3g25ec8fak8jch5349jd2dcafh61c.amr"};

            for (String src : arys) {
                try {
                    String output = AudioWrapper.of(src).setInputType("amr").setOutputType("mp3").asFile();
                    System.out.println(output);
                    log.info("the parse audio abs path: {}", output);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("parse audio error! e: {}", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
