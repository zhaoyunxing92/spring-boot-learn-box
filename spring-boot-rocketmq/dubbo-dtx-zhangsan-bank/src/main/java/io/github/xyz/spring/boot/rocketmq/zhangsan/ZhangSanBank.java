/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.zhangsan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhaoyunxing
 * @date: 2019-10-24 00:00
 */
@SpringBootApplication
@MapperScan("io.github.xyz.spring.boot.rocketmq.zhangsan.mapper")
public class ZhangSanBank {
    public static void main(String[] args) {
        SpringApplication.run(ZhangSanBank.class, args);
    }
}
