/**
 * Copyright(C) 2019 Hangzhou sunny Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.zhangsan.mapper;

import org.springframework.stereotype.Repository;

/**
 * @author sunny
 * @date: 2019-10-28 15:44:50
 */
@Repository
public interface TxMsgLogMapper {
    /**
     * 保存日志
     *
     * @param txId
     * @return
     */
    int insertMsgLog(String txId);

    /**
     * 查询事物日志
     *
     * @param txId
     * @return
     */
    String selectMsgLog(String txId);
}