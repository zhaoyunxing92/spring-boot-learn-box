package io.github.xyz.spring.boot.elasticsearch.java.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

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
    private Integer age;
    private String desc;
    private Date registerTime;
}
