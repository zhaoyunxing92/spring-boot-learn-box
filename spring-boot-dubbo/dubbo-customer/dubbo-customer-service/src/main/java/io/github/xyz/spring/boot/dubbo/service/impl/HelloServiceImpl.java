package io.github.xyz.spring.boot.dubbo.service.impl;

import io.github.xyz.spring.boot.dubbo.service.HelloService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

/**
 * @author zhaoyunxing
 * @date 2020/11/10 19:07
 */
@DubboService
public class HelloServiceImpl implements HelloService {

    @Override
    public String echo() {

        return "hello " + RpcContext.getContext().getRemoteApplicationName();
    }
}
