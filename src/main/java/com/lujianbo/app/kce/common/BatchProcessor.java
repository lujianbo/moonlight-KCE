package com.lujianbo.app.kce.common;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by jianbo on 2017/3/23.
 */
public class BatchProcessor<T> {

    private ExecutorService producerThread;

    private ExecutorService consumerThread;

    private Supplier<T> supplier;

    private final List<ConsumerWorker> consumerWorkers;

    private int index=0;

    private BatchProcessor(int threadCount,int maxBatchSize){
        consumerWorkers =new ArrayList<>(threadCount);
        for (int i=0;i<threadCount;i++){
            consumerWorkers.add(new ConsumerWorker(maxBatchSize));
        }
        init();
    }

    private void init(){

        producerThread=Executors.newSingleThreadExecutor();
        consumerThread= Executors.newFixedThreadPool(consumerWorkers.size());
        //init producer
        producerThread.execute(() -> {
            while (true){
                try {
                    T object=supplier.get();
                    index=(index+1)%consumerWorkers.size();
                    consumerWorkers.get(index).put(object);
                }catch (Exception e){
                    break;
                }
            }
        });
        //init consumer
        for (ConsumerWorker consumerWorker : consumerWorkers) {
            consumerThread.submit(consumerWorker);
        }
    }


    private class ConsumerWorker implements Runnable{

        private ReadWriteQueue<T> queue;

        private Consumer<Collection<T>> consumer;

        public ConsumerWorker(int size){
            queue=new ReadWriteQueue<T>(size);
        }

        public void put(T object) throws InterruptedException {
            this.queue.put(object);
        }

        @Override
        public void run() {
            while (true){
                try {
                    Collection<T> collection=queue.fetchQueueForRead();
                    if (collection.isEmpty()){
                        Thread.sleep(1000);
                    }else {
                        consumer.accept(collection);
                    }
                }catch (Exception e){
                    break;
                }
            }
        }
    }
}
