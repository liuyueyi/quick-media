package com.github.hui.quick.plugin.qrcode.v3.options.qr;

import com.google.zxing.qrcode.encoder.ByteMatrix;

/**
 * @author
 * @date 2022/6/11
 */
public enum QrDetectPosition {
    LEFT(true, 0) {
        @Override
        public int startX(ByteMatrix matrix) {
            return 0;
        }

        @Override
        public int startY(ByteMatrix matrix) {
            return 0;
        }

        @Override
        public int endX(ByteMatrix matrix) {
            return detectSize(matrix) - 1;
        }

        @Override
        public int endY(ByteMatrix matrix) {
            return detectSize(matrix) - 1;
        }
    },
    RIGHT(true, 1) {
        @Override
        public int startX(ByteMatrix matrix) {
            return matrix.getWidth() - detectSize(matrix);
        }

        @Override
        public int startY(ByteMatrix matrix) {
            return 0;
        }

        @Override
        public int endX(ByteMatrix matrix) {
            return matrix.getWidth() - 1;
        }

        @Override
        public int endY(ByteMatrix matrix) {
            return detectSize(matrix) - 1;
        }
    },
    BOTTOM(true, 2) {
        @Override
        public int startX(ByteMatrix matrix) {
            return 0;
        }

        @Override
        public int startY(ByteMatrix matrix) {
            return matrix.getHeight() - detectSize(matrix);
        }

        @Override
        public int endX(ByteMatrix matrix) {
            return detectSize(matrix) - 1;
        }

        @Override
        public int endY(ByteMatrix matrix) {
            return matrix.getHeight() - 1;
        }
    },
    NONE(false, 3) {
        @Override
        public int startX(ByteMatrix matrix) {
            return 0;
        }

        @Override
        public int startY(ByteMatrix matrix) {
            return 0;
        }

        @Override
        public int endX(ByteMatrix matrix) {
            return 0;
        }

        @Override
        public int endY(ByteMatrix matrix) {
            return 0;
        }
    };

    private boolean detect;
    private int position;

    QrDetectPosition(boolean detect, int position) {
        this.position = position;
        this.detect = detect;
    }

    public int getPosition() {
        return position;
    }

    public boolean isDetect() {
        return detect;
    }

    public abstract int startX(ByteMatrix matrix);

    public abstract int startY(ByteMatrix matrix);

    public abstract int endX(ByteMatrix matrix);

    public abstract int endY(ByteMatrix matrix);

    public static int detectSize(ByteMatrix matrix) {
        return matrix.get(0, 5) == 1 ? 7 : 5;
    }
}
