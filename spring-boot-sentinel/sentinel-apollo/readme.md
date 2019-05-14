# sentinel-apollo
使用[apollo](https://github.com/ctripcorp/apollo)为sentinel做数据持久化,sentinel的使用可以看[sentinel使用](https://github.com/zhaoyunxing92/spring-boot-learn-box/tree/master/spring-boot-sentinel),本项目打算采用[spring-cloud-alibaba](https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/Sentinel)跟sentinel整合

## 参考文档
* [sentinel动态规则扩展](https://github.com/alibaba/Sentinel/wiki/%E5%8A%A8%E6%80%81%E8%A7%84%E5%88%99%E6%89%A9%E5%B1%95)
* [Sentinel使用Apollo存储规则](https://www.jianshu.com/p/f31c86628994)
* [spring-cloud-alibaba](https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/Sentinel)

## 整合流程(拉模式)

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
    name: spring-boot-sentinel-apollo
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
            flowRulesKey: flowRules
            rule-type: flow #flow,degrade,authority,system, param-flow
        degrade: # 熔断降级规则
          apollo:
            namespaceName: application
            flowRulesKey: degrades
            rule-type: degrade
        authority: # 授权规则  未验证,官方不推荐
          apollo:
            namespaceName: application
            flowRulesKey: authoritys
            rule-type: authority
        system: # 系统规则
          apollo:
            namespaceName: application
            flowRulesKey: systems
            rule-type: system
        param-flow: # 热点规则
          apollo:
            namespaceName: application
            flowRulesKey: paramflows
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

### flow(流控规则)参数格式json

```json
[
    {
        "resource": "/hello", 
        "limitApp": "default",
        "grade": 1,
        "count": 3,
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    }
]
```

### flow(流控规则)参数规则说明

|      字段       |                             描述                             |  默认值  |
| :-------------: | :----------------------------------------------------------: | :------: |
|    resource     |                 资源名，即限流规则的作用对象                 |          |
|    limitApp     |      流控针对的调用来源，若为 default 则不区分调用来源       | default  |
|      grade      | 限流阈值类型（QPS 或并发线程数）；0代表根据并发数量来限流，1代表根据QPS来进行流量控制 | QPS 模式 |
|      count      |                           限流阈值                           |          |
|    strategy     |                       调用关系限流策略                       |          |
| controlBehavior |         流量控制效果（直接拒绝、Warm Up、匀速排队）          |   拒绝   |
|   clusterMode   |                        是否为集群模式                        |          |

### system 系统规则参数格式,四个参数只能选择一个不能设置-1

```
[{"qps": 2}]
```

### system参数列表

|   参数    |            说明            |
| :-------: | :------------------------: |
|  avgLoad  |       最大的 `load`        |
|   avgRt   | 所有入口流量的平均响应时间 |
| maxThread |    入口流量的最大并发数    |
|    qps    |     所有入口资源的 QPS     |




### degrade 参数格式 json

```json
[
    {
        "resource": "/rt",
        "count": 50,
        "timeWindow": 5,
        "grade": 0
    },
    {
        "resource": "/count",
        "count": 5,
        "timeWindow": 8,
        "grade": 2
    },
    {
        "resource": "/erro",
        "count": 0.5,
        "timeWindow": 5,
        "grade": 1
    }
]
```

### degrade(熔断降级规则)参数规则说明

|    字段     |                      描述                      | 默认值 |
| :--------: | :--------------------------------------------: | :----: |
|  resource  |          资源名，即限流规则的作用对象              |        |
|   count    |                      阈值                      |        |
|   grade    | 降级模式，根据 RT (0)、异常数(2)、 异常比例(1)      | RT (0) |
| timeWindow |              降级的时间，单位为 s                 |        |


### param-flow(热点规则) json

```json
[
    {
        "resource": "/hello",
        "grade": 1,
        "paramIdx": 1,
        "count": 10,
        "paramFlowItemList": []
    }
]
```

### param-flow(热点规则) 参数

|       字段        |                             描述                             | 默认值 |
| :---------------: | :----------------------------------------------------------: | :----: |
|     resource      |                         资源名，必填                         |        |
|       grade       |                           限流模式                           | qps(1) |
|     paramIdx      | 热点参数的索引，必填，对应 `SphU.entry(xxx, args)` 中的参数索引位置 |        |
|       count       |                        限流阈值，必填                        |        |
| paramFlowItemList | 参数例外项，可以针对指定的参数值单独设置限流阈值，不受前面 `count` 阈值的限制。 |        |



### apollo上配置

```properties
server.port = 7852
server.servlet.context-path = /sentinel
flowRules = [{"resource": "/hello","limitApp": "default","grade": 1,"count": 3,"strategy": 0,"controlBehavior": 0,"clusterMode": false}]
degrades = [{"resource": "/rt","count": 50,"timeWindow": 5,"grade": 0},{"resource": "/count","count": 5,"timeWindow": 8,"grade": 2},{"resource": "/erro","count": 0.5,"timeWindow": 5,"grade": 1}]
authoritys = [{"resource": "/hello","limitApp": "192.168.12.215","strategy": 1}]
paramflows = [{"resource": "/hello","grade": 1,"paramIdx": 1,"count": 10,"paramFlowItemList": []}]
systems = [{"qps": 20}]
```

### 拉去规则成功日志

```log
  2019-05-14 09:26:46.072  INFO 10100 --- [           main] o.s.c.a.s.c.SentinelDataSourceHandler    : [Sentinel Starter] DataSource authority-sentinel-apollo-datasource load 1 AuthorityRule
  2019-05-14 09:26:46.090  INFO 10100 --- [           main] o.s.c.a.s.c.SentinelDataSourceHandler    : [Sentinel Starter] DataSource degrade-sentinel-apollo-datasource load 3 DegradeRule
  2019-05-14 09:26:46.099  INFO 10100 --- [           main] o.s.c.a.s.c.SentinelDataSourceHandler    : [Sentinel Starter] DataSource flow-sentinel-apollo-datasource load 1 FlowRule
  2019-05-14 09:26:46.115  INFO 10100 --- [           main] o.s.c.a.s.c.SentinelDataSourceHandler    : [Sentinel Starter] DataSource param-flow-sentinel-apollo-datasource load 1 ParamFlowRule
  2019-05-14 09:26:46.122  INFO 10100 --- [           main] o.s.c.a.s.c.SentinelDataSourceHandler    : [Sentinel Starter] DataSource system-sentinel-apollo-datasource load 1 SystemRule
```

### 测试接口

http://localhost:7852/sentinel/hello

### 最终效果图

![sentinel-dashboard](https://gitee.com/sunny9/resource/raw/master/sentinel/sentinel.png)

![apollo-dashboard](https://gitee.com/sunny9/resource/raw/master/sentinel/apollo.png)