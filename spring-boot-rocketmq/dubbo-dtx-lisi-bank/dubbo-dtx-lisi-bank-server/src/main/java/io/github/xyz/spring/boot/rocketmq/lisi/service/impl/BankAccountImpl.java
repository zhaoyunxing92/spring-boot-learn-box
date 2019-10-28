/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.lisi.service.impl;

import io.github.xyz.spring.boot.rocketmq.lisi.LiSiDto;
import io.github.xyz.spring.boot.rocketmq.lisi.mapper.AccountMapper;
import io.github.xyz.spring.boot.rocketmq.lisi.mapper.TxMsgLogMapper;
import io.github.xyz.spring.boot.rocketmq.lisi.model.Account;
import io.github.xyz.spring.boot.rocketmq.lisi.service.Bank02Account;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author zhaoyunxing
 * @date: 2019-10-24 08:57
 */
@Service
@org.springframework.stereotype.Service
public class BankAccountImpl implements Bank02Account {

    private final AccountMapper accountMapper;
    private final TxMsgLogMapper txMsgLogMapper;

    public BankAccountImpl(AccountMapper accountMapper, TxMsgLogMapper txMsgLogMapper) {
        this.accountMapper = accountMapper;
        this.txMsgLogMapper = txMsgLogMapper;
    }

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

    /**
     * 事物消息转账
     *
     * @param account
     */
    @Transactional(rollbackFor = Exception.class)
    public void txMsgTransfer(Account account) {
        String txId = account.getTxId();
        if (StringUtils.hasText(txMsgLogMapper.selectMsgLog(txId))) {
            return;
        }
        Long money = account.getMoney();
        updateAccount(account.getAccountName(), money);
        txMsgLogMapper.insertMsgLog(txId);
    }
}
