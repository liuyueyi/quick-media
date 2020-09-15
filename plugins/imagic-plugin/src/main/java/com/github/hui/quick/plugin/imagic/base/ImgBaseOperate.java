package com.github.hui.quick.plugin.imagic.base;

import com.github.hui.quick.plugin.imagic.ImgWrapper;
import com.github.hui.quick.plugin.imagic.exception.ImgOperateException;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by yihui on 18/04/17.
 */
public class ImgBaseOperate {

    private static final Logger log = LoggerFactory.getLogger(ImgBaseOperate.class);

    /**
     * 执行图片的复合操作
     *
     * @param operates
     * @param sourceFilename 原始图片名
     * @param outputFilename 生成图片名
     * @return
     * @throws ImgOperateException
     */
    public static boolean operate(List<ImgWrapper.Builder.Operate> operates, String sourceFilename, String outputFilename) throws ImgOperateException {
        try {
            IMOperation op = new IMOperation();
            boolean operateTag = false;
            String waterFilename = null;
            for (ImgWrapper.Builder.Operate operate : operates) {
                if (!operate.valid()) {
                    continue;
                }

                if (operate.getOperateType() == ImgWrapper.Builder.OperateType.CROP) {
                    op.crop(operate.getWidth(), operate.getHeight(), operate.getX(), operate.getY());
//                    if (operate.getRadio() != null && Math.abs(operate.getRadio() - 1.0) > 0.005) {
//                        // 需要对图片进行缩放
//                        op.resize((int) Math.ceil(operate.getWidth() * operate.getRadio()));
//                    }
                    operateTag = true;
                } else if (operate.getOperateType() == ImgWrapper.Builder.OperateType.ROTATE) {
                    // fixme 180度旋转后裁图,会出现bug, 先这么兼容
                    double rotate = operate.getRotate();
                    if (Math.abs((rotate % 360) - 180) <= 0.005) {
                        rotate += 0.01;
                    }
                    op.rotate(rotate);
                    operateTag = true;
                } else if (operate.getOperateType() == ImgWrapper.Builder.OperateType.SCALE) {
                    if (operate.getRadio() == null) {
                        if (operate.isForceScale()) { // 强制根据给定的参数进行压缩时
                            StringBuilder builder = new StringBuilder();
                            builder.append("!").append(operate.getWidth() == null ? "" : operate.getWidth()).append("x");
                            builder.append(operate.getHeight() == null ? "" : operate.getHeight());
                            op.addRawArgs("-resize", builder.toString());
                        } else {
                            op.resize(operate.getWidth(), operate.getHeight());
                        }
                    } else if(Math.abs(operate.getRadio() - 1) > 0.005) {
                        // 对图片进行比例缩放
                        op.addRawArgs("-resize", "%" + (operate.getRadio() * 100));
                    }

                    if (operate.getQuality() != null && operate.getQuality() > 0) {
                        op.quality(operate.getQuality().doubleValue());
                    }
                    operateTag = true;
                } else if (operate.getOperateType() == ImgWrapper.Builder.OperateType.FLIP) {
                    op.flip();
                    operateTag = true;
                } else if (operate.getOperateType() == ImgWrapper.Builder.OperateType.FLOP) {
                    op.flop();
                    operateTag = true;
                } else if (operate.getOperateType() == ImgWrapper.Builder.OperateType.WATER && waterFilename == null) {
                    // 当前只支持添加一次水印
                    op.geometry(operate.getWidth(), operate.getHeight(), operate.getX(), operate.getY())
                            .composite();
                    waterFilename = operate.getWaterFilename();
                    operateTag = true;
                } else if (operate.getOperateType() == ImgWrapper.Builder.OperateType.BOARD) {
                    op.border(operate.getWidth(), operate.getHeight()).bordercolor(operate.getColor());
                    operateTag = true;
                }
            }

            if (!operateTag) {
                throw new ImgOperateException("operate illegal! operates: " + operates);
            }
            op.addImage(sourceFilename);
            if (waterFilename != null) {
                op.addImage(waterFilename);
            }
            op.addImage(outputFilename);
            /** 传true到构造函数中,则表示使用GraphicMagic, 裁图时,图片大小会变 */
            ConvertCmd convert = new ConvertCmd();
            convert.run(op);
        } catch (IOException e) {
            log.error("file read error!, e: {}", e);
            return false;
        } catch (InterruptedException e) {
            log.error("interrupt exception! e: {}", e);
            return false;
        } catch (IM4JavaException e) {
            log.error("im4java exception! e: {}", e);
            return false;
        }
        return true;
    }

}
