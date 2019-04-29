/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.aliyun.kafka.service;

/**
 * @author zhaoyunxing
 * @date: 2019-04-28 14:28
 * @des:
 */
public interface KafkaService {
    String TOPIC = "aliyun_kafka_test";

    /**
     * 发送信息
     *
     * @return true or false
     */
    String send();
}
