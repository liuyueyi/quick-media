package com.hust.hui.quickmedia.common.test;

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
        String[] arys = new String[]{
                "test.amr",
                "/Users/yihui/GitHub/quick-media/common/src/test/resources/test.amr",
                "http://s11.mogucdn.com/mlcdn/c45406/170713_3g25ec8fak8jch5349jd2dcafh61c.amr"
        };

        for (String src : arys) {
            try {
                String output = AudioWrapper.of(src)
                        .setInputType("amr")
                        .setOutputType("mp3")
                        .asFile();
                System.out.println(output);
                log.info("the parse audio abs path: {}", output);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("parse audio error! e: {}", e);
            }
        }
    }
}
