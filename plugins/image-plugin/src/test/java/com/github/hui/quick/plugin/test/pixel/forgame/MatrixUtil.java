package com.github.hui.quick.plugin.test.pixel.forgame;

/**
 * @author YiHui
 * @date 2025/2/5
 */
public class MatrixUtil {

    /**
     * 上下翻转矩阵
     *
     * @param matrix
     * @param <T>
     * @return
     */
    public static <T> T[][] flip(T[][] matrix) {
        int col = matrix.length;
        int row = matrix[0].length;

        for (int x = 0; x < col; x++) {
            for (int y = 0; y < row / 2; y++) {
                T temp = matrix[x][y];
                matrix[x][y] = matrix[x][row - y - 1];
                matrix[x][row - y - 1] = temp;
            }
        }
        return matrix;
    }
}
