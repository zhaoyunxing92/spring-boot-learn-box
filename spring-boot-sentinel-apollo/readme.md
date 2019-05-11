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
        ds:
          apollo:
            namespaceName: application # 命名空间
            flowRulesKey: flowRules # apollo 上配置的key
            rule-type: flow
app:
  id: ${spring.application.name}
apollo:
  meta: http://127.0.0.1:8080
  cacheDir: ./apolloconfig  # 缓存文件位置
#  bootstrap:  todo：缓存文件删除是否可用
#    enabled: true
#    namespaces: application
#    eagerLoad:
#      enabled: true
```
* flowRules参数规则说明
```json

```