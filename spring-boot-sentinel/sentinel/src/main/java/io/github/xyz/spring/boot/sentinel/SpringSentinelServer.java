/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.sentinel;

import io.github.xyz.spring.boot.sentinel.config.SentinelConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author zhaoyunxing
 * @date: 2019-05-05 10:32
 * @des:
 */
@SpringBootApplication
@Import(SentinelConfig.class)
public class SpringSentinelServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringSentinelServer.class, args);
    }
}
