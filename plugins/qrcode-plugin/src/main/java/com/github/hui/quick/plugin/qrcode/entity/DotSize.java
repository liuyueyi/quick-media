package com.github.hui.quick.plugin.qrcode.entity;

import java.util.Objects;

/**
 * Created by @author yihui in 19:31 19/3/12.
 */
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

    public DotSize() {
    }

    public DotSize(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DotSize dotSize = (DotSize) o;
        return row == dotSize.row && col == dotSize.col;
    }

    @Override
    public int hashCode() {

        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "DotSize{" + "row=" + row + ", col=" + col + '}';
    }
}