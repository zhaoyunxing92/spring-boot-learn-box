/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.kafka.controller;

import io.github.xyz.spring.boot.kafka.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoyunxing
 * @date: 2019-04-28 10:54
 * @des:
 */
@RestController
@RequestMapping("/")
public class KafkaController {

    private final KafkaService kafkaService;

    @Autowired
    public KafkaController(KafkaService kafkaService) {this.kafkaService = kafkaService;}

    @GetMapping("/send")
    public String send() {
        //log.info("kafka config: {}", kafkaAdmin.getConfig());

        // 判断

        //        kafkaAdmin.
        //        AdminClient.create(kafkaAdmin.getConfig()).createTopics(() -> {
        //        });


        return kafkaService.send();
    }
}
