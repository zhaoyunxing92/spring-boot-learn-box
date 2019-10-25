/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.zhangsan.controller;


import io.github.xyz.spring.boot.rocketmq.zhangsan.model.Account;
import io.github.xyz.spring.boot.rocketmq.zhangsan.service.Bank01Account;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public Map<String, Object> hello() {
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

}
