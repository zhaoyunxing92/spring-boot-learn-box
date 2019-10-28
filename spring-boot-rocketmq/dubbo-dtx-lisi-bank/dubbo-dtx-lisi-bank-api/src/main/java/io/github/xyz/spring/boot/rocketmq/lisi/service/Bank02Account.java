/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.lisi.service;

import io.github.xyz.spring.boot.rocketmq.lisi.LiSiDto;

/**
 * @author zhaoyunxing
 * @date: 2019-10-24 18:00
 */
public interface Bank02Account {
    /**
     * 修改账号金额
     *
     * @param accountId 账号
     * @param money     金额
     */
    void updateAccount(String accountId, Long money);

    /**
     * 获取账户信息
     *
     * @param accountId
     * @return
     */
    LiSiDto getAccount(String accountId);

}
