package com.lujianbo.app.kce;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by jianbo on 2017/3/23.
 */
public class ReadWriteDoubleQueue<T> {

    private int maxSize;

    private AtomicReference<ConcurrentLinkedQueue<T>> readerQueue=new AtomicReference<>(new ConcurrentLinkedQueue<T>());

    public ReadWriteDoubleQueue(int maxSize){

    }

    public void put(T object){
        readerQueue.get().add(object);
    }

    public ConcurrentLinkedQueue<T> fetchQueueForWrite(){
        return readerQueue.getAndSet(new ConcurrentLinkedQueue<T>());
    }


}
