package com.github.hui.quick.plugin.test.pixel.forgame.draw;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import com.github.hui.quick.plugin.image.wrapper.pixel.ImgPixelWrapper;
import com.github.hui.quick.plugin.image.wrapper.pixel.model.PixelStyleEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 根据原图 + 线图，生成填色的图片
 * <p>
 * - 像素块填色
 * - 涂色
 *
 * @author YiHui
 * @date 2025/2/5
 */
public class ImgToDrawRect {

    @Test
    public void toPixelImg() {
        String img = "D:\\quick-media\\draw\\doraemon.jpg";

    }

    public static class MatrixConf {
        Map<String, String> numColorMap;
        Map<String, String> borderColorMap;
        List<List<String>> matrix;
    }


    private static List<List<String>> formatMatrix(List<String> lines) {
        List<List<String>> res = new ArrayList<>();
        for (String sub : lines) {
            if (sub.contains(",")) {
                // 包含分割符的场景
                String[] cs = StringUtils.split(sub, ",");
                List<String> line = new ArrayList<>();
                for (int i = 0; i < cs.length; i++) {
                    line.add(cs[i]);
                }
                res.add(line);
            } else {
                // 不包含分隔符的场景
                List<String> line = new ArrayList<>();
                for (int i = 0; i < sub.length(); i++) {
                    line.add(sub.charAt(i) + "");
                }
                res.add(line);
            }
        }
        return res;
    }

    /**
     * 预览输出
     *
     * @param s
     */
    private static String preview(List<String> s) {
        System.out.println("------- 分割 -------");
        StringBuilder ans = new StringBuilder();
        System.out.println("[");
        ans.append("[\n");
        for (String t : s) {
            ans.append("\t[");
            if (t.contains(",")) {
                String[] cs = StringUtils.split(t, ",");
                for (int i = 0; i < cs.length; i++) {
                    String ch = cs[i];
                    ans.append("\"").append(ch).append("\"");
                    if (i != t.length() - 1) {
                        ans.append(",");
                    }
                }
            } else {
                // 无分隔符场景
                for (int i = 0; i < t.length(); i++) {
                    ans.append("\"").append(t.charAt(i)).append("\"");
                    if (i != t.length() - 1) {
                        ans.append(",");
                    }
                }
            }

            ans.append("],\n");
        }
        ans.append("]\n");
        return ans.toString();
    }

    private static String print(List<List<String>> matrix) {
        StringBuilder builder = new StringBuilder();
        builder.append("[\n");
        for (List<String> strings : matrix) {
            builder.append("\t[");
            for (int i = 0; i < strings.size(); i++) {
                String item = strings.get(i);
                if (NumberUtils.isDigits(item)) {
                    builder.append(item);
                } else {
                    builder.append("\"").append(item).append("\"");
                }
                if (i != strings.size() - 1) {
                    builder.append(",");
                }
            }
            builder.append("],\n");
        }
        builder.append("]\n");
        return builder.toString();
    }

    /**
     * 线图转矩阵
     *
     * @param path
     * @param file
     * @param blockSize
     * @return
     */
    public static List<String> xianTuToStr(MatrixConf conf, String path, String file, int blockSize) {
        if (!path.endsWith("/")) path += "/";
        String img = path + file;
        ImgPixelWrapper wrapper = ImgPixelWrapper.build().setSourceImg(img)
                .setBlockSize(blockSize)
                .setFontSize(30)
                .setChars("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                .setBgChar('0')
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
                        return rc.getRed() >= 150 && rc.getGreen() >= 150 && rc.getBlue() >= 150;
                    }
                })
                .build();

        ImmutablePair<List<String>, Map<String, Color>> charsWithColor = wrapper.asCharsWithColor().get(0);

        Map<String, String> colorMap = new HashMap<>();
        for (Map.Entry<String, Color> entry : charsWithColor.getRight().entrySet()) {
            colorMap.put(entry.getKey(), ColorUtil.color2htmlColor(entry.getValue()));
        }
        conf.borderColorMap = colorMap;
        System.out.println("选中的blockSize = " + blockSize + " 颜色数：" + colorMap.size());
        return charsWithColor.getLeft();
    }


    public List<String> sourceImgToStr(MatrixConf conf, String path, String file, int blockSize, float threshold) {
        if (!path.endsWith("/")) path += "/";
        String img = path + file;
        ImgPixelWrapper wrapper = ImgPixelWrapper.build().setSourceImg(img)
                .setBlockSize(blockSize)
                .setFontSize(30)
                .setChars("")
                .setCharSeparate(',')
                .setSameColorThreshold(threshold)
                .setBgChar('0')
                .setPixelType(PixelStyleEnum.CHAR_COLOR)
                .setBgPredicate(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer color) {
                        if (color == 0) {
                            return true;
                        }

                        Color rc = ColorUtil.int2color(color);
                        return rc.getAlpha() < 10;
                    }
                })
                .build();

        ImmutablePair<List<String>, Map<String, Color>> charsWithColor = wrapper.asCharsWithColor().get(0);

        // 打印字体颜色
        Map<String, String> colorMap = new HashMap<>();
        for (Map.Entry<String, Color> entry : charsWithColor.getRight().entrySet()) {
            colorMap.put(entry.getKey(), ColorUtil.color2htmlColor(entry.getValue()));
        }
        conf.numColorMap = colorMap;
        System.out.println("颜色数: " + colorMap.size());
        return charsWithColor.getLeft();
    }
//
//    /**
//     * 将线图转二维矩阵输出
//     */
//    @Test
//    public void borderImgToMatrix() throws FileNotFoundException {
//        // 这个表示将线图，转换为二维矩阵
//        String path = "D://quick-media/pixel-in/";
//        String file = "x_512.jpg";
//        List<String> s = xianTuToStr(path, file, 3);
//
//
//        String ans = preview(s);
//        String content = "export const sampleMatrix = \n" + ans;
//        FileWriteUtil.FileInfo fileInfo = new FileWriteUtil.FileInfo();
//        fileInfo.setPath("D:\\tmp\\trae\\src\\config");
//        fileInfo.setFilename("matrixConfig");
//        fileInfo.setFileType("ts");
//        FileWriteUtil.saveContent(fileInfo, content);
//    }

    private static List<List<String>> mergeMatrix(List<List<String>> xtMatrix, List<List<String>> ytMatrix, String noLineTag) {
        List<List<String>> ans = new ArrayList<>();
        for (int i = 0; i < xtMatrix.size(); i++) {
            List<String> line = xtMatrix.get(i);

            List<String> target = new ArrayList<>(line.size());
            for (int j = 0; j < line.size(); j++) {
                String item = line.get(j);
                if (item.equals(noLineTag)) {
                    // 非线条时，使用原图的矩阵信息进行填充
                    target.add(ytMatrix.get(i).get(j));
                } else {
                    target.add(item);
                }
            }
            ans.add(target);
        }
        return ans;
    }

    public static String toPrettyFormat(MatrixConf json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    @Test
    public void genRenderMatrix() throws Exception {
        // 这个表示将线图，转换为二维矩阵
        String path = "D://quick-media/pixel-in/";
        int blockSize = 3;
        String file = "x_pink.jpg";
        float threshold = 8f;

        MatrixConf conf = new MatrixConf();

        List<String> x_s = xianTuToStr(conf, path, file, blockSize);
        List<List<String>> s = formatMatrix(x_s);

        // 接下来我们将原图，也转换为二维矩阵
        String origin = "o_pink.jpg";
        List<String> o_o = sourceImgToStr(conf, path, origin, blockSize, threshold);
        List<List<String>> o = formatMatrix(o_o);

        // 然后进行两个矩阵的合并，边框使用线图的，内容则使用原图的进行填充
        List<List<String>> ans = mergeMatrix(s, o, "0");
        conf.matrix = ans;

        System.out.println("输出矩阵大小: " + ans.size() + " * " + ans.get(0).size());

        // 保存文件
        String content = toPrettyFormat(conf);
        FileWriteUtil.saveContent(new File("D:\\tmp\\trae\\src\\config\\config_2.json"), content);
        System.out.println("文件保存成功");
    }

    /**
     * 线图颜色补全
     */
    @Test
    public void renderImgBorder() throws IOException {
        String path = "D://quick-media/pixel-in/";
        String xt = "x_512.jpg";
        String yt = "o_512.jpg";
        BufferedImage xtImg = ImageLoadUtil.getImageByPath(path + xt);
        BufferedImage ytImg = ImageLoadUtil.getImageByPath(path + yt);

        int thread = 225;

        // 输出的图片
        BufferedImage cImg = GraphicUtil.createImg(xtImg.getWidth(), xtImg.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < xtImg.getWidth(); i++) {
            for (int j = 0; j < xtImg.getHeight(); j++) {
                Color xtColor = new Color(xtImg.getRGB(i, j), true);
                if (xtColor.getAlpha() <= 10) {
                    // 背景色，过滤掉
                    cImg.setRGB(i, j, Color.WHITE.getRGB());
                } else if (xtColor.getRed() >= thread && xtColor.getGreen() >= thread && xtColor.getBlue() >= thread) {
                    // 背景色，过滤掉
                    cImg.setRGB(i, j, xtColor.getRGB());
                } else {
                    // 前置色
                    cImg.setRGB(i, j, ytImg.getRGB(i, j));
                }
            }
        }
        ImageIO.write(cImg, "jpg", new File(path + "c_512.jpg"));
    }
}
