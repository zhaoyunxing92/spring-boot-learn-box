package io.github.zyx.spring.boot.redis.service.impl;

import io.github.zyx.spring.boot.redis.service.RedisService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

/**
 * @author zhaoyunxing
 * @date 2020/5/7 20:17
 */
@Service
public class RedisServiceImpl implements RedisService {
    /**
     * 获取名称
     *
     * @param name 名称
     * @return string
     */
    @Override
    @Cacheable(key = "#name", cacheNames = "my-redis-cache1")
    public String getName(String name) {
        return name;
    }
}
