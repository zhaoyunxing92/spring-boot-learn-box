/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.hystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author zhaoyunxing
 * @date: 2019-04-30 16:39
 * @des:
 */
@RestController
@RequestMapping("/hello")
@Slf4j
public class HelloController {

    @GetMapping
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "50")
    }, fallbackMethod = "fallbackMethod")
    public String say(@RequestParam(required = false, defaultValue = "10") long time) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(time);
        log.info("{} sleep time {}毫秒", Thread.currentThread().getName(), time);
        return "hello hystrix";
    }

    public String fallbackMethod(long time) {
        log.info("{} fallback ,sleep time {}毫秒", Thread.currentThread().getName(), time);
        return "fallback, hystrix intervention";
    }
}
