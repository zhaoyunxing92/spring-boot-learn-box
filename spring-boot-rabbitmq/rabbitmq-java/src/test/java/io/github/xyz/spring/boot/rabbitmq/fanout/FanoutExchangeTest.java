/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rabbitmq.fanout;

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
 * @date: 2019-07-15 11:19
 * @des: 扇型交换机
 */
@Slf4j
public class FanoutExchangeTest {
    private Connection connection;
    private Channel channel;

    private String exchangeName = "fanout_exchange";

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
        // 4.声明exchange
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true);
        // 5.声明队列
        String queueName = "fanout_test";
        channel.queueDeclare(queueName, false, false, false, null);
        // 6.绑定 TODO: 绑定的时候没有设置路由键（routing key）
        channel.queueBind(queueName, exchangeName, "");

        log.info("[*] waiting for msg");
        //回调
        DeliverCallback callback = (consumerTag, delivery) -> log.info(new String(delivery.getBody(), StandardCharsets.UTF_8));

        //5.创建消费者
        channel.basicConsume(queueName, true, callback, cancelled -> log.info("[*]callback when the consumer is cancelled。{}", cancelled));
        // 防止线程退出
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 生产者
     */
    @Test
    public void product() throws IOException {
        log.info("[*] product msg");
        //4.发送消息 TODO:没有指定路由键直接发送消息
        String msg = "hello fanout exchange";
        channel.basicPublish(exchangeName, "", null, msg.getBytes());
    }
}
