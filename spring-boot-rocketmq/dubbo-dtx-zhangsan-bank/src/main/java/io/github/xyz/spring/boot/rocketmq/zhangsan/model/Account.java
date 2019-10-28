/**
 * Copyright(C) 2019 Hangzhou sunny Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.zhangsan.model;

import lombok.Data;

import java.util.Date;

/**
 * @author sunny
 * @date: 2019-10-24 13:52:49
 */
@Data
public class Account {
    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 账户金额
     */
    private Long money;

    /**
     * 修改时间
     */
    private Date modifierTime;
    /**
     * 操作流水
     */
    private String txId;
}