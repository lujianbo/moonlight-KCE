package com.lujianbo.app.kce;

import com.lujianbo.app.kce.common.BatchProcessor;
import com.lujianbo.app.kce.common.Config;
import com.lujianbo.app.kce.impl.KTETaskFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class Application {

    public static void main(String[] args) {
        try {
            KTETaskFactory kteTaskFactory=new KTETaskFactory(Config.readConfig());
            BatchProcessor<ConsumerRecord<byte[], byte[]>,byte[]> batchProcessor=new BatchProcessor<>(kteTaskFactory);
            batchProcessor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
