package com.lujianbo.app.kte.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * Created by jianbo on 2017/3/23.
 */
public class KafkaInput implements Supplier<ConsumerRecord<byte[], byte[]>> {

    private final Properties properties;
    private final String topic;
    private KafkaConsumer<byte[], byte[]> consumer;
    private boolean isAutoCommit;

    private Iterator<ConsumerRecord<byte[], byte[]>> iterator;

    public KafkaInput(Properties props, String topic) {
        this.properties = props;
        this.topic = topic;
        init();
    }

    private void init() {
        this.consumer = new KafkaConsumer<>(properties);
        this.consumer.subscribe(Collections.singletonList(topic));
        isAutoCommit = "true".equals(properties.getProperty("enable.auto.commit"));
    }


    @Override
    public ConsumerRecord<byte[], byte[]> get() {
        ConsumerRecord<byte[], byte[]> result = null;
        while (result == null) {
            if (this.iterator != null && iterator.hasNext()) {
                result = iterator.next();
            } else {
                if (!isAutoCommit) {
                    consumer.commitAsync();
                }
                this.iterator = consumer.poll(1000).iterator();
            }
        }
        return result;
    }
}
