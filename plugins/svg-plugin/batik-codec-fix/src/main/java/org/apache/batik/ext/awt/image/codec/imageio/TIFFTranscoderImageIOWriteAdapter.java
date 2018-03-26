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
package org.apache.batik.ext.awt.image.codec.imageio;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.rendered.FormatRed;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.TIFFTranscoder;

/**
 * This class is a helper to <code>TIFFTranscoder</code> that writes TIFF images 
 * through the Image I/O API.
 *
 * @version $Id$
 */
public class TIFFTranscoderImageIOWriteAdapter 
    implements TIFFTranscoder.WriteAdapter {

    /**
     * @throws TranscoderException 
     * @see org.apache.batik.transcoder.image.TIFFTranscoder.WriteAdapter#writeImage(TIFFTranscoder, java.awt.image.BufferedImage, org.apache.batik.transcoder.TranscoderOutput)
     */
    public void writeImage(TIFFTranscoder transcoder, BufferedImage img,
            TranscoderOutput output) throws TranscoderException {

        TranscodingHints hints = transcoder.getTranscodingHints();

        ImageWriter writer = ImageWriterRegistry.getInstance()
            .getWriterFor("image/tiff");
        ImageWriterParams params = new ImageWriterParams();

        float PixSzMM = transcoder.getUserAgent().getPixelUnitToMillimeter();
        int PixSzInch = (int)(25.4 / PixSzMM + 0.5);
        params.setResolution(PixSzInch);

        if (hints.containsKey(TIFFTranscoder.KEY_COMPRESSION_METHOD)) {
            String method = (String)hints.get(TIFFTranscoder.KEY_COMPRESSION_METHOD);
            //Values set here as defined in TIFFImageWriteParam of JAI Image I/O Tools
            if ("packbits".equals(method)) {
                params.setCompressionMethod("PackBits");
            } else if ("deflate".equals(method)) {
                params.setCompressionMethod("Deflate");
            } else if ("lzw".equals(method)) {
                params.setCompressionMethod("LZW");
            } else if ("jpeg".equals(method)) {
                params.setCompressionMethod("JPEG");
            } else {
                //nop
            }
        }

        try {
            OutputStream ostream = output.getOutputStream();
            int w = img.getWidth();
            int h = img.getHeight();
            SinglePixelPackedSampleModel sppsm;
            sppsm = (SinglePixelPackedSampleModel)img.getSampleModel();
            int bands = sppsm.getNumBands();
            int [] off = new int[bands];
            for (int i = 0; i < bands; i++)
                off[i] = i;
            SampleModel sm = new PixelInterleavedSampleModel
                (DataBuffer.TYPE_BYTE, w, h, bands, w * bands, off);
            
            RenderedImage rimg = new FormatRed(GraphicsUtil.wrap(img), sm);
            writer.writeImage(rimg, ostream, params);
            ostream.flush();
        } catch (IOException ex) {
            throw new TranscoderException(ex);
        }
    }

}
