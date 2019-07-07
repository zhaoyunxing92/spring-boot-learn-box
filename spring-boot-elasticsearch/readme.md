# spring-boot-elasticsearch

## elasticsearch有三种操作方式
  
 - [x] [elasticsearch-in-java](./elasticsearch-in-java) 使用elasticsearch自带的api操作
 - [x] [spring-data-elasticsearch](./spring-boot-data-elasticsearch) spring data方式操作elasticsearch
 - [x] [jest-elasticsearch](./spring-boot-jest-elasticsearch) jest 操作elasticsearch，这个懒得写了可以看【[elasticsearch入门到放弃之elasticsearch java](https://www.jianshu.com/p/9f6f7f67df4e)】都差不多
 
## 可能遇到的问题

 * Consider defining a bean of type 'UserService' in your configuration.

  出现这个问题是`spring.data.elasticsearch.repositories.enabled`属性需要设置为`true`
  
 * None of the configured nodes are available: [{#transport#-1}{-sq-D5sBSTCbdr1apbd9WA}{172.26.104.209}{172.26.104.209:9300}] 
  
    * 出现这个问题需要验证下你`spring.data.elasticsearch.cluster-name`属性配置的是否跟你es配置的名称一致
  
    * 检查你的`spring.data.elasticsearch.cluster-nodes`端口是否配置正确`9300`而不是`9200`
    
    * es开启了xpack认证，账号密码不对或者没有设置 