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

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * A partial implementation of the <code>ImageDecoder</code> interface
 * useful for subclassing.
 *
 * @version $Id$
 */
public abstract class ImageDecoderImpl implements ImageDecoder {

    /**
     * The <code>SeekableStream</code> associcted with this
     * <code>ImageEncoder</code>.
     */
    protected SeekableStream input;

    /**
     * The <code>ImageDecodeParam</code> object associated with this
     * <code>ImageEncoder</code>.
     */
    protected ImageDecodeParam param;

    /**
     * Constructs an <code>ImageDecoderImpl</code> with a given
     * <code>SeekableStream</code> and <code>ImageDecodeParam</code>
     * instance.
     */
    public ImageDecoderImpl(SeekableStream input,
                            ImageDecodeParam param) {
        this.input = input;
        this.param = param;
    }

    /**
     * Constructs an <code>ImageDecoderImpl</code> with a given
     * <code>InputStream</code> and <code>ImageDecodeParam</code>
     * instance.  The <code>input</code> parameter will be used to
     * construct a <code>ForwardSeekableStream</code>; if the ability
     * to seek backwards is required, the caller should construct
     * an instance of <code>SeekableStream</code> and
     * make use of the other contructor.
     */
    public ImageDecoderImpl(InputStream input,
                            ImageDecodeParam param) {
        this.input = new ForwardSeekableStream(input);
        this.param = param;
    }

    /**
     * Returns the current parameters as an instance of the
     * <code>ImageDecodeParam</code> interface.  Concrete
     * implementations of this interface will return corresponding
     * concrete implementations of the <code>ImageDecodeParam</code>
     * interface.  For example, a <code>JPEGImageDecoder</code> will
     * return an instance of <code>JPEGDecodeParam</code>.
     */
    public ImageDecodeParam getParam() {
        return param;
    }

    /**
     * Sets the current parameters to an instance of the
     * <code>ImageDecodeParam</code> interface.  Concrete
     * implementations of <code>ImageDecoder</code> may throw a
     * <code>RuntimeException</code> if the <code>param</code>
     * argument is not an instance of the appropriate subclass or
     * subinterface.  For example, a <code>JPEGImageDecoder</code>
     * will expect <code>param</code> to be an instance of
     * <code>JPEGDecodeParam</code>.
     */
    public void setParam(ImageDecodeParam param) {
        this.param = param;
    }

    /**
     * Returns the <code>SeekableStream</code> associated with
     * this <code>ImageDecoder</code>.
     */
    public SeekableStream getInputStream() {
        return input;
    }

    /**
     * Returns the number of pages present in the current stream.
     * By default, the return value is 1.  Subclasses that deal with
     * multi-page formats should override this method.
     */
    public int getNumPages() throws IOException {
        return 1;
    }

    /**
     * Returns a <code>Raster</code> that contains the decoded
     * contents of the <code>SeekableStream</code> associated
     * with this <code>ImageDecoder</code>.  Only
     * the first page of a multi-page image is decoded.
     */
    public Raster decodeAsRaster() throws IOException {
        return decodeAsRaster(0);
    }

    /**
     * Returns a <code>Raster</code> that contains the decoded
     * contents of the <code>SeekableStream</code> associated
     * with this <code>ImageDecoder</code>.
     * The given page of a multi-page image is decoded.  If
     * the page does not exist, an IOException will be thrown.
     * Page numbering begins at zero.
     *
     * @param page The page to be decoded.
     */
    public Raster decodeAsRaster(int page) throws IOException {
        RenderedImage im = decodeAsRenderedImage(page);
        return im.getData();
    }

    /**
     * Returns a <code>RenderedImage</code> that contains the decoded
     * contents of the <code>SeekableStream</code> associated
     * with this <code>ImageDecoder</code>.  Only
     * the first page of a multi-page image is decoded.
     */
    public RenderedImage decodeAsRenderedImage() throws IOException {
        return decodeAsRenderedImage(0);
    }

    /**
     * Returns a <code>RenderedImage</code> that contains the decoded
     * contents of the <code>SeekableStream</code> associated
     * with this <code>ImageDecoder</code>.
     * The given page of a multi-page image is decoded.  If
     * the page does not exist, an IOException will be thrown.
     * Page numbering begins at zero.
     *
     * @param page The page to be decoded.
     */
    public abstract RenderedImage decodeAsRenderedImage(int page)
        throws IOException;
}
