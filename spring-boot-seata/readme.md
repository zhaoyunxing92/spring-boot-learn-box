# spring-boot-seata

基于dubbo使用seata实现分布式事物实现`wangwu`给`zhangsan`转账

## 使用环境信息

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

## 可能遇到问题

* io.seata.config.ConfigurationFactory#FileConfiguration,所以`registry.conf`这个文件是必须存在的

## 特别注意

*  如果`registry.conf`是`nacos模式`那么必须修改seata服务的`conf/nacos-config.txt`文件,并且运行`sh nacos-config.sh 127.0.0.1`,可以参考[seata+nacos](https://github.com/yuhuangbin/spring-cloud-alibaba-samples)