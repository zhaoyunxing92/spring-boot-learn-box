package io.github.xyz.spring.boot.dubbo.annotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhaoyunxing
 * @date 2020/11/10 16:44
 */
@SpringBootApplication
public class DubboConsumer {
    public static void main(String[] args) {
        SpringApplication.run(DubboConsumer.class, args);
    }
}
