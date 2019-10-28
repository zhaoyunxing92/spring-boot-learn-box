/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.zhangsan.service;

import io.github.xyz.spring.boot.rocketmq.zhangsan.model.Account;

import java.util.List;

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
    List<Object> banks();


    /**
     * 转账
     *
     * @param account
     */
    void transfer(Account account);

    /**
     * 事物转账
     *
     * @param account
     */
    void txTransfer(Account account);

    /**
     * 事物消息转账
     *
     * @param account
     */
    void txMsgTransfer(Account account);

    /**
     * 是否执行过
     *
     * @param txId
     * @return
     */
    boolean hasTxLog(String txId);

    /**
     * 获取账户信息
     *
     * @param accountId
     * @return
     */
    Account getAccount(String accountId);

    /**
     * rocket 消息转账
     *
     * @param account
     */
    void msgTransfer(Account account);
}
