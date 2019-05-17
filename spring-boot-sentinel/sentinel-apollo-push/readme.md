# sentinel-apollo-推模式

> 代码地址:[https://github.com/zhaoyunxing92/spring-boot-learn-box/tree/master/spring-boot-sentinel/sentinel-apollo](https://github.com/zhaoyunxing92/spring-boot-learn-box/tree/master/spring-boot-sentinel/sentinel-apollo) 

使用[apollo](https://github.com/ctripcorp/apollo)为sentinel做数据持久化,sentinel的使用可以看[sentinel使用](https://github.com/zhaoyunxing92/spring-boot-learn-box/tree/master/spring-boot-sentinel),本项目打算采用[spring-cloud-alibaba](https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/Sentinel)跟sentinel整合

## 参考文档

* [Sentinel Dashboard同步Apollo存储规则](https://www.jianshu.com/p/fd798ebf5dbe)

## 整合流程

### pom.xml

```xml
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
      <version>0.9.0.RELEASE</version>
  </dependency>
  <dependency>
      <groupId>com.alibaba.csp</groupId>
      <artifactId>sentinel-datasource-apollo</artifactId>
      <version>1.6.0</version>
  </dependency>
```

### application.yml

```yaml

spring:
  application:
    name: sentinel-apollo-push
  cloud:
    sentinel:
      transport:
        port: 8719 # 向sentinel-dashboard传输数据的端口 默认:8719
        dashboard: localhost:8100 # sentinel-dashboard
      log:
        dir: ./logs # 默认值${home}/logs/csp/
        switch-pid: true # 日志带上线程id
      datasource:
        flow: # 流控规则
          apollo:
            namespaceName: application
            flowRulesKey: sentinel.flowRules # 注意该key必须固定
            rule-type: flow #flow,degrade,authority,system, param-flow
        degrade: # 熔断降级规则
          apollo:
            namespaceName: application
            flowRulesKey: sentinel.degradeRules
            rule-type: degrade
        authority: # 授权规则
          apollo:
            namespaceName: application
            flowRulesKey: sentinel.authorityRules
            rule-type: authority
        system: # 系统规则
          apollo:
            namespaceName: application
            flowRulesKey: sentinel.systemRules
            rule-type: system
        param-flow: # 热点规则
          apollo:
            namespaceName: application
            flowRulesKey: sentinel.paramFlowRules
            rule-type: param-flow
app:
  id: ${spring.application.name}
apollo:
  meta: http://127.0.0.1:8080
  cacheDir: ./apolloconfig  # 缓存文件位置
```

### java

```java
@SpringBootApplication
@EnableApolloConfig // 开启apollo
public class SpringSentinelApolloServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringSentinelApolloServer.class, args);
    }
}
```
### jvm参数配置

```shell
-Denv=DEV
```
### apollo申请token

![apollo-token](https://gitee.com/sunny9/resource/raw/master/sentinel/apollo-token.png)

### sentinel-dashboard 设置token

```shell
--apollo.portal.token= apollo申请的token
```

### 测试接口

http://localhost:7853/sentinel/hello

### apollo效果图
![sentinel-dashboard-push](https://gitee.com/sunny9/resource/raw/master/sentinel/sentinel-dashboard-push.png)