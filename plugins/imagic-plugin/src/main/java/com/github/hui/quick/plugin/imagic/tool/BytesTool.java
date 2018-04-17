package com.github.hui.quick.plugin.imagic.tool;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by yihui on 2018/4/17.
 */
public class BytesTool {

    public static byte[] file2bytes(String filename) {
        BufferedInputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(filename));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int size = 0;
            byte[] temp = new byte[1024];
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            in.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
