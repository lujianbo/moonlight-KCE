package com.lujianbo.app.kce;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jianbo on 2017/3/23.
 */
public class Config {

    public static Config readConfig(){
        Yaml yaml = new Yaml();
        return yaml.loadAs(Config.class.getClassLoader().getResourceAsStream("kce.yaml"),Config.class);
    }

    private String kafkaTopic;
    private String elasticIndex;
    private String elasticIndexType;
    private List<String> elasticTransportAddress;
    private Map<String,String> kafkaConsumer;
    private Map<String,String> elasticSetting;

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public String getElasticIndex() {
        return elasticIndex;
    }

    public void setElasticIndex(String elasticIndex) {
        this.elasticIndex = elasticIndex;
    }

    public String getElasticIndexType() {
        return elasticIndexType;
    }

    public void setElasticIndexType(String elasticIndexType) {
        this.elasticIndexType = elasticIndexType;
    }

    public List<String> getElasticTransportAddress() {
        return elasticTransportAddress;
    }

    public void setElasticTransportAddress(List<String> elasticTransportAddress) {
        this.elasticTransportAddress = elasticTransportAddress;
    }

    public Map<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void setKafkaConsumer(Map<String, String> kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    public Map<String, String> getElasticSetting() {
        return elasticSetting;
    }

    public void setElasticSetting(Map<String, String> elasticSetting) {
        this.elasticSetting = elasticSetting;
    }

    @Override
    public String toString() {
        return "Config{" +
                "kafkaTopic='" + kafkaTopic + '\'' +
                ", elasticIndex='" + elasticIndex + '\'' +
                ", elasticIndexType='" + elasticIndexType + '\'' +
                ", elasticTransportAddress=" + elasticTransportAddress +
                ", kafkaConsumer=" + kafkaConsumer +
                ", elasticSetting=" + elasticSetting +
                '}';
    }
}
