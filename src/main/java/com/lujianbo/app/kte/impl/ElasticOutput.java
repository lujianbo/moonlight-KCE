package com.lujianbo.app.kte.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.Closeable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by jianbo on 2017/3/23.
 */
public class ElasticOutput implements Closeable, Consumer<Collection<byte[]>> {

    private TransportClient client;

    private String index;

    private String type;

    public ElasticOutput(String index, String type, Map<String, String> setting, List<String> addresses) throws UnknownHostException {
        this.index = index;
        this.type = type;
        Settings settings = Settings.builder()
                .put(setting)
                .build();
        PreBuiltTransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings);
        for (String address : addresses) {
            String host = StringUtils.substringBefore(address, ":");
            int port = Integer.parseInt(StringUtils.substringAfter(address, ":"));
            preBuiltTransportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
        }
        this.client = preBuiltTransportClient;
    }


    public void close() {
        this.client.close();
    }

    @Override
    public void accept(Collection<byte[]> sources) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (byte[] source : sources) {
            bulkRequest.add(client.prepareIndex(index, type).setSource(source));
        }
        BulkResponse bulkResponse = bulkRequest.get();
    }
}
