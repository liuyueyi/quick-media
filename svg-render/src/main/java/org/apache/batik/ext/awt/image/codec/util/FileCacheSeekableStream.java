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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * A subclass of <code>SeekableStream</code> that may be used to wrap
 * a regular <code>InputStream</code>.  Seeking backwards is supported
 * by means of a file cache.  In circumstances that do not allow the
 * creation of a temporary file (for example, due to security
 * consideration or the absence of local disk), the
 * <code>MemoryCacheSeekableStream</code> class may be used instead.
 *
 * <p> The <code>mark()</code> and <code>reset()</code> methods are
 * supported.
 *
 * <p><b> This class is not a committed part of the JAI API.  It may
 * be removed or changed in future releases of JAI.</b>
 *
 * @version $Id$
 */
public final class FileCacheSeekableStream extends SeekableStream {

    /** The source stream. */
    private InputStream stream;

    /** The cache File. */
    private File cacheFile;

    /** The cache as a RandomAcessFile. */
    private RandomAccessFile cache;

    /** The length of the read buffer. */
    private int bufLen = 1024;

    /** The read buffer. */
    private byte[] buf = new byte[bufLen];

    /** Number of bytes in the cache. */
    private long length = 0;

    /** Next byte to be read. */
    private long pointer = 0;

    /** True if we've encountered the end of the source stream. */
    private boolean foundEOF = false;

    /**
     * Constructs a <code>MemoryCacheSeekableStream</code> that takes
     * its source data from a regular <code>InputStream</code>.
     * Seeking backwards is supported by means of an file cache.
     *
     * <p> An <code>IOException</code> will be thrown if the
     * attempt to create the cache file fails for any reason.
     */
    public FileCacheSeekableStream(InputStream stream)
        throws IOException {
        this.stream = stream;
        this.cacheFile = File.createTempFile("jai-FCSS-", ".tmp");
        cacheFile.deleteOnExit();
        this.cache = new RandomAccessFile(cacheFile, "rw");
    }

    /**
     * Ensures that at least <code>pos</code> bytes are cached,
     * or the end of the source is reached.  The return value
     * is equal to the smaller of <code>pos</code> and the
     * length of the source file.
     */
    private long readUntil(long pos) throws IOException {
        // We've already got enough data cached
        if (pos < length) {
            return pos;
        }
        // pos >= length but length isn't getting any bigger, so return it
        if (foundEOF) {
            return length;
        }

        long len = pos - length;
        cache.seek(length);
        while (len > 0) {
            // Copy a buffer's worth of data from the source to the cache
            // bufLen will always fit into an int so this is safe
            int nbytes = stream.read(buf, 0, (int)Math.min(len, bufLen));
            if (nbytes == -1) {
                foundEOF = true;
                return length;
            }

            cache.setLength(cache.length() + nbytes);
            cache.write(buf, 0, nbytes);
            len -= nbytes;
            length += nbytes;
        }

        return pos;
    }

    /**
     * Returns <code>true</code> since all
     * <code>FileCacheSeekableStream</code> instances support seeking
     * backwards.
     */
    public boolean canSeekBackwards() {
        return true;
    }

    /**
     * Returns the current offset in this file.
     *
     * @return     the offset from the beginning of the file, in bytes,
     *             at which the next read occurs.
     */
    public long getFilePointer() {
        return pointer;
    }

    /**
     * Sets the file-pointer offset, measured from the beginning of this
     * file, at which the next read occurs.
     *
     * @param      pos   the offset position, measured in bytes from the
     *                   beginning of the file, at which to set the file
     *                   pointer.
     * @exception  IOException  if <code>pos</code> is less than
     *                          <code>0</code> or if an I/O error occurs.
     */
    public void seek(long pos) throws IOException {
        if (pos < 0) {
            throw new IOException(PropertyUtil.getString("FileCacheSeekableStream0"));
        }
        pointer = pos;
    }

    /**
     * Reads the next byte of data from the input stream. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
    public int read() throws IOException {
        long next = pointer + 1;
        long pos = readUntil(next);
        if (pos >= next) {
            cache.seek(pointer++);
            return cache.read();
        } else {
            return -1;
        }
    }

    /**
     * Reads up to <code>len</code> bytes of data from the input stream into
     * an array of bytes.  An attempt is made to read as many as
     * <code>len</code> bytes, but a smaller number may be read, possibly
     * zero. The number of bytes actually read is returned as an integer.
     *
     * <p> This method blocks until input data is available, end of file is
     * detected, or an exception is thrown.
     *
     * <p> If <code>b</code> is <code>null</code>, a
     * <code>NullPointerException</code> is thrown.
     *
     * <p> If <code>off</code> is negative, or <code>len</code> is negative, or
     * <code>off+len</code> is greater than the length of the array
     * <code>b</code>, then an <code>IndexOutOfBoundsException</code> is
     * thrown.
     *
     * <p> If <code>len</code> is zero, then no bytes are read and
     * <code>0</code> is returned; otherwise, there is an attempt to read at
     * least one byte. If no byte is available because the stream is at end of
     * file, the value <code>-1</code> is returned; otherwise, at least one
     * byte is read and stored into <code>b</code>.
     *
     * <p> The first byte read is stored into element <code>b[off]</code>, the
     * next one into <code>b[off+1]</code>, and so on. The number of bytes read
     * is, at most, equal to <code>len</code>. Let <i>k</i> be the number of
     * bytes actually read; these bytes will be stored in elements
     * <code>b[off]</code> through <code>b[off+</code><i>k</i><code>-1]</code>,
     * leaving elements <code>b[off+</code><i>k</i><code>]</code> through
     * <code>b[off+len-1]</code> unaffected.
     *
     * <p> In every case, elements <code>b[0]</code> through
     * <code>b[off]</code> and elements <code>b[off+len]</code> through
     * <code>b[b.length-1]</code> are unaffected.
     *
     * <p> If the first byte cannot be read for any reason other than end of
     * file, then an <code>IOException</code> is thrown. In particular, an
     * <code>IOException</code> is thrown if the input stream has been closed.
     *
     * @param      b     the buffer into which the data is read.
     * @param      off   the start offset in array <code>b</code>
     *                   at which the data is written.
     * @param      len   the maximum number of bytes to read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end of
     *             the stream has been reached.
     * @exception  IOException  if an I/O error occurs.
     */
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        if ((off < 0) || (len < 0) || (off + len > b.length)) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }

        long pos = readUntil(pointer + len);

        // len will always fit into an int so this is safe
        len = (int)Math.min(len, pos - pointer);
        if (len > 0) {
            cache.seek(pointer);
            cache.readFully(b, off, len);
            pointer += len;
            return len;
        } else {
            return -1;
        }
    }

    /**
     * Closes this stream and releases any system resources
     * associated with the stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException {
        super.close();
        cache.close();
        cacheFile.delete();
    }
}
