# apollo
> [apollo](https://github.com/ctripcorp/apollo)

Apollo（阿波罗）是携程框架部门研发的分布式配置中心，能够集中化管理应用不同环境、不同集群的配置，配置修改后能够实时推送到应用端，并且具备规范的权限、流程治理等特性，适用于微服务配置管理场景。

## 准备工作
 * 下载[apollo-build-scripts](https://github.com/nobodyiam/apollo-build-scripts.git)或者源码编译
 * 使用包说明
   * [apollo-adminservice](https://github.com/ctripcorp/apollo/tree/master/apollo-adminservice) 提供配置的修改、发布等功能，服务对象是Apollo Portal（管理界面）
   * [apollo-configservice](https://github.com/ctripcorp/apollo/tree/master/apollo-configservice)和Admin Service都是多实例、无状态部署，所以需要将自己注册到Eureka中并保持心跳
   * [apollo-portal](https://github.com/ctripcorp/apollo/tree/master/apollo-portal)管理界面
 * mysql 5.7安装可以参考[docker for mysql](https://github.com/zhaoyunxing92/docker-case/tree/develop/mysql)
   * ApolloPortalDB和ApolloConfigDB两个库创建 [sql](./doc)
 * 修改数据库配置
   
## [spring boot 整合](https://github.com/ctripcorp/apollo/wiki/Java%E5%AE%A2%E6%88%B7%E7%AB%AF%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97#3213-spring-boot%E9%9B%86%E6%88%90%E6%96%B9%E5%BC%8F%E6%8E%A8%E8%8D%90)
 * pom
 ```xml
  <dependencies>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
       
       <dependency>
           <groupId>com.ctrip.framework.apollo</groupId>
           <artifactId>apollo-client</artifactId>
       </dependency>
   </dependencies>
 ```
 * 启动文件
 ```java
    @SpringBootApplication
    @EnableApolloConfig // 开启apollo
    public class SpringApolloServer {
    
        public static void main(String[] args) {
            SpringApplication.run(SpringApolloServer.class, args);
        }
    }
 ```
 * application.yml
 ```yaml
    app:
      id: spring-boot-apollo # 配置的id
    apollo:
      meta: http://127.0.0.1:8080 # 拉去配置文件地址
      cacheDir: ./apolloconfig  # 缓存文件位置
      bootstrap:
        enabled: true
        namespaces: application,application.yml
        eagerLoad:
          enabled: true # 优先加载
 ```
 * jvm启动添加 `-Denv=DEV`指定环境
 ```shell
  java -jar -Denv=DEV
 ```