package io.github.xyz.spring.boot.data.elasticsearch.service;


import io.github.xyz.spring.boot.data.elasticsearch.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author zhaoyunxing
 * @date: 2019-06-28 14:43
 * @des:
 */
public interface ArticleService extends ElasticsearchRepository<Article, String> {
    /**
     * 根据名称查询【spring-data-elasticsearch命名规范】
     * 默认值返回10条数据
     *
     * @param name 文章名称
     * @return List<Article>
     */
    List<Article> findArticleByName(String name);

    /**
     * 标题中包含或者内容中包含
     *
     * @param name     文章名称
     * @param content  文章内容
     * @param pageable 分页对象
     * @return List<Article>
     */
    List<Article> findArticleByNameOrContent(String name, String content, Pageable pageable);

    /**
     * 指定时间域名并且根据id进行deac排序
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param pageable 分页对象
     * @return  List<Article>
     */
    List<Article> findArticlesByCreateTimeBetweenOrderByIdDesc(String start, String end, Pageable pageable);

}
