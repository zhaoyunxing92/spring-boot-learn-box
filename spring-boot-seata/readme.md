# spring-boot-seata

基于dubbo使用seata实现分布式事物

## 使用环境

* jdk 1.8

* seata 0.9.0

* dubbo 2.7.3

* mysql 5.7

* spring boot 2.1.0.RELEASE

* nacos 1.1.1

## 参考文档

* [dubbo-spring-boot](https://github.com/apache/dubbo-spring-boot-project/blob/master/README_CN.md) dubbo在spring boot 中使用

## maven 打包

```shell
mvn clean package -pl spring-boot-seata/dubbo-seata-zhangsan,spring-boot-seata/dubbo-seata-wangwu -am
```