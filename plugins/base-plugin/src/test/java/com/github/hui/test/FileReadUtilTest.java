package com.github.hui.test;

import com.github.hui.quick.plugin.base.FileReadUtil;
import com.github.hui.quick.plugin.base.ImageLoadUtil;
import com.github.hui.quick.plugin.base.gif.GifDecoder;
import com.github.hui.quick.plugin.base.gif.GifHelper;
import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by @author yihui in 19:38 18/9/10.
 */
public class FileReadUtilTest {

    @Test
    public void testGetAbsFile() throws IOException {
        try {
            // linux or mac os
            String fileName = "/tmp/ttt.txt";
            String content = FileReadUtil.readAll(fileName);
            System.out.println(content);


            fileName = "~/test.txt";
            content = FileReadUtil.readAll(fileName);
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFileMagic() {
        try {
            String file = "quick.jpg";

            String magicNum = FileReadUtil.getMagicNum(file);
            System.out.println("jpg magic: " + magicNum);

            magicNum = FileReadUtil.getMagicNum("quick.png");
            System.out.println("png magic: " + magicNum);

            magicNum = FileReadUtil.getMagicNum("quick.gif");
            System.out.println("gif magic: " + magicNum);

            magicNum = FileReadUtil.getMagicNum("quick.webp");
            System.out.println("webp magic: " + magicNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
