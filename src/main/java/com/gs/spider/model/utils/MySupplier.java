package com.gs.spider.model.utils;

/**
 * MySupplier
 *
 * @author Gao Shen
 * @version 16/2/27
 */
@FunctionalInterface
public interface MySupplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws Exception;
}
