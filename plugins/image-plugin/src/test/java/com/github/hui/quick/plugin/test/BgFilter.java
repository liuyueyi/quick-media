package com.github.hui.quick.plugin.test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

/**
 * @link https://www.bytebang.at/Blog/Removing+a+uniform+background+from+an+image+with+Java
 */
public class BgFilter {
    private File inputFile = null;
    BufferedImage img = null;

    public BgFilter(String inputFileName) throws IOException {

        this.inputFile = new File(inputFileName);
        if (!this.inputFile.exists()) {
            throw new IOException("Qaf File does not exist");
        }
        // Read the image
        this.img = ImageIO.read(this.inputFile);
    }
    public BufferedImage getColorTransparentImage() {
        Color leftTopColor = new Color(this.img.getRGB(0, 0));
        ImageFilter filter = new SimilarColorFilter(leftTopColor, 0.5, 0.2, 0.06);

        // Apply the filter
        final ImageProducer ip = new FilteredImageSource(this.img.getSource(), filter);
        Image transparentImage = Toolkit.getDefaultToolkit().createImage(ip);

        // Convert to Buffered Image
        final BufferedImage bufferedImage = new BufferedImage(transparentImage.getWidth(null),
                transparentImage.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(transparentImage, 0, 0, null);
        g2d.dispose();
        return bufferedImage;
    }

    /**
     * Makes the background of the qaf transparent and saves it under the given name
     *
     * @param outputFileName
     * @return
     * @throws IOException
     */
    public void saveAsTransparentPng(String outputFileName) throws IOException {
        if (this.img == null) {
            throw new IllegalStateException("File can not be converted.");
        }

        BufferedImage bi = getColorTransparentImage();
        File out = new File(outputFileName);
        ImageIO.write(bi, "PNG", out);
    }

    /**
     * Filter for making üixels with the same color transparent
     *
     * @author gue
     */
    public static class SimilarColorFilter extends RGBImageFilter {
        Double delta_hue;
        Double delta_saturation;
        Double delta_brightness;

        float thue;
        float tsaturation;
        float tbrightness;

        public SimilarColorFilter(Color color, Double delta_hue, Double delta_saturation, Double delta_brightness) {
            this.delta_hue = delta_hue;
            this.delta_saturation = delta_saturation;
            this.delta_brightness = delta_brightness;

            // Convert to HSV
            int r, g, b;
            r = color.getRed();
            g = color.getGreen();
            b = color.getBlue();

            float[] hsv = new float[3];
            Color.RGBtoHSB(r, g, b, hsv);

            // Remember the current parameters
            this.thue = hsv[0];
            this.tsaturation = hsv[1];
            this.tbrightness = hsv[2];
        }

        @Override
        public int filterRGB(int x, int y, int rgb) {
            Color cpix = new Color(rgb);
            float[] hsv = new float[3];
            Color.RGBtoHSB(cpix.getRed(), cpix.getGreen(), cpix.getBlue(), hsv);

            float phue = hsv[0];
            float psaturation = hsv[1];
            float pbrightness = hsv[2];

            if (isInTolerance(phue, this.thue, this.delta_hue) &&
                    isInTolerance(psaturation, this.tsaturation, this.delta_saturation) &&
                    isInTolerance(pbrightness, this.tbrightness, this.delta_brightness)) {
                // Mark the alpha bits as zero - transparent
                return 0x00FFFFFF & rgb;
            } else {
                // nothing to do
                return rgb;
            }
        }

        public boolean isInTolerance(double actual_value, double target_value, Double max_delta) {
            if (max_delta == null) {
                return true;
            }

            return Math.abs(actual_value - target_value) <= max_delta;
        }
    }
}
