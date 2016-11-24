package com.gs.spider.model.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * ResultBundleBuilder
 *
 * @author Gao Shen
 * @version 16/2/27
 */
@Component
@Scope("singleton")
public class ResultBundleBuilder {
    private Logger LOG = LogManager.getLogger(ResultBundleBuilder.class);

    public <T> ResultBundle<T> bundle(String keyword, MySupplier<T> supplier) {
        ResultBundle<T> resultBundle;
        long start = System.currentTimeMillis();
        try {
            T t = supplier.get();
            resultBundle = new ResultBundle<>(t, keyword, System.currentTimeMillis() - start);
        } catch (Exception e) {
            resultBundle = new ResultBundle<>(keyword, System.currentTimeMillis() - start, false, e.getClass().getName() + ":" + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return resultBundle;
    }

    public <T> ResultListBundle<T> listBundle(String keyword, MySupplier<? extends Collection<T>> supplier) {
        ResultListBundle<T> resultBundle;
        long start = System.currentTimeMillis();
        try {
            Collection<T> t = supplier.get();
            resultBundle = new ResultListBundle<>(t, keyword, System.currentTimeMillis() - start);
        } catch (Exception e) {
            resultBundle = new ResultListBundle<>(keyword, System.currentTimeMillis() - start, false, e.getClass().getName() + ":" + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return resultBundle;
    }
}
