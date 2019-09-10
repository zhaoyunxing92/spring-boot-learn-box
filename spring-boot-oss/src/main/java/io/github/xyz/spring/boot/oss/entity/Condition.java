/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.oss.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.aliyuncs.sts.transform.v20150401.AssumeRoleResponseUnmarshaller;
import lombok.Data;
import lombok.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@link Condition} 代表 Policy 授权的一些条件，上面的示例里面可以设置对于 acs:UserAgent 的检查、acs:SourceIp 的检查，还有 oss:Prefix 项用来在 GetBucket 的时候对资源进行限制。
 *
 * <table>
 * <thead>
 * <tr>
 * <th>condition</th>
 * <th>功能</th>
 * <th>合法取值</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>acs:SourceIp</td>
 * <td>指定 ip 网段</td>
 * <td>普通的 ip，支持*通配</td>
 * </tr>
 * <tr>
 * <td>acs:UserAgent</td>
 * <td>指定 http useragent 头</td>
 * <td>字符串</td>
 * </tr>
 * <tr>
 * <td>acs:CurrentTime</td>
 * <td>指定合法的访问时间</td>
 * <td>ISO8601格式</td>
 * </tr>
 * <tr>
 * <td>acs:SecureTransport</td>
 * <td>是否是 https 协议</td>
 * <td>“true”或者”false”</td>
 * </tr>
 * <tr>
 * <td>oss:Prefix</td>
 * <td>用作 ListObjects 时的 prefix</td>
 * <td>合法的object name</td>
 * </tr>
 * </tbody>
 * </table>
 *
 * <p>示例数据</p>
 * <pre>
 *  "Condition": {
 *                 "StringEquals": {
 *                     "acs:UserAgent": "java-sdk",
 *                     "oss:Prefix": "foo"
 *                 },
 *                 "IpAddress": {
 *                     "acs:SourceIp": "192.168.0.1"
 *                 }
 *             }
 * </pre>
 *
 * @author zhaoyunxing
 * @date: 2019-09-10 17:50
 * @desc:
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss.sts.policy.statement.condition")
@ConditionalOnClass(AssumeRoleResponseUnmarshaller.class)
public class Condition {
    /**
     * 指定 ip 网段,支持*通配
     */
    @JSONField(name = "acs:SourceIp")
    private List<String> sourceIp;
}
