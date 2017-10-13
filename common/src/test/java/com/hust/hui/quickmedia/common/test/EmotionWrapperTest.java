package com.hust.hui.quickmedia.common.test;

import com.hust.hui.quickmedia.common.emoticon.EmotionWrapper;
import com.hust.hui.quickmedia.common.img.create.ImgCreateOptions;
import com.hust.hui.quickmedia.common.util.FontUtil;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;

/**
 * Created by yihui on 2017/9/19.
 */
public class EmotionWrapperTest {

    @Test
    public void testEmotion() {
        String msg = "璧月香风，万家帘幕烟如昼。\n" +
                "闹蛾雪柳。人似梅花瘦。\n" +
                "行乐清时，莫惜笙歌奏。\n" +
                "更阑後。满斟金斗。且醉厌厌酒";


        int i = 0;
        boolean[] ans = new boolean[]{true, false};
        for (ImgCreateOptions.DrawStyle drawStyle : ImgCreateOptions.DrawStyle.values()) {
            for (boolean imgFirst : ans) {
                try {
                    EmotionWrapper.ofContent(msg)
                            .setW(300)
                            .setH(300)
                            .setGif("/Users/yihui/Desktop/emoj.gif")
                            .setLeftPadding(10)
                            .setRightPadding(10)
                            .setTopPadding(10)
                            .setBottomPadding(10)
                            .setLinePadding(10)
                            .setFont(FontUtil.DEFAULT_FONT)
                            .setFontColor(Color.BLACK)
                            .setDrawStyle(drawStyle)
                            .setImgFirst(imgFirst)
                            .build()
                            .asFile("/Users/yihui/Desktop/out_" + i + ".gif");
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
