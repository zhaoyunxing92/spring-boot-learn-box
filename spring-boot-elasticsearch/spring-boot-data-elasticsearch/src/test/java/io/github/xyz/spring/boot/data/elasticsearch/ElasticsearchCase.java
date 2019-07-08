package io.github.xyz.spring.boot.data.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFilter;
import io.github.xyz.spring.boot.data.elasticsearch.entity.Article;
import io.github.xyz.spring.boot.data.elasticsearch.service.ArticleService;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 创建文章,更新文档只有保证id一直就可以了
     */
    @Test
    public void addDocument() {
        System.out.println("***********************一次性插入30条数据***********************");
        List<Article> articles = new ArrayList<>(30);
        for (int i = 1; i <= 30; i++) {
            Article article = new Article();
            article.setName("elasticsearch入门到放弃之docker搭建" + i);
            article.setContent("在我的观念里elasticsearch是" + i + "大数据的产物");
            article.setCreateTime(new Date());
            article.setId(String.valueOf(i));
            articles.add(article);
        }
        articleService.saveAll(articles);
    }

    /**
     * 查询全部 默认是查询10条数据的，但是findAll()查询的是全部数据，说明它真的是findAll()
     */
    @Test
    public void findAll() {
        System.out.println("***********************获取全部数据***********************");
        articleService.findAll().forEach(System.out::println);
    }

    /**
     * 根据id查询
     */
    @Test
    public void findById() {
        System.out.println("***********************根据id查询***********************");
        Article article = articleService.findById(String.valueOf(27)).orElseGet(Article::new);
        System.out.println(article);
    }

    /**
     * 根据名称查询 默认值返回10条数据
     */
    @Test
    public void findByName() {
        System.out.println("***********************根据名称查询***********************");
        articleService.findArticleByName("docker搭建").forEach(System.out::println);
    }

    /**
     * 标题中包含或者内容中包含,设置分页 三条数据分页
     */
    @Test
    public void findArticleByNameOrContent() {
        System.out.println("***********************根据名称和内容查询***********************");
        articleService.findArticleByNameOrContent("docker搭建", "30", PageRequest.of(0, 3)).forEach(System.out::println);
    }

    /**
     * 指定时间域名并且根据id进行deac排序(aec)类似不写了
     */
    @Test
    public void findArticlesByCreateTimeBetweenAndOrderByCreateTimeDesc() {
        System.out.println("***********************指定时间域名并且根据id进行deac排序***********************");

        Page<Article> page = articleService.findArticlesByCreateTimeBetweenOrderByIdDesc("2019-07-07 14:41:39:998", "2019-07-07 16:33:29:175", PageRequest.of(0, 3));

        System.out.println("匹配的总条数：" + page.getTotalElements());
        page.get().forEach(System.out::println);
       // page.get().collect(Collectors.toCollection(ArrayList::new)).forEach(System.out::println);
    }

    /**
     * 模糊匹配查询对应的是QueryString方法这个可以参考：<a>https://www.jianshu.com/p/9f6f7f67df4e</a>
     */
    @Test
    public void nativeSearchQuery() {
        System.out.println("***********************模糊查询***********************");
        // 构建查询对象
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("sunny在学用docker搭建elasticsearch环境").defaultField("content"))
                .withPageable(PageRequest.of(0, 3))
                .build();

        elasticsearchTemplate.queryForList(query, Article.class).forEach(System.out::println);
    }

    /**
     * 删除文章 deleteAll()、delete() 这些方法类似不写了
     */
    @Test
    public void deleteDocumentById() {
        System.out.println("***********************根据id删除***********************");
        articleService.deleteById("1");
    }
}
