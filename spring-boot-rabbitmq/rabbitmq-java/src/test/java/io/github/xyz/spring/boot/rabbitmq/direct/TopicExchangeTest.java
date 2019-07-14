/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rabbitmq.direct;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author zhaoyunxing
 * @date: 2019-07-14 21:51
 * @des: topic模式
 */
@Slf4j
public class TopicExchangeTest {
    private Connection connection;
    private Channel channel;

    private String exchangeName = "topic_exchange";

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
    public void consumer() throws IOException {
        // 4.声明exchange
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true);
        // 5.声明队列
        String queueName = "topic_test";
        channel.queueDeclare(queueName, true, true, true, null);
        // 6.绑定
        /**
         * 1.`*` 只匹配一个词，可以理解为只有一个占位符,例如：`test.*`,只能匹配到`test.direct`却匹配不到`test.direct.sunny`
         * 2.`#` 可以匹配多个词，可以理解为有多个占位符，例如：`test.#`,就可以匹配`test.direct`和`test.direct.sunny`
         */
        channel.queueBind(queueName, exchangeName, "test.#");

        log.info("[*] waiting for msg");
        //回调
        DeliverCallback callback = (consumerTag, delivery) -> log.info(new String(delivery.getBody(), StandardCharsets.UTF_8));

        while (true) {
            //5.创建消费者
            channel.basicConsume(queueName, true, callback, cancelled -> log.info("[*]callback when the consumer is cancelled。{}", cancelled));
        }
    }

    /**
     * 生产者
     */
    @Test
    public void product() throws IOException {
        log.info("[*] product msg");
        //4.发送消息
        String routingKey1 = "test.abc";
        String routingKey2 = "test.abc.efg";
        String routingKey3 = "acb.test.abc";
        channel.basicPublish(exchangeName, routingKey1, null, routingKey1.getBytes());
        channel.basicPublish(exchangeName, routingKey2, null, routingKey2.getBytes());
        channel.basicPublish(exchangeName, routingKey3, null, routingKey3.getBytes());
    }

}
