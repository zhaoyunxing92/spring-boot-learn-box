# spring-boot-rabbitmq

## 文章列表

 * [x] [rabbitmq入门到放弃之docker rabbitmq](https://www.jianshu.com/p/f6999a902777) rabbitmq安装
 * [x] [exchange](./exchange.md) exchange交换机详解
 
 ## 可能遇到的问题
 
 * `User can only log in via localhost`
 
   按照官方的说法禁止使用guest/guest权限通过除localhost外的访问
   ```shell
    # 开启都可以远程登录，可以数组限制指定用户
    loopback_users = none
   ```