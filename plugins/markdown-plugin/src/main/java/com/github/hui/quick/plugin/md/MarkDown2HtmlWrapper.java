package com.github.hui.quick.plugin.md;

import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.md.entity.MarkdownEntity;
import com.google.common.base.Joiner;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 将 markdown 文档 转为 HTML
 * <p>
 * Created by yihui on 2017/9/9.
 */
public class MarkDown2HtmlWrapper {

    public static String MD_CSS = null;

    static {
        MD_CSS = buildCssContent("md/huimarkdown.css");
    }

    public static String buildCssContent(String file) {
        String css;
        try {
            css = FileReadUtil.readAll(file);
            css = "<style type=\"text/css\">\n" + css + "\n</style>\n";
        } catch (Exception e) {
            css = "";
        }
        return css;
    }


    /**
     * 将本地的markdown文件，转为html文档输出
     *
     * @param path 相对地址or绝对地址 ("/" 开头)
     * @return
     * @throws IOException
     */
    public static MarkdownEntity ofFile(String path) throws IOException {
        return ofStream(FileReadUtil.getStreamByFileName(path));
    }


    /**
     * 将网络的markdown文件，转为html文档输出
     *
     * @param url http开头的url格式
     * @return
     * @throws IOException
     */
    public static MarkdownEntity ofUrl(String url) throws IOException {
        return ofStream(FileReadUtil.getStreamByFileName(url));
    }


    /**
     * 将流转为html文档输出
     *
     * @param stream
     * @return
     */
    public static MarkdownEntity ofStream(InputStream stream) throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            List<String> lines = bufferedReader.lines().collect(Collectors.toList());
            String content = Joiner.on("\n").join(lines);
            return ofContent(content);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }


    /**
     * 直接将markdown语义的文本转为html格式输出
     *
     * @param content markdown语义文本
     * @return
     */
    public static MarkdownEntity ofContent(String content) {
        String html = parse(content);
        MarkdownEntity entity = new MarkdownEntity();
        entity.setCss(MD_CSS);
        entity.setHtml(html);
        entity.addDivStyle("class", "markdown-body ");
        return entity;
    }


    /**
     * markdown to html
     *
     * @param content markdown contents
     * @return parse html contents
     */
    public static String parse(String content) {
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);

        // enable table parse!
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));


        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(content);
        return renderer.render(document);
    }

}
