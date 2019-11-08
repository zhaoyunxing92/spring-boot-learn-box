/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.dubbo.seata.zhangsan.service;

import io.github.xyz.dubbo.seata.service.AccountService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author zhaoyunxing
 * @date: 2019-11-07 17:51
 */
@Service(group = "zhangSanAccount", interfaceClass = AccountService.class)
public class ZhangSanAccountImpl implements AccountService {
}
