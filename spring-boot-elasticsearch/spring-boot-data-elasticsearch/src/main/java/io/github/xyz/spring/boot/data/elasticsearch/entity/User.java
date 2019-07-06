package io.github.xyz.spring.boot.data.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author zhaoyunxing
 * @date: 2019-06-28 14:38
 * @des:
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "elastic", type = "sunny")
public class User {
    @Id
    @Field(type = FieldType.Text, store = false)
    private String id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
}
