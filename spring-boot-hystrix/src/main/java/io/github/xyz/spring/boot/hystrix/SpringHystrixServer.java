/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * @author zhaoyunxing
 * @date: 2019-04-30 16:29
 * @des:
 */
@SpringBootApplication
@EnableHystrix
public class SpringHystrixServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringHystrixServer.class, args);
    }
}
