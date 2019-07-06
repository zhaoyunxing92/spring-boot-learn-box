package io.github.xyz.spring.boot.data.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zhaoyunxing
 * @date: 2019-06-28 14:23
 * @des:
 */
@SpringBootApplication
public class SpringDataElasticsearchServer {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataElasticsearchServer.class, args);
    }

    @Bean
    public TransportClient transportClient() throws UnknownHostException {
        return new PreBuiltXPackTransportClient(Settings.builder()
                .put("cluster.name", "elasticsearch")
                .put("xpack.security.user", "elastic:123456")
                .build())
                .addTransportAddress(new TransportAddress(InetAddress.getByName("172.26.104.209"), 9300));
    }
}
