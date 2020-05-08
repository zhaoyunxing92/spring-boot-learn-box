package io.github.zyx.spring.boot.redis.service.impl;

import io.github.zyx.spring.boot.redis.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhaoyunxing
 * @date 2020/5/7 20:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceImplTest {
    @Autowired
    private RedisService redisService;

    @Test
    public void testTestGetName() {
        redisService.getName("zhaoyunxing");
    }
}