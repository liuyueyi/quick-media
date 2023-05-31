package com.github.hui.quick.plugin.svg.helper;

import com.github.hui.quick.plugin.svg.util.UriUtil;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGAElement;
import org.w3c.dom.svg.SVGImageElement;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yihui on 2018/1/14.
 */
public class SvgDocumentHelper {

    private static final String SVG_CONTENT_TAG = "<svg";

    /**
     * 特殊参数标记，表示需要替换的是标签的content，而不是标签的属性
     */
    public static final String SVG_CONTENT_REPLACE_KEY = "svgContent";

    public static int CACHE_MAX_SIZE = 100;
    private static Map<String, Document> cacheDocMap = new ConcurrentHashMap<>();

    private SvgDocumentHelper() {
    }

    public static Document loadDocument(String path, Map<String, Object> paramMap, boolean cacheEnable) throws URISyntaxException, IOException {
        Document doc = getDocument(path, cacheEnable);
        fillById(doc, paramMap);
        return doc;
    }


    private static Document getDocument(String path, boolean cacheEnable) throws URISyntaxException, IOException {
        Document cache = cacheDocMap.get(path);
        if (cache == null) {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);

            if (path.startsWith(SVG_CONTENT_TAG)) {
                // 表示直接传递的svg内容
                cache = f.createDocument(null, new ByteArrayInputStream(path.getBytes()));
            } else { // 传递的是文件形式
                cache = f.createDocument(UriUtil.getAbsUri(path));
            }

            // 缓存map数量添加限制，防止内存撑爆
            if (cacheEnable) {
                if (cacheDocMap.size() < CACHE_MAX_SIZE) {
                    cacheDocMap.put(path, cache);
                }
            }
        }

        return (Document) cache.cloneNode(true);
    }


    private static void fillById(Document doc, Map<String, Object> paramMap) {
        if (paramMap == null) {
            return;
        }

        // 遍历参数，填充数据
        paramMap.forEach((key, conf) -> {
            final Element temp = doc.getElementById(key);
            if (temp == null) {
                return;
            }

            conf = paramMap.get(key);
            if (conf instanceof String) {
                // value 如果不是map，表示替换默认的标签内容
                fillTagValue(temp, (String) conf);
            } else if (conf instanceof Map) {
                // 表示需要修改标签的内容， 修改标签的属性， 这个时候就需要遍历替换
                // 约定 key 为 {@link SVG_CONTENT_REPLACE_KEY} 的表示需要替换内容
                // 其他的表示根据传入的kv替换属性
                Map<String, Object> confMap = (Map) conf;
                confMap.forEach((k, v) -> {
                    if (Objects.equals(SVG_CONTENT_REPLACE_KEY, k) || "href".equalsIgnoreCase(k) || "xlink:href".equalsIgnoreCase(k)) {
                        fillTagValue(temp, String.valueOf(v));
                    } else {
                        temp.setAttribute(k, String.valueOf(v));
                    }
                });
            }
        });
    }

    /**
     * 替换标签的链接，后者替换标签的内容
     *
     * @param e
     * @param val
     */
    private static void fillTagValue(Element e, String val) {
        // 对于a标签，image标签，默认替换的是 href 值
        if (e instanceof SVGImageElement) {
            ((SVGImageElement) e).getHref().setBaseVal(val);
        } else if (e instanceof SVGAElement) {
            ((SVGAElement) e).getHref().setBaseVal(val);
        } else {
            e.setTextContent(val);
        }
    }

}
