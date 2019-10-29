/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.zhangsan.service.impl;

import com.alibaba.fastjson.JSONObject;
import io.github.xyz.spring.boot.rocketmq.lisi.constant.TxConstant;
import io.github.xyz.spring.boot.rocketmq.lisi.service.Bank02Account;
import io.github.xyz.spring.boot.rocketmq.zhangsan.mapper.AccountMapper;
import io.github.xyz.spring.boot.rocketmq.zhangsan.mapper.TxMsgLogMapper;
import io.github.xyz.spring.boot.rocketmq.zhangsan.model.Account;
import io.github.xyz.spring.boot.rocketmq.zhangsan.service.Bank01Account;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.github.xyz.spring.boot.rocketmq.lisi.constant.TxConstant.TX_PRODUCER_GROUP;

/**
 * @author zhaoyunxing
 * @date: 2019-10-24 15:14
 */
@Service
@org.springframework.stereotype.Service
public class Bank01AccountImpl implements Bank01Account {

    private final AccountMapper accountMapper;
    private final TxMsgLogMapper txMsgLogMapper;
    private final RocketMQTemplate rocketMQTemplate;
    @Reference
    private Bank02Account bank02Account;

    public Bank01AccountImpl(AccountMapper accountMapper, TxMsgLogMapper txMsgLogMapper, RocketMQTemplate rocketMQTemplate) {
        this.accountMapper = accountMapper;
        this.txMsgLogMapper = txMsgLogMapper;
        this.rocketMQTemplate = rocketMQTemplate;
    }

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
        if (money == 10) {
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
        // 设置事物号
        account.setTxId(UUID.randomUUID().toString());
        String json = JSONObject.toJSONString(account);
        Message<String> msg = MessageBuilder.withPayload(json).build();

        rocketMQTemplate.sendMessageInTransaction(TX_PRODUCER_GROUP, TxConstant.TX_TOPIC, msg, null);
    }

    /**
     * rocket 消息转账
     *
     * @param account
     */
    @Override
    public void msgTransfer(Account account) {

    }

    /**
     * 事物消息转账
     *
     * @param account
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void txMsgTransfer(Account account) {
        String txId = txMsgLogMapper.selectMsgLog(account.getTxId());
        if (StringUtils.hasText(txId)) {
            return;
        }
        Long money = account.getMoney();
        accountMapper.updateAccount("zhangsan", money * -1);
        txMsgLogMapper.insertMsgLog(account.getTxId());
        if (money == 10) {
            throw new RuntimeException("转账金额超过10元");
        }
    }

    /**
     * 是否执行过
     *
     * @param txId
     * @return
     */
    @Override
    public boolean hasTxLog(String txId) {

        return StringUtils.hasText(txMsgLogMapper.selectMsgLog(txId));
    }
}
