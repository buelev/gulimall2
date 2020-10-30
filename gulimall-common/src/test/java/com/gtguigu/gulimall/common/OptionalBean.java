package com.gtguigu.gulimall.common;

import java.util.Objects;
import java.util.function.Function;

/**
 * description: OptionalBean
 * date: 2020-10-28 09:40
 * author: buelev
 * version: 1.0
 */
public class OptionalBean<T> {
    private static final OptionalBean<?> EMPTY = new OptionalBean<>();
    private final T value;

    private OptionalBean() {
        this.value = null;
    }

    /**
     * 有空会抛出异常
     *
     * @param value
     */
    private OptionalBean(T value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * 包装一个不能为空的Bean
     */
    public static <T> OptionalBean<T> of(T value) {
        return new OptionalBean<>(value);
    }

    /**
     * 包装一个可以为空的Bean
     */
    public static <T> OptionalBean<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    /**
     * 取出具体的值
     *
     * @return
     */
    public T get() {
        return Objects.isNull(value) ? null : value;
    }

    public <R> OptionalBean<R> getBean(Function<? super T, ? extends R> fn) {
        return Objects.isNull(value) ? OptionalBean.empty() : OptionalBean.ofNullable(fn.apply(value));
    }

    private static <T> OptionalBean<T> empty() {
        return null;
    }
}
