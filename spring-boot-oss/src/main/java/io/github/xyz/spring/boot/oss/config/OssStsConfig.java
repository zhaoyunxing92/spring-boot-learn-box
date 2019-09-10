/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.oss.config;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.sts.transform.v20150401.AssumeRoleResponseUnmarshaller;
import com.sun.media.sound.SoftTuning;
import io.github.xyz.spring.boot.oss.entity.Policy;
import io.github.xyz.spring.boot.oss.entity.Statement;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * {@link Policy} policy是规则
 *
 * @author zhaoyunxing
 * @date: 2019-09-09 11:14
 * @desc: oss 配置文件.
 */
@Data
@ToString
@Component
@ConfigurationProperties(prefix = "oss.sts")
@ConditionalOnClass(AssumeRoleResponseUnmarshaller.class)
public class OssStsConfig {

    /**
     * STS服务的所有接入地址，每个地址的功能都相同，请尽量在同区域进行调用.默认值:sts.cn-hangzhou.aliyuncs.com.
     */
    @NotBlank(message = "请配置【endpoint】属性")
    private String endpoint = "sts.cn-hangzhou.aliyuncs.com";
    /**
     * 子账号AK信息
     */
    @NotBlank(message = "请配置【accessKeyId】属性")
    private String accessKeyId;
    /**
     * 子账号AK信息
     */
    @NotBlank(message = "请配置【accessKeySecret】属性")
    private String accessKeySecret;
    /**
     * RAM角色的全局资源描述符,格式：acs:ram::$accountID:role/$roleName.
     */
    @NotBlank(message = "请配置【roleArn】属性")
    private String roleArn;
    /**
     * 用来标识临时凭证的名称，建议使用不同的应用程序用户来区分.
     */
    @NotBlank(message = "请配置【roleSessionName】属性")
    private String roleSessionName;

    /**
     * 设置临时凭证的有效期，单位是s，最小为900，最大为3600.默认值1000
     */
    @Range(min = 900, max = 3600, message = "有效期必须在{min}~{max}之间")
    private Long durationSeconds = 1000L;

    /**
     * 这里传入的Policy是用来限制扮演角色之后的临时凭证的权限。临时凭证最后获得的权限是角色的权限和这里传入的Policy的交集。若policy为空，则用户将获得该角色下所有权限.
     */
    @Valid
    private Policy policy;

    /**
     * bean初始化完成后进行参数验证
     */
    @PostConstruct
    public void validator() {
        // 参数验证
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<OssStsConfig>> validators = validator.validate(this);
        //todo：validators.iterator().hasNext()这个必须判断下
        Assert.isTrue(validators.isEmpty(), validators.iterator().hasNext() ? validators.iterator().next().getMessage() : "ossConfig参数验证不通过");
        System.out.println(JSONObject.toJSONString(this));
    }
}
