/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.oss.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.aliyuncs.sts.transform.v20150401.AssumeRoleResponseUnmarshaller;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhaoyunxing
 * @date: 2019-09-10 16:16
 * @desc: 通过Statement描述授权语义，其中可以根据业务场景包含多条语义，每条包含对 Action、Effect、Resource 和 Condition 的描述。每次请求系统会逐条依次匹配检查，所有匹配成功的 Statement 会根据 Effect 的设置不同分为通过（Allow）、禁止（Deny），其中禁止（Deny）的优先。如果匹配成功的都为通过，该条请求即鉴权通过。如果匹配成功有一条禁止，或者没有任何条目匹配成功，该条请求被禁止访问。
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss.sts.policy.statement")
@ConditionalOnClass(AssumeRoleResponseUnmarshaller.class)
public class Statement {
    /**
     * Effect 代表本条的Statement的授权的结果，分为 Allow 和 Deny，分别指代通过和禁止。多条 Statement 同时匹配成功时，禁止（Deny）的优先级更高。默认是Allow
     */
    @JSONField(name = "Effect")
    @Pattern(regexp = "(Allow|Deny)", message = "effect属性必须是：Allow or Deny")
    private String effect="Allow";
    /**
     * Action 分为三大类：
     *
     * <ul>
     * <li> Service 级别操作，对应的是 GetService 操作，用来列出所有属于该用户的 Bucket 列表。
     * <li> Bucket 级别操作，对应类似于 oss:PutBucketAcl、oss:GetBucketLocation之类的操作，操作的对象是 Bucket，它们的名称和相应的接口名称一一对应。
     * <li> Object 级别操作，分为 oss:GetObject、oss:PutObject、oss:DeleteObject和oss:AbortMultipartUpload，操作对象是 Object。
     * </ul>
     */
    @JSONField(name = "Action")
    private List<String> action = Arrays.asList("oss:ListObjects", "oss:GetObject");

    /**
     * Resource 指代的是 OSS 上面的某个具体的资源或者某些资源（支持*通配），resource的规则是acs:oss:{region}:{bucket_owner}:{bucket_name}/{object_name}。对于所有 Bucket 级别的操作来说不需要最后的斜杠和{object_name}，即acs:oss:{region}:{bucket_owner}:{bucket_name}。Resource 也是一个列表，可以有多个 Resource。其中的 region 字段暂时不做支持，设置为*。
     */
    @JSONField(name = "Resource")
    private List<String> resource;
    /**
     * {@link Condition} 代表 Policy 授权的一些条件，上面的示例里面可以设置对于 acs:UserAgent 的检查、acs:SourceIp 的检查，还有 oss:Prefix 项用来在 GetBucket 的时候对资源进行限制。
     */
    @Valid
    @JSONField(name = "Condition")
    private Condition condition;
}
