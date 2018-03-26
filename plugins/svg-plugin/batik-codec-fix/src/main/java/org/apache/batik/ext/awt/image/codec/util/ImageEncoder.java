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
 * An interface describing objects that transform a BufferedImage or
 * Raster into an OutputStream.
 *
 * <p><b> This interface is not a committed part of the JAI API.  It may
 * be removed or changed in future releases of JAI.</b>
 *
 * @version $Id$
 */
public interface ImageEncoder {

    /**
     * Returns the current parameters as an instance of the
     * ImageEncodeParam interface.  Concrete implementations of this
     * interface will return corresponding concrete implementations of
     * the ImageEncodeParam interface.  For example, a JPEGImageEncoder
     * will return an instance of JPEGEncodeParam.
     */
    ImageEncodeParam getParam();

    /**
     * Sets the current parameters to an instance of the
     * ImageEncodeParam interface.  Concrete implementations
     * of ImageEncoder may throw a RuntimeException if the
     * params argument is not an instance of the appropriate
     * subclass or subinterface.  For example, a JPEGImageEncoder
     * will expect param to be an instance of JPEGEncodeParam.
     */
    void setParam(ImageEncodeParam param);

    /** Returns the OutputStream associated with this ImageEncoder. */
    OutputStream getOutputStream();

    /**
     * Encodes a Raster with a given ColorModel and writes the output
     * to the OutputStream associated with this ImageEncoder.
     */
    void encode(Raster ras, ColorModel cm) throws IOException;

    /**
     * Encodes a RenderedImage and writes the output to the
     * OutputStream associated with this ImageEncoder.
     */
    void encode(RenderedImage im) throws IOException;
}
