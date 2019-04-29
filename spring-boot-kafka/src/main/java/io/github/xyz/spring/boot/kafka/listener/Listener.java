/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.kafka.listener;

import io.github.xyz.spring.boot.kafka.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author zhaoyunxing
 * @date: 2019-04-28 16:07
 * @des:
 */
@Slf4j
public class Listener {

    @KafkaListener(topics = {KafkaService.TOPIC})
    public void listen(ConsumerRecord<?, ?> record) {
        log.info("topic [{}] msg:{} ", record.topic(), record.value().toString());
    }
}
