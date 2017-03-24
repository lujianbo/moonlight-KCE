package com.lujianbo.app.kce.common;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jianbo on 2017/3/23.
 */
public class ReadWriteQueue<T> {

    private final int maxSize;

    private AtomicReference<ConcurrentLinkedQueue<T>> readerQueue = new AtomicReference<>(new ConcurrentLinkedQueue<T>());

    private ReentrantLock lock = new ReentrantLock();

    private Condition waitWrite = lock.newCondition();

    public ReadWriteQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public void put(T object) throws InterruptedException {
        if (readerQueue.get().size() >= maxSize) {
            waitWrite.await();
        }
        readerQueue.get().add(object);
    }

    public Collection<T> fetchQueueForRead() {
        ConcurrentLinkedQueue<T> readQueue = readerQueue.getAndSet(new ConcurrentLinkedQueue<T>());
        waitWrite.signalAll();
        return readQueue;
    }
}
