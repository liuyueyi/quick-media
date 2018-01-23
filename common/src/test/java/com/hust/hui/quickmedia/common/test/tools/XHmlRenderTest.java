package com.hust.hui.quickmedia.common.test.tools;

import com.hust.hui.quickmedia.common.util.GraphicUtil;
import com.hust.hui.quickmedia.common.util.HttpUtil;
import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xhtmlrenderer.simple.Graphics2DRenderer;
import org.xml.sax.InputSource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Created by yihui on 2017/12/15.
 */
public class XHmlRenderTest {

    private static DOMParser domParser;

    static {
        domParser = new DOMParser(new HTMLConfiguration());
        try {
            domParser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
        } catch (Exception e) {
            throw new RuntimeException("Can't create HtmlParserImpl", e);
        }
    }

    private Document parseDocument(String content) throws Exception {
        domParser.parse(new InputSource(new StringReader(content)));
        return domParser.getDocument();
    }


    private String readHtmlContent(String url) throws Exception {
        InputStream in = HttpUtil.downFile(url);
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }


    @Test
    public void testRender() {
        try {
            String url = "http://www.baidu.com";
            Document doc = parseDocument(readHtmlContent(url));

            int width = 800;
            int height = 1024;
            Graphics2DRenderer renderer = new Graphics2DRenderer();
            renderer.setDocument(doc, doc.getDocumentURI());


            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = GraphicUtil.getG2d(bufferedImage);


            // do layout with temp buffer
            renderer.layout(graphics2D, new Dimension(width, height));
            graphics2D.dispose();

            Rectangle size = renderer.getMinimumSize();
            final int autoWidth = width;
            final int autoHeight = (int) size.getHeight();
            bufferedImage = new BufferedImage(autoWidth, autoHeight, BufferedImage.TYPE_INT_RGB);
            Dimension dimension = new Dimension(autoWidth, autoHeight);

            graphics2D = GraphicUtil.getG2d(bufferedImage);


            renderer.layout(graphics2D, dimension);
            renderer.render(graphics2D);
            graphics2D.dispose();
            System.out.println("---------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
