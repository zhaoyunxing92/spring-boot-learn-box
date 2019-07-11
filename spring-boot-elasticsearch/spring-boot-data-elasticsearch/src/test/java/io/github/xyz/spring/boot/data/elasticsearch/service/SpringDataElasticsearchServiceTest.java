package io.github.xyz.spring.boot.data.elasticsearch.service;

import io.github.xyz.spring.boot.data.elasticsearch.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author zhaoyunxing
 * @date: 2019-07-09 16:15
 * @des: 官方文档使用 <a href="https://docs.spring.io/spring-data/elasticsearch/docs/2.1.22.RELEASE/reference/html/#repositories.core-concepts">官方文档</a>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringDataElasticsearchServiceTest {

    @Autowired
    private SpringDataElasticsearchService articleService;

    @Test
    public void findArticleByName() {
        Page<Article> articles = articleService.findArticleByName("docker搭建", PageRequest.of(0, 5));
        log.info("匹配总条数：{}", articles.getTotalElements());
        articles.get().forEach(System.out::println);
    }

    @Test
    public void findArticleByNameSort() {
        // Sort.Order.desc("id");
        Page<Article> articles = articleService.findArticleByName("docker搭建", PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id", "createTime")));
        log.info("匹配总条数：{}", articles.getTotalElements());
        articles.get().forEach(System.out::println);
    }

    @Test
    public void findArticleByNameOrderByCreateTimeDesc() {
        Page<Article> articles = articleService.findArticleByNameOrderByCreateTimeDesc("docker搭建", PageRequest.of(0, 5));
        log.info("匹配总条数：{}", articles.getTotalElements());
        articles.get().forEach(System.out::println);
    }

    @Test
    public void countArticleByName() {
        Long count = articleService.countArticleByName("docker搭建");
        log.info("count: {}", count);
    }
}