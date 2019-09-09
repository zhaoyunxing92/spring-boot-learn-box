/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.oss.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author zhaoyunxing
 * @date: 2019-09-09 15:31
 * @desc:
 */
@Data
public class OssStsInfo {

    @JSONField(name = "AccessKeyId")
    private String accessKeyId;

    @JSONField(name = "AccessKeySecret")
    private String accessKeySecret;

    @JSONField(name = "SecurityToken")
    private String securityToken;

    @JSONField(name = "Expiration")
    private String expiration;

    @JSONField(name = "StatusCode")
    private String code;

    private String requestId;

    private String message;
}
