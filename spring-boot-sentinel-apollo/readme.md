# sentinel-apollo
使用[apollo](https://github.com/ctripcorp/apollo)为sentinel做数据持久化,sentinel的使用可以看[sentinel使用](https://github.com/zhaoyunxing92/spring-boot-learn-box/tree/master/spring-boot-sentinel),本项目打算采用[spring-cloud-alibaba](https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/Sentinel)跟sentinel整合

## 参考文档
* [sentinel动态规则扩展](https://github.com/alibaba/Sentinel/wiki/%E5%8A%A8%E6%80%81%E8%A7%84%E5%88%99%E6%89%A9%E5%B1%95)
* [Sentinel使用Apollo存储规则](https://www.jianshu.com/p/f31c86628994)
* [spring-cloud-alibaba](https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/Sentinel)

## 整合流程

* pom
```yaml
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
