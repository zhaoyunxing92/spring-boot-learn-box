package io.github.xyz.spring.boot.elasticsearch.service;

import io.github.xyz.spring.boot.elasticsearch.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author zhaoyunxing
 * @date: 2019-06-28 14:43
 * @des:
 */
public interface UserService extends ElasticsearchRepository<User, String> {
}
