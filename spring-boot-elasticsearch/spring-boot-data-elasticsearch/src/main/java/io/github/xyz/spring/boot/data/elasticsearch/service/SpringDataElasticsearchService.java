package io.github.xyz.spring.boot.data.elasticsearch.service;


import io.github.xyz.spring.boot.data.elasticsearch.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author zhaoyunxing
 * @date: 2019-06-28 14:43
 * @des:
 */
public interface SpringDataElasticsearchService extends ElasticsearchRepository<Article, String> {
    /**
     * name包含字段
     * 默认值返回10条数据
     *
     * @param name     文章名称
     * @param pageable 分页对象
     * @return Page<Article>
     */
    Page<Article> findArticleByName(String name, Pageable pageable);

    /**
     * 根据文章名称搜索，然后根据时间倒序
     *
     * @param name     文章名称
     * @param pageable 分页对象
     * @return
     */
    Page<Article> findArticleByNameOrderByCreateTimeDesc(String name, Pageable pageable);

    /**
     * 根据名称统计条数
     *
     * @param name 文章名称
     * @return
     */
    Long countArticlesByName(String name);

    /**
     * 根据文章id删除，返回文章信息（只能针对主键使用）
     *
     * @param id 文章id
     * @return
     */
    List<Article> deleteArticlesById(String id);

}
