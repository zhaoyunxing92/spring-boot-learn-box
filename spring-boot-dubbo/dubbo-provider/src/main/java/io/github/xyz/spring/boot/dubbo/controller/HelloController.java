package io.github.xyz.spring.boot.dubbo.controller;

import io.github.xyz.spring.boot.dubbo.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoyunxing
 * @date 2020/11/10 19:15
 */
@Slf4j
@RestController
public class HelloController {

    @DubboReference
    private HelloService helloService;

    @GetMapping("/echo")
    public String echo() {
        RpcContext context = RpcContext.getContext();
        context.setAttachment("hello", "provider");

        String echo = helloService.echo();
        String name = context.getAttachment("name");

        log.info("name:{}", name);
        return echo;
    }
}
