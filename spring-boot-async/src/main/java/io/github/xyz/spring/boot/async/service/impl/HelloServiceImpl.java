package io.github.xyz.spring.boot.async.service.impl;

import io.github.xyz.spring.boot.async.service.HelloService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @author zyx
 * @date 2021-10-20 15:19:12
 */
@Slf4j
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    @SneakyThrows
    @Async("asyncServiceExecutor")
    public void echo() {
        log.info("任务开始:{}", Instant.now().getEpochSecond());
        TimeUnit.SECONDS.sleep(5);
    }
}
