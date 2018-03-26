package com.github.hui.quick.plugin.svg;


import com.github.hui.quick.plugin.svg.helper.SvgDocumentHelper;
import com.github.hui.quick.plugin.svg.util.ImgParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by yihui on 2017/12/12.
 */
@Slf4j
public class SvgRenderWrapper {

    private static final Logger log = LoggerFactory.getLogger(SvgRenderWrapper.class);
    
    private static ByteArrayOutputStream saveAsBytes(ImageTranscoder t, Document doc) throws Exception {
        TranscoderInput input = new TranscoderInput(doc);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(outputStream);
        t.transcode(input, output);
        outputStream.flush();
        return outputStream;
    }


    private static BufferedImage saveAsImg(ImageTranscoder t, Document doc) throws Exception {
        ByteArrayOutputStream outputStream = saveAsBytes(t, doc);

        // outputstream to image
        byte[] bytes = outputStream.toByteArray();
        BufferedImage bufferedImage = ImgParseUtil.parseBytes2Image(bytes);
        outputStream.close();
        bytes = null; // 加速内存回收
        return bufferedImage;
    }


    /**
     * 将SVG转换成PNG
     *
     * @param path     SVG文件路径
     * @param paramMap 变更参数键值对，key为svg元素Id value为替换内容
     * @throws TranscoderException
     * @throws IOException
     */
    public static BufferedImage convertToPngAsImg(String path, Map<String, Object> paramMap)
            throws Exception {
        try {
            Document doc = SvgDocumentHelper.loadDocument(path, paramMap);
            PNGTranscoder t = new PNGTranscoder();
            BufferedImage bf = saveAsImg(t, doc);
            // 加速内存回收
            doc = null;
            return bf;
        } catch (Exception e) {
            log.error("render svg to png error! path:{}, data:{}, e: {}", path, paramMap, e);
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] convertToPngAsBytes(String path, Map<String, Object> paramMap)
            throws Exception {
        try {
            Document doc = SvgDocumentHelper.loadDocument(path, paramMap);
            PNGTranscoder t = new PNGTranscoder();
            ByteArrayOutputStream output = saveAsBytes(t, doc);
            byte[] ans = output.toByteArray();
            output.close();

            // 加速内存回收
            doc = null;
            return ans;
        } catch (Exception e) {
            log.error("render svg to png error! path:{}, data:{}, e: {}", path, paramMap, e);
            return null;
        }
    }


    /**
     * 将SVG转换成PNG
     *
     * @param path     SVG文件路径
     * @param paramMap 变更参数键值对，key为svg元素Id value为替换内容
     * @throws TranscoderException
     * @throws IOException
     */
    public static BufferedImage convertToJpegAsImg(String path, Map<String, Object> paramMap)
            throws Exception {

        try {
            Document doc = SvgDocumentHelper.loadDocument(path, paramMap);
            // Create a JPEG transcoder
            JPEGTranscoder t = new JPEGTranscoder();
//            t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 1);
            BufferedImage bf = saveAsImg(t, doc);
            // 加速内存回收
            doc = null;
            return bf;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("render svg to jpeg error! path:{}, data:{}, e: {}", path, paramMap, e);
            return null;
        }
    }


    public static byte[] convertToJpegAsBytes(String path, Map<String, Object> paramMap) throws Exception {
        try {
            Document doc = SvgDocumentHelper.loadDocument(path, paramMap);
            // Create a JPEG transcoder
            JPEGTranscoder t = new JPEGTranscoder();
            ByteArrayOutputStream output = saveAsBytes(t, doc);
            byte[] bytes = output.toByteArray();
            output.close();
            // 加速内存回收
            doc = null;
            return bytes;
        } catch (Exception e) {
            log.error("render svg to jpeg error! path:{}, data:{}, e: {}", path, paramMap, e);
            return null;
        }
    }
}
