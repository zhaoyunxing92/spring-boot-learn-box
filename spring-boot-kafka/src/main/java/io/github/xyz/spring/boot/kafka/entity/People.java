/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author zhaoyunxing
 * @date: 2019-04-28 14:44
 * @des:
 */
@Data
@AllArgsConstructor
public class People {
    private String id;
    private String name;
    private Integer age;
    private Date loginTime;
}
