package com.github.hui.quick.plugin.photo.operator;

import com.github.hui.quick.plugin.photo.options.OperateOptions;

/**
 * @author yihui
 * @date 2022/6/14
 */
public enum OperatorEnum {
    /**
     * 素描
     */
    SKETCH() {
        public <T> SketchOperator.SketchOperateOptions<T> create(T t) {
            return new SketchOperator.SketchOperateOptions<>(t);
        }
    },
    /**
     * 图片边缘检测
     */
    EDGE() {
        public <T> EdgeOperator.EdgeOperateOptions<T> create(T t) {
            return new EdgeOperator.EdgeOperateOptions<>(t);
        }
    };

    public abstract <T> OperateOptions<T> create(T t);
}
