package com.github.hui.quick.plugin.test.pixel;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import com.github.hui.quick.plugin.image.wrapper.pixel.ImgPixelWrapper;
import com.github.hui.quick.plugin.image.wrapper.pixel.model.PixelStyleEnum;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author YiHui
 * @date 2025/1/10
 */
public class Icon2PixelTest {

    public static List<String> toStr(String path, String file, int blockSize) {
        while (true) {
            if (!path.endsWith("/")) path += "/";
            String img = path + file;
            ImgPixelWrapper wrapper = ImgPixelWrapper.build().setSourceImg(img)
                    .setBlockSize(blockSize)
                    .setFontSize(30)
                    .setChars("1")
                    .setBgChar(' ')
                    .setPixelType(PixelStyleEnum.BLACK_CHAR_BORDER)
                    .setBgPredicate(new Predicate<Integer>() {
                        @Override
                        public boolean test(Integer color) {
                            if (color == 0) {
                                return true;
                            }

                            Color rc = ColorUtil.int2color(color);
                            if (rc.getAlpha() < 10) {
                                // 透明的直接过滤掉
                                return true;
                            }
                            // 将白色当作背景色
                            return rc.getRed() >= 40 && rc.getGreen() >= 40 && rc.getBlue() >= 40;
                        }
                    })
                    .build();

            List<String> s = wrapper.asChars().get(0);
            List<String> out = new ArrayList<>();
            boolean first = true;
            for (String sub : s) {
                // 干掉开头的空白行
                if (StringUtils.isBlank(sub) && first) {
                    continue;
                }

                first = false;
                out.add(sub);
            }
            s = out;

            // 然后再干掉尾部的空白行
            for (int i = s.size() - 1; i >= 0; i--) {
                if (StringUtils.isBlank(s.get(i))) {
                    s.remove(i);
                } else {
                    break;
                }
            }

            int row = s.size();
            int col = s.get(0).length();
            if (row >= 120 || col >= 120) {
                blockSize += 1;
                continue;
            }
            return s;
        }

    }

    /**
     * 预览输出
     *
     * @param s
     */
    private static void preview(List<String> s) {
        for (String t : s) {
            System.out.println(t);
        }

        System.out.println("------- 分割 -------");
    }

    private static void saveToFile(String path, String file, List<String> s) throws FileNotFoundException {
        int row = s.size();
        int col = s.get(0).length();

        // 输出矩阵
        char[][] matrix = new char[col][row];
        for (int x = 0; x < col; x++) {
            for (int y = 0; y < row; y++) {
                char ch = s.get(s.size() - y - 1).charAt(x);
                matrix[x][y] = ch == ' ' ? '0' : ch;
            }
        }
        StringBuilder out = new StringBuilder("{\n");
        out.append("\t\"row\":").append(row).append(",\n");
        out.append("\t\"column\":").append(col).append(",\n");
        out.append("\t\"matrix\":[").append("\n");

        for (int x = 0; x < col; x++) {
            for (int y = 0; y < row; y++) {
                if (y == 0) out.append("\t\t[");
                out.append(matrix[x][y]);
                if (y != row - 1) out.append(",");
                else {
                    if (x == col - 1) out.append("]\n\t],\n"); // 最后一个
                    else out.append("],\n");
                }
            }
        }
        out.append("\t\"border\":").append("[1,1,1,1]\n").append("}");

        System.out.println("\n-------------\n");
        System.out.println(out);
        System.out.println("\n-----------\n");

        FileWriteUtil.FileInfo fileInfo = new FileWriteUtil.FileInfo();
        fileInfo.setPath(path);
        fileInfo.setFilename(file);
        fileInfo.setFileType("json");
        FileWriteUtil.saveContent(fileInfo, out.toString());
    }

    @Test
    public void testBorderOut() throws Exception {
        String absPath = "d://quick-media/pixel-in/";
        String in = "day";
        // icon 对应的是6
        int blockSize = 6;
        // 复杂图，对应大一点
        blockSize = 26;
        File file = new File(absPath + in);
        int index = 1;
        for (File f : file.listFiles()) {
            String fName = f.getName();
            System.out.println(">>>>>>" + fName);
            List<String> s = toStr(absPath + in, fName, blockSize);
            preview(s);
            saveToFile(absPath + "out", "lvl" + index, s);
            index += 1;
        }
    }
}
