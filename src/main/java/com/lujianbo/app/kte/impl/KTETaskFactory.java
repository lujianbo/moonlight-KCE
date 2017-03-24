package com.lujianbo.app.kte.impl;

import com.lujianbo.app.kte.common.TaskFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by jianbo on 2017/3/24.
 */
public class KTETaskFactory implements TaskFactory<ConsumerRecord<byte[], byte[]>, byte[]> {

    protected Config config;

    public KTETaskFactory(Config config) {
        this.config = config;
    }
    public int workThreadNumber() {
        return Integer.valueOf(config.getWorkerThreadNumber());
    }

    public int maxBatchSize() {
        return Integer.valueOf(config.getMaxBatchSize());
    }

    public Supplier<ConsumerRecord<byte[], byte[]>> generateProducer() {
        Properties kafkaConsumerProperties = new Properties();
        kafkaConsumerProperties.putAll(config.getKafkaConsumer());
        return new KafkaInput(new Properties(kafkaConsumerProperties), config.getKafkaTopic());
    }

    public Consumer<Collection<byte[]>> generateConsumer() throws UnknownHostException {
        return new ElasticOutput(config.getElasticIndex(), config.getElasticIndexType(),
                config.getElasticSetting(), config.getElasticTransportAddress());
    }

    public Function<ConsumerRecord<byte[], byte[]>, byte[]> generateFunction() {
        return ConsumerRecord::value;
    }
}
