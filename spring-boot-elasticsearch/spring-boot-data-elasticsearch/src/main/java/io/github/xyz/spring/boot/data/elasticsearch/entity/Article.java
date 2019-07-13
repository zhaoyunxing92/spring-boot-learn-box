package io.github.xyz.spring.boot.data.elasticsearch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author zhaoyunxing
 * @date: 2019-06-28 14:38
 * @des:
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "blog", type = "article")
public class Article {
    @Id
    @Field(type = FieldType.Text, store = false)
    private String id;
    /**
     * 文章名称
     */
    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_max_word")
    private String name;
    /**
     * 内容
     */
    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_max_word")
    private String content;
    /**
     * 创建时间
     * 关于日期可以参考：https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-date-format.html#strict-date-time
     */
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
    private Date createTime;
}
