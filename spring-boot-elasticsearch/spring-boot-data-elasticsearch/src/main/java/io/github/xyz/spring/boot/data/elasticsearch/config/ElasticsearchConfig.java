package io.github.xyz.spring.boot.data.elasticsearch.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author zhaoyunxing
 * @date: 2019-07-07 09:47
 * @des: es 配置类
 * @see org.springframework.data.elasticsearch.client.TransportClientFactoryBean
 * @see org.springframework.data.elasticsearch.client.ClusterNodes
 */
@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchConfig {

    private final ElasticsearchProperties properties;

    public ElasticsearchConfig(ElasticsearchProperties properties) {
        this.properties = properties;
    }

    @Bean
    public TransportClient transportClient(){
        return new PreBuiltXPackTransportClient(settings())
                .addTransportAddresses(addresses());
    }

    /**
     * .put("client.transport.sniff", true)
     * .put("client.transport.ignore_cluster_name", false)
     * .put("client.transport.ping_timeout", clientPingTimeout)
     * .put("client.transport.nodes_sampler_interval", clientNodesSamplerInterval)
     *
     * @return Settings
     */
    private Settings settings() {
        Settings.Builder builder = Settings.builder();
        builder.put("cluster.name", properties.getClusterName());
        properties.getProperties().forEach(builder::put);
        return builder.build();
    }

    private TransportAddress[] addresses() {
        String clusterNodesStr = properties.getClusterNodes();
        Assert.hasText(clusterNodesStr, "Cluster nodes source must not be null or empty!");
        String[] nodes = StringUtils.delimitedListToStringArray(clusterNodesStr, ",");

        return Arrays.stream(nodes).map(node -> {
            String[] segments = StringUtils.delimitedListToStringArray(node, ":");
            Assert.isTrue(segments.length == 2,
                    () -> String.format("Invalid cluster node %s in %s! Must be in the format host:port!", node, clusterNodesStr));
            String host = segments[0].trim();
            String port = segments[1].trim();
            Assert.hasText(host, () -> String.format("No host name given cluster node %s!", node));
            Assert.hasText(port, () -> String.format("No port given in cluster node %s!", node));
            return new TransportAddress(toInetAddress(host), Integer.valueOf(port));
        }).toArray(TransportAddress[]::new);
    }

    private static InetAddress toInetAddress(String host) {
        try {
            return InetAddress.getByName(host);
        } catch (UnknownHostException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
