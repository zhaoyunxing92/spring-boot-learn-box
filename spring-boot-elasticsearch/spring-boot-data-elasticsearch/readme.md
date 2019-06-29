# spring-boot-data-elasticsearch

## pom.xml文件

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
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
      cluster-name: elasticsearch
```