package com.github.hui.quick.plugin.svg;

import com.github.hui.quick.plugin.svg.helper.SvgDocumentHelper;
import com.github.hui.quick.plugin.svg.model.RenderType;
import com.github.hui.quick.plugin.svg.util.ImgParseUtil;
import com.github.hui.quick.plugin.svg.util.UriUtil;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YiHui
 * @date 2023/1/30
 */
public class SvgRenderWrapper {
    private static final Logger log = LoggerFactory.getLogger(SvgRenderWrapper.class);

    /**
     * @param svg SVG文件路径（本地或http路径），或者是以 ‘<svg' 开头的svg文本
     * @return
     */
    public static Builder of(String svg) {
        return new Builder().setSvg(svg);
    }

    private static ByteArrayOutputStream saveAsBytes(ImageTranscoder t, Document doc) throws Exception {
        TranscoderInput input = new TranscoderInput(doc);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(outputStream);
        t.transcode(input, output);
        outputStream.flush();
        return outputStream;
    }

    public static class Builder {
        /**
         * svg模板的地址(本地路径或网络url)， 或者是以<svg开头的svg文本
         */
        private String svg;
        /**
         * 渲染的图片类型，jpg 或 png
         */
        private RenderType type;

        /**
         * 用于模板参数替换
         * key = svg中的标签id
         * value = svg标签用于替换的内容
         */
        private Map<String, Object> params;

        /**
         * 是否开启缓存；默认不带参数的svg渲染，不加缓存；带参数的，加上缓存
         */
        private Boolean cacheEnable;

        private Integer cacheSize;

        public String getSvg() {
            return svg;
        }

        public Builder setSvg(String svg) {
            this.svg = svg;
            return this;
        }

        public RenderType getType() {
            return type;
        }

        public Builder setType(RenderType type) {
            this.type = type;
            return this;
        }

        public Map<String, Object> getParams() {
            return params;
        }

        public Builder setParams(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder addParams(String key, Object value) {
            if (this.params == null) {
                this.params = new HashMap<>(8);
            }
            this.params.put(key, value);
            return this;
        }

        public Boolean getCacheEnable() {
            return cacheEnable;
        }

        public Builder setCacheEnable(Boolean cacheEnable) {
            this.cacheEnable = cacheEnable;
            return this;
        }

        public Builder setCacheSize(Integer cacheSize) {
            this.cacheSize = cacheSize;
            return this;
        }

        public Builder build() {
            if (svg == null) throw new IllegalArgumentException("miss render svg template!");
            if (params == null) params = new HashMap<>(0);
            if (type == null) type = RenderType.PNG;
            if (cacheEnable == null) cacheEnable = !params.isEmpty();
            if (cacheSize != null) SvgDocumentHelper.CACHE_MAX_SIZE = cacheSize;
            return this;
        }

        public ByteArrayOutputStream asStream() {
            build();
            try {
                Document doc = SvgDocumentHelper.loadDocument(svg, params, cacheEnable);
                ImageTranscoder t = type.getTranscoder();
                ByteArrayOutputStream output = saveAsBytes(t, doc);
                // 加速内存回收
                doc = null;
                return output;
            } catch (Exception e) {
                log.error("render svg to png error! svg:{}, param:{}", svg, params, e);
                throw new RuntimeException("render svg to img error!", e);
            }
        }

        public byte[] asBytes() {
            try (ByteArrayOutputStream s = asStream()) {
                return s.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public BufferedImage asImg() {
            byte[] bytes = asBytes();
            try {
                return ImgParseUtil.parseBytes2Image(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                bytes = null; // 加速内存回收
            }
        }


        public void asFile(String fileName) {
            autoInitRenderType(fileName);
            try (ByteArrayOutputStream stream = asStream()) {
                FileOutputStream fileSave = new FileOutputStream(UriUtil.initFile(fileName));
                try {
                    fileSave.write(stream.toByteArray());
                    fileSave.flush();
                } finally {
                    fileSave.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 自动根据传入的文件名后缀，来选择渲染的图片类型
         *
         * @param fileName 渲染后输出图片文件名
         * @return
         */
        private String autoInitRenderType(String fileName) {
            int index = fileName.lastIndexOf(".");
            if (index > 0) {
                String prefix = fileName.substring(index + 1);
                if (type == null) {
                    if (prefix.equalsIgnoreCase(RenderType.PNG.name())) {
                        type = RenderType.PNG;
                    } else if (prefix.equalsIgnoreCase(RenderType.JPG.name()) || prefix.equalsIgnoreCase(RenderType.JPEG.name())) {
                        type = RenderType.JPEG;
                    } else if (prefix.equalsIgnoreCase(RenderType.TIFF.name())) {
                        type = RenderType.TIFF;
                    } else {
                        throw new IllegalArgumentException("only support parse svg to jpg | png | tiff, but save fileName:" + fileName);
                    }
                }
            } else {
                if (type == null) type = RenderType.PNG;
                fileName = fileName + "." + type.name().toLowerCase();
            }
            return fileName;
        }
    }
}
