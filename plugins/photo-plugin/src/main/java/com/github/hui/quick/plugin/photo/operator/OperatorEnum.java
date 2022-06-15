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
        @Override
        public <T> OperateOptions<T> create(T t) {
            return new SketchOperator.SketchOperateOptions<>(t);
        }
    },
    ;

    public abstract <T> OperateOptions<T> create(T t);
}
