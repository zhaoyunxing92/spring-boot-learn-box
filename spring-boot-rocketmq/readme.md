# spring-boot-rocketmq

主要是借助rocketmq和spring boot的整合说明分布式事物

## 可以参考文档

* [docker-rocketmq](https://github.com/foxiswho/docker-rocketmq) 看网上说这个比官网的坑要少很多就用了

* [springboot-dubbo-seata](https://github.com/seata/seata-samples/tree/master/springboot-dubbo-seata) springboot-dubbo-seata

* [transaction-example](http://rocketmq.apache.org/docs/transaction-example/) rocketmq官网事物消息说明文档,大概就是一个事物消息15执行次后就放弃

## 使用前准备

* nacos 用于dubbo服务的注册

* rocketmq 4.5.2 (主要用事物消息)

* seata 0.9.0

## 本地编译jar

```shell
#根目录下
mvn clean package -pl spring-boot-rocketmq/dubbo-dtx-zhangsan-bank,spring-boot-rocketmq/dubbo-dtx-lisi-bank/dubbo-dtx-lisi-bank-server -am
```

## 基本概念

### seata

 * Transaction Coordinator(TC)：管理全局的分支事务的状态，用于全局性事务的提交和回滚。
 
 * Transaction Manager(TM)：事务管理器，用于开启全局事务、提交或者回滚全局事务，是全局事务的开启者。
 
 * Resource Manager(RM)：资源管理器，用于分支事务上的资源管理，向TC注册分支事务，上报分支事务的状态，接受TC的命令来提交或者回滚分支事务。
 
#### seata获取TM地址流程
 
 1. 项目启动后先调用:`GlobalTransactionAutoConfiguration#globalTransactionScanner` 这个方法会设置事物组默认是`${spring.application.name}-fescar-service-group`
 
 2. `GlobalTransactionScanner` bean初始化时候调用`afterPropertiesSet()`然后调用`initClient()`由于我们是TM所以看`TMClient.init(applicationId, txServiceGroup)`
 
 3. TMClient.init 方法调用`TmRpcClient.init()` 然后调用 `AbstractRpcRemotingClient.init()` 中独立一个线程` clientChannelManager.reconnect(getTransactionServiceGroup());` 然后调用`io.seata.core.rpc.netty.NettyClientChannelManager.getAvailServerList`
 
 4. `io.seata.discovery.registry.RegistryFactory.getInstance().lookup(transactionServiceGroup)` 由于配置里面是`file`模式所以:`io.seata.discovery.registry.FileRegistryServiceImpl.lookup()`
 
 5.  `String clusterName = config.getConfig(service + . + vgroup_mapping.+ key)` 这个key就是第一步设置的
 
 6. 根据`clusterName`的值然后再 `String endpointStr = CONFIG.getConfig(service + . + clusterName + .grouplist);` 至此配置地址获取完成