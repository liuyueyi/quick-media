package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author YiHui
 * @date 2023/2/10
 */
public class BasicTest {

    @Test
    public void testSave() throws IOException {
        File file = new File("D:\\Workspace\\hui\\blogs\\hui-doc\\src\\spring\\web");
        for (File sub : file.listFiles()) {
            if (sub.isDirectory() && !sub.getName().equals("Request")) {
                for (File f : sub.listFiles()) {
                    if (f.isFile()) {
                        String contents = FileReadUtil.readAll(f.getAbsolutePath());
                        String newContent = parseContent(contents);
                        String fileName = f.getName();
                        int index = fileName.indexOf(".");
                        String newFileName = index == 2 ? fileName.substring(3) : fileName;
                        FileWriteUtil.saveContent(new File(sub.getAbsolutePath() + "/" + newFileName), newContent);
                    }
                }
            }
        }
    }

    private String parseContent(String contents) {
        String[] lines = StringUtils.splitByWholeSeparatorPreserveAllTokens(contents, "\n");
        StringBuilder result = new StringBuilder();
        int start = 0;
        for (String line : lines) {
            if (line.startsWith("---")) {
                start += 1;
            }

            if (start >= 2) {
                result.append(line).append("\n");
                continue;
            }

            if (line.startsWith("banner:") || line.startsWith("permalink:")) {
                continue;
            }

            if (line.startsWith("weight:")) {
                line = line.replace("weight:", "order:");
            } else if (line.startsWith("tags:")) {
                line = line.replace("tags:", "tag:");
            } else if (line.startsWith("categories:")) {
                line = line.replace("categories:", "category:");
            }

            result.append(line).append("\n");
        }
        return result.toString();
    }
}
