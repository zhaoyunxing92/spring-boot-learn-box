/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.sentinel.apollo.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoyunxing
 * @date: 2019-05-05 11:22
 * @des:
 */
@RestController
@RequestMapping
public class HelloController {

    @GetMapping("/hello")
    @SentinelResource(value = "/hello")
    public String hello() {
        return "hello sentinel";
    }
}
