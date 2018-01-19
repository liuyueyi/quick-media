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

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.renderable.DeferRable;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.RedRable;
import org.apache.batik.ext.awt.image.rendered.Any2sRGBRed;
import org.apache.batik.ext.awt.image.rendered.CachableRed;
import org.apache.batik.ext.awt.image.rendered.FormatRed;
import org.apache.batik.ext.awt.image.spi.ImageTagRegistry;
import org.apache.batik.ext.awt.image.spi.MagicNumberRegistryEntry;
import org.apache.batik.util.ParsedURL;

/**
 *
 * @version $Id$
 */
public class PNGRegistryEntry
    extends MagicNumberRegistryEntry {


    static final byte [] signature = {(byte)0x89, 80, 78, 71, 13, 10, 26, 10};

    public PNGRegistryEntry() {
        super("PNG", "png", "image/png", 0, signature);
    }

    /**
     * Decode the Stream into a RenderableImage
     *
     * @param inIS The input stream that contains the image.
     * @param origURL The original URL, if any, for documentation
     *                purposes only.  This may be null.
     * @param needRawData If true the image returned should not have
     *                    any default color correction the file may
     *                    specify applied.  */
    public Filter handleStream(InputStream inIS,
                               ParsedURL   origURL,
                               boolean needRawData) {

        final DeferRable  dr  = new DeferRable();
        final InputStream is  = inIS;
        final boolean     raw = needRawData;
        final String      errCode;
        final Object []   errParam;
        if (origURL != null) {
            errCode  = ERR_URL_FORMAT_UNREADABLE;
            errParam = new Object[] {"PNG", origURL};
        } else {
            errCode  = ERR_STREAM_FORMAT_UNREADABLE;
            errParam = new Object[] {"PNG"};
        }

        Thread t = new Thread() {
                public void run() {
                    Filter filt;
                    try {
                        PNGDecodeParam param = new PNGDecodeParam();
                        param.setExpandPalette(true);

                        if (raw)
                            param.setPerformGammaCorrection(false);
                        else {
                            param.setPerformGammaCorrection(true);
                            param.setDisplayExponent(2.2f); // sRGB gamma
                        }
                        CachableRed cr = new PNGRed(is, param);
                        dr.setBounds(new Rectangle2D.Double
                                     (0, 0, cr.getWidth(), cr.getHeight()));

                        cr = new Any2sRGBRed(cr);
                        cr = new FormatRed(cr, GraphicsUtil.sRGB_Unpre);
                        WritableRaster wr = (WritableRaster)cr.getData();
                        ColorModel cm = cr.getColorModel();
                        BufferedImage image;
                        image = new BufferedImage
                            (cm, wr, cm.isAlphaPremultiplied(), null);
                        cr = GraphicsUtil.wrap(image);
                        filt = new RedRable(cr);
                    } catch (IOException ioe) {
                        filt = ImageTagRegistry.getBrokenLinkImage
                            (PNGRegistryEntry.this, errCode, errParam);
                    } catch (ThreadDeath td) {
                        filt = ImageTagRegistry.getBrokenLinkImage
                            (PNGRegistryEntry.this, errCode, errParam);
                        dr.setSource(filt);
                        throw td;
                    } catch (Throwable t) {
                        filt = ImageTagRegistry.getBrokenLinkImage
                            (PNGRegistryEntry.this, errCode, errParam);
                    }

                    dr.setSource(filt);
                }
            };
        t.start();
        return dr;
    }
}
