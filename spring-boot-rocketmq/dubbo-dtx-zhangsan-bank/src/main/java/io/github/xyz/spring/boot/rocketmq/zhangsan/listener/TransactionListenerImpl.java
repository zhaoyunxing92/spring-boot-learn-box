/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.zhangsan.listener;

import com.alibaba.fastjson.JSONObject;
import io.github.xyz.spring.boot.rocketmq.zhangsan.model.Account;
import io.github.xyz.spring.boot.rocketmq.zhangsan.service.Bank01Account;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

import static io.github.xyz.spring.boot.rocketmq.lisi.constant.TxConstant.TX_PRODUCER_GROUP;

/**
 * @author zhaoyunxing
 * @date: 2019-10-28 14:03
 */
@Slf4j
@RocketMQTransactionListener(txProducerGroup = TX_PRODUCER_GROUP)
public class TransactionListenerImpl implements RocketMQLocalTransactionListener {

    private final Bank01Account bank01Account;

    public TransactionListenerImpl(Bank01Account bank01Account) {this.bank01Account = bank01Account;}

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            log.info("execute:{}", JSONObject.toJSONString(msg.getHeaders()));
            String payload = new String((byte[]) msg.getPayload());
            Account account = JSONObject.parseObject(payload, Account.class);
            bank01Account.txMsgTransfer(account);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.info("check:{}", JSONObject.toJSONString(msg.getHeaders()));
        String payload = new String((byte[]) msg.getPayload());
        Account account = JSONObject.parseObject(payload, Account.class);

        if (bank01Account.hasTxLog(account.getTxId())) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}
