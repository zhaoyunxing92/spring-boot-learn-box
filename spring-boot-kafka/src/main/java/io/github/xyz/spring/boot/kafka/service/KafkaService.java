/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.kafka.service;

/**
 * @author zhaoyunxing
 * @date: 2019-04-28 14:28
 * @des:
 */
public interface KafkaService {
    String TOPIC = "spring.kafka.test";

    /**
     * 发送信息
     *
     * @return true or false
     */
    String send();
}
