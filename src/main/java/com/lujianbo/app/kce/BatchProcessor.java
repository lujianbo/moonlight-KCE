package com.lujianbo.app.kce;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * Created by jianbo on 2017/3/23.
 */
public class BatchProcessor<T> {

    private int maxSize;

    private Consumer<Collection<T>> consumer;

    private ConcurrentLinkedQueue<T> items1=new ConcurrentLinkedQueue<T>();

    private ConcurrentLinkedQueue<T> items2=new ConcurrentLinkedQueue<T>();

    private AtomicReference<ConcurrentLinkedQueue<T>> readerQueue=new AtomicReference<>();

    private AtomicReference<ConcurrentLinkedQueue<T>> writerQueue=new AtomicReference<>();

    private ReentrantLock readLock=new ReentrantLock();

    private ReentrantLock writeLock=new ReentrantLock();

    public BatchProcessor(int maxSize, Consumer<Collection<T>> consumer){

    }

    private void queueSwitch(){

    }

    public void put(T object){

    }

    public void run(){
        while (true){

        }


    }

}
