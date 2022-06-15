package com.github.hui.quick.plugin.test.util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 边界检测
 *
 * @author yihui
 * @time 2022/05/31
 */
public class BorderDetectUtil {

    public static class Border<T> {
        private final int w, h;
        private T[][] border;

        public Border(T[][] border) {
            this.w = border.length;
            this.h = border[0].length;
            this.border = border;
        }

        public int getW() {
            return w;
        }

        public int getH() {
            return h;
        }

        public T values(int x, int y) {
            if (x < 0 || x >= w || y < 0 || y >= h) {
                return null;
            }

            return border[x][y];
        }

        public void clear(int x, int y) {
            border[x][y] = null;
        }

        public void clear(Point point) {
            clear(point.x, point.y);
        }

        public int point2key(int x, int y) {
            return x * h + y;
        }

        public int point2key(Point point) {
            return point2key(point.x, point.y);
        }

        public Point key2point(int key) {
            int y = key % h;
            int x = key / h;
            return new Point(x, y);
        }
    }

    public static List<List<Integer>> filterBorder(Border<Byte> border) {
        List<List<Integer>> borders = new ArrayList<>();
        for (int x = 0; x < border.getW(); x++) {
            for (int y = 0; y < border.getH(); y++) {
                if (!isBorderPoint(new Point(x, y), border)) {
                    continue;
                }


            }
        }
        return null;
    }

    private static List<List<Integer>> filterFromPoint(Point point, Border<Byte> border) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        Point next = nextOrderPoint(point, border);
        while (next != null) {
            int key = border.point2key(next);
            if (list.contains(key)) {
                // 闭合区间了，将这个闭合区间保存下来，并删除从起始点开始到下一个交叉点之间的线点
                int index = list.indexOf(key);
                List<Integer> borderPoints = new ArrayList<>(list.subList(index, list.size()));
                result.add(borderPoints);
                removeLinePointBetweenCrossPoint(borderPoints, border);

                //
                list = new ArrayList<>(list.subList(0, index));
            } else {
                list.add(key);
            }
        }
        return null;
    }

    /**
     * 移除 两个交叉点之间的 线点
     *
     * @return
     */
    private static void removeLinePointBetweenCrossPoint(List<Integer> list, Border<Byte> border) {
        for (int i = 1; i < list.size(); i++) {
            Point point = border.key2point(list.get(i));
            if (nextSinglePoint(point, border) != null) {
                border.clear(point);
            } else {
                return;
            }
        }

    }

    /**
     * 按照顺时针的顺序返回下一个点
     *
     * @return
     */
    private static Point nextOrderPoint(Point point, Border<Byte> border) {
        Point next = new Point(point.x, point.y - 1);
        if (isBorderPoint(next, border)) {
            return next;
        }

        next = new Point(point.x + 1, point.y - 1);
        if (isBorderPoint(next, border)) return next;

        next = new Point(point.x, point.y + 1);
        if (isBorderPoint(next, border)) return next;


        next = new Point(point.x + 1, point.y + 1);
        if (isBorderPoint(next, border)) return next;


        next = new Point(point.x, point.y + 1);
        if (isBorderPoint(next, border)) return next;


        next = new Point(point.x - 1, point.y + 1);
        if (isBorderPoint(next, border)) return next;


        next = new Point(point.x - 1, point.y);
        if (isBorderPoint(next, border)) return next;


        next = new Point(point.x - 1, point.y - 1);
        if (isBorderPoint(next, border)) return next;

        return null;
    }

    /**
     * 捞出所有的线段
     *
     * @param border
     * @return
     */
    public static List<List<Integer>> filterLine(Border<Byte> border) {
        List<List<Integer>> lines = new ArrayList<>();
        for (int x = 0; x < border.getW(); x++) {
            for (int y = 0; y < border.getH(); y++) {
                if (!isBorderPoint(new Point(x, y), border)) {
                    continue;
                }

                // 正好是边界
                List<Integer> line = lineFromPoint(x, y, border);
                if (line != null) {
                    lines.add(line);
                }
            }
        }

        return lines;
    }

    public static List<Integer> lineFromPoint(int x, int y, Border<Byte> border) {
        List<Integer> list = new ArrayList<>();
        Point point = new Point(x, y);
        Point next = nextSinglePoint(point, border);
        if (next == null) {
            return null;
        }

        while (next != null) {
            border.clear(point.x, point.y);
            list.add(border.point2key(point));
            point = next;
            next = nextSinglePoint(point, border);
        }
        return list;
    }

    /**
     * 从 point 这个点出发，找下一个点，下一个点要求它的四周，除了point之外，只有一个其他连接点
     * <p>
     * 线点：这个点的米字方向，只有两个点
     * 交叉点：这个点的米子方向，连接点 > 2
     *
     * @param point
     * @param border
     * @return
     */
    private static Point nextSinglePoint(Point point, Border<Byte> border) {
        int x = point.x, y = point.y;
        Point checkPoint = new Point(x - 1, y - 1);
        Point target = null;
        if (isBorderPoint(checkPoint, border)) {
            target = new Point(checkPoint);
        }

        checkPoint.setLocation(x - 1, y);
        if (isBorderPoint(checkPoint, border)) {
            if (target != null) {
                return null;
            }
            target = new Point(checkPoint);
        }

        checkPoint.setLocation(x - 1, y + 1);
        if (isBorderPoint(checkPoint, border)) {
            if (target != null) {
                return null;
            }
            target = new Point(checkPoint);
        }

        checkPoint.setLocation(x, y - 1);
        if (isBorderPoint(checkPoint, border)) {
            if (target != null) {
                return null;
            }
            target = new Point(checkPoint);
        }

        checkPoint.setLocation(x, y + 1);
        if (isBorderPoint(checkPoint, border)) {
            if (target != null) {
                return null;
            }
            target = new Point(checkPoint);
        }

        checkPoint.setLocation(x + 1, y - 1);
        if (isBorderPoint(checkPoint, border)) {
            if (target != null) {
                return null;
            }
            target = new Point(checkPoint);
        }

        checkPoint.setLocation(x + 1, y);
        if (isBorderPoint(checkPoint, border)) {
            if (target != null) {
                return null;
            }
            target = new Point(checkPoint);
        }


        checkPoint.setLocation(x + 1, y + 1);
        if (isBorderPoint(checkPoint, border)) {
            if (target != null) {
                return null;
            }
            target = new Point(checkPoint);
        }
        return target;
    }

    private static boolean isBorderPoint(Point point, Border<Byte> border) {
        Byte b = border.values(point.x, point.y);
        return b != null && b == 1;
    }
}
