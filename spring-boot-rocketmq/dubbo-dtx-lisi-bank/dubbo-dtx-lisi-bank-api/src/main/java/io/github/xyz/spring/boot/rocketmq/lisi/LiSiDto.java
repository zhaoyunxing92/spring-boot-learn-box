/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.rocketmq.lisi;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaoyunxing
 * @date: 2019-10-24 18:07
 */
@Getter
@Setter
public class LiSiDto implements Serializable {
    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 账户金额
     */
    private Long money;

    /**
     * 修改时间
     */
    private Date modifierTime;
}
