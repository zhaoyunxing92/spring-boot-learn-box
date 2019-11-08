/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.dubbo.seata.wangwu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhaoyunxing
 * @date: 2019-11-08 10:03
 */
@SpringBootApplication
public class WangwuServer {
    public static void main(String[] args) {
        //System.setProperty("seata.config.name","registry");
        SpringApplication.run(WangwuServer.class, args);
    }
}
