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

import java.io.Serializable;

/**
 * A class representing the fields of a PNG suggested palette entry.
 *
 * <p><b> This class is not a committed part of the JAI API.  It may
 * be removed or changed in future releases of JAI.</b>
 *
 * @version $Id$
 */
public class PNGSuggestedPaletteEntry implements Serializable {

    /** The name of the entry. */
    public String name;

    /** The depth of the color samples. */
    public int sampleDepth;

    /** The red color value of the entry. */
    public int red;

    /** The green color value of the entry. */
    public int green;

    /** The blue color value of the entry. */
    public int blue;

    /** The alpha opacity value of the entry. */
    public int alpha;

    /** The probable frequency of the color in the image. */
    public int frequency;
}
