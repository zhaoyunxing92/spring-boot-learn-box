/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.lisi.service.impl;

import io.github.xyz.spring.boot.rocketmq.lisi.LiSiDto;
import io.github.xyz.spring.boot.rocketmq.lisi.mapper.AccountMapper;
import io.github.xyz.spring.boot.rocketmq.lisi.model.Account;
import io.github.xyz.spring.boot.rocketmq.lisi.service.Bank02Account;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;

import java.util.Map;

/**
 * @author zhaoyunxing
 * @date: 2019-10-24 08:57
 */
@Service
public class BankAccountImpl implements Bank02Account {

    private final AccountMapper accountMapper;

    public BankAccountImpl(AccountMapper accountMapper) {this.accountMapper = accountMapper;}

    /**
     * 修改账号金额
     *
     * @param accountId 账号
     * @param money     金额
     */
    @Override
    public void updateAccount(String accountId, Long money) {
        accountMapper.updateAccount(accountId, money);
    }

    /**
     * 获取账户信息
     *
     * @param accountId
     * @return
     */
    @Override
    public LiSiDto getAccount(String accountId) {
        Account account = accountMapper.selectByPrimaryKey(accountId);
        LiSiDto dto = new LiSiDto();
        BeanUtils.copyProperties(account, dto);
        return dto;
    }
}
