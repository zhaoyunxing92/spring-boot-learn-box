/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.lisi.listener;

import io.github.xyz.spring.boot.rocketmq.lisi.constant.TxConstant;
import io.github.xyz.spring.boot.rocketmq.lisi.model.Account;
import io.github.xyz.spring.boot.rocketmq.lisi.service.impl.BankAccountImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消息监听
 *
 * @author zhaoyunxing
 * @date: 2019-10-29 14:13
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = TxConstant.MSG_TOPIC, consumerGroup = TxConstant.MSG_CONSUMER_GROUP)
public class MsgListenerImpl implements RocketMQListener<Account> {

    private final BankAccountImpl bank02Account;

    public MsgListenerImpl(BankAccountImpl bank02Account) {this.bank02Account = bank02Account;}


    @Override
    public void onMessage(Account msg) {
        log.info("msg-topic:{}",msg);
        // 接受到转账消息更新
        bank02Account.txMsgTransfer(msg);
    }
}
