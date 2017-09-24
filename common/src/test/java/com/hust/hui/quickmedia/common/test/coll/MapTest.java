package com.hust.hui.quickmedia.common.test.coll;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 2017/9/24.
 */
public class MapTest {

    public static class Demo {
        public int num;

        public Demo(int num) {
            this.num = num;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Demo demo = (Demo) o;

            return num == demo.num;
        }

        @Override
        public int hashCode() {
            return num % 3 + 16;
        }
    }


    @Test
    public void testMapResize() {
        Map<Demo, Integer> map = new HashMap<>();
        for(int i = 1; i < 12; i++) {
            map.put(new Demo(i), i);
        }

        map.put(new Demo(12), 12);
        map.put(new Demo(13), 13);
    }

}
