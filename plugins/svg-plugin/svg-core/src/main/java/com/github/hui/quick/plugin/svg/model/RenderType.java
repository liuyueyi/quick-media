package com.github.hui.quick.plugin.svg.model;

import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.image.TIFFTranscoder;

/**
 * @author YiHui
 * @date 2023/1/30
 */
public enum RenderType {
    PNG() {
        @Override
        public ImageTranscoder getTranscoder() {
            return new PNGTranscoder();
        }
    },
    JPEG() {
        @Override
        public ImageTranscoder getTranscoder() {
            return new JPEGTranscoder();
        }
    },
    JPG() {
        @Override
        public ImageTranscoder getTranscoder() {
            return new JPEGTranscoder();
        }
    },
    TIFF() {
        @Override
        public ImageTranscoder getTranscoder() {
            return new TIFFTranscoder();
        }
    },
    ;

    public abstract ImageTranscoder getTranscoder();
}
