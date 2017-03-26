package com.lujianbo.app.kte.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jianbo on 2017/3/23.
 */
public class BatchQueue<T> {

    private final int maxSize;

    private AtomicReference<ArrayList<T>> readerQueue;

    private ReentrantLock lock = new ReentrantLock();

    private Condition waitWrite = lock.newCondition();

    public BatchQueue(int maxSize) {
        this.maxSize = maxSize;
        this.readerQueue = new AtomicReference<>(new ArrayList<T>(maxSize));
    }

    /**
     * 非线程安全操作
     * */
    public boolean put(T object){
        if (readerQueue.get().size() >= maxSize) {
            return false;
        }
        return readerQueue.get().add(object);
    }

    public Collection<T> fetchQueueForRead() {
        ArrayList<T> readQueue = readerQueue.getAndSet(new ArrayList<T>(maxSize));
        waitWrite.signalAll();
        return readQueue;
    }
}
