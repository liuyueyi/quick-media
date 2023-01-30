package com.github.hui.quick.plugin.svg.helper;

import com.github.hui.quick.plugin.svg.util.UriUtil;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGImageElement;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yihui on 2018/1/14.
 */
public class SvgDocumentHelper {

    private static final String SVG_CONTENT_TAG = "<svg";

    private static final String SVG_CONTENT_REPLACE_KEY = "svgContent";
    public static int CACHE_MAX_SIZE = 100;
    private static Map<String, Document> cacheDocMap = new ConcurrentHashMap<>();

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
        // 遍历参数，填充数据
        if (paramMap == null) {
            paramMap = Collections.emptyMap();
        }
        Set<String> keySet = paramMap.keySet();
        Object conf;
        Element temp;
        for (String key : keySet) {
            temp = doc.getElementById(key);
            if (temp == null) {
                continue;
            }

            conf = paramMap.get(key);
            if (conf instanceof String) {
                fillTagValue(temp, (String) conf);
            } else if (conf instanceof Map) {
                // 表示需要修改标签的内容， 修改标签的属性， 这个时候就需要遍历替换
                // 约定 key 为 {@link SVG_CONTENT_REPLACE_KEY} 的表示需要替换内容
                // 其他的表示根据传入的kv替换属性
                for (Object entry : ((Map) conf).entrySet()) {
                    if (SVG_CONTENT_REPLACE_KEY.equals(((Map.Entry) entry).getKey() + "")) {
                        fillTagValue(temp, ((Map.Entry) entry).getValue() + "");
                    } else {
                        temp.setAttribute(((Map.Entry) entry).getKey() + "", ((Map.Entry) entry).getValue() + "");
                    }
                }
            }
        }
    }


    private static void fillTagValue(Element e, String val) {
        if (e instanceof SVGImageElement) {
            ((SVGImageElement) e).getHref().setBaseVal(val);
        } else {
            e.setTextContent(val);
        }
    }

}
