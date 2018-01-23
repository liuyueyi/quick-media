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
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * An <code>OutputStream</code> which can seek to an arbitrary offset.
 *
 * @version $Id$
 */
public class SeekableOutputStream extends OutputStream {

    private RandomAccessFile file;

    /**
     * Constructs a <code>SeekableOutputStream</code> from a
     * <code>RandomAccessFile</code>.  Unless otherwise indicated,
     * all method invocations are fowarded to the underlying
     * <code>RandomAccessFile</code>.
     *
     * @param file The <code>RandomAccessFile</code> to which calls
     *             will be forwarded.
     * @exception IllegalArgumentException if <code>file</code> is
     *            <code>null</code>.
     */
    public SeekableOutputStream(RandomAccessFile file) {
        if(file == null) {
            throw new IllegalArgumentException("SeekableOutputStream0");
        }
        this.file = file;
    }

    public void write(int b) throws IOException {
        file.write(b);
    }

    public void write(byte[] b) throws IOException {
        file.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        file.write(b, off, len);
    }

    /**
     * Invokes <code>getFD().sync()</code> on the underlying
     * <code>RandomAccessFile</code>.
     */
    public void flush() throws IOException {
        file.getFD().sync();
    }

    public void close() throws IOException {
        file.close();
    }

    public long getFilePointer() throws IOException {
        return file.getFilePointer();
    }

    public void seek(long pos) throws IOException {
        file.seek(pos);
    }
}
