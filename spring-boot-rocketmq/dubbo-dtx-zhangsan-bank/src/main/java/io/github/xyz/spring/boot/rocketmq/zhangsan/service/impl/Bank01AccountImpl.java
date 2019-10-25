/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.zhangsan.service.impl;

import io.github.xyz.spring.boot.rocketmq.lisi.service.Bank02Account;
import io.github.xyz.spring.boot.rocketmq.zhangsan.mapper.AccountMapper;
import io.github.xyz.spring.boot.rocketmq.zhangsan.model.Account;
import io.github.xyz.spring.boot.rocketmq.zhangsan.service.Bank01Account;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoyunxing
 * @date: 2019-10-24 15:14
 */
@Service
@org.springframework.stereotype.Service
public class Bank01AccountImpl implements Bank01Account {

    private final AccountMapper accountMapper;
    @Reference
    private Bank02Account bank02Account;

    public Bank01AccountImpl(AccountMapper accountMapper) {this.accountMapper = accountMapper;}

    /**
     * 获取账户信息
     *
     * @param accountId
     * @return
     */
    @Override
    public Account getAccount(String accountId) {
        return accountMapper.selectByPrimaryKey(accountId);
    }

    /**
     * 转账
     *
     * @param account
     */
    @Override
    public void transfer(Account account) {
        Long money = account.getMoney();
        accountMapper.updateAccount("zhangsan", money * -1);
        bank02Account.updateAccount(account.getAccountName(), money);
    }

    @Override
    public Map<String, Object> banks() {
        Map<String, Object> map = new HashMap<>(2);

        map.put("lisi", bank02Account.getAccount("lisi"));
        map.put("zhangsan", getAccount("zhangsan"));
        return map;
    }
}
