package io.github.zyx.spring.boot.redis.config;

import org.springframework.data.redis.cache.CacheKeyPrefix;

/**
 * @author zhaoyunxing
 * @date 2020/5/8 9:54
 */
public class CustomCacheKeyPrefix implements CacheKeyPrefix {
    /**
     * Compute the prefix for the actual {@literal key} stored in Redis.
     *
     * @param name will never be {@literal null}.
     * @return never {@literal null}.
     */
    @Override
    public String compute(String name) {
        return name + ":";
    }
}
