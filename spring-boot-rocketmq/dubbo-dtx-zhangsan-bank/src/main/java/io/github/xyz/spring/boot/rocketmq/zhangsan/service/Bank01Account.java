/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.zhangsan.service;

import io.github.xyz.spring.boot.rocketmq.zhangsan.model.Account;

import java.util.Map;

/**
 * @author zhaoyunxing
 * @date: 2019-10-24 15:13
 */
public interface Bank01Account {
    /**
     * 获取账户
     *
     * @return
     */
    Map<String, Object> banks();


    /**
     * 转账
     *
     * @param account
     */
    void transfer(Account account);

    /**
     * 获取账户信息
     *
     * @param accountId
     * @return
     */
    Account getAccount(String accountId);
}
