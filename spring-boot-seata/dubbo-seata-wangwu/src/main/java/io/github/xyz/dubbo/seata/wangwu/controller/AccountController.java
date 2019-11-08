/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.dubbo.seata.wangwu.controller;

import io.github.xyz.dubbo.seata.model.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 账户控制器
 *
 * @author zhaoyunxing
 * @date: 2019-11-08 11:32
 */
@RestController
@RequestMapping("/")
public class AccountController {
    /**
     * 获取账户
     *
     * @return Account
     */
    @GetMapping("/accounts")
    public List<Account> accounts() {
        return null;
    }
}
