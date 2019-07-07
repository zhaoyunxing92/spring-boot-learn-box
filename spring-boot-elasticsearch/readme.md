# spring-boot-elasticsearch

## elasticsearch有三种操作方式
  
 - [x] [elasticsearch-in-java](./elasticsearch-in-java) 使用elasticsearch自带的api操作
 - [x] [spring-data-elasticsearch](./spring-boot-data-elasticsearch) spring data方式操作elasticsearch
 - [x] [jest-elasticsearch](./spring-boot-jest-elasticsearch) jest 操作elasticsearch，这个懒得写了可以看【[elasticsearch入门到放弃之elasticsearch java](https://www.jianshu.com/p/9f6f7f67df4e)】都差不多
 
## 可能遇到的问题

 * `Consider defining a bean of type 'UserService' in your configuration.`

    出现这个问题是`spring.data.elasticsearch.repositories.enabled`属性需要设置为`true`
  
 * `None of the configured nodes are available: [{#transport#-1}{-sq-D5sBSTCbdr1apbd9WA}{172.26.104.209}{172.26.104.209:9300}]` 
  
    * 出现这个问题需要验证下你`spring.data.elasticsearch.cluster-name`属性配置的是否跟你es配置的名称一致
  
    * 检查你的`spring.data.elasticsearch.cluster-nodes`端口是否配置正确`9300`而不是`9200`
    
    * es开启了xpack认证，账号密码不对或者没有设置
    
 * `Caused by: java.lang.ClassNotFoundException: org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse`
  
   出现这个问题是因为你选择的`spring-boot-starter-data-elasticsearch`版本没有对`elasticsearch`兼容,你找一个有`PutMappingResponse`的版本，我选择的是`6.4.0`
 
 * `failed to load elasticsearch nodes : org.elasticsearch.index.mapper.MapperParsingException: analyzer [ik_smart] not found for field [name]`
   
   出现这个问题说明你的`elasticsearch`没有安装`ik`插件，可以看我的[elasticsearch-in-java](https://www.jianshu.com/p/9f6f7f67df4e)
 
 * `Constructor threw exception; nested exception is java.lang.IllegalArgumentException: Rejecting mapping update to [elastic] as the final mapping would have more than 1 type: [sunny, user]`
  
   出现这个问题是以为你的索引`elastic`存在两个mapping,在[elasticsearch-head](https://www.jianshu.com/p/80bb53bc1256)删除索引重新开始
   
 * `IllegalArgumentException[Parse failure at index [0] of [Sun Jul 07 06:41:39 UTC 2019]]`
    
   出现这个问题主要是你设置了日期类型跟参数类型不对称导致，入参改为string即可,可以参考[ArticleService#findArticlesByCreateTimeBetweenOrderByIdDesc](https://github.com/zhaoyunxing92/spring-boot-learn-box/blob/master/spring-boot-elasticsearch/spring-boot-data-elasticsearch/src/main/java/io/github/xyz/spring/boot/data/elasticsearch/service/ArticleService.java)是怎么处理的
   
 * `挂载容器内部目录到物理机上出现 [0.001s][error][logging] Error opening log file 'logs/gc.log': Permission denied`
 
      这个是权限问题
      
      ```shell
          # 给目录775权限
          sudo chmod -R 775 /data/es/
          # 修改文件归属者
          sudo chown -R 1000:1000 /data/es/
      ```
 
 * `java.lang.RuntimeException: can not run elasticsearch as root`
 
      > es禁止root用户直接运行,切换用户就可以了,不过用docker好像没有出现
 
 * `max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]`
 
      > jvm 内存不够需要修改系统jvm内存
    
      ```shell
       vim /etc/sysctl.conf
       # 追加配置
       vm.max_map_count=655300
      ```
      修改完后执行: `sudo sysctl -p`可以执行:`more /proc/sys/vm/max_map_count`验证下是否修改成功
 
 * `failed to read [id:1, file:/usr/share/elasticsearch/data/nodes/0/_state/node-1.st]`
 
   挂载的宿主目录文件加不是空导致,清空即可
 
 * `java.lang.OutOfMemoryError: Java heap space (oom和gc等这里问题我电脑无法还原就弄这个oom说明问题吧)`
 
   jvm内存设置的小了修改 `ES_JAVA_OPTS`参数即可
 
 * `docker启动了但是不能正常访问或者只能本地访问,另外在用网段的电脑不能访问`
 
   这个主要是docker网络模式的问题`network_mode: host`设置上就可以了,你也可以通过`docker inspect 容器id` 查看docker帮我们绑定的ip    