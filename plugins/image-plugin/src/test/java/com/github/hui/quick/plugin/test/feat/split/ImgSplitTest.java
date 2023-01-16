package com.github.hui.quick.plugin.test.feat.split;

import com.github.hui.quick.plugin.image.wrapper.split.ImgSplitWrapper;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author YiHui
 * @date 2023/1/12
 */
public class ImgSplitTest {

    @Test
    public void testSplit() throws IOException {
        String path = "D:\\quick-media\\resource\\assets\\bundle_item_icon\\img";
        File file = new File(path);
        int i = 1;
        for (File f : file.listFiles()) {
            if (f.getName().endsWith("png")) {
                String name = f.getAbsolutePath();
                System.out.println("process: " + i + "_" + name);
                String out = name.substring(name.indexOf(".") + 1, name.lastIndexOf("."));
                ++i;

                try {
                    ImgSplitWrapper.build().setImg(name)
                            .setOutputPath("d:/quick-media/items/" + out)
                            .setOutputImgName(out)
                            .setOutputIncrIndex(1).build()
                            .splitAndSave();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("over");
    }

    @Test
    public void split() throws IOException {
        try {
            String img = "D:\\quick-media\\resource\\assets\\bundle_item_icon\\img\\2766150e-9c1a-4025-b652-5b0d6b7b3fa9.e792b.png";
            ImgSplitWrapper.build().setImg(img)
                    .setOutputPath("d:/quick-media/items/e792b")
                    .setOutputImgName("e792b")
                    .setOutputIncrIndex(1).build()
                    .splitAndSave();
            System.out.println("over");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
