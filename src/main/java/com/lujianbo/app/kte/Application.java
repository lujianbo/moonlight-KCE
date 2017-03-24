package com.lujianbo.app.kte;

import com.lujianbo.app.kte.common.BatchProcessor;
import com.lujianbo.app.kte.impl.Config;
import com.lujianbo.app.kte.impl.KTETaskFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class Application {

    public static void main(String[] args) {
        try {
            KTETaskFactory kteTaskFactory = new KTETaskFactory(Config.readConfig());
            BatchProcessor<ConsumerRecord<byte[], byte[]>, byte[]> batchProcessor = new BatchProcessor<>(kteTaskFactory);
            batchProcessor.start();
            Runtime.getRuntime().addShutdownHook(new Thread(batchProcessor::stop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
