# sentinel-apollo
使用[apollo](https://github.com/ctripcorp/apollo)为sentinel做数据持久化,sentinel的使用可以看[sentinel使用](https://github.com/zhaoyunxing92/spring-boot-learn-box/tree/master/spring-boot-sentinel),本项目打算采用[spring-cloud-alibaba](https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/Sentinel)跟sentinel整合

## 参考文档
* [sentinel动态规则扩展](https://github.com/alibaba/Sentinel/wiki/%E5%8A%A8%E6%80%81%E8%A7%84%E5%88%99%E6%89%A9%E5%B1%95)
* [Sentinel使用Apollo存储规则](https://www.jianshu.com/p/f31c86628994)
* [spring-cloud-alibaba](https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/Sentinel)

## 整合流程

* pom.xml
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
* application.yml
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
        dir: ./logs
      datasource:
        ds1:
          apollo:
            namespaceName: application # 命名空间
            flowRulesKey: flowRules # apollo 上配置的key
            rule-type: flow #flow,degrade,authority,system, param-flow
app:
  id: ${spring.application.name}
apollo:
  meta: http://127.0.0.1:8080
  cacheDir: ./apolloconfig  # 缓存文件位置
```
* java
```java
@SpringBootApplication
@EnableApolloConfig // 开启apollo
public class SpringSentinelApolloServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringSentinelApolloServer.class, args);
    }
}
```

* flow(流控规则)参数格式json
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
* flow(流控规则)参数规则说明
|      字段       |                             描述                             |  默认值  |
| :-------------: | :----------------------------------------------------------: | :------: |
|    resource     |                 资源名，即限流规则的作用对象                 |          |
|    limitApp     |      流控针对的调用来源，若为 default 则不区分调用来源       | default  |
|      grade      | 限流阈值类型（QPS 或并发线程数）；0代表根据并发数量来限流，1代表根据QPS来进行流量控制 | QPS 模式 |
|      count      |                           限流阈值                           |          |
|    strategy     |                       调用关系限流策略                       |          |
| controlBehavior |         流量控制效果（直接拒绝、Warm Up、匀速排队）          |   拒绝   |
|   clusterMode   |                        是否为集群模式                        |          |

* degrade 参数格式 json
```json

```
* degrade参数规则说明

* 拉去规则成功日志
```log
2019-05-12 14:30:50.811  INFO 27388 --- [           main] o.s.c.a.s.c.SentinelDataSourceHandler    : [Sentinel Starter] DataSource ds-sentinel-apollo-datasource load 1 FlowRule
```