# spring-boot-rocketmq

主要是借助rocketmq和spring boot的整合说明分布式事物

## 可以参考文档

* [docker-rocketmq](https://github.com/foxiswho/docker-rocketmq) 看网上说这个比官网的坑要少很多就用了

* [springboot-dubbo-seata](https://github.com/seata/seata-samples/tree/master/springboot-dubbo-seata) springboot-dubbo-seata

* [transaction-example](http://rocketmq.apache.org/docs/transaction-example/) rocketmq官网事物消息说明文档,大概就是一个事物消息15执行次后就放弃

## 使用前准备

* nacos 用于dubbo服务的注册

* rocketmq 4.5.2 ()

## 基本概念

### seata

 * Transaction Coordinator(TC)：管理全局的分支事务的状态，用于全局性事务的提交和回滚。
 
 * Transaction Manager(TM)：事务管理器，用于开启全局事务、提交或者回滚全局事务，是全局事务的开启者。
 
 * Resource Manager(RM)：资源管理器，用于分支事务上的资源管理，向TC注册分支事务，上报分支事务的状态，接受TC的命令来提交或者回滚分支事务。