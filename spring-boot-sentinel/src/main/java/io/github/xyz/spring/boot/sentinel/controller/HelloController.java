/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.sentinel.controller;

import io.github.xyz.spring.boot.sentinel.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author zhaoyunxing
 * @date: 2019-05-05 11:22
 * @des:
 */
@RestController
@RequestMapping("/hello")
public class HelloController {
    private final HelloService helloService;

    @Autowired
    public HelloController(HelloService helloService) {this.helloService = helloService;}

    @GetMapping
    public String say(@RequestParam(required = false, defaultValue = "10") long time) {
        return helloService.say(time);
    }
}
