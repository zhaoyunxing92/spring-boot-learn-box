package io.github.xyz.spring.boot.data.elasticsearch;

import io.github.xyz.spring.boot.data.elasticsearch.entity.Article;
import io.github.xyz.spring.boot.data.elasticsearch.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author zhaoyunxing
 * @date: 2019-07-07 13:19
 * @des:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchCase {

    @Autowired
    private ArticleService articleService;

    /**
     * 创建文章,更新文档只有保证id一直就可以了
     */
    @Test
    public void addDocument() {
        Article article = new Article();
        article.setName("elasticsearch入门到放弃之docker搭建");
        article.setContent("在我的观念里elasticsearch是大数据的产物");
        article.setCreateTime(new Date());
        article.setId("1");
        articleService.save(article);
    }

    @Test
    public void findAll() {
        System.out.println("***********************获取全部数据***********************");
        articleService.findAll().forEach(System.out::println);
    }


    /**
     * 删除文章 deleteAll()、delete() 这些方法类似不写了
     */
    @Test
    public void deleteDocumentById() {
        articleService.deleteById("1");
    }
}
