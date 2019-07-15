package io.github.xyz.spring.boot.rabbitmq.direct;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

/**
 * @author zhaoyunxing
 * @date: 2019-07-14 19:55
 * @des: direct（直连模式）测试
 */
@Slf4j
public class DirectExchangeTest {

    private Connection connection;
    private Channel channel;

    private String queueName = "direct_test";

    @Before
    public void setUp() throws Exception {
        //1.创建连接工程
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setPassword("123456");
        factory.setUsername("guest");
        /*虚拟机节点*/
        factory.setVirtualHost("/");
        //2.创建连接
        connection = factory.newConnection();

        //3.获取channel
        channel = connection.createChannel();
    }

    @After
    public void setDown() throws IOException, TimeoutException {
        log.info("[*] close channel");
        //5.关闭通道
        channel.close();
        connection.close();
    }

    /**
     * 消费者
     */
    @Test
    public void consumer() throws IOException, InterruptedException {
        String exchangeName = "direct_exchange";
        // 4.声明exchange
        //channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true);

        /**
         * 5.声明队列
         * @param queue 队列名称
         * @param durable 是否持久，即使服务重启
         * @param exclusive 是否只有这个队列消费,true后消费者停止了这个队列也就自动删除
         * @param autoDelete 不使用的时候自动删除
         * @param arguments 其他参数
         */
        channel.queueDeclare(queueName, false, true, true, null);

        // 6.绑定
        // channel.queueBind(queueName, exchangeName, "test.direct");

        log.info("[*] waiting for msg");
        //回调
        DeliverCallback callback = (consumerTag, delivery) -> {
            log.info(new String(delivery.getBody(), StandardCharsets.UTF_8));
            Envelope envelope = delivery.getEnvelope();
            log.info("[*] routing key is 【{}】", envelope.getRoutingKey());
            log.info("[*] delivery tag is 【{}】", envelope.getDeliveryTag());
            log.info("[*] exchange is 【{}】", envelope.getExchange());
        };

        //5.创建消费者
        channel.basicConsume("", true, callback, cancelled -> log.info("[*]callback when the consumer is cancelled。{}", cancelled));
        // 防止线程退出
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 生产者
     */
    @Test
    public void product() throws IOException {
        log.info("[*] product msg");
        //4.发送消息
        String msg = "hello rabbitmq";
        /**
         * @param exchange 交换机
         * @param routingKey 路由键
         * @param props other properties for the message - routing headers etc
         * @param body 消息体
         */
        channel.basicPublish("", queueName, null, msg.getBytes());
    }
}