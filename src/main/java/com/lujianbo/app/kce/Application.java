package com.lujianbo.app.kce;


import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.net.UnknownHostException;
import java.util.Properties;
import java.util.stream.Stream;

public class Application {

    private final Config config;

    public Application(Config config){
        this.config=config;
    }

    public KafkaInput generateKafkaInput(){
        Properties kafkaConsumerProperties=new Properties();
        kafkaConsumerProperties.putAll(config.getKafkaConsumer());
        return new KafkaInput(new Properties(kafkaConsumerProperties),config.getKafkaTopic());
    }

    public ElasticOutput generateElasticOutput() throws UnknownHostException {
        return new ElasticOutput(config.getElasticIndex(),config.getElasticIndexType(),
                config.getElasticSetting(),config.getElasticTransportAddress());
    }


    public static void main(String[] args) {

        try {
            Application application=new Application(Config.readConfig());
            KafkaInput kafkaInput=application.generateKafkaInput();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
