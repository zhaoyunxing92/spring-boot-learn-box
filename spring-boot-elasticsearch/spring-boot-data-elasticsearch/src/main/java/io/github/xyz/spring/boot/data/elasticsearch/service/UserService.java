package io.github.xyz.spring.boot.data.elasticsearch.service;


import io.github.xyz.spring.boot.data.elasticsearch.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author zhaoyunxing
 * @date: 2019-06-28 14:43
 * @des:
 */
public interface UserService extends ElasticsearchRepository<Article, String> {
}
