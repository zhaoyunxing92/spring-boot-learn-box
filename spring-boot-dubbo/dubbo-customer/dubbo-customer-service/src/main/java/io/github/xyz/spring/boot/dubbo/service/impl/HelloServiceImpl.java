package io.github.xyz.spring.boot.dubbo.service.impl;

import io.github.xyz.spring.boot.dubbo.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

/**
 * @author zhaoyunxing
 * @date 2020/11/10 19:07
 */
@Slf4j
@DubboService
public class HelloServiceImpl implements HelloService {

    @Override
    public String echo() {
        RpcContext context = RpcContext.getContext();
        String hello = context.getAttachment("hello");
        log.info("attachment:{}", hello);
        context.setAttachment("name", "dubbo");
        return "hello " + RpcContext.getContext().getRemoteApplicationName();
    }
}
