# spring-boot-oss

  这个分支主要解决阿里云oss的使用

## 参考的文档

* [Policy属性含义](https://help.aliyun.com/document_detail/100680.html?spm=a2c4g.11186623.2.8.320841f0tGGFLR#h2-url-7) 这个很关键，字段含义都在里面

* [Policy策略编辑器](http://gosspublic.alicdn.com/ram-policy-editor/index.html?spm=a2c4g.11186623.2.10.3a8f3eb10uleqj) Policy策略编辑器

* [快速搭建移动应用直传服务](https://help.aliyun.com/document_detail/31920.html?spm=a2c4g.11186623.6.1378.1b307d43Se1NJZ) 快速搭建移动应用直传服务

* [oss-sts](https://help.aliyun.com/document_detail/100624.html?spm=5176.10695662.1996646101.searchclickresult.64e0515aCLJzVr) STS（Security Token Service）进行临时授权访问

* [AssumeRole](https://helpcdn.aliyun.com/document_detail/28763.html?spm=a2c4g.11186623.2.16.481e7074k4fkgq#reference-clc-3sv-xdb) 调用AssumeRole接口获取一个扮演该角色的临时身份

* [sts-endpoint](https://helpcdn.aliyun.com/document_detail/66053.html?spm=a2c4g.11186623.2.15.481e7074bRCZ8B#reference-sdg-3pv-xdb) STS服务的所有接入地址

## 使用的关键jar

* pom.xml
```xml
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>aliyun-java-sdk-sts</artifactId>
    <version>3.0.0</version>
</dependency>
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>aliyun-java-sdk-core</artifactId>
    <version>3.5.0</version>
</dependency>
```

* OssStsConfig
```java
@Data
@ToString
@Component
@ConfigurationProperties(prefix = "oss.sts")
@ConditionalOnClass(AssumeRoleResponseUnmarshaller.class)
public class OssStsConfig {
    /**
     * 是否启用sts，默认启用
     */
    private Boolean enable = true;
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
    @NotNull(message = "请配置【durationSeconds】属性")
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
        Iterator<ConstraintViolation<OssStsConfig>> iterator = validators.iterator();
        Assert.isTrue(validators.isEmpty(), iterator.hasNext() ? iterator.next().getMessage() : "OssStsConfig参数验证不通过");
    }
}
```

* java
```java
@RestController
@Slf4j
public class OssController {

    private final OssStsConfig ossConfig;

    @Autowired
    public OssController(OssStsConfig ossConfig) { this.ossConfig = ossConfig; }

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
            request.setPolicy(JSONObject.toJSONString(ossConfig.getPolicy()));
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
```
## spring boot starter

[oss-spring-boot-starter](https://github.com/zhaoyunxing92/sunny-spring-boot-starter/tree/master/oss-spring-boot-starter)
