package com.lujianbo.app.kte.common;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by jianbo on 2017/3/24.
 */
public interface  TaskFactory<I, O> {

    int workThreadNumber();

    int maxBatchSize();

    Supplier<I> generateProducer() throws Exception;

    Function<I, O> generateFunction() throws Exception;

    Consumer<Collection<O>> generateConsumer() throws Exception;
}
