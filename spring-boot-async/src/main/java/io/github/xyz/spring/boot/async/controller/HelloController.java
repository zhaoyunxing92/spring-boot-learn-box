package io.github.xyz.spring.boot.async.controller;

import io.github.xyz.spring.boot.async.service.HelloService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 至少说点啥吧
 *
 * @author zyx
 * @date 2021-10-20 15:09:19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/hello")
public class HelloController {

    private final HelloService helloService;

    @GetMapping
    @SneakyThrows
    public String echo() {
        helloService.echo();
        return "ok";
    }
}
