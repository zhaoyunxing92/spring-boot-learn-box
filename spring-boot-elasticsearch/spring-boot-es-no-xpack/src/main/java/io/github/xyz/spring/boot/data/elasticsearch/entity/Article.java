package io.github.xyz.spring.boot.data.elasticsearch.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author zhaoyunxing
 * @date 2020/7/15 18:58
 */
@Data
@Document(indexName = "doc", type = "article")
public class Article {
    /**
     * 指标id
     */
    @Id
    @Field(type = FieldType.Text)
    private String id;

    /**
     * 指标名称
     */
    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_max_word")
    private String name;

    /**
     * 描述
     */
    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_max_word")
    private String desc;

    /**
     * code编码
     */
    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_max_word")
    private String code;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time")
    private Date create;

    /**
     * 修改时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time")
    private Date modified;
}
