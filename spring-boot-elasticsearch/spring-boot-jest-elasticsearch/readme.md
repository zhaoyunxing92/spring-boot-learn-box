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
      cluster-nodes: 172.26.104.209:9300
      cluster-name: elasticsearch
```

## 可能遇到的问题

 * Consider defining a bean of type 'UserService' in your configuration.

  出现这个问题是`spring.data.elasticsearch.repositories.enabled`属性需要设置为`true`
  
 * None of the configured nodes are available: [{#transport#-1}{-sq-D5sBSTCbdr1apbd9WA}{172.26.104.209}{172.26.104.209:9300}] 
  
    * 出现这个问题需要验证下你`spring.data.elasticsearch.cluster-name`属性配置的是否跟你es配置的名称一致
  
    * 检查你的端口是否配置正确`9300`而不是`9200`