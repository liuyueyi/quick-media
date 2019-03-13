package com.github.hui.quick.plugin.qrcode.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Created by @author yihui in 19:31 19/3/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DotSize {
    public static final DotSize SIZE_1_1 = new DotSize(1, 1);
    public static final DotSize SIZE_2_1 = new DotSize(2, 1);
    public static final DotSize SIZE_1_2 = new DotSize(1, 2);
    public static final DotSize SIZE_2_2 = new DotSize(2, 2);

    private int row;
    private int col;

    public int size() {
        return row * col;
    }

    public static DotSize create(int row, int col) {
        if (row == 1 && col == 1) {
            return SIZE_1_1;
        } else if (row == 2 && col == 1) {
            return SIZE_2_1;
        } else if (row == 1 && col == 2) {
            return SIZE_1_2;
        } else if (row == 2 && col == 2) {
            return SIZE_2_2;
        } else {
            return new DotSize(row, col);
        }
    }
}