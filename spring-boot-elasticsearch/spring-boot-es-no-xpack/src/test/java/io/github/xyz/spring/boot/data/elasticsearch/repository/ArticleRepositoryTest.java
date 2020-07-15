package io.github.xyz.spring.boot.data.elasticsearch.repository;


import io.github.xyz.spring.boot.data.elasticsearch.SpringDataElasticsearchServer;
import io.github.xyz.spring.boot.data.elasticsearch.entity.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author zhaoyunxing
 * @date 2020/7/15 19:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void saveTest() {
        Article article = new Article();
        article.setName("Nginx Lua开发实战");
        article.setCode("nginx lua");
        article.setDesc("Nginx的基本知识，包含Nginx的使用、配置、安装、技术架构、技术特点、主要工作流程等。外围关系型数据库、NoSQL数据库、缓存等的使用范围、安装、使用方法、配置，如MySQL、PostgreSQL、MongoDB、Redis、Memcached。Lua语法详解，包含Lua系统库。Lua常用库，包含Redis、MySQL、Memcached、PostgreSQL、MongoDB、Bit、lfs、restry.http、lcurl、FFI、cjson、Template、WebSocket。");
        article.setCreate(new Date());
        article.setModified(new Date());

        articleRepository.save(article);

        article.setName("Lua开发实战");
        article.setCode("lua");
        article.setDesc("test分词");
        article.setCreate(new Date());
        article.setModified(new Date());

        articleRepository.save(article);
    }

    @Test
    public void deleteTest() {
        articleRepository.deleteById("IGFTUnMBO0RSeww3H7yc");
    }
}