package com.lujianbo.app.kce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * Created by jianbo on 2017/3/23.
 */
public class KafkaInput implements Closeable{

    private KafkaConsumer<byte[], byte[]> consumer;

    private final Properties properties;

    private final String topic;

    private  boolean isAutoCommit;

    public KafkaInput(Properties props, String topic) {
        this.properties = props;
        this.topic = topic;
        init();
    }

    private void init() {
        this.consumer = new KafkaConsumer<>(properties);
        this.consumer.subscribe(Collections.singletonList(topic));
        isAutoCommit="true".equals(properties.getProperty("enable.auto.commit"));
    }

    public void fetch(int timeout, Consumer<List<ConsumerRecord<byte[], byte[]>>> handler) {
        ConsumerRecords<byte[], byte[]> records = consumer.poll(timeout);
        for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<byte[], byte[]>> partitionRecords = records.records(partition);
            handler.accept(partitionRecords);
            long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
            consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
        }
        if (!isAutoCommit) {
            consumer.commitAsync();
        }
    }


    private void reConnect() {
        this.consumer.close();
        init();
    }

    @Override
    public void close() throws IOException {

    }
}
