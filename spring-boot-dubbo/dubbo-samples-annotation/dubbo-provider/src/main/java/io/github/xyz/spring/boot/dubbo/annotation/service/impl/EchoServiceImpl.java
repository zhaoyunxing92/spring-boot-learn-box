package io.github.xyz.spring.boot.dubbo.annotation.service.impl;

import io.github.xyz.spring.boot.dubbo.service.EchoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

/**
 * @author zhaoyunxing
 * @date 2021/3/3 15:22
 */
@Slf4j
@DubboService
public class EchoServiceImpl implements EchoService {
    /**
     * echo
     *
     * @param msg 消息
     * @return 字符串
     */
    @Override
    public String echo(String msg) {
        log.info("Hello {} request from consumer {}", msg, RpcContext.getContext().getRemoteAddress());
        return msg;
    }
}
