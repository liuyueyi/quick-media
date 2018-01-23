/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.apache.batik.ext.awt.image.codec.png;

import org.apache.batik.ext.awt.image.codec.util.ImageDecodeParam;
import org.apache.batik.ext.awt.image.codec.util.PropertyUtil;

/**
 * An instance of <code>ImageDecodeParam</code> for decoding images in
 * the PNG format.
 *
 * <code>PNGDecodeParam</code> allows several aspects of the decoding
 * process for PNG images to be controlled.  By default, decoding produces
 * output images with the following properties:
 *
 * <p> Images with a bit depth of 8 or less use a
 * <code>DataBufferByte</code> to hold the pixel data.  16-bit images
 * use a <code>DataBufferUShort</code>.
 *
 * <p> Palette color images and non-transparent grayscale images with
 * bit depths of 1, 2, or 4 will have a
 * <code>MultiPixelPackedSampleModel</code> and an
 * <code>IndexColorModel</code>.  For palette color images, the
 * <code>ColorModel</code> palette contains the red, green, blue, and
 * optionally alpha palette information.  For grayscale images, the
 * palette is used to expand the pixel data to cover the range 0-255.
 * The pixels are stored packed 8, 4, or 2 to the byte.
 *
 * <p> All other images are stored using a
 * <code>PixelInterleavedSampleModel</code> with each sample of a pixel
 * occupying its own <code>byte</code> or <code>short</code> within
 * the <code>DataBuffer</code>.  A <code>ComponentColorModel</code> is
 * used which simply extracts the red, green, blue, gray, and/or alpha
 * information from separate <code>DataBuffer</code> entries.
 *
 * <p> Five aspects of this process may be altered by means of methods
 * in this class.
 *
 * <p> <code>setSuppressAlpha()</code> prevents an alpha channel
 * from appearing in the output.
 *
 * <p> <code>setExpandPalette()</code> turns palette-color images into
 * 3-or 4-channel full-color images.
 *
 * <p> <code>setOutput8BitGray()</code> causes 1, 2, or 4 bit
 * grayscale images to be output in 8-bit form, using a
 * <code>ComponentSampleModel</code> and
 * <code>ComponentColorModel</code>.
 *
 * <p> <code>setDecodingExponent()</code> causes the output image to be
 * gamma-corrected using a supplied output gamma value.
 *
 * <p> <code>setExpandGrayAlpha()</code> causes 2-channel gray/alpha
 * (GA) images to be output as full-color (GGGA) images, which may
 * simplify further processing and display.
 *
 * <p><b> This class is not a committed part of the JAI API.  It may
 * be removed or changed in future releases of JAI.</b>
 *
 * @version $Id$
 */
public class PNGDecodeParam implements ImageDecodeParam {

    /**
     * Constructs a default instance of <code>PNGDecodeParam</code>.
     */
    public PNGDecodeParam() {}

    private boolean suppressAlpha = false;

    /**
     * Returns <code>true</code> if alpha (transparency) will
     * be prevented from appearing in the output.
     */
    public boolean getSuppressAlpha() {
        return suppressAlpha;
    }

    /**
     * If set, no alpha (transparency) channel will appear in the
     * output image.
     *
     * <p> The default is to allow transparency to appear in the
     * output image.
     */
    public void setSuppressAlpha(boolean suppressAlpha) {
        this.suppressAlpha = suppressAlpha;
    }

    private boolean expandPalette = false;

    /**
     * Returns true if palette-color images will be expanded to
     * produce full-color output.
     */
    public boolean getExpandPalette() {
        return expandPalette;
    }

    /**
     * If set, palette color images (PNG color type 3) will
     * be decoded into full-color (RGB) output images.  The output
     * image may have 3 or 4 channels, depending on the presence of
     * transparency information.
     *
     * <p> The default is to output palette images using a single
     * channel.  The palette information is used to construct the
     * output image's <code>ColorModel</code>.
     */
    public void setExpandPalette(boolean expandPalette) {
        this.expandPalette = expandPalette;
    }

    private boolean output8BitGray = false;

    /**
     * Returns the current value of the 8-bit gray output parameter.
     */
    public boolean getOutput8BitGray() {
        return output8BitGray;
    }

    /**
     * If set, grayscale images with a bit depth less than 8
     * (1, 2, or 4) will be output in 8 bit form.  The output values
     * will occupy the full 8-bit range.  For example, gray values
     * 0, 1, 2, and 3 of a 2-bit image will be output as
     * 0, 85, 170, and 255.
     *
     * <p> The decoding of non-grayscale images and grayscale images
     * with a bit depth of 8 or 16 are unaffected by this setting.
     *
     * <p> The default is not to perform expansion.  Grayscale images
     * with a depth of 1, 2, or 4 bits will be represented using
     * a <code>MultiPixelPackedSampleModel</code> and an
     * <code>IndexColorModel</code>.
     */
    public void setOutput8BitGray(boolean output8BitGray) {
        this.output8BitGray = output8BitGray;
    }

    private boolean performGammaCorrection = true;

    /**
     * Returns <code>true</code> if gamma correction is to be performed
     * on the image data.  The default is <code>true</code>.
     *
     * <p> If gamma correction is to be performed, the
     * <code>getUserExponent()</code> and
     * <code>getDisplayExponent()</code> methods are used in addition to
     * the gamma value stored within the file (or the default value of
     * 1/2.2 used if no value is found) to produce a single exponent
     * using the formula:
     * <pre>
     * decoding_exponent = user_exponent/(gamma_from_file * display_exponent)
     * </pre>
     */
    public boolean getPerformGammaCorrection() {
        return performGammaCorrection;
    }

    /**
     * Turns gamma corection of the image data on or off.
     */
    public void setPerformGammaCorrection(boolean performGammaCorrection) {
        this.performGammaCorrection = performGammaCorrection;
    }

    private float userExponent = 1.0F;

    /**
     * Returns the current value of the user exponent parameter.
     * By default, the user exponent is equal to 1.0F.
     */
    public float getUserExponent() {
        return userExponent;
    }

    /**
     * Sets the user exponent to a given value.  The exponent
     * must be positive.  If not, an
     * <code>IllegalArgumentException</code> will be thrown.
     *
     * <p> The output image pixels will be placed through a transformation
     * of the form:
     *
     * <pre>
     * sample = integer_sample / (2^bitdepth - 1.0)
     * decoding_exponent = user_exponent/(gamma_from_file * display_exponent)
     * output = sample ^ decoding_exponent
     * </pre>
     *
     * where <code>gamma_from_file</code> is the gamma of the file
     * data, as determined by the <code>gAMA</code>, </code>sRGB</code>,
     * and/or <code>iCCP</code> chunks, and <code>display_exponent</code>
     * is the exponent of the intrinsic transfer curve of the display,
     * generally 2.2.
     *
     * <p> Input files which do not specify any gamma are assumed to
     * have a gamma of <code>1/2.2</code>; such images may be displayed
     * on a CRT with an exponent of 2.2 using the default user
     * exponent of 1.0.
     *
     * <p> The user exponent may be used in order to change the
     * effective gamma of a file.  If a file has a stored gamma of
     * X, but the decoder believes that the true file gamma is Y,
     * setting a user exponent of Y/X will produce the same result
     * as changing the file gamma.
     *
     * <p> This parameter affects the decoding of all image types.
     *
     * @throws IllegalArgumentException if <code>userExponent</code> is
     * negative.
     */
    public void setUserExponent(float userExponent) {
        if (userExponent <= 0.0F) {
            throw new IllegalArgumentException(PropertyUtil.getString("PNGDecodeParam0"));
        }
        this.userExponent = userExponent;
    }

    private float displayExponent = 2.2F;

    /**
     * Returns the current value of the display exponent parameter.
     * By default, the display exponent is equal to 2.2F.
     */
    public float getDisplayExponent() {
        return displayExponent;
    }

    /**
     * Sets the display exponent to a given value.  The exponent
     * must be positive.  If not, an
     * <code>IllegalArgumentException</code> will be thrown.
     *
     * <p> The output image pixels will be placed through a transformation
     * of the form:
     *
     * <pre>
     * sample = integer_sample / (2^bitdepth - 1.0)
     * decoding_exponent = user_exponent/(gamma_from_file * display_exponent)
     * output = sample ^ decoding_exponent
     * </pre>
     *
     * where <code>gamma_from_file</code> is the gamma of the file
     * data, as determined by the <code>gAMA</code>, </code>sRGB</code>,
     * and/or <code>iCCP</code> chunks, and <code>user_exponent</code>
     * is an additional user-supplied parameter.
     *
     * <p> Input files which do not specify any gamma are assumed to
     * have a gamma of <code>1/2.2</code>; such images should be
     * decoding using the default display exponent of 2.2.
     *
     * <p> If an image is to be processed further before being displayed,
     * it may be preferable to set the display exponent to 1.0 in order
     * to produce a linear output image.
     *
     * <p> This parameter affects the decoding of all image types.
     *
     * @throws IllegalArgumentException if <code>userExponent</code> is
     * negative.
     */
    public void setDisplayExponent(float displayExponent) {
        if (displayExponent <= 0.0F) {
            throw new IllegalArgumentException(PropertyUtil.getString("PNGDecodeParam1"));
        }
        this.displayExponent = displayExponent;
    }

    private boolean expandGrayAlpha = false;

    /**
     * Returns the current setting of the gray/alpha expansion.
     */
    public boolean getExpandGrayAlpha() {
        return expandGrayAlpha;
    }

    /**
     * If set, images containing one channel of gray and one channel of
     * alpha (GA) will be output in a 4-channel format (GGGA).  This
     * produces output that may be simpler to process and display.
     *
     * <p> This setting affects both images of color type 4 (explicit
     * alpha) and images of color type 0 (grayscale) that contain
     * transparency information.
     *
     * <p> By default, no expansion is performed.
     */
    public void setExpandGrayAlpha(boolean expandGrayAlpha) {
        this.expandGrayAlpha = expandGrayAlpha;
    }

    private boolean generateEncodeParam = false;

    private PNGEncodeParam encodeParam = null;

    /**
     * Returns <code>true</code> if an instance of
     * <code>PNGEncodeParam</code> will be available after an image
     * has been decoded via the <code>getEncodeParam</code> method.
     */
    public boolean getGenerateEncodeParam() {
        return generateEncodeParam;
    }

    /**
     * If set, an instance of <code>PNGEncodeParam</code> will be
     * available after an image has been decoded via the
     * <code>getEncodeParam</code> method that encapsulates information
     * about the contents of the PNG file.  If not set, this information
     * will not be recorded and <code>getEncodeParam()</code> will
     * return <code>null</code>.
     */
    public void setGenerateEncodeParam(boolean generateEncodeParam) {
        this.generateEncodeParam = generateEncodeParam;
    }

    /**
     * If <code>getGenerateEncodeParam()</code> is <code>true</code>,
     * this method may be called after decoding has completed, and
     * will return an instance of <code>PNGEncodeParam</code> containing
     * information about the contents of the PNG file just decoded.
     */
    public PNGEncodeParam getEncodeParam() {
        return encodeParam;
    }

    /**
     * Sets the current encoder param instance.  This method is
     * intended to be called by the PNG decoder and will overwrite the
     * current instance returned by <code>getEncodeParam</code>.
     */
    public void setEncodeParam(PNGEncodeParam encodeParam) {
        this.encodeParam = encodeParam;
    }
}
