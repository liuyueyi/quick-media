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

package org.apache.batik.ext.awt.image.codec.util;

import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A partial implementation of the ImageEncoder interface useful for
 * subclassing.
 *
 * <p><b> This class is not a committed part of the JAI API.  It may
 * be removed or changed in future releases of JAI.</b>
 *
 * @version $Id$
 */
public abstract class ImageEncoderImpl implements ImageEncoder {

    /** The OutputStream associcted with this ImageEncoder. */
    protected OutputStream output;

    /** The ImageEncodeParam object associcted with this ImageEncoder. */
    protected ImageEncodeParam param;

    /**
     * Constructs an ImageEncoderImpl with a given OutputStream
     * and ImageEncoderParam instance.
     */
    public ImageEncoderImpl(OutputStream output,
                            ImageEncodeParam param) {
        this.output = output;
        this.param = param;
    }

    /**
     * Returns the current parameters as an instance of the
     * ImageEncodeParam interface.  Concrete implementations of this
     * interface will return corresponding concrete implementations of
     * the ImageEncodeParam interface.  For example, a JPEGImageEncoder
     * will return an instance of JPEGEncodeParam.
     */
    public ImageEncodeParam getParam() {
        return param;
    }

    /**
     * Sets the current parameters to an instance of the
     * ImageEncodeParam interface.  Concrete implementations
     * of ImageEncoder may throw a RuntimeException if the
     * params argument is not an instance of the appropriate
     * subclass or subinterface.  For example, a JPEGImageEncoder
     * will expect param to be an instance of JPEGEncodeParam.
     */
    public void setParam(ImageEncodeParam param) {
        this.param = param;
    }

    /** Returns the OutputStream associated with this ImageEncoder. */
    public OutputStream getOutputStream() {
        return output;
    }

    /**
     * Encodes a Raster with a given ColorModel and writes the output
     * to the OutputStream associated with this ImageEncoder.
     */
    public void encode(Raster ras, ColorModel cm) throws IOException {
        RenderedImage im = new SingleTileRenderedImage(ras, cm);
        encode(im);
    }

    /**
     * Encodes a RenderedImage and writes the output to the
     * OutputStream associated with this ImageEncoder.
     */
    public abstract void encode(RenderedImage im) throws IOException;
}
