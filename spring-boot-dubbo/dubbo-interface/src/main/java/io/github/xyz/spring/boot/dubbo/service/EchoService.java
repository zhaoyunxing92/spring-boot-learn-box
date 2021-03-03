package io.github.xyz.spring.boot.dubbo.service;

/**
 * @author zhaoyunxing
 * @date 2021/3/3 15:08
 */
public interface EchoService {
    /**
     * echo
     *
     * @param msg 消息
     * @return 字符串
     */
    String echo(String msg);
}
