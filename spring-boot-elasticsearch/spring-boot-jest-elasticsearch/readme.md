# spring-boot-jest-elasticsearch

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