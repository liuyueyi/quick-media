package com.github.hui.quick.plugin.md;

import com.github.hui.quick.plugin.base.Base64Util;
import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import com.github.hui.quick.plugin.md.entity.HtmlRenderOptions;
import com.github.hui.quick.plugin.md.entity.MarkdownEntity;
import com.github.hui.quick.plugin.md.helper.HtmlRenderHelper;
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

    /**
     * html片段，可以额外指定css样式的渲染方式
     *
     * @param html
     * @return
     */
    public static Builder of(String html) {
        return new Builder().setHtml(html);
    }

    /**
     * 对markdown文档进行渲染
     *
     * @param entity
     * @return
     */
    public static Builder ofMd(MarkdownEntity entity) {
        return new Builder().setHtml(entity);
    }

    /**
     * 对一个完整的html进行渲染
     *
     * @param html
     * @return
     * @throws Exception
     */
    public static Builder ofDoc(String html) throws Exception {
        return new Builder().setDocument(html);
    }


    public BufferedImage asImage() {
        BufferedImage bf = HtmlRenderHelper.parseImage(options);
        return bf;
    }


    public boolean asFile(String absFileName) throws IOException {
        File file = new File(absFileName);
        FileWriteUtil.mkDir(file.getParentFile());

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

        /**
         * 样式内容
         */
        private String css;

        /**
         * css font-family: "Pingfang SC",STHeiti,"Lantinghei SC","Open Sans",Arial,"Hiragino Sans GB","Microsoft YaHei","WenQuanYi Micro Hei",SimSun,sans-serif;
         */
        private String fontFamily;

        private Font font = new Font("宋体", Font.PLAIN, 18);


        private Integer fontColor;

        /**
         * 如果想直接渲染整个html，则直接设置这个即可
         */
        private Document document;


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
            this.html = new MarkdownEntity(html);
            return this;
        }

        public Builder setHtml(MarkdownEntity html) {
            this.html = html;
            return this;
        }

        public Builder setCss(String css) {
            this.css = css;
            return this;
        }

        public Builder setFont(Font font) {
            this.font = font;
            return this;
        }

        public Builder setFontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
            return this;
        }

        public Builder setFontColor(int fontColor) {
            this.fontColor = fontColor;
            return this;
        }

        public Builder setDocument(String html) throws Exception {
            this.document = parseDocument(html);
            return this;
        }

        public Integer getW() {
            return w;
        }

        public Integer getH() {
            return h;
        }

        public boolean isAutoW() {
            return autoW;
        }

        public boolean isAutoH() {
            return autoH;
        }

        public String getOutType() {
            return outType;
        }

        public MarkdownEntity getHtml() {
            return html;
        }

        public String getCss() {
            return css;
        }

        public String getFontFamily() {
            return fontFamily;
        }

        public Font getFont() {
            return font;
        }

        public Integer getFontColor() {
            return fontColor;
        }

        public Document getDocument() {
            return document;
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

            if (document != null) {
                options.setDocument(document);
            } else if (css != null) {
                html.setCss(css);
                options.setDocument(parseDocument(html.toString()));
            } else {
                // 没有指定css时，默认配置
                if (fontColor != null) {
                    html.addDivStyle("style", "color:" + options.getFontColor() + ";");
                }
                if (fontFamily != null) {
                    html.addDivStyle("style", "font-family:" + fontFamily + ";");
                }
                html.addDivStyle("style", "width:" + w + ";");
                html.addWidthCss("img");
                html.addWidthCss("code");
                options.setDocument(parseDocument(html.toString()));
            }

            return new Html2ImageWrapper(options);
        }
    }
}
