/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.lisi.listener;

import com.alibaba.fastjson.JSONObject;
import io.github.xyz.spring.boot.rocketmq.lisi.constant.TxConstant;
import io.github.xyz.spring.boot.rocketmq.lisi.model.Account;
import io.github.xyz.spring.boot.rocketmq.lisi.service.Bank02Account;
import io.github.xyz.spring.boot.rocketmq.lisi.service.impl.BankAccountImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhaoyunxing
 * @date: 2019-10-28 16:21
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = TxConstant.TX_TOPIC, consumerGroup = TxConstant.TX_CONSUMER_GROUP)
public class TransactionListenerImpl implements RocketMQListener<Account> {

    private final BankAccountImpl bank02Account;

    public TransactionListenerImpl(BankAccountImpl bank02Account) {this.bank02Account = bank02Account;}


    /**
     * 接受到消息
     *
     * @param account
     */
    @Override
    public void onMessage(Account account) {
        log.info("topic:{}", JSONObject.toJSONString(account));
        // log.info("topic:{}", JSONObject.toJSONString(msg));
        // Account account = JSONObject.parseObject(msg, Account.class);
        bank02Account.txMsgTransfer(account);
    }
}
