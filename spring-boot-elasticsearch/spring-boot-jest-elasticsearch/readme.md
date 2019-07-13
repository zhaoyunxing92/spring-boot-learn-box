# spring-boot-jest-elasticsearch

懒得写了可以看【[elasticsearch入门到放弃之elasticsearch java](https://www.jianshu.com/p/9f6f7f67df4e)】都差不多

## pom.xml配置

```xml
<dependency>
    <groupId>io.searchbox</groupId>
    <artifactId>jest</artifactId>
</dependency>
```

## application.yml配置

```yaml
spring:
  elasticsearch:
    jest:
      uris:
        - http://127.0.0.1:9200 # 多个使用数组
      username: elastic  # 账号
      password: xxxx # 密码
```