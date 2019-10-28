/**
 * Copyright(C) 2019 Hangzhou sunny Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.zhangsan.model;

import java.util.Date;
import lombok.Data;

/**
 * @author sunny
 * @date: 2019-10-28 15:44:50
 */
@Data
public class TxMsgLog {
    /**
     * 消息id
     */
    private String msgId;

    /**
     * 创建时间
     */
    private Date creatorTime;
}