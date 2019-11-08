/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.dubbo.seata.zhangsan.service;

import io.github.xyz.dubbo.seata.Account;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author zhaoyunxing
 * @date: 2019-11-07 17:51
 */
@Service(group = "zhangSanAccount", interfaceClass = Account.class)
public class ZhangSanAccountImpl implements Account {
}
