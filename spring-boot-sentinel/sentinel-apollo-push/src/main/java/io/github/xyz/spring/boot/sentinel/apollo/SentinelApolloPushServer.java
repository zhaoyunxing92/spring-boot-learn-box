/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.sentinel.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhaoyunxing
 * @date: 2019-05-10 21:03
 * @des:
 */
@SpringBootApplication
@EnableApolloConfig
public class SentinelApolloPushServer {
    public static void main(String[] args) {
        SpringApplication.run(SentinelApolloPushServer.class, args);
    }
}
