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

import java.io.IOException;
import java.io.InputStream;

/**
 * A subclass of <code>SeekableStream</code> that may be used
 * to wrap a regular <code>InputStream</code> efficiently.
 * Seeking backwards is not supported.
 *
 * @version $Id$
 */
public class ForwardSeekableStream extends SeekableStream {

    /** The source <code>InputStream</code>. */
    private InputStream src;

    /** The current position. */
    long pointer = 0L;

    /**
     * Constructs a <code>InputStreamForwardSeekableStream</code> from a
     * regular <code>InputStream</code>.
     */
    public ForwardSeekableStream(InputStream src) {
        this.src = src;
    }

    /** Forwards the request to the real <code>InputStream</code>. */
    public final int read() throws IOException {
        int result = src.read();
        if (result != -1) {
            ++pointer;
        }
        return result;
    }

    /** Forwards the request to the real <code>InputStream</code>. */
    public final int read(byte[] b, int off, int len) throws IOException {
        int result = src.read(b, off, len);
        if (result != -1) {
            pointer += result;
        }
        return result;
    }

    /** Forwards the request to the real <code>InputStream</code>. */
    public final long skip(long n) throws IOException {
        long skipped = src.skip(n);
        pointer += skipped;
        return skipped;
    }

    /** Forwards the request to the real <code>InputStream</code>. */
    public final int available() throws IOException {
        return src.available();
    }

    /** Forwards the request to the real <code>InputStream</code>. */
    public final void close() throws IOException {
        src.close();
    }

    /**
     * Forwards the request to the real <code>InputStream</code>.
     * We use {@link SeekableStream#markPos}
     */
    public final synchronized void mark(int readLimit) {
        markPos = pointer;
        src.mark(readLimit);
    }

    /**
     * Forwards the request to the real <code>InputStream</code>.
     * We use {@link SeekableStream#markPos}
     */
    public final synchronized void reset() throws IOException {
        if (markPos != -1) {
            pointer = markPos;
        }
        src.reset();
    }

    /** Forwards the request to the real <code>InputStream</code>. */
    public boolean markSupported() {
        return src.markSupported();
    }

    /** Returns <code>false</code> since seking backwards is not supported. */
    public final boolean canSeekBackwards() {
        return false;
    }

    /** Returns the current position in the stream (bytes read). */
    public final long getFilePointer() {
        return pointer;
    }

    /**
     * Seeks forward to the given position in the stream.
     * If <code>pos</code> is smaller than the current position
     * as returned by <code>getFilePointer()</code>, nothing
     * happens.
     */
    public final void seek(long pos) throws IOException {
        while (pos - pointer > 0) {
            pointer += src.skip(pos - pointer);
        }
    }
}
