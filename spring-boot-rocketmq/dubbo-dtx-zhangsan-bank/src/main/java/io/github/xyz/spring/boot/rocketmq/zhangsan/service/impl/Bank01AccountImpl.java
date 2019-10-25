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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @Transactional(rollbackFor = Exception.class)
    public void transfer(Account account) {
        Long money = account.getMoney();
        accountMapper.updateAccount("zhangsan", money * -1);
        bank02Account.updateAccount(account.getAccountName(), money);
        if (money > 10) {
            throw new RuntimeException("转账金额超过10元");
        }
    }

    @Override
    public List<Object> banks() {
        List<Object> banks = new ArrayList<>(2);
        banks.add(bank02Account.getAccount("lisi"));
        banks.add(getAccount("zhangsan"));
        return banks;
    }

    /**
     * 事物转账
     *
     * @param account
     */
    @Override
    public void txTransfer(Account account) {

    }

    /**
     * rocket 消息转账
     *
     * @param account
     */
    @Override
    public void msgTransfer(Account account) {

    }
}
