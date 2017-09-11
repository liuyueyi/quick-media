package com.hust.hui.quickmedia.common.html;

import com.hust.hui.quickmedia.common.markdown.MarkdownEntity;
import com.hust.hui.quickmedia.common.util.Base64Util;
import com.hust.hui.quickmedia.common.util.ColorUtil;
import com.hust.hui.quickmedia.common.util.FileUtil;
import lombok.Getter;
import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by yihui on 2017/9/9.
 */
public class Html2ImageWrapper {

    private static DOMParser domParser;

    static {
        domParser = new DOMParser(new HTMLConfiguration());
        try {
            domParser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
        } catch (Exception e) {
            throw new RuntimeException("Can't create HtmlParserImpl", e);
        }
    }


    private HtmlRenderOptions options;


    private Html2ImageWrapper(HtmlRenderOptions options) {
        this.options = options;
    }


    private static Document parseDocument(String content) throws Exception {
        domParser.parse(new InputSource(new StringReader(content)));
        return domParser.getDocument();
    }


    public static Builder of(String html) {
        return new Builder().setHtml(html);
    }


    public static Builder ofMd(MarkdownEntity entity) {
        return new Builder().setHtml(entity);
    }


    public BufferedImage asImage() {
        BufferedImage bf = HtmlRender.parseImage(options);
        return bf;
    }


    public boolean asFile(String absFileName) throws IOException {
        File file = new File(absFileName);
        FileUtil.mkDir(file);

        BufferedImage bufferedImage = asImage();
        if (!ImageIO.write(bufferedImage, options.getOutType(), file)) {
            throw new IOException("save image error!");
        }

        return true;
    }


    public String asString() throws IOException {
        BufferedImage img = asImage();
        return Base64Util.encode(img, options.getOutType());
    }


    @Getter
    public static class Builder {
        /**
         * 输出图片的宽
         */
        private Integer w = 600;

        /**
         * 输出图片的高度
         */
        private Integer h;

        /**
         * true，根据网页的实际宽渲染；
         * false， 则根据指定的宽进行渲染
         */
        private boolean autoW = true;

        /**
         * true，根据网页的实际高渲染；
         * false， 则根据指定的高进行渲染
         */
        private boolean autoH = false;


        /**
         * 输出图片的格式
         */
        private String outType = "jpg";


        /**
         * 待转换的html内容
         */
        private MarkdownEntity html;


        private Font font = new Font("宋体", Font.PLAIN, 18);


        private Integer fontColor;


        public Builder setW(Integer w) {
            this.w = w;
            return this;
        }

        public Builder setH(Integer h) {
            this.h = h;
            return this;
        }

        public Builder setAutoW(boolean autoW) {
            this.autoW = autoW;
            return this;
        }

        public Builder setAutoH(boolean autoH) {
            this.autoH = autoH;
            return this;
        }

        public Builder setOutType(String outType) {
            this.outType = outType;
            return this;
        }


        public Builder setHtml(String html) {
            this.html = new MarkdownEntity();
            return this;
        }


        public Builder setHtml(MarkdownEntity html) {
            this.html = html;
            return this;
        }

        public Builder setFont(Font font) {
            this.font = font;
            return this;
        }

        public Builder setFontColor(int fontColor) {
            this.fontColor = fontColor;
            return this;
        }


        public Html2ImageWrapper build() throws Exception {
            HtmlRenderOptions options = new HtmlRenderOptions();
            options.setFont(font);
            options.setFontColor(fontColor == null ? null : ColorUtil.int2htmlColor(fontColor));
            options.setW(w);
            options.setH(h);
            options.setAutoW(autoW);
            options.setAutoH(autoH);
            options.setOutType(outType);


            if (fontColor != null) {
                html.addDivStyle("style", "color:" + options.getFontColor());
            }
            html.addDivStyle("style", "width:" + w + ";");
            html.addWidthCss("img");
            html.addWidthCss("code");

            options.setDocument(parseDocument(html.toString()));

            return new Html2ImageWrapper(options);
        }

    }
}
