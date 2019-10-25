/**
 * Copyright(C) 2019 Hangzhou sunny Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.zhangsan.mapper;


import io.github.xyz.spring.boot.rocketmq.zhangsan.model.Account;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author sunny
 * @date: 2019-10-24 13:52:49
 */
@Repository
public interface AccountMapper {
    /**
     * vx
     *
     * @param accountId
     * @return
     */
    Account selectByPrimaryKey(@Param("accountId") String accountId);

    /**
     * 更新账户
     *
     * @param accountId 账户
     * @param money     金额
     */
    void updateAccount(@Param("accountId") String accountId, @Param("money") Long money);
}