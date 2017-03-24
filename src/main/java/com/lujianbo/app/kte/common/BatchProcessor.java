package com.lujianbo.app.kte.common;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by jianbo on 2017/3/23.
 */
public class BatchProcessor<I, O> {

    private ExecutorService producerThread;
    private ExecutorService consumerThread;

    private List<ConsumerWorker> consumerWorkers;
    private ProducerWorker producerWorker;

    private int threadCount;
    private int maxBatchSize;
    private TaskFactory<I, O> taskFactory;

    private int index = 0;

    public BatchProcessor(TaskFactory<I, O> taskFactory) throws Exception {
        this.taskFactory = taskFactory;
        this.threadCount = taskFactory.workThreadNumber();
        this.maxBatchSize = taskFactory.maxBatchSize();
        init();
    }

    public void init() throws Exception {
        this.producerWorker = new ProducerWorker(taskFactory.generateProducer(), taskFactory.generateFunction());
        consumerWorkers = new ArrayList<>(threadCount);
        for (int i = 0; i < threadCount; i++) {
            consumerWorkers.add(new ConsumerWorker(maxBatchSize, taskFactory.generateConsumer()));
        }
    }

    public void start() throws Exception {
        producerThread = Executors.newSingleThreadExecutor();
        consumerThread = Executors.newFixedThreadPool(threadCount);
        producerThread.execute(producerWorker);
        for (ConsumerWorker consumerWorker : consumerWorkers) {
            consumerThread.submit(consumerWorker);
        }
    }

    public void stop() {
        this.producerWorker.close();
        consumerWorkers.forEach(ConsumerWorker::close);
        this.producerThread.shutdownNow();
        this.consumerThread.shutdownNow();
    }

    private void dispatch(O output) {
        index = (index + 1) % consumerWorkers.size();
        try {
            consumerWorkers.get(index).put(output);
        } catch (InterruptedException e) {

        }
    }

    private class ProducerWorker implements Runnable {

        private Supplier<I> supplier;

        private Function<I, O> function;

        private boolean isRunning = true;

        public ProducerWorker(Supplier<I> supplier, Function<I, O> function) {
            this.supplier = supplier;
            this.function = function;
        }

        @Override
        public void run() {
            while (isRunning) {
                try {
                    I input = supplier.get();
                    O output = function.apply(input);
                    dispatch(output);
                } catch (Exception e) {
                    break;
                }
            }
        }

        public void close() {
            isRunning = false;
        }
    }

    private class ConsumerWorker implements Runnable {

        private BatchQueue<O> queue;

        private Consumer<Collection<O>> consumer;

        private boolean isRunning = true;

        public ConsumerWorker(int size, Consumer<Collection<O>> consumer) {
            this.queue = new BatchQueue<>(size);
            this.consumer = consumer;
        }

        public void put(O object) throws InterruptedException {
            this.queue.put(object);
        }

        @Override
        public void run() {
            while (isRunning) {
                try {
                    Collection<O> collection = queue.fetchQueueForRead();
                    if (collection.isEmpty()) {
                        Thread.sleep(1000);
                    } else {
                        consumer.accept(collection);
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }

        public void close() {
            this.isRunning = false;
        }
    }
}
