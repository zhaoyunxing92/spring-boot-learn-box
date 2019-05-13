/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.sentinel.controller;

import io.github.xyz.spring.boot.sentinel.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/another/{name}")
    public String say(@PathVariable String name) {
        helloService.test();
        return helloService.helloAnother(name);
    }
}
