package com.github.hui.quick.plugin.imagic;


import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import com.github.hui.quick.plugin.imagic.base.ImgBaseOperate;
import com.github.hui.quick.plugin.imagic.exception.ImgOperateException;
import com.github.hui.quick.plugin.imagic.tool.BytesTool;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 图片转换的操作类 (裁剪+旋转+伸缩+水印+边框+)
 * <p/>
 * Created by yihui on 16/11/2.
 */
public class ImgWrapper {
    /**
     * 根据本地图片进行处理
     *
     * @param file
     * @return
     */
    public static Builder<String> of(String file) {
        checkForNull(file, "Cannot specify null for input file.");
        if (file.startsWith("http")) {
            throw new IllegalArgumentException("file should not be URI resources! file: " + file);
        }
        return Builder.ofString(file);
    }

    public static Builder<URI> of(URI uri) {
        checkForNull(uri, "Cannot specify null for input uri.");
        return Builder.ofUrl(uri);
    }

    public static Builder<InputStream> of(InputStream inputStream) {
        checkForNull(inputStream, "Cannot specify null for InputStream.");
        return Builder.ofStream(inputStream);
    }


    private static void checkForNull(Object o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
    }


    public static class Builder<T> {
        private T sourceFile;

        /**
         * 图片类型 JPEG, PNG, GIF ...
         * <p>
         * 默认为jpg图片
         */
        private String outputFormat = "jpg";

        private List<Operate> operates = new ArrayList<>();

        public Builder(T sourceFile) {
            this.sourceFile = sourceFile;
        }


        private static Builder<String> ofString(String str) {
            return new Builder<String>(ImgWrapper.class.getClassLoader().getResource(str).getFile());
        }


        private static Builder<URI> ofUrl(URI url) {
            return new Builder<URI>(url);
        }

        private static Builder<InputStream> ofStream(InputStream stream) {
            return new Builder<InputStream>(stream);
        }


        /**
         * 设置输出的文件格式
         *
         * @param format
         * @return
         */
        public Builder<T> setOutputFormat(String format) {
            this.outputFormat = format;
            return this;
        }


        private void updateOutputFormat(String originType) {
            if (this.outputFormat != null || originType == null) {
                return;
            }

            int index = originType.lastIndexOf(".");
            if (index <= 0) {
                return;
            }
            this.outputFormat = originType.substring(index + 1);
        }

        /**
         * 缩放
         *
         * @param width
         * @param height
         * @return
         */
        public Builder<T> scale(Integer width, Integer height, Integer quality) {
            return scale(width, height, quality, false);
        }


        public Builder<T> scale(Integer width, Integer height, Integer quality, boolean forceScale) {
            Operate operate = new Operate();
            operate.setOperateType(OperateType.SCALE);
            operate.setWidth(width);
            operate.setHeight(height);
            operate.setQuality(quality);
            operate.setForceScale(forceScale);
            operates.add(operate);
            return this;
        }

        /**
         * 按照比例进行缩放
         *
         * @param radio 1.0 表示不缩放, 0.5 缩放为一半
         * @return
         */
        public Builder<T> scale(Double radio, Integer quality) {
            Operate operate = new Operate();
            operate.setOperateType(OperateType.SCALE);
            operate.setRadio(radio);
            operate.setQuality(quality);
            operates.add(operate);
            return this;
        }


        /**
         * 裁剪
         *
         * @param x
         * @param y
         * @param width
         * @param height
         * @return
         */
        public Builder<T> crop(int x, int y, int width, int height) {
            Operate operate = new Operate();
            operate.setOperateType(OperateType.CROP);
            operate.setWidth(width);
            operate.setHeight(height);
            operate.setX(x);
            operate.setY(y);
            operates.add(operate);
            return this;
        }


        /**
         * 旋转
         *
         * @param rotate
         * @return
         */
        public Builder<T> rotate(double rotate) {
            Operate operate = new Operate();
            operate.setOperateType(OperateType.ROTATE);
            operate.setRotate(rotate);
            operates.add(operate);
            return this;
        }

        /**
         * 上下翻转
         *
         * @return
         */
        public Builder<T> flip() {
            Operate operate = new Operate();
            operate.setOperateType(OperateType.FLIP);
            operates.add(operate);
            return this;
        }

        /**
         * 左右翻转,即镜像
         *
         * @return
         */
        public Builder<T> flop() {
            Operate operate = new Operate();
            operate.setOperateType(OperateType.FLOP);
            operates.add(operate);
            return this;
        }

        /**
         * 添加边框
         *
         * @param width  边框的宽
         * @param height 边框的高
         * @param color  边框的填充色
         * @return
         */
        public Builder<T> board(Integer width, Integer height, String color) {
            Operate args = new Operate();
            args.setOperateType(OperateType.BOARD);
            args.setWidth(width);
            args.setHeight(height);
            args.setColor(color);
            operates.add(args);
            return this;
        }

        /**
         * 添加水印
         *
         * @param water 水印的源图片 (默认为png格式)
         * @param x     添加到目标图片的x坐标
         * @param y     添加到目标图片的y坐标
         * @param <U>
         * @return
         */
        public <U> Builder<T> water(U water, int x, int y) {
            return water(water, "png", x, y);
        }

        /**
         * 添加水印
         *
         * @param water
         * @param imgType 水印图片的类型; 当传入的为inputStream时, 此参数才有意义
         * @param x
         * @param y
         * @param <U>
         * @return
         */
        public <U> Builder<T> water(U water, String imgType, int x, int y) {
            Operate<U> operate = new Operate<>();
            operate.setOperateType(OperateType.WATER);
            operate.setX(x);
            operate.setY(y);
            operate.setWater(water);
            operate.setWaterImgType(imgType);
            operates.add(operate);
            return this;
        }


        /**
         * 执行图片处理, 并保存文件为: 源文件_out.jpg （类型由输出的图片类型决定）
         *
         * @return 保存的文件名
         * @throws Exception
         */
        public String toFile() throws Exception {
            return toFile(null);
        }


        /**
         * 执行图片处理,并将结果保存为指定文件名的file
         *
         * @param outputFilename 若为null, 则输出文件为 源文件_out.jpg 这种格式
         * @return
         * @throws Exception
         */
        public String toFile(String outputFilename) throws Exception {
            if (CollectionUtils.isEmpty(operates)) {
                throw new ImgOperateException("operates null!");
            }

            /**
             * 获取原始的图片信息， 并构建输出文件名
             *  1. 远程图片，则保存到临时目录下
             *  2. stream， 保存到临时目录下
             *  3. 本地文件
             *
             * 输出文件都放在临时文件夹内，和原文件同名，加一个_out进行区分
             **/
            FileWriteUtil.FileInfo sourceFile = createFile();
            if (outputFilename == null) {
                outputFilename = FileWriteUtil.getTmpPath() + "/"
                        + sourceFile.getFilename() + "_"
                        + System.currentTimeMillis() + "_out." + outputFormat;
            }

            /** 执行图片的操作 */
            if (ImgBaseOperate.operate(operates, sourceFile.getAbsFile(), outputFilename)) {
                return outputFilename;
            } else {
                return null;
            }
        }

        /**
         * 执行图片操作,并输出字节流
         *
         * @return
         * @throws Exception
         */
        public InputStream asStream() throws Exception {
            if (CollectionUtils.isEmpty(operates)) {
                throw new ImgOperateException("operate null!");
            }

            String outputFilename = this.toFile();
            if (StringUtils.isBlank(outputFilename)) {
                return null;
            }

            return new FileInputStream(new File(outputFilename));
        }


        public byte[] asBytes() throws Exception {
            if (CollectionUtils.isEmpty(operates)) {
                throw new ImgOperateException("operate null!");
            }

            String outputFilename = this.toFile();
            if (StringUtils.isBlank(outputFilename)) {
                return null;
            }


            return BytesTool.file2bytes(outputFilename);
        }


        public BufferedImage asImg() throws Exception {
            if (CollectionUtils.isEmpty(operates)) {
                throw new ImgOperateException("operate null!");
            }

            String outputFilename = this.toFile();
            if (StringUtils.isBlank(outputFilename)) {
                return null;
            }

            return ImageIO.read(new File(outputFilename));
        }


        private FileWriteUtil.FileInfo createFile() throws Exception {
            if (this.sourceFile instanceof String) {
                /** 生成的文件在源文件目录下 */
                updateOutputFormat((String) this.sourceFile);
            } else if (this.sourceFile instanceof URI) {
                /** 源文件和生成的文件都保存在临时目录下 */
                String urlPath = ((URI) this.sourceFile).getPath();
                updateOutputFormat(urlPath);
            }

            return FileWriteUtil.saveFile(this.sourceFile, outputFormat);
        }


        public static class Operate<T> {
            /**
             * 操作类型
             */
            private OperateType operateType;

            /**
             * 裁剪宽; 缩放宽
             */
            private Integer width;
            /**
             * 高
             */
            private Integer height;
            /**
             * 裁剪时,起始 x
             */
            private Integer x;
            /**
             * 裁剪时,起始y
             */
            private Integer y;
            /**
             * 旋转角度
             */
            private Double rotate;

            /**
             * 按照整体的缩放参数, 1 表示不变, 和裁剪一起使用
             */
            private Double radio;

            /**
             * 图片精度, 1 - 100
             */
            private Integer quality;

            /**
             * 颜色 (添加边框中的颜色; 去除图片中某颜色)
             */
            private String color;

            /**
             * 水印图片, 可以为图片名, uri, 或者inputstream
             */
            private T water;

            /**
             * 水印图片的类型
             */
            private String waterImgType;

            /**
             * 强制按照给定的参数进行压缩
             */
            private boolean forceScale;


            public boolean valid() {
                switch (operateType) {
                    case CROP:
                        return width != null && height != null && x != null && y != null;
                    case SCALE:
                        return width != null || height != null || radio != null;
                    case ROTATE:
                        return rotate != null;
                    case WATER:
                        // 暂时不支持水印操作
                        return water != null;
                    case BOARD:
                        if (width == null) {
                            width = 3;
                        }
                        if (height == null) {
                            height = 3;
                        }
                        if (color == null) {
                            color = "#ffffff";
                        }
                    case FLIP:
                    case FLOP:
                        return true;
                    default:
                        return false;
                }
            }

            /**
             * 获取水印图片的路径
             *
             * @return
             */
            public String getWaterFilename() throws ImgOperateException {
                try {
                    return FileWriteUtil.saveFile(water, waterImgType).getAbsFile();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public OperateType getOperateType() {
                return operateType;
            }

            public void setOperateType(OperateType operateType) {
                this.operateType = operateType;
            }

            public Integer getWidth() {
                return width;
            }

            public void setWidth(Integer width) {
                this.width = width;
            }

            public Integer getHeight() {
                return height;
            }

            public void setHeight(Integer height) {
                this.height = height;
            }

            public Integer getX() {
                return x;
            }

            public void setX(Integer x) {
                this.x = x;
            }

            public Integer getY() {
                return y;
            }

            public void setY(Integer y) {
                this.y = y;
            }

            public Double getRotate() {
                return rotate;
            }

            public void setRotate(Double rotate) {
                this.rotate = rotate;
            }

            public Double getRadio() {
                return radio;
            }

            public void setRadio(Double radio) {
                this.radio = radio;
            }

            public Integer getQuality() {
                return quality;
            }

            public void setQuality(Integer quality) {
                this.quality = quality;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public T getWater() {
                return water;
            }

            public void setWater(T water) {
                this.water = water;
            }

            public String getWaterImgType() {
                return waterImgType;
            }

            public void setWaterImgType(String waterImgType) {
                this.waterImgType = waterImgType;
            }

            public boolean isForceScale() {
                return forceScale;
            }

            public void setForceScale(boolean forceScale) {
                this.forceScale = forceScale;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                Operate<?> operate = (Operate<?>) o;
                return forceScale == operate.forceScale && operateType == operate.operateType &&
                        Objects.equals(width, operate.width) && Objects.equals(height, operate.height) &&
                        Objects.equals(x, operate.x) && Objects.equals(y, operate.y) &&
                        Objects.equals(rotate, operate.rotate) && Objects.equals(radio, operate.radio) &&
                        Objects.equals(quality, operate.quality) && Objects.equals(color, operate.color) &&
                        Objects.equals(water, operate.water) && Objects.equals(waterImgType, operate.waterImgType);
            }

            @Override
            public int hashCode() {

                return Objects
                        .hash(operateType, width, height, x, y, rotate, radio, quality, color, water, waterImgType,
                                forceScale);
            }

            @Override
            public String toString() {
                return "Operate{" + "operateType=" + operateType + ", width=" + width + ", height=" + height + ", x=" +
                        x + ", y=" + y + ", rotate=" + rotate + ", radio=" + radio + ", quality=" + quality +
                        ", color='" + color + '\'' + ", water=" + water + ", waterImgType='" + waterImgType + '\'' +
                        ", forceScale=" + forceScale + '}';
            }
        }


        public enum OperateType {
            /**
             * 裁剪
             */
            CROP,
            /**
             * 缩放
             */
            SCALE,
            /**
             * 旋转
             */
            ROTATE,
            /**
             * 水印
             */
            WATER,

            /**
             * 上下翻转
             */
            FLIP,

            /**
             * 水平翻转
             */
            FLOP,
            /**
             * 添加边框
             */
            BOARD;
        }
    }
}
