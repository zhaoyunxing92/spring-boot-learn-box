package io.github.xyz.spring.boot.elasticsearch.java.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zhaoyunxing
 * @date: 2019-07-05 14:57
 * @des:
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class User {
    private Long id;
    private String name;
    private Long age;
    private String desc;
}
