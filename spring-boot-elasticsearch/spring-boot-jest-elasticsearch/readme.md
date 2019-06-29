# spring-boot-jest-elasticsearch

## pom.xml配置

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
<dependency>
    <groupId>io.searchbox</groupId>
    <artifactId>jest</artifactId>
</dependency>
```

## application.yml配置

```yaml
spring:
  data:
    elasticsearch:
      repositories:
        enabled: true
      cluster-nodes: 127.0.0.1:9300
      cluster-name: elasticsearch # 集群名称
  elasticsearch:
    jest:
      uris:
        - http://127.0.0.1:9200 # 多个使用数组
      username: elastic  # 账号
      password: xxxx # 密码
```