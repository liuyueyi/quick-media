package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.DomUtil;
import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.base.constants.MediaType;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import com.github.hui.quick.plugin.image.wrapper.create.LineGifCreateWrapper;
import com.github.hui.quick.plugin.image.wrapper.create.WordGifCreateWrapper;
import com.github.hui.quick.plugin.image.util.FontUtil;
import org.junit.Test;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by yihui on 2017/9/15.
 */
public class LineCreateWrapperTest {

    private static final String sign = "https://gitee.com/liuyueyi/Source/raw/master/img/info/blogInfoV2.png";

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
                .drawImage(sign);

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
                .drawImage(sign);

        String str = build.asString();

        String dom = "<img src=\"" + DomUtil.toDomSrc(str, MediaType.ImageGif) + "\"/>";
        System.out.println(dom);
    }

}
