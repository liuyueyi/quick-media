package com.hust.hui.quickmedia.common.qrcode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hust.hui.quickmedia.common.util.Base64Util;
import com.hust.hui.quickmedia.common.util.FileUtil;
import com.hust.hui.quickmedia.common.util.QrCodeUtil;
import lombok.ToString;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 2017/7/17.
 */
public class QrCodeGenWrapper {
    public static Builder of(String content) {
        return new Builder().setMsg(content);
    }


    private static BufferedImage asBufferedImage(QrCodeOptions qrCodeConfig) throws WriterException, IOException {
        BitMatrix bitMatrix = QrCodeUtil.encode(qrCodeConfig);
        return QrCodeUtil.toBufferedImage(qrCodeConfig, bitMatrix);
    }

    private static String asString(QrCodeOptions qrCodeOptions) throws WriterException, IOException {
        BufferedImage bufferedImage = asBufferedImage(qrCodeOptions);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, qrCodeOptions.getPicType(), outputStream);
        return Base64Util.encode(outputStream);
    }

    private static boolean asFile(QrCodeOptions qrCodeConfig, String absFileName) throws WriterException, IOException {
        File file = new File(absFileName);
        FileUtil.mkDir(file);

        BufferedImage bufferedImage = asBufferedImage(qrCodeConfig);
        if (!ImageIO.write(bufferedImage, qrCodeConfig.getPicType(), file)) {
            throw new IOException("save qrcode image error!");
        }

        return true;
    }


    @ToString
    public static class Builder {
        private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();

        /**
         * The message to put into QrCode
         */
        private String msg;


        /**
         * qrcode center logo
         */
        private String logo;


        /**
         * logo的样式
         */
        private QrCodeOptions.LogoStyle logoStyle = QrCodeOptions.LogoStyle.NORMAL;


        /**
         * qrcode image width
         */
        private Integer w;


        /**
         * qrcode image height
         */
        private Integer h;


        /**
         * qrcode bgcolor, default white
         */
        private Integer bgColor;


        /**
         * qrcode msg color, default black
         */
        private Integer preColor;


        /**
         * qrcode message's code, default UTF-8
         */
        private String code = "utf-8";


        /**
         * 0 - 4
         */
        private Integer padding;


        /**
         * error level, default H
         */
        private ErrorCorrectionLevel errorCorrection = ErrorCorrectionLevel.H;


        /**
         * output qrcode image type, default png
         */
        private String picType = "png";


        public String getMsg() {
            return msg;
        }

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder setLogo(String logo) {
            this.logo = logo;
            return this;
        }


        public Builder setLogoStyle(QrCodeOptions.LogoStyle logoStyle) {
            this.logoStyle = logoStyle;
            return this;
        }


        public Integer getW() {
            return w == null ? (h == null ? 200 : h) : w;
        }

        public Builder setW(Integer w) {
            if (w != null && w <= 0) {
                throw new IllegalArgumentException("生成二维码的宽必须大于0");
            }
            this.w = w;
            return this;
        }

        public Integer getH() {
            return h == null ? (w == null ? 200 : w) : h;
        }

        public Builder setH(Integer h) {
            if (h != null && h <= 0) {
                throw new IllegalArgumentException("生成功能二维码的搞必须大于0");
            }
            this.h = h;
            return this;
        }

        public Integer getBgColor() {
            return bgColor == null ? MatrixToImageConfig.WHITE : bgColor;
        }

        public Builder setBgColor(Integer bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public Integer getPreColor() {
            return preColor == null ? MatrixToImageConfig.BLACK : preColor;
        }

        public Builder setPreColor(Integer preColor) {
            this.preColor = preColor;
            return this;
        }

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public Integer getPadding() {
            if (padding == null) {
                return 1;
            }

            if (padding < 0) {
                return 0;
            }

            if (padding > 4) {
                return 4;
            }

            return padding;
        }

        public Builder setPadding(Integer padding) {
            this.padding = padding;
            return this;
        }

        public Builder setPicType(String picType) {
            this.picType = picType;
            return this;
        }

        public void setErrorCorrection(ErrorCorrectionLevel errorCorrection) {
            this.errorCorrection = errorCorrection;
        }

        private void validate() {
            if (msg == null || msg.length() == 0) {
                throw new IllegalArgumentException("生成二维码的内容不能为空!");
            }
        }


        private QrCodeOptions build() {
            this.validate();

            QrCodeOptions qrCodeConfig = new QrCodeOptions();
            qrCodeConfig.setMsg(getMsg());
            qrCodeConfig.setH(getH());
            qrCodeConfig.setW(getW());
            qrCodeConfig.setLogo(logo);
            qrCodeConfig.setLogoStyle(logoStyle);
            qrCodeConfig.setPicType(picType);

            Map<EncodeHintType, Object> hints = new HashMap<>(3);
            hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrection);
            hints.put(EncodeHintType.CHARACTER_SET, code);
            hints.put(EncodeHintType.MARGIN, this.getPadding());
            qrCodeConfig.setHints(hints);


            MatrixToImageConfig config;
            if (getPreColor() == MatrixToImageConfig.BLACK
                    && getBgColor() == MatrixToImageConfig.WHITE) {
                config = DEFAULT_CONFIG;
            } else {
                config = new MatrixToImageConfig(getPreColor(), getBgColor());
            }
            qrCodeConfig.setMatrixToImageConfig(config);


            return qrCodeConfig;
        }


        public String asString() throws IOException, WriterException {
            return QrCodeGenWrapper.asString(build());
        }


        public BufferedImage asBufferedImage() throws IOException, WriterException {
            return QrCodeGenWrapper.asBufferedImage(build());
        }


        public boolean asFile(String absFileName) throws IOException, WriterException {
            return QrCodeGenWrapper.asFile(build(), absFileName);
        }
    }
}
