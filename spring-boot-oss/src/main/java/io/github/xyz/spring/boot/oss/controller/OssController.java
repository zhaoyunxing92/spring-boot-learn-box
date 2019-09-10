/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.oss.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import io.github.xyz.spring.boot.oss.config.OssStsConfig;
import io.github.xyz.spring.boot.oss.entity.OssStsInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author zhaoyunxing
 * @date: 2019-09-09 10:28
 * @desc:
 */
@RestController
@Slf4j
public class OssController {

    private final OssStsConfig ossConfig;

    @Autowired
    public OssController(OssStsConfig ossConfig) {
        this.ossConfig = ossConfig;
        // 参数验证
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<OssStsConfig>> validators = validator.validate(ossConfig);
        //todo：validators.iterator().hasNext()这个必须判断下
        Assert.isTrue(validators.isEmpty(), validators.iterator().hasNext() ? validators.iterator().next().getMessage() : "ossConfig参数验证不通过");
    }

    @GetMapping
    public String ossConfig() {

        OssStsInfo osi = new OssStsInfo();
        try {
            // 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
            DefaultProfile.addEndpoint("", "", "Sts", ossConfig.getEndpoint());
            // 构造default profile（参数留空，无需添加region ID）
            IClientProfile profile = DefaultProfile.getProfile("", ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
            // 用profile构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(ossConfig.getRoleArn());
            request.setRoleSessionName(ossConfig.getRoleSessionName());
            // 若policy为空，则用户将获得该角色下所有权限
            //            request.setPolicy(ossConfig.getPolicy());
            request.setPolicy(null);
            // 设置凭证有效时间
            request.setDurationSeconds(ossConfig.getDurationSeconds());

            final AssumeRoleResponse response = client.getAcsResponse(request);
            //        statement
            osi.setCode("200");
            osi.setMessage("获取sts成功");

            AssumeRoleResponse.Credentials credentials = response.getCredentials();

            osi.setAccessKeyId(credentials.getAccessKeyId());
            osi.setAccessKeySecret(credentials.getAccessKeySecret());
            osi.setRequestId(response.getRequestId());
            osi.setSecurityToken(credentials.getSecurityToken());
            osi.setExpiration(credentials.getExpiration());

        } catch (ClientException e) {
            osi.setMessage(e.getErrMsg());
            osi.setCode(e.getErrCode());
            osi.setRequestId(e.getRequestId());
        }
        return JSONObject.toJSONString(osi);
    }
}
