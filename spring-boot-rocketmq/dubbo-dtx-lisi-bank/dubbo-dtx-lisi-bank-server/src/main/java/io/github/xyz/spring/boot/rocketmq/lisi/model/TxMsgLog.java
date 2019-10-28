/**
 * Copyright(C) 2019 Hangzhou sunny Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.lisi.model;

import java.util.Date;
import lombok.Data;

/**
 * @author sunny
 * @date: 2019-10-28 15:50:53
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