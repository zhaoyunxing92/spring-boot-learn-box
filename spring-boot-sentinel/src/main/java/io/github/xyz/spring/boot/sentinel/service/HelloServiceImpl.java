/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.sentinel.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.github.xyz.spring.boot.sentinel.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhaoyunxing
 * @date: 2019-05-05 14:02
 * @des:
 */
@Service
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    @SentinelResource(value = "say", fallback = "sayFallback")
    public String say(long arg) {
        if (arg > 10) {
            throw new IllegalArgumentException("invalid arg");
        }
        return String.format("hello sentinel at %d", arg);
    }

    /**
     * 对应的 `handleException` 函数需要位于 `ExceptionUtil` 类中，并且必须为 static 函数.
     */
    @Override
    @SentinelResource(value = "test", blockHandler = "handleException", blockHandlerClass = {ExceptionUtil.class})
    public void test() {
        log.info("test blockHandler");
    }

    @Override
    @SentinelResource(value = "helloAnother", defaultFallback = "defaultFallback", exceptionsToIgnore = {IllegalStateException.class})
    public String helloAnother(String name) {
        if (name == null || "bad".equals(name)) {
            throw new IllegalArgumentException("oops");
        }
        if ("foo".equals(name)) {
            throw new IllegalStateException("oops");
        }
        return "Hello, " + name;
    }

    public String sayFallback(long arg, Throwable ex) {
        // Do some log here.
        ex.printStackTrace();
        return "Oops, error occurred at " + arg;
    }

    public String defaultFallback() {
        log.info("Go to default fallback");
        return "default_fallback";
    }
}
