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

import java.awt.Color;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.batik.ext.awt.image.codec.util.ImageDecoderImpl;
import org.apache.batik.ext.awt.image.codec.util.PropertyUtil;
import org.apache.batik.ext.awt.image.codec.util.SimpleRenderedImage;

/**
 * @version $Id$
 */
public class PNGImageDecoder extends ImageDecoderImpl {

    public PNGImageDecoder(InputStream input,
                           PNGDecodeParam param) {
        super(input, param);
    }

    public RenderedImage decodeAsRenderedImage(int page) throws IOException {
        if (page != 0) {
            throw new IOException(PropertyUtil.getString("PNGImageDecoder19"));
        }
        return new PNGImage(input, (PNGDecodeParam)param);
    }
}

class PNGChunk {
    int length;
    int type;
    byte[] data;
    int crc;

    final String typeString;

    PNGChunk(int length, int type, byte[] data, int crc) {
        this.length = length;
        this.type = type;
        this.data = data;
        this.crc = crc;

        typeString = ""
                     + (char)((type >>> 24) & 0xff)
                     + (char)((type >>> 16) & 0xff)
                     + (char)((type >>>  8) & 0xff)
                     + (char)((type       ) & 0xff);
    }

    public int getLength() {
        return length;
    }

    public int getType() {
        return type;
    }

    public String getTypeString() {
        return typeString;
    }

    public byte[] getData() {
        return data;
    }

    public byte getByte(int offset) {
        return data[offset];
    }

    public int getInt1(int offset) {
        return data[offset] & 0xff;
    }

    public int getInt2(int offset) {
        return ((data[offset] & 0xff) << 8) |
            (data[offset + 1] & 0xff);
    }

    public int getInt4(int offset) {
        return ((data[offset] & 0xff) << 24) |
            ((data[offset + 1] & 0xff) << 16) |
            ((data[offset + 2] & 0xff) << 8) |
            (data[offset + 3] & 0xff);
    }

    public String getString4(int offset) {
        return  ""
            + (char)data[offset]
            + (char)data[offset + 1]
            + (char)data[offset + 2]
            + (char)data[offset + 3];
    }

    public boolean isType(String typeName) {
        return typeString.equals(typeName);
    }
}

/**
 * TO DO:
 *
 * zTXt chunks
 *
 */
class PNGImage extends SimpleRenderedImage {

    public static final int PNG_COLOR_GRAY = 0;
    public static final int PNG_COLOR_RGB = 2;
    public static final int PNG_COLOR_PALETTE = 3;
    public static final int PNG_COLOR_GRAY_ALPHA = 4;
    public static final int PNG_COLOR_RGB_ALPHA = 6;

    private static final String[] colorTypeNames = {
        "Grayscale", "Error", "Truecolor", "Index",
        "Grayscale with alpha", "Error", "Truecolor with alpha"
    };

    public static final int PNG_FILTER_NONE = 0;
    public static final int PNG_FILTER_SUB = 1;
    public static final int PNG_FILTER_UP = 2;
    public static final int PNG_FILTER_AVERAGE = 3;
    public static final int PNG_FILTER_PAETH = 4;

    private int[][] bandOffsets = {
        null,
        { 0 }, // G
        { 0, 1 }, // GA in GA order
        { 0, 1, 2 }, // RGB in RGB order
        { 0, 1, 2, 3 } // RGBA in RGBA order
    };

    private int bitDepth;
    private int colorType;

    private int compressionMethod;
    private int filterMethod;
    private int interlaceMethod;

    private int paletteEntries;
    private byte[] redPalette;
    private byte[] greenPalette;
    private byte[] bluePalette;
    private byte[] alphaPalette;

    private int bkgdRed;
    private int bkgdGreen;
    private int bkgdBlue;

    private int grayTransparentAlpha;
    private int redTransparentAlpha;
    private int greenTransparentAlpha;
    private int blueTransparentAlpha;

    private int maxOpacity;

    private int[] significantBits = null;

    // Parameter information

    // If true, the user wants destination alpha where applicable.
    private boolean suppressAlpha = false;

    // If true, perform palette lookup internally
    private boolean expandPalette = false;

    // If true, output < 8 bit gray images in 8 bit components format
    private boolean output8BitGray = false;

    // Create an alpha channel in the destination color model.
    private boolean outputHasAlphaPalette = false;

    // Perform gamma correction on the image
    private boolean performGammaCorrection = false;

    // Expand GA to GGGA for compatbility with Java2D
    private boolean expandGrayAlpha = false;

    // Produce an instance of PNGEncodeParam
    private boolean generateEncodeParam = false;

    // PNGDecodeParam controlling decode process
    private PNGDecodeParam decodeParam = null;

    // PNGEncodeParam to store file details in
    private PNGEncodeParam encodeParam = null;

    private boolean emitProperties = true;

    private float fileGamma = 45455/100000.0F;

    private float userExponent = 1.0F;

    private float displayExponent = 2.2F;

    private float[] chromaticity = null;

    private int sRGBRenderingIntent = -1;

    // Post-processing step implied by above parameters
    private int postProcess = POST_NONE;

    // Possible post-processing steps

    // Do nothing
    private static final int POST_NONE = 0;

    // Gamma correct only
    private static final int POST_GAMMA = 1;

    // Push gray values through grayLut to expand to 8 bits
    private static final int POST_GRAY_LUT = 2;

    // Push gray values through grayLut to expand to 8 bits, add alpha
    private static final int POST_GRAY_LUT_ADD_TRANS = 3;

    // Push palette value through R,G,B lookup tables
    private static final int POST_PALETTE_TO_RGB = 4;

    // Push palette value through R,G,B,A lookup tables
    private static final int POST_PALETTE_TO_RGBA = 5;

    // Add transparency to a given gray value (w/ optional gamma)
    private static final int POST_ADD_GRAY_TRANS = 6;

    // Add transparency to a given RGB value (w/ optional gamma)
    private static final int POST_ADD_RGB_TRANS = 7;

    // Remove the alpha channel from a gray image (w/ optional gamma)
    private static final int POST_REMOVE_GRAY_TRANS = 8;

    // Remove the alpha channel from an RGB image (w/optional gamma)
    private static final int POST_REMOVE_RGB_TRANS = 9;

    // Mask to add expansion of GA -> GGGA
    private static final int POST_EXP_MASK = 16;

    // Expand gray to G/G/G
    private static final int POST_GRAY_ALPHA_EXP =
        POST_NONE | POST_EXP_MASK;

    // Expand gray to G/G/G through a gamma lut
    private static final int POST_GAMMA_EXP =
        POST_GAMMA | POST_EXP_MASK;

    // Push gray values through grayLut to expand to 8 bits, expand, add alpha
    private static final int POST_GRAY_LUT_ADD_TRANS_EXP =
        POST_GRAY_LUT_ADD_TRANS | POST_EXP_MASK;

    // Add transparency to a given gray value, expand
    private static final int POST_ADD_GRAY_TRANS_EXP =
        POST_ADD_GRAY_TRANS | POST_EXP_MASK;

    private List streamVec = new ArrayList();
    private DataInputStream dataStream;

    private int bytesPerPixel; // number of bytes per input pixel
    private int inputBands;
    private int outputBands;

    // Number of private chunks
    private int chunkIndex = 0;

    private List textKeys = new ArrayList();
    private List textStrings = new ArrayList();

    private List ztextKeys = new ArrayList();
    private List ztextStrings = new ArrayList();

    private WritableRaster theTile;

    private int[] gammaLut = null;

    private void initGammaLut(int bits) {
        double exp = (double)userExponent/(fileGamma*displayExponent);
        int numSamples = 1 << bits;
        int maxOutSample = (bits == 16) ? 65535 : 255;

        gammaLut = new int[numSamples];
        for (int i = 0; i < numSamples; i++) {
            double gbright = (double)i/(numSamples - 1);
            double gamma = Math.pow(gbright, exp);
            int igamma = (int)(gamma*maxOutSample + 0.5);
            if (igamma > maxOutSample) {
                igamma = maxOutSample;
            }
            gammaLut[i] = igamma;
        }
    }

    private final byte[][] expandBits = {
        null,
        { (byte)0x00, (byte)0xff },
        { (byte)0x00, (byte)0x55, (byte)0xaa, (byte)0xff },
        null,
        { (byte)0x00, (byte)0x11, (byte)0x22, (byte)0x33,
          (byte)0x44, (byte)0x55, (byte)0x66, (byte)0x77,
          (byte)0x88, (byte)0x99, (byte)0xaa, (byte)0xbb,
          (byte)0xcc, (byte)0xdd, (byte)0xee, (byte)0xff }
    };

    private int[] grayLut = null;

    private void initGrayLut(int bits) {
        int len = 1 << bits;
        grayLut = new int[len];

        if (performGammaCorrection) {
            System.arraycopy( gammaLut, 0, grayLut, 0, len );
        } else {
            for (int i = 0; i < len; i++) {
                grayLut[i] = expandBits[bits][i];
            }
        }
    }

    public PNGImage(InputStream stream, PNGDecodeParam decodeParam)
        throws IOException {

        if (!stream.markSupported()) {
            stream = new BufferedInputStream(stream);
        }
        DataInputStream distream = new DataInputStream(stream);

        if (decodeParam == null) {
            decodeParam = new PNGDecodeParam();
        }
        this.decodeParam = decodeParam;

        // Get parameter values
        this.suppressAlpha = decodeParam.getSuppressAlpha();
        this.expandPalette = decodeParam.getExpandPalette();
        this.output8BitGray = decodeParam.getOutput8BitGray();
        this.expandGrayAlpha = decodeParam.getExpandGrayAlpha();
        if (decodeParam.getPerformGammaCorrection()) {
            this.userExponent = decodeParam.getUserExponent();
            this.displayExponent = decodeParam.getDisplayExponent();
            performGammaCorrection = true;
            output8BitGray = true;
        }
        this.generateEncodeParam = decodeParam.getGenerateEncodeParam();

        if (emitProperties) {
            properties.put("file_type", "PNG v. 1.0");
        }

        try {
            long magic = distream.readLong();
            if (magic != 0x89504e470d0a1a0aL) {
                String msg = PropertyUtil.getString("PNGImageDecoder0");
                throw new RuntimeException(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = PropertyUtil.getString("PNGImageDecoder1");
            throw new RuntimeException(msg);
        }

        do {
            try {
                PNGChunk chunk;

                String chunkType = getChunkType(distream);
                if (chunkType.equals("IHDR")) {
                    chunk = readChunk(distream);
                    parse_IHDR_chunk(chunk);
                } else if (chunkType.equals("PLTE")) {
                    chunk = readChunk(distream);
                    parse_PLTE_chunk(chunk);
                } else if (chunkType.equals("IDAT")) {
                    chunk = readChunk(distream);
                    streamVec.add(new ByteArrayInputStream(chunk.getData()));
                } else if (chunkType.equals("IEND")) {
                    chunk = readChunk(distream);
                    parse_IEND_chunk(chunk);
                    break; // fall through to the bottom
                } else if (chunkType.equals("bKGD")) {
                    chunk = readChunk(distream);
                    parse_bKGD_chunk(chunk);
                } else if (chunkType.equals("cHRM")) {
                    chunk = readChunk(distream);
                    parse_cHRM_chunk(chunk);
                } else if (chunkType.equals("gAMA")) {
                    chunk = readChunk(distream);
                    parse_gAMA_chunk(chunk);
                } else if (chunkType.equals("hIST")) {
                    chunk = readChunk(distream);
                    parse_hIST_chunk(chunk);
                } else if (chunkType.equals("iCCP")) {
                    chunk = readChunk(distream);
                    parse_iCCP_chunk(chunk);
                } else if (chunkType.equals("pHYs")) {
                    chunk = readChunk(distream);
                    parse_pHYs_chunk(chunk);
                } else if (chunkType.equals("sBIT")) {
                    chunk = readChunk(distream);
                    parse_sBIT_chunk(chunk);
                } else if (chunkType.equals("sRGB")) {
                    chunk = readChunk(distream);
                    parse_sRGB_chunk(chunk);
                } else if (chunkType.equals("tEXt")) {
                    chunk = readChunk(distream);
                    parse_tEXt_chunk(chunk);
                } else if (chunkType.equals("tIME")) {
                    chunk = readChunk(distream);
                    parse_tIME_chunk(chunk);
                } else if (chunkType.equals("tRNS")) {
                    chunk = readChunk(distream);
                    parse_tRNS_chunk(chunk);
                } else if (chunkType.equals("zTXt")) {
                    chunk = readChunk(distream);
                    parse_zTXt_chunk(chunk);
                } else {
                    chunk = readChunk(distream);
                    // Output the chunk data in raw form

                    String type = chunk.getTypeString();
                    byte[] data = chunk.getData();
                    if (encodeParam != null) {
                        encodeParam.addPrivateChunk(type, data);
                    }
                    if (emitProperties) {
                        String key = "chunk_" + chunkIndex++ + ':' + type;
                        properties.put(key.toLowerCase(), data);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                String msg = PropertyUtil.getString("PNGImageDecoder2");
                throw new RuntimeException(msg);
            }
        } while (true);

        // Final post-processing

        if (significantBits == null) {
            significantBits = new int[inputBands];
            for (int i = 0; i < inputBands; i++) {
                significantBits[i] = bitDepth;
            }

            if (emitProperties) {
                properties.put("significant_bits", significantBits);
            }
        }
    }

    private static String getChunkType(DataInputStream distream) {
        try {
            distream.mark(8);
            /* int length = */ distream.readInt();
            int type      =    distream.readInt();
            distream.reset();

            String typeString = "";        // todo simplify this
            typeString += (char)(type >> 24);
            typeString += (char)((type >> 16) & 0xff);
            typeString += (char)((type >> 8) & 0xff);
            typeString += (char)(type & 0xff);
            return typeString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static PNGChunk readChunk(DataInputStream distream) {
        try {
            int length = distream.readInt();
            int type = distream.readInt();
            byte[] data = new byte[length];
            distream.readFully(data);
            int crc = distream.readInt();

            return new PNGChunk(length, type, data, crc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void parse_IHDR_chunk(PNGChunk chunk) {
        tileWidth = width = chunk.getInt4(0);
        tileHeight = height = chunk.getInt4(4);

        bitDepth = chunk.getInt1(8);

        if ((bitDepth != 1) && (bitDepth != 2) && (bitDepth != 4) &&
            (bitDepth != 8) && (bitDepth != 16)) {
            // Error -- bad bit depth
            String msg = PropertyUtil.getString("PNGImageDecoder3");
            throw new RuntimeException(msg);
        }
        maxOpacity = (1 << bitDepth) - 1;

        colorType = chunk.getInt1(9);
        if ((colorType != PNG_COLOR_GRAY) &&
            (colorType != PNG_COLOR_RGB) &&
            (colorType != PNG_COLOR_PALETTE) &&
            (colorType != PNG_COLOR_GRAY_ALPHA) &&
            (colorType != PNG_COLOR_RGB_ALPHA)) {
            System.out.println(PropertyUtil.getString("PNGImageDecoder4"));
        }

        if ((colorType == PNG_COLOR_RGB) && (bitDepth < 8)) {
            // Error -- RGB images must have 8 or 16 bits
            String msg = PropertyUtil.getString("PNGImageDecoder5");
            throw new RuntimeException(msg);
        }

        if ((colorType == PNG_COLOR_PALETTE) && (bitDepth == 16)) {
            // Error -- palette images must have < 16 bits
            String msg = PropertyUtil.getString("PNGImageDecoder6");
            throw new RuntimeException(msg);
        }

        if ((colorType == PNG_COLOR_GRAY_ALPHA) && (bitDepth < 8)) {
            // Error -- gray/alpha images must have >= 8 bits
            String msg = PropertyUtil.getString("PNGImageDecoder7");
            throw new RuntimeException(msg);
        }

        if ((colorType == PNG_COLOR_RGB_ALPHA) && (bitDepth < 8)) {
            // Error -- RGB/alpha images must have >= 8 bits
            String msg = PropertyUtil.getString("PNGImageDecoder8");
            throw new RuntimeException(msg);
        }

        if (emitProperties) {
            properties.put("color_type", colorTypeNames[colorType]);
        }

        if (generateEncodeParam) {
            if (colorType == PNG_COLOR_PALETTE) {
                encodeParam = new PNGEncodeParam.Palette();
            } else if (colorType == PNG_COLOR_GRAY ||
                       colorType == PNG_COLOR_GRAY_ALPHA) {
                encodeParam = new PNGEncodeParam.Gray();
            } else {
                encodeParam = new PNGEncodeParam.RGB();
            }
            decodeParam.setEncodeParam(encodeParam);
        }

        if (encodeParam != null) {
            encodeParam.setBitDepth(bitDepth);
        }
        if (emitProperties) {
            properties.put("bit_depth", new Integer(bitDepth));
        }

        if (performGammaCorrection) {
            // Assume file gamma is 1/2.2 unless we get a gAMA chunk
            float gamma = (1.0F/2.2F)*(displayExponent/userExponent);
            if (encodeParam != null) {
                encodeParam.setGamma(gamma);
            }
            if (emitProperties) {
                properties.put("gamma", new Float(gamma));
            }
        }

        compressionMethod = chunk.getInt1(10);
        if (compressionMethod != 0) {
            // Error -- only know about compression method 0
            String msg = PropertyUtil.getString("PNGImageDecoder9");
            throw new RuntimeException(msg);
        }

        filterMethod = chunk.getInt1(11);
        if (filterMethod != 0) {
            // Error -- only know about filter method 0
            String msg = PropertyUtil.getString("PNGImageDecoder10");
            throw new RuntimeException(msg);
        }

        interlaceMethod = chunk.getInt1(12);
        if (interlaceMethod == 0) {
            if (encodeParam != null) {
                encodeParam.setInterlacing(false);
            }
            if (emitProperties) {
                properties.put("interlace_method", "None");
            }
        } else if (interlaceMethod == 1) {
            if (encodeParam != null) {
                encodeParam.setInterlacing(true);
            }
            if (emitProperties) {
                properties.put("interlace_method", "Adam7");
            }
        } else {
            // Error -- only know about Adam7 interlacing
            String msg = PropertyUtil.getString("PNGImageDecoder11");
            throw new RuntimeException(msg);
        }

        bytesPerPixel = (bitDepth == 16) ? 2 : 1;

        switch (colorType) {
        case PNG_COLOR_GRAY:
            inputBands = 1;
            outputBands = 1;

            if (output8BitGray && (bitDepth < 8)) {
                postProcess = POST_GRAY_LUT;
            } else if (performGammaCorrection) {
                postProcess = POST_GAMMA;
            } else {
                postProcess = POST_NONE;
            }
            break;

        case PNG_COLOR_RGB:
            inputBands = 3;
            bytesPerPixel *= 3;
            outputBands = 3;

            if (performGammaCorrection) {
                postProcess = POST_GAMMA;
            } else {
                postProcess = POST_NONE;
            }
            break;

        case PNG_COLOR_PALETTE:
            inputBands = 1;
            bytesPerPixel = 1;
            outputBands = expandPalette ? 3 : 1;

            if (expandPalette) {
                postProcess = POST_PALETTE_TO_RGB;
            } else {
                postProcess = POST_NONE;
            }
            break;

        case PNG_COLOR_GRAY_ALPHA:
            inputBands = 2;
            bytesPerPixel *= 2;

            if (suppressAlpha) {
                outputBands = 1;
                postProcess = POST_REMOVE_GRAY_TRANS;
            } else {
                if (performGammaCorrection) {
                    postProcess = POST_GAMMA;
                } else {
                    postProcess = POST_NONE;
                }
                if (expandGrayAlpha) {
                    postProcess |= POST_EXP_MASK;
                    outputBands = 4;
                } else {
                    outputBands = 2;
                }
            }
            break;

        case PNG_COLOR_RGB_ALPHA:
            inputBands = 4;
            bytesPerPixel *= 4;
            outputBands = (!suppressAlpha) ? 4 : 3;

            if (suppressAlpha) {
                postProcess = POST_REMOVE_RGB_TRANS;
            } else if (performGammaCorrection) {
                postProcess = POST_GAMMA;
            } else {
                postProcess = POST_NONE;
            }
            break;
        }
    }

    private void parse_IEND_chunk(PNGChunk chunk) throws Exception {
        // Store text strings
        int textLen = textKeys.size();
        String[] textArray = new String[2*textLen];
        for (int i = 0; i < textLen; i++) {
            String key = (String)textKeys.get(i);
            String val = (String)textStrings.get(i);
            textArray[2*i] = key;
            textArray[2*i + 1] = val;
            if (emitProperties) {
                String uniqueKey = "text_" + i + ':' + key;
                properties.put(uniqueKey.toLowerCase(), val);
            }
        }
        if (encodeParam != null) {
            encodeParam.setText(textArray);
        }

        // Store compressed text strings
        int ztextLen = ztextKeys.size();
        String[] ztextArray = new String[2*ztextLen];
        for (int i = 0; i < ztextLen; i++) {
            String key = (String)ztextKeys.get(i);
            String val = (String)ztextStrings.get(i);
            ztextArray[2*i] = key;
            ztextArray[2*i + 1] = val;
            if (emitProperties) {
                String uniqueKey = "ztext_" + i + ':' + key;
                properties.put(uniqueKey.toLowerCase(), val);
            }
        }
        if (encodeParam != null) {
            encodeParam.setCompressedText(ztextArray);
        }

        // Parse prior IDAT chunks
        InputStream seqStream =
            new SequenceInputStream( Collections.enumeration( streamVec ) );
        InputStream infStream =
            new InflaterInputStream(seqStream, new Inflater());
        dataStream = new DataInputStream(infStream);

        // Create an empty WritableRaster
        int depth = bitDepth;
        if ((colorType == PNG_COLOR_GRAY) &&
            (bitDepth < 8) && output8BitGray) {
            depth = 8;
        }
        if ((colorType == PNG_COLOR_PALETTE) && expandPalette) {
            depth = 8;
        }
        int bytesPerRow = (outputBands*width*depth + 7)/8;
        int scanlineStride =
            (depth == 16) ? (bytesPerRow/2) : bytesPerRow;

        theTile = createRaster(width, height, outputBands,
                               scanlineStride,
                               depth);

        if (performGammaCorrection && (gammaLut == null)) {
            initGammaLut(bitDepth);
        }
        if ((postProcess == POST_GRAY_LUT) ||
            (postProcess == POST_GRAY_LUT_ADD_TRANS) ||
            (postProcess == POST_GRAY_LUT_ADD_TRANS_EXP)) {
            initGrayLut(bitDepth);
        }

        decodeImage(interlaceMethod == 1);
        sampleModel = theTile.getSampleModel();

        if ((colorType == PNG_COLOR_PALETTE) && !expandPalette) {
            if (outputHasAlphaPalette) {
                colorModel = new IndexColorModel(bitDepth,
                                                 paletteEntries,
                                                 redPalette,
                                                 greenPalette,
                                                 bluePalette,
                                                 alphaPalette);
            } else {
                colorModel = new IndexColorModel(bitDepth,
                                                 paletteEntries,
                                                 redPalette,
                                                 greenPalette,
                                                 bluePalette);
            }
        } else if ((colorType == PNG_COLOR_GRAY) &&
                   (bitDepth < 8) && !output8BitGray) {
            byte[] palette = expandBits[bitDepth];
            colorModel = new IndexColorModel(bitDepth,
                                             palette.length,
                                             palette,
                                             palette,
                                             palette);
        } else {
            colorModel =
                createComponentColorModel(sampleModel);
        }
    }

    private static final int[] GrayBits8 = { 8 };
    private static final ComponentColorModel colorModelGray8 =
        new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
                                GrayBits8, false, false,
                                Transparency.OPAQUE,
                                DataBuffer.TYPE_BYTE);

    private static final int[] GrayAlphaBits8 = { 8, 8 };
    private static final ComponentColorModel colorModelGrayAlpha8 =
        new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
                                GrayAlphaBits8, true, false,
                                Transparency.TRANSLUCENT,
                                DataBuffer.TYPE_BYTE);

    private static final int[] GrayBits16 = { 16 };
    private static final ComponentColorModel colorModelGray16 =
        new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
                                GrayBits16, false, false,
                                Transparency.OPAQUE,
                                DataBuffer.TYPE_USHORT);

    private static final int[] GrayAlphaBits16 = { 16, 16 };
    private static final ComponentColorModel colorModelGrayAlpha16 =
        new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
                                GrayAlphaBits16, true, false,
                                Transparency.TRANSLUCENT,
                                DataBuffer.TYPE_USHORT);

    private static final int[] GrayBits32 = { 32 };
    private static final ComponentColorModel colorModelGray32 =
        new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
                                GrayBits32, false, false,
                                Transparency.OPAQUE,
                                DataBuffer.TYPE_INT);

    private static final int[] GrayAlphaBits32 = { 32, 32 };
    private static final ComponentColorModel colorModelGrayAlpha32 =
        new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
                                GrayAlphaBits32, true, false,
                                Transparency.TRANSLUCENT,
                                DataBuffer.TYPE_INT);

    private static final int[] RGBBits8 = { 8, 8, 8 };
    private static final ComponentColorModel colorModelRGB8 =
      new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                              RGBBits8, false, false,
                              Transparency.OPAQUE,
                              DataBuffer.TYPE_BYTE);

    private static final int[] RGBABits8 = { 8, 8, 8, 8 };
    private static final ComponentColorModel colorModelRGBA8 =
      new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                              RGBABits8, true, false,
                              Transparency.TRANSLUCENT,
                              DataBuffer.TYPE_BYTE);

    private static final int[] RGBBits16 = { 16, 16, 16 };
    private static final ComponentColorModel colorModelRGB16 =
      new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                              RGBBits16, false, false,
                              Transparency.OPAQUE,
                              DataBuffer.TYPE_USHORT);

    private static final int[] RGBABits16 = { 16, 16, 16, 16 };
    private static final ComponentColorModel colorModelRGBA16 =
      new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                              RGBABits16, true, false,
                              Transparency.TRANSLUCENT,
                              DataBuffer.TYPE_USHORT);

    private static final int[] RGBBits32 = { 32, 32, 32 };
    private static final ComponentColorModel colorModelRGB32 =
      new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                              RGBBits32, false, false,
                              Transparency.OPAQUE,
                              DataBuffer.TYPE_INT);

    private static final int[] RGBABits32 = { 32, 32, 32, 32 };
    private static final ComponentColorModel colorModelRGBA32 =
      new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                              RGBABits32, true, false,
                              Transparency.TRANSLUCENT,
                              DataBuffer.TYPE_INT);
    /**
     * A convenience method to create an instance of
     * <code>ComponentColorModel</code> suitable for use with the
     * given <code>SampleModel</code>.  The <code>SampleModel</code>
     * should have a data type of <code>DataBuffer.TYPE_BYTE</code>,
     * <code>TYPE_USHORT</code>, or <code>TYPE_INT</code> and between
     * 1 and 4 bands.  Depending on the number of bands of the
     * <code>SampleModel</code>, either a gray, gray+alpha, rgb, or
     * rgb+alpha <code>ColorModel</code> is returned.
     */
    public static ColorModel createComponentColorModel(SampleModel sm) {
        int type = sm.getDataType();
        int bands = sm.getNumBands();
        ComponentColorModel cm = null;

        if (type == DataBuffer.TYPE_BYTE) {
            switch (bands) {
            case 1:
                cm = colorModelGray8;
                break;
            case 2:
                cm = colorModelGrayAlpha8;
                break;
            case 3:
                cm = colorModelRGB8;
                break;
            case 4:
                cm = colorModelRGBA8;
                break;
            }
        } else if (type == DataBuffer.TYPE_USHORT) {
            switch (bands) {
            case 1:
                cm = colorModelGray16;
                break;
            case 2:
                cm = colorModelGrayAlpha16;
                break;
            case 3:
                cm = colorModelRGB16;
                break;
            case 4:
                cm = colorModelRGBA16;
                break;
            }
        } else if (type == DataBuffer.TYPE_INT) {
            switch (bands) {
            case 1:
                cm = colorModelGray32;
                break;
            case 2:
                cm = colorModelGrayAlpha32;
                break;
            case 3:
                cm = colorModelRGB32;
                break;
            case 4:
                cm = colorModelRGBA32;
                break;
            }
        }

        return cm;
    }

    private void parse_PLTE_chunk(PNGChunk chunk) {
        paletteEntries = chunk.getLength()/3;
        redPalette = new byte[paletteEntries];
        greenPalette = new byte[paletteEntries];
        bluePalette = new byte[paletteEntries];

        int pltIndex = 0;

        // gAMA chunk must precede PLTE chunk
        if (performGammaCorrection) {
            if (gammaLut == null) {
                initGammaLut(bitDepth == 16 ? 16 : 8);
            }

            for (int i = 0; i < paletteEntries; i++) {
                byte r = chunk.getByte(pltIndex++);
                byte g = chunk.getByte(pltIndex++);
                byte b = chunk.getByte(pltIndex++);

                redPalette[i] = (byte)gammaLut[r & 0xff];
                greenPalette[i] = (byte)gammaLut[g & 0xff];
                bluePalette[i] = (byte)gammaLut[b & 0xff];
            }
        } else {
            for (int i = 0; i < paletteEntries; i++) {
                redPalette[i] = chunk.getByte(pltIndex++);
                greenPalette[i] = chunk.getByte(pltIndex++);
                bluePalette[i] = chunk.getByte(pltIndex++);
            }
        }
    }

    private void parse_bKGD_chunk(PNGChunk chunk) {
        switch (colorType) {
        case PNG_COLOR_PALETTE:
            int bkgdIndex = chunk.getByte(0) & 0xff;

            bkgdRed = redPalette[bkgdIndex] & 0xff;
            bkgdGreen = greenPalette[bkgdIndex] & 0xff;
            bkgdBlue = bluePalette[bkgdIndex] & 0xff;

            if (encodeParam != null) {
                ((PNGEncodeParam.Palette)encodeParam).
                    setBackgroundPaletteIndex(bkgdIndex);
            }
            break;
        case PNG_COLOR_GRAY: case PNG_COLOR_GRAY_ALPHA:
            int bkgdGray = chunk.getInt2(0);
            bkgdRed = bkgdGreen = bkgdBlue = bkgdGray;

            if (encodeParam != null) {
                ((PNGEncodeParam.Gray)encodeParam).
                    setBackgroundGray(bkgdGray);
            }
            break;
        case PNG_COLOR_RGB: case PNG_COLOR_RGB_ALPHA:
            bkgdRed = chunk.getInt2(0);
            bkgdGreen = chunk.getInt2(2);
            bkgdBlue = chunk.getInt2(4);

            int[] bkgdRGB = new int[3];
            bkgdRGB[0] = bkgdRed;
            bkgdRGB[1] = bkgdGreen;
            bkgdRGB[2] = bkgdBlue;
            if (encodeParam != null) {
                ((PNGEncodeParam.RGB)encodeParam).
                    setBackgroundRGB(bkgdRGB);
            }
            break;
        }

        int r = 0, g = 0, b = 0;
        if (bitDepth < 8) {
            r = expandBits[bitDepth][bkgdRed];
            g = expandBits[bitDepth][bkgdGreen];
            b = expandBits[bitDepth][bkgdBlue];
        } else if (bitDepth == 8) {
            r = bkgdRed;
            g = bkgdGreen;
            b = bkgdBlue;
        } else if (bitDepth == 16) {
            r = bkgdRed >> 8;
            g = bkgdGreen >> 8;
            b = bkgdBlue >> 8;
        }
        if (emitProperties) {
            properties.put("background_color", new Color(r, g, b));
        }
    }

    private void parse_cHRM_chunk(PNGChunk chunk) {
        // If an sRGB chunk exists, ignore cHRM chunks
        if (sRGBRenderingIntent != -1) {
            return;
        }

        chromaticity = new float[8];
        chromaticity[0] = chunk.getInt4(0)/100000.0F;
        chromaticity[1] = chunk.getInt4(4)/100000.0F;
        chromaticity[2] = chunk.getInt4(8)/100000.0F;
        chromaticity[3] = chunk.getInt4(12)/100000.0F;
        chromaticity[4] = chunk.getInt4(16)/100000.0F;
        chromaticity[5] = chunk.getInt4(20)/100000.0F;
        chromaticity[6] = chunk.getInt4(24)/100000.0F;
        chromaticity[7] = chunk.getInt4(28)/100000.0F;

        if (encodeParam != null) {
            encodeParam.setChromaticity(chromaticity);
        }
        if (emitProperties) {
            properties.put("white_point_x", new Float(chromaticity[0]));
            properties.put("white_point_y", new Float(chromaticity[1]));
            properties.put("red_x", new Float(chromaticity[2]));
            properties.put("red_y", new Float(chromaticity[3]));
            properties.put("green_x", new Float(chromaticity[4]));
            properties.put("green_y", new Float(chromaticity[5]));
            properties.put("blue_x", new Float(chromaticity[6]));
            properties.put("blue_y", new Float(chromaticity[7]));
        }
    }

    private void parse_gAMA_chunk(PNGChunk chunk) {
        // If an sRGB chunk exists, ignore gAMA chunks
        if (sRGBRenderingIntent != -1) {
            return;
        }

        fileGamma = chunk.getInt4(0)/100000.0F;

        float exp =
            performGammaCorrection ? displayExponent/userExponent : 1.0F;
        if (encodeParam != null) {
            encodeParam.setGamma(fileGamma*exp);
        }
        if (emitProperties) {
            properties.put("gamma", new Float(fileGamma*exp));
        }
    }

    private void parse_hIST_chunk(PNGChunk chunk) {
        if (redPalette == null) {
            String msg = PropertyUtil.getString("PNGImageDecoder18");
            throw new RuntimeException(msg);
        }

        int length = redPalette.length;
        int[] hist = new int[length];
        for (int i = 0; i < length; i++) {
            hist[i] = chunk.getInt2(2*i);
        }

        if (encodeParam != null) {
            encodeParam.setPaletteHistogram(hist);
        }
    }

    private void parse_iCCP_chunk(PNGChunk chunk) {
        String name = "";  // todo simplify this
        byte b;

        int textIndex = 0;
        while ((b = chunk.getByte(textIndex++)) != 0) {
            name += (char)b;
        }
    }

    private void parse_pHYs_chunk(PNGChunk chunk) {
        int xPixelsPerUnit = chunk.getInt4(0);
        int yPixelsPerUnit = chunk.getInt4(4);
        int unitSpecifier = chunk.getInt1(8);

        if (encodeParam != null) {
            encodeParam.setPhysicalDimension(xPixelsPerUnit,
                                             yPixelsPerUnit,
                                             unitSpecifier);
        }
        if (emitProperties) {
            properties.put("x_pixels_per_unit", new Integer(xPixelsPerUnit));
            properties.put("y_pixels_per_unit", new Integer(yPixelsPerUnit));
            properties.put("pixel_aspect_ratio",
                           new Float((float)xPixelsPerUnit/yPixelsPerUnit));
            if (unitSpecifier == 1) {
                properties.put("pixel_units", "Meters");
            } else if (unitSpecifier != 0) {
                // Error -- unit specifier must be 0 or 1
                String msg = PropertyUtil.getString("PNGImageDecoder12");
                throw new RuntimeException(msg);
            }
        }
    }

    private void parse_sBIT_chunk(PNGChunk chunk) {
        if (colorType == PNG_COLOR_PALETTE) {
            significantBits = new int[3];
        } else {
            significantBits = new int[inputBands];
        }
        for (int i = 0; i < significantBits.length; i++) {
            int bits = chunk.getByte(i);
            int depth = (colorType == PNG_COLOR_PALETTE) ? 8 : bitDepth;
            if (bits <= 0 || bits > depth) {
                // Error -- significant bits must be between 0 and
                // image bit depth.
                String msg = PropertyUtil.getString("PNGImageDecoder13");
                throw new RuntimeException(msg);
            }
            significantBits[i] = bits;
        }

        if (encodeParam != null) {
            encodeParam.setSignificantBits(significantBits);
        }
        if (emitProperties) {
            properties.put("significant_bits", significantBits);
        }
    }

    private void parse_sRGB_chunk(PNGChunk chunk) {
        sRGBRenderingIntent = chunk.getByte(0);

        // The presence of an sRGB chunk implies particular
        // settings for gamma and chroma.
        fileGamma = 45455/100000.0F;

        chromaticity = new float[8];
        chromaticity[0] = 31270/10000.0F;
        chromaticity[1] = 32900/10000.0F;
        chromaticity[2] = 64000/10000.0F;
        chromaticity[3] = 33000/10000.0F;
        chromaticity[4] = 30000/10000.0F;
        chromaticity[5] = 60000/10000.0F;
        chromaticity[6] = 15000/10000.0F;
        chromaticity[7] =  6000/10000.0F;

        if (performGammaCorrection) {
            // File gamma is 1/2.2
            float gamma = fileGamma*(displayExponent/userExponent);
            if (encodeParam != null) {
                encodeParam.setGamma(gamma);
                encodeParam.setChromaticity(chromaticity);
            }
            if (emitProperties) {
                properties.put("gamma", new Float(gamma));
                properties.put("white_point_x", new Float(chromaticity[0]));
                properties.put("white_point_y", new Float(chromaticity[1]));
                properties.put("red_x", new Float(chromaticity[2]));
                properties.put("red_y", new Float(chromaticity[3]));
                properties.put("green_x", new Float(chromaticity[4]));
                properties.put("green_y", new Float(chromaticity[5]));
                properties.put("blue_x", new Float(chromaticity[6]));
                properties.put("blue_y", new Float(chromaticity[7]));
            }
        }
    }

    private void parse_tEXt_chunk(PNGChunk chunk) {

        byte b;
        StringBuffer key = new StringBuffer();
        int textIndex = 0;
        while ((b = chunk.getByte(textIndex++)) != 0) {
            key.append( (char)b );
        }

        StringBuffer value= new StringBuffer();
        for (int i = textIndex; i < chunk.getLength(); i++) {
            value.append( (char)chunk.getByte(i) );
        }

        textKeys.add(key.toString() );
        textStrings.add(value.toString() );
    }

    private void parse_tIME_chunk(PNGChunk chunk) {
        int year = chunk.getInt2(0);
        int month = chunk.getInt1(2) - 1;
        int day = chunk.getInt1(3);
        int hour = chunk.getInt1(4);
        int minute = chunk.getInt1(5);
        int second = chunk.getInt1(6);

        TimeZone gmt = TimeZone.getTimeZone("GMT");

        GregorianCalendar cal = new GregorianCalendar(gmt);
        cal.set(year, month, day,
                hour, minute, second);
        Date date = cal.getTime();

        if (encodeParam != null) {
            encodeParam.setModificationTime(date);
        }
        if (emitProperties) {
            properties.put("timestamp", date);
        }
    }

    private void parse_tRNS_chunk(PNGChunk chunk) {
        if (colorType == PNG_COLOR_PALETTE) {
            int entries = chunk.getLength();
            if (entries > paletteEntries) {
                // Error -- mustn't have more alpha than RGB palette entries
                String msg = PropertyUtil.getString("PNGImageDecoder14");
                throw new RuntimeException(msg);
            }

            // Load beginning of palette from the chunk
            alphaPalette = new byte[paletteEntries];
            for (int i = 0; i < entries; i++) {
                alphaPalette[i] = chunk.getByte(i);
            }

            // Fill rest of palette with 255
            for (int i = entries; i < paletteEntries; i++) {
                alphaPalette[i] = (byte)255;
            }

            if (!suppressAlpha) {
                if (expandPalette) {
                    postProcess = POST_PALETTE_TO_RGBA;
                    outputBands = 4;
                } else {
                    outputHasAlphaPalette = true;
                }
            }
        } else if (colorType == PNG_COLOR_GRAY) {
            grayTransparentAlpha = chunk.getInt2(0);

            if (!suppressAlpha) {
                if (bitDepth < 8) {
                    output8BitGray = true;
                    maxOpacity = 255;
                    postProcess = POST_GRAY_LUT_ADD_TRANS;
                } else {
                    postProcess = POST_ADD_GRAY_TRANS;
                }

                if (expandGrayAlpha) {
                    outputBands = 4;
                    postProcess |= POST_EXP_MASK;
                } else {
                    outputBands = 2;
                }

                if (encodeParam != null) {
                    ((PNGEncodeParam.Gray)encodeParam).
                        setTransparentGray(grayTransparentAlpha);
                }
            }
        } else if (colorType == PNG_COLOR_RGB) {
            redTransparentAlpha = chunk.getInt2(0);
            greenTransparentAlpha = chunk.getInt2(2);
            blueTransparentAlpha = chunk.getInt2(4);

            if (!suppressAlpha) {
                outputBands = 4;
                postProcess = POST_ADD_RGB_TRANS;

                if (encodeParam != null) {
                    int[] rgbTrans = new int[3];
                    rgbTrans[0] = redTransparentAlpha;
                    rgbTrans[1] = greenTransparentAlpha;
                    rgbTrans[2] = blueTransparentAlpha;
                    ((PNGEncodeParam.RGB)encodeParam).
                        setTransparentRGB(rgbTrans);
                }
            }
        } else if (colorType == PNG_COLOR_GRAY_ALPHA ||
                   colorType == PNG_COLOR_RGB_ALPHA) {
            // Error -- GA or RGBA image can't have a tRNS chunk.
            String msg = PropertyUtil.getString("PNGImageDecoder15");
            throw new RuntimeException(msg);
        }
    }

    private void parse_zTXt_chunk(PNGChunk chunk) {

        int textIndex = 0;
        StringBuffer key = new StringBuffer();
        byte b;
        while ((b = chunk.getByte(textIndex++)) != 0) {
            key.append( (char)b );
        }
        /* int method = */ chunk.getByte(textIndex++);

        StringBuffer value = new StringBuffer();
        try {
            int length = chunk.getLength() - textIndex;
            byte[] data = chunk.getData();
            InputStream cis =
                new ByteArrayInputStream(data, textIndex, length);
            InputStream iis = new InflaterInputStream(cis);

            int c;
            while ((c = iis.read()) != -1) {
                value.append( (char)c );
            }

            ztextKeys.add(key.toString() );
            ztextStrings.add(value.toString() );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WritableRaster createRaster(int width, int height, int bands,
                                        int scanlineStride,
                                        int bitDepth) {

        DataBuffer dataBuffer;
        WritableRaster ras = null;
        Point origin = new Point(0, 0);
        if ((bitDepth < 8) && (bands == 1)) {
            dataBuffer = new DataBufferByte(height*scanlineStride);
            ras = Raster.createPackedRaster(dataBuffer,
                                            width, height,
                                            bitDepth,
                                            origin);
        } else if (bitDepth <= 8) {
            dataBuffer = new DataBufferByte(height*scanlineStride);
           ras = Raster.createInterleavedRaster(dataBuffer,
                                                 width, height,
                                                 scanlineStride,
                                                 bands,
                                                 bandOffsets[bands],
                                                 origin);
        } else {
            dataBuffer = new DataBufferUShort(height*scanlineStride);
            ras = Raster.createInterleavedRaster(dataBuffer,
                                                 width, height,
                                                 scanlineStride,
                                                 bands,
                                                 bandOffsets[bands],
                                                 origin);
        }

        return ras;
    }

    // Data filtering methods

    private static void decodeSubFilter(byte[] curr, int count, int bpp) {
        for (int i = bpp; i < count; i++) {
            int val;

            val = curr[i] & 0xff;
            val += curr[i - bpp] & 0xff;

            curr[i] = (byte)val;
        }
    }

    private static void decodeUpFilter(byte[] curr, byte[] prev,
                                       int count) {
        for (int i = 0; i < count; i++) {
            int raw = curr[i] & 0xff;
            int prior = prev[i] & 0xff;

            curr[i] = (byte)(raw + prior);
        }
    }

    private static void decodeAverageFilter(byte[] curr, byte[] prev,
                                            int count, int bpp) {
        int raw, priorPixel, priorRow;

        for (int i = 0; i < bpp; i++) {
            raw = curr[i] & 0xff;
            priorRow = prev[i] & 0xff;

            curr[i] = (byte)(raw + priorRow/2);
        }

        for (int i = bpp; i < count; i++) {
            raw = curr[i] & 0xff;
            priorPixel = curr[i - bpp] & 0xff;
            priorRow = prev[i] & 0xff;

            curr[i] = (byte)(raw + (priorPixel + priorRow)/2);
        }
    }

    private static void decodePaethFilter(byte[] curr, byte[] prev,
                                          int count, int bpp) {
        int raw, priorPixel, priorRow, priorRowPixel;

        for (int i = 0; i < bpp; i++) {
            raw = curr[i] & 0xff;
            priorRow = prev[i] & 0xff;

            curr[i] = (byte)(raw + priorRow);
        }

        for (int i = bpp; i < count; i++) {
            raw = curr[i] & 0xff;
            priorPixel = curr[i - bpp] & 0xff;
            priorRow = prev[i] & 0xff;
            priorRowPixel = prev[i - bpp] & 0xff;

            curr[i] = (byte)(raw + PNGEncodeParam.paethPredictor(priorPixel,
                                                  priorRow,
                                                  priorRowPixel));
        }
    }

    private void processPixels(int process,
                               Raster src, WritableRaster dst,
                               int xOffset, int step, int y, int width) {
        int srcX, dstX;

        // Create an array suitable for holding one pixel
        int[] ps = src.getPixel(0, 0, (int[])null);
        int[] pd = dst.getPixel(0, 0, (int[])null);

        dstX = xOffset;
        switch (process) {
        case POST_NONE:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);
                dst.setPixel(dstX, y, ps);
                dstX += step;
            }
            break;

        case POST_GAMMA:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                for (int i = 0; i < inputBands; i++) {
                    int x = ps[i];
                    ps[i] = gammaLut[x];
                }

                dst.setPixel(dstX, y, ps);
                dstX += step;
            }
            break;

        case POST_GRAY_LUT:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                pd[0] = grayLut[ps[0]];

                dst.setPixel(dstX, y, pd);
                dstX += step;
            }
            break;

        case POST_GRAY_LUT_ADD_TRANS:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                int val = ps[0];
                pd[0] = grayLut[val];
                if (val == grayTransparentAlpha) {
                    pd[1] = 0;
                } else {
                    pd[1] = maxOpacity;
                }

                dst.setPixel(dstX, y, pd);
                dstX += step;
            }
            break;

        case POST_PALETTE_TO_RGB:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                int val = ps[0];
                pd[0] = redPalette[val];
                pd[1] = greenPalette[val];
                pd[2] = bluePalette[val];

                dst.setPixel(dstX, y, pd);
                dstX += step;
            }
            break;

        case POST_PALETTE_TO_RGBA:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                int val = ps[0];
                pd[0] = redPalette[val];
                pd[1] = greenPalette[val];
                pd[2] = bluePalette[val];
                pd[3] = alphaPalette[val];

                dst.setPixel(dstX, y, pd);
                dstX += step;
            }
            break;

        case POST_ADD_GRAY_TRANS:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                int val = ps[0];
                if (performGammaCorrection) {
                    val = gammaLut[val];
                }
                pd[0] = val;
                if (val == grayTransparentAlpha) {
                    pd[1] = 0;
                } else {
                    pd[1] = maxOpacity;
                }

                dst.setPixel(dstX, y, pd);
                dstX += step;
            }
            break;

        case POST_ADD_RGB_TRANS:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                int r = ps[0];
                int g = ps[1];
                int b = ps[2];
                if (performGammaCorrection) {
                    pd[0] = gammaLut[r];
                    pd[1] = gammaLut[g];
                    pd[2] = gammaLut[b];
                } else {
                    pd[0] = r;
                    pd[1] = g;
                    pd[2] = b;
                }
                if ((r == redTransparentAlpha) &&
                    (g == greenTransparentAlpha) &&
                    (b == blueTransparentAlpha)) {
                    pd[3] = 0;
                } else {
                    pd[3] = maxOpacity;
                }

                dst.setPixel(dstX, y, pd);
                dstX += step;
            }
            break;

        case POST_REMOVE_GRAY_TRANS:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                int g = ps[0];
                if (performGammaCorrection) {
                    pd[0] = gammaLut[g];
                } else {
                    pd[0] = g;
                }

                dst.setPixel(dstX, y, pd);
                dstX += step;
            }
            break;

        case POST_REMOVE_RGB_TRANS:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                int r = ps[0];
                int g = ps[1];
                int b = ps[2];
                if (performGammaCorrection) {
                    pd[0] = gammaLut[r];
                    pd[1] = gammaLut[g];
                    pd[2] = gammaLut[b];
                } else {
                    pd[0] = r;
                    pd[1] = g;
                    pd[2] = b;
                }

                dst.setPixel(dstX, y, pd);
                dstX += step;
            }
            break;

        case POST_GAMMA_EXP:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                int val = ps[0];
                int alpha = ps[1];
                int gamma = gammaLut[val];
                pd[0] = gamma;
                pd[1] = gamma;
                pd[2] = gamma;
                pd[3] = alpha;

                dst.setPixel(dstX, y, pd);
                dstX += step;
            }
            break;

        case POST_GRAY_ALPHA_EXP:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                int val = ps[0];
                int alpha = ps[1];
                pd[0] = val;
                pd[1] = val;
                pd[2] = val;
                pd[3] = alpha;

                dst.setPixel(dstX, y, pd);
                dstX += step;
            }
            break;

        case POST_ADD_GRAY_TRANS_EXP:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                int val = ps[0];
                if (performGammaCorrection) {
                    val = gammaLut[val];
                }
                pd[0] = val;
                pd[1] = val;
                pd[2] = val;
                if (val == grayTransparentAlpha) {
                    pd[3] = 0;
                } else {
                    pd[3] = maxOpacity;
                }

                dst.setPixel(dstX, y, pd);
                dstX += step;
            }
            break;

        case POST_GRAY_LUT_ADD_TRANS_EXP:
            for (srcX = 0; srcX < width; srcX++) {
                src.getPixel(srcX, 0, ps);

                int val = ps[0];
                int val2 = grayLut[val];
                pd[0] = val2;
                pd[1] = val2;
                pd[2] = val2;
                if (val == grayTransparentAlpha) {
                    pd[3] = 0;
                } else {
                    pd[3] = maxOpacity;
                }

                dst.setPixel(dstX, y, pd);
                dstX += step;
            }
            break;
        }
    }

    /**
     * Reads in an image of a given size and returns it as a
     * WritableRaster.
     */
    private void decodePass(WritableRaster imRas,
                            int xOffset, int yOffset,
                            int xStep, int yStep,
                            int passWidth, int passHeight) {
        if ((passWidth == 0) || (passHeight == 0)) {
            return;
        }

        int bytesPerRow = (inputBands*passWidth*bitDepth + 7)/8;
        int eltsPerRow = (bitDepth == 16) ? bytesPerRow/2 : bytesPerRow;
        byte[] curr = new byte[bytesPerRow];
        byte[] prior = new byte[bytesPerRow];

        // Create a 1-row tall Raster to hold the data
        WritableRaster passRow =
            createRaster(passWidth, 1, inputBands,
                         eltsPerRow,
                         bitDepth);
        DataBuffer dataBuffer = passRow.getDataBuffer();
        int type = dataBuffer.getDataType();
        byte[] byteData = null;
        short[] shortData = null;
        if (type == DataBuffer.TYPE_BYTE) {
            byteData = ((DataBufferByte)dataBuffer).getData();
        } else {
            shortData = ((DataBufferUShort)dataBuffer).getData();
        }

        // Decode the (sub)image row-by-row
        int srcY, dstY;
        for (srcY = 0, dstY = yOffset;
             srcY < passHeight;
             srcY++, dstY += yStep) {
            // Read the filter type byte and a row of data
            int filter = 0;
            try {
                filter = dataStream.read();
                dataStream.readFully(curr, 0, bytesPerRow);
            } catch (Exception e) {
                e.printStackTrace();
            }

            switch (filter) {
            case PNG_FILTER_NONE:
                break;
            case PNG_FILTER_SUB:
                decodeSubFilter(curr, bytesPerRow, bytesPerPixel);
                break;
            case PNG_FILTER_UP:
                decodeUpFilter(curr, prior, bytesPerRow);
                break;
            case PNG_FILTER_AVERAGE:
                decodeAverageFilter(curr, prior, bytesPerRow, bytesPerPixel);
                break;
            case PNG_FILTER_PAETH:
                decodePaethFilter(curr, prior, bytesPerRow, bytesPerPixel);
                break;
            default:
                // Error -- uknown filter type
                String msg = PropertyUtil.getString("PNGImageDecoder16");
                throw new RuntimeException(msg);
            }

            // Copy data into passRow byte by byte
            if (bitDepth < 16) {
                System.arraycopy(curr, 0, byteData, 0, bytesPerRow);
            } else {
                int idx = 0;
                for (int j = 0; j < eltsPerRow; j++) {
                    shortData[j] =
                        (short)((curr[idx] << 8) | (curr[idx + 1] & 0xff));
                    idx += 2;
                }
            }

            processPixels(postProcess,
                          passRow, imRas, xOffset, xStep, dstY, passWidth);

            // Swap curr and prior
            byte[] tmp = prior;
            prior = curr;
            curr = tmp;
        }
    }

    private void decodeImage(boolean useInterlacing) {
        if (!useInterlacing) {
            decodePass(theTile, 0, 0, 1, 1, width, height);
        } else {
            decodePass(theTile, 0, 0, 8, 8, (width + 7)/8, (height + 7)/8);
            decodePass(theTile, 4, 0, 8, 8, (width + 3)/8, (height + 7)/8);
            decodePass(theTile, 0, 4, 4, 8, (width + 3)/4, (height + 3)/8);
            decodePass(theTile, 2, 0, 4, 4, (width + 1)/4, (height + 3)/4);
            decodePass(theTile, 0, 2, 2, 4, (width + 1)/2, (height + 1)/4);
            decodePass(theTile, 1, 0, 2, 2, width/2, (height + 1)/2);
            decodePass(theTile, 0, 1, 1, 2, width, height/2);
        }
    }

    // RenderedImage stuff

    public Raster getTile(int tileX, int tileY) {
        if (tileX != 0 || tileY != 0) {
            // Error -- bad tile requested
            String msg = PropertyUtil.getString("PNGImageDecoder17");
            throw new IllegalArgumentException(msg);
        }
        return theTile;
    }
}
