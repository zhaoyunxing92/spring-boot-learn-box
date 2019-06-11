# spring-boot-skywalking

> 基于5.x版本编译，elasticsearch首先搭建完成,可以去看我的[docker for elasticsearch](<https://github.com/zhaoyunxing92/docker-case/tree/develop/elasticsearch>)
>
> 微信公众号用户可以直接跳转到:<https://www.jianshu.com/u/c5a69d112958>

## 参考文档

> <https://github.com/apache/skywalking/blob/5.x/docs/README_ZH.md>

### 下载地址

```shell
http://skywalking.apache.org/downloads/
```

### 解压

```shell
tar -xzf  apache-skywalking-apm-incubating.tar.gz
```

### 配置

> #### [参考地址](https://github.com/apache/incubator-skywalking/blob/5.x/docs/cn/Deploy-backend-in-cluster-mode-CN.md)

* apache-skywalking-apm-incubating/config/application.yml

```yaml
core:
  default:
    restHost: ${SW_CORE_REST_HOST:127.0.0.1}
    restPort: ${SW_CORE_REST_PORT:12800}
    restContextPath: ${SW_CORE_REST_CONTEXT_PATH:/}
    gRPCHost: ${SW_CORE_GRPC_HOST:127.0.0.1}
    gRPCPort: ${SW_CORE_GRPC_PORT:11800}
    downsampling:
    - Hour
    - Day
    - Month
    # Set a timeout on metric data. After the timeout has expired, the metric data will automatically be deleted.
    recordDataTTL: ${SW_CORE_RECORD_DATA_TTL:90} # Unit is minute
    minuteMetricsDataTTL: ${SW_CORE_MINUTE_METRIC_DATA_TTL:90} # Unit is minute
    hourMetricsDataTTL: ${SW_CORE_HOUR_METRIC_DATA_TTL:36} # Unit is hour
    dayMetricsDataTTL: ${SW_CORE_DAY_METRIC_DATA_TTL:45} # Unit is day
    monthMetricsDataTTL: ${SW_CORE_MONTH_METRIC_DATA_TTL:18} # Unit is month
storage: # 启用es
  elasticsearch:
    nameSpace: ${SW_NAMESPACE:skywalking}
    clusterNodes: ${SW_STORAGE_ES_CLUSTER_NODES:127.0.0.1:9200}
    indexShardsNumber: ${SW_STORAGE_ES_INDEX_SHARDS_NUMBER:2}
    indexReplicasNumber: ${SW_STORAGE_ES_INDEX_REPLICAS_NUMBER:0}
    # Batch process setting, refer to https://www.elastic.co/guide/en/elasticsearch/client/java-api/5.5/java-docs-bulk-processor.html
    bulkActions: ${SW_STORAGE_ES_BULK_ACTIONS:2000} # Execute the bulk every 2000 requests
    bulkSize: ${SW_STORAGE_ES_BULK_SIZE:20} # flush the bulk every 20mb
    flushInterval: ${SW_STORAGE_ES_FLUSH_INTERVAL:10} # flush the bulk every 10 seconds whatever the number of requests
    concurrentRequests: ${SW_STORAGE_ES_CONCURRENT_REQUESTS:2} # the number of concurrent requests
#  mysql:
receiver-register:
  default:
receiver-trace:
  default:
    bufferPath: ${SW_RECEIVER_BUFFER_PATH:../trace-buffer/}  # Path to trace buffer files, suggest to use absolute path
    bufferOffsetMaxFileSize: ${SW_RECEIVER_BUFFER_OFFSET_MAX_FILE_SIZE:100} # Unit is MB
    bufferDataMaxFileSize: ${SW_RECEIVER_BUFFER_DATA_MAX_FILE_SIZE:500} # Unit is MB
    bufferFileCleanWhenRestart: ${SW_RECEIVER_BUFFER_FILE_CLEAN_WHEN_RESTART:false}
    sampleRate: ${SW_TRACE_SAMPLE_RATE:10000} # The sample rate precision is 1/10000. 10000 means 100% sample in default.
receiver-jvm:
  default:
query:
  graphql:
    path: ${SW_QUERY_GRAPHQL_PATH:/graphql}
alarm:
  default:
```

* /apache-skywalking-apm-incubating/webapp/webapp.yml

```yaml
server:
  port: 7810
collector:
  path: /graphql
  ribbon:
    ReadTimeout: 10000
    # Point to all backend's restHost:restPort, split by ,
    listOfServers: 127.0.0.1:12800
security:
  user:
    # username
    admin:
      # password
      password: admin
```

### 启动

```shell
# /apache-skywalking-apm-incubating/bin
./startup.sh
```

## 客户端使用

> 默认情况下skywalking会加载/apache-skywalking-apm-incubating/agent/config/agent.config文件,也可以手动指定

#### jvm

```shell
java -javaagent:/apache-skywalking-apm-incubating/agent/skywalking-agent.jar 
     -Dskywalking.agent.service_name=trace-api 
     -Dskywalking.collector.backend_service=localhost:11800 
     -jar xxxx.jar
```

* `javaagent`  agent包路径
* `skywalking.agent.service_name` 服务名称
* `skywalking.collector.backend_service` 采集信息的服务地址  agent.config配置了就可以不用指定

#### agent.config配置

```verilog
# The agent namespace
# agent.namespace=${SW_AGENT_NAMESPACE:default-namespace}

# The service name in UI
agent.service_name=${SW_AGENT_NAME:Your_ApplicationName}

# The number of sampled traces per 3 seconds
# Negative number means sample traces as many as possible, most likely 100%
# agent.sample_n_per_3_secs=${SW_AGENT_SAMPLE:-1}

# Authentication active is based on backend setting, see application.yml for more details.
# agent.authentication = ${SW_AGENT_AUTHENTICATION:xxxx}

# The max amount of spans in a single segment.
# Through this config item, skywalking keep your application memory cost estimated.
# agent.span_limit_per_segment=${SW_AGENT_SPAN_LIMIT:300}

# Ignore the segments if their operation names start with these suffix.
# agent.ignore_suffix=${SW_AGENT_IGNORE_SUFFIX:.jpg,.jpeg,.js,.css,.png,.bmp,.gif,.ico,.mp3,.mp4,.html,.svg}

# If true, skywalking agent will save all instrumented classes files in `/debugging` folder.
# Skywalking team may ask for these files in order to resolve compatible problem.
# agent.is_open_debugging_class = ${SW_AGENT_OPEN_DEBUG:true}

# Backend service addresses.
collector.backend_service=${SW_AGENT_COLLECTOR_BACKEND_SERVICES:127.0.0.1:11800}

# Logging level
logging.level=${SW_LOGGING_LEVEL:DEBUG}
```

## 手动追踪

#### pom添加

```xml
   <dependency>
      <groupId>org.apache.skywalking</groupId>
      <artifactId>apm-toolkit-trace</artifactId>
      <version>${skywalking.version}</version>
   </dependency>
```

#### java代码

```java
    @GetMapping
    @Trace
    public String rest(String msg) {
        ActiveSpan.tag("args", "{msg:" + msg + "}");
        return helloService.say(msg);
    }
```

## logback日志集成

#### pom添加

```xml
<dependency>
   <groupId>org.apache.skywalking</groupId>
   <artifactId>apm-toolkit-logback-1.x</artifactId>
   <version>{project.release.version}</version>
</dependency>
```

#### logback-spring.xml配置

```xml
<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
            <layout class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout">
                <Pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%tid] [%thread] %-5level %logger{36} -%msg%n</Pattern>
            </layout>
        </encoder>
    </appender>
```

> 默认情况下输出`TID: N/A` 说以不用紧张