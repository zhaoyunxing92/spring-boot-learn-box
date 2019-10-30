/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.zhangsan.controller;


import io.github.xyz.spring.boot.rocketmq.zhangsan.model.Account;
import io.github.xyz.spring.boot.rocketmq.zhangsan.service.Bank01Account;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhaoyunxing
 * @date: 2019-10-24 00:01
 */
@RestController
@RequestMapping("/bank")
public class AccountController {

    private final Bank01Account bank01Account;

    public AccountController(Bank01Account bank01Account) {this.bank01Account = bank01Account;}

    @GetMapping
    public List<Object> banks() {
        return bank01Account.banks();
    }

    /**
     * 转账
     *
     * @param account
     */
    @PostMapping("/transfer")
    public void transfer(@RequestBody Account account) {
        bank01Account.transfer(account);
    }

    /**
     * rocket 事物转账
     *
     * @param account
     */
    @PostMapping("/transfer/tx")
    public void txTransfer(@RequestBody Account account) {
        bank01Account.txTransfer(account);
    }


    /**
     * 事物转账
     *
     * @param account
     */
    @PostMapping("/transfer/msg")
    public void msgTransfer(@RequestBody Account account) {
        bank01Account.msgTransfer(account);
    }

    /**
     * seata转账
     *
     * @param account
     */
    @PostMapping("/transfer/seata")
    public void seataTransfer(@RequestBody Account account) {
        bank01Account.seataTransfer(account);
    }
}
