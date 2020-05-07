package io.github.zyx.spring.boot.redis.service;

/**
 * @author zhaoyunxing
 * @date 2020/5/7 20:16
 */
public interface RedisService {
    /**
     * 获取名称
     * @param name 名称
     * @return string
     */
    String getName(String name);
}
