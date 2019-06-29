package io.github.xyz.spring.boot.elasticsearch.entity;

import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author zhaoyunxing
 * @date: 2019-06-28 14:38
 * @des:
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @JestId
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
