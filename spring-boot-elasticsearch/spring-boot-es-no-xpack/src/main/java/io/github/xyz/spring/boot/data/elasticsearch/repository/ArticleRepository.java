package io.github.xyz.spring.boot.data.elasticsearch.repository;

import io.github.xyz.spring.boot.data.elasticsearch.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author zhaoyunxing
 * @date 2020/7/15 19:18
 */
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {

}
