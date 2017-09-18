package com.hust.hui.quickmedia.common.test;

import com.hust.hui.quickmedia.common.constants.MediaType;
import com.hust.hui.quickmedia.common.image.ImgCreateOptions;
import com.hust.hui.quickmedia.common.image.LineGifCreateWrapper;
import com.hust.hui.quickmedia.common.image.WordGifCreateWrapper;
import com.hust.hui.quickmedia.common.util.DomUtil;
import com.hust.hui.quickmedia.common.util.FileReadUtil;
import com.hust.hui.quickmedia.common.util.FontUtil;
import org.junit.Test;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by yihui on 2017/9/15.
 */
public class LineCreateWrapperTest {

    @Test
    public void genVerticalImg() throws IOException, FontFormatException {
        int h = 500;
        int leftPadding = 10;
        int topPadding = 10;
        int bottomPadding = 10;
        int linePadding = 10;

        LineGifCreateWrapper.Builder build = (LineGifCreateWrapper.Builder) LineGifCreateWrapper.build()
                .setImgH(h)
                .setDrawStyle(ImgCreateOptions.DrawStyle.VERTICAL_RIGHT)
                .setLeftPadding(leftPadding)
                .setTopPadding(topPadding)
                .setBottomPadding(bottomPadding)
                .setLinePadding(linePadding)
                .setFont(FontUtil.DEFAULT_FONT)
                .setFontColor(Color.BLUE)
                .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
                .setBgColor(Color.WHITE)
                .setBorder(true)
                .setBorderColor(0xFFF7EED6);


        BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt");
        String line;
        while ((line = reader.readLine()) != null) {
            build.drawContent(line);
        }

        build.setAlignStyle(ImgCreateOptions.AlignStyle.BOTTOM)
                .drawImage("/Users/yihui/Desktop/12.jpg");

//        build.asGif("/Users/yihui/Desktop/out.gif");
        String str = build.asString();

        String dom = "<img src=\"" + DomUtil.toDomSrc(str, MediaType.ImageGif) + "\"/>";
        System.out.println(dom);
    }



    @Test
    public void testWordGif() throws IOException {
        int h = 300;
        int leftPadding = 10;
        int topPadding = 10;
        int bottomPadding = 10;
        int linePadding = 10;

        WordGifCreateWrapper.Builder build = (WordGifCreateWrapper.Builder) WordGifCreateWrapper.build()
                .setDelay(100)
                .setImgH(h)
                .setImgW(h)
                .setDrawStyle(ImgCreateOptions.DrawStyle.VERTICAL_RIGHT)
                .setLeftPadding(leftPadding)
                .setTopPadding(topPadding)
                .setBottomPadding(bottomPadding)
                .setLinePadding(linePadding)
                .setFont(FontUtil.DEFAULT_FONT)
                .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
                .setBgColor(Color.WHITE)
                .setBorder(true)
                .setBorderColor(0xFFF7EED6)
                ;


        BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt");
        String line;
        while ((line = reader.readLine()) != null) {
            build.drawContent(line);
        }

        build.drawContent(" ");

        build.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
                .drawImage("/Users/yihui/Desktop/long_out.png");

        build.asGif("/Users/yihui/Desktop/out.gif");
//        String str = build.asString();
//
//        String dom = "<img src=\"" + DomUtil.toDomSrc(str, MediaType.ImageGif) + "\"/>";
//        System.out.println(dom);
    }

}
