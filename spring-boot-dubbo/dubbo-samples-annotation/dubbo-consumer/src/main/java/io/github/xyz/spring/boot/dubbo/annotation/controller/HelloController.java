package io.github.xyz.spring.boot.dubbo.annotation.controller;

import io.github.xyz.spring.boot.dubbo.service.EchoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
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
    private EchoService echoService;

    @GetMapping("/echo")
    public String echo() {
        RpcContext context = RpcContext.getContext();
        context.setAttachment("hello", "provider");

        String echo = echoService.echo("dubbo");
        String name = context.getAttachment("name");

        log.info("name:{}", name);
        return echo;
    }
}
