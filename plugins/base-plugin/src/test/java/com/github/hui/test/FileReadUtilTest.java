package com.github.hui.test;

import com.github.hui.quick.plugin.base.FileReadUtil;
import org.junit.Test;

import java.io.IOException;

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

}
