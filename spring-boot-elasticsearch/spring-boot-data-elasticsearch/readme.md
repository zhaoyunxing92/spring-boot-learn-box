# spring-boot-data-elasticsearch

> 代码地址：https://github.com/zhaoyunxing92/spring-boot-learn-box/tree/master/spring-boot-elasticsearch/spring-boot-data-elasticsearch

这个就跟我在以前写的【[springboot mongodb配置解析](https://www.jianshu.com/p/a980d7fcc1d9)】一样效果，单纯的以为设置好正好密码就可以很嗨皮的使用`mongodb`了，`elasticsearch`同样也遇到了该问题。期间的过程我不想多说了，直接往下看吧.(下面主要在`x-pack`开启模式下使用,正常模式下很简单不写了)

## 系列文章

* [elasticsearch入门到放弃之docker搭建](https://www.jianshu.com/p/ba7caa5bed53) es环境搭建
* [elasticsearch入门到放弃之x-pack安全认证](https://www.jianshu.com/p/3b01817996c8) x-pack保驾护航你的es
* [elasticsearch入门到放弃之elasticsearch-head](https://www.jianshu.com/p/80bb53bc1256) es-head可视化你的es
* [elasticsearch入门到放弃之elasticsearch-in-java](https://www.jianshu.com/p/9f6f7f67df4e) 这个很关键，看完这个，再看这个就很容易理解了

## 参考文档

* [spring-data-elasticsearch](https://github.com/spring-projects/spring-data-elasticsearch) 官方的代码最能说明问题了

* [官方文档](https://docs.spring.io/spring-data/elasticsearch/docs/2.1.22.RELEASE/reference/html/) 官方的文档也是很棒的

* [官方日期处理](https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-date-format.html#strict-date-time) 日期

## 环境信息

> 环境信息很重要,不然很难成功

* jdk 1.8
* elasticsearch 6.8.2
* x-pack 6.8.2
* spring-boot-starter-data-elasticsearch 6.8.2
* spring-boot 2.1.0 (它默认带的elasticsearch是6.2.2的)

## pom.xml文件

```xml
 <!--修改es版本这样设置最简单-->
<properties>
    <elasticsearch.version>6.8.2</elasticsearch.version>
</properties>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.elasticsearch.client</groupId>
            <artifactId>transport</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>x-pack-transport</artifactId>
    <version>${elasticsearch.version}</version>
</dependency>

<!--这个好像要添加不然下载不到 spring-boot-starter-data-elasticsearch:6.8.2 -->
<repository>
    <id>spring-libs-snapshot</id>
    <name>Spring Snapshot Repository</name>
    <url>https://repo.spring.io/libs-snapshot</url>
</repository>
```

## application.yml配置

```yaml
spring:
  data:
    elasticsearch:
      repositories:
        enabled: true
      cluster-nodes: 172.26.104.209:9300 # 集群模式下用逗号分开
      cluster-name: elasticsearch
      properties:
        xpack.security.user: elastic:123456
```
## spring-data-elasticsearch 主键解释

### @Document

这个主键对应的[ElasticsearchCase](https://github.com/zhaoyunxing92/spring-boot-learn-box/blob/develop/spring-boot-elasticsearch/elasticsearch-in-java/src/test/java/ElasticsearchCase.java)#createIndex()方法

* `indexName` 索引名称
* `type` 类型
* `useServerConfiguration` 是否使用系统配置
* `shards` 集群模式下分片存储，默认分5片
* `replicas` 数据复制几份，默认一份
* `refreshInterval` 多久刷新数据 默认:1s
* `indexStoreType` 索引存储模式 默认:fs，为深入研究
* `createIndex` 是否创建索引，默认:true

### @Id

看名字就知道了，不解释了

### @Field

这个主键对应的[ElasticsearchCase](https://github.com/zhaoyunxing92/spring-boot-learn-box/blob/develop/spring-boot-elasticsearch/elasticsearch-in-java/src/test/java/ElasticsearchCase.java)#setMappings()方法

* `type` 字段类型 默认根据java类型推断,可选类型：Text,Integer,Long,Date,Float,Double,Boolean,Object,Auto,Nested,Ip,Attachment,Keyword,新的数据类型请参考官网
* `index` 应该是建索引没有研究过
* `format` 数据格式，可以理解为一个正则拦截可存储的数据格式
* `pattern` 使用场景：format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS"
* `searchAnalyzer` 搜索的分词，新的ik分词只有`ik_smart`和`ik_max_word`两个模式
* `store` 是否单独存储，应该是存储在`_source`
* `analyzer` 分词模式
* `ignoreFields` 没有研究过
* `includeInParent` 没有研究过
* `fielddata` 没有研究过


## spring-data-elasticsearch 正式编码

我这里是重写了`TransportClient`类，不过很抱歉elasticsearch的开发者说到8.0就换一种方式了。[issues#188983](https://discuss.elastic.co/t/hello-how-should-i-initialization-prebuiltxpacktransportclient-i-opened-the-x-pack/188983)

### ElasticsearchConfig.java

> 主要是替换`TransportClientFactoryBean`为`PreBuiltXPackTransportClient`,支持`x-pack`

```java
/**
 * @author zhaoyunxing
 * @date: 2019-07-07 09:47
 * @des: es 配置类
 * @see org.springframework.data.elasticsearch.client.TransportClientFactoryBean
 * @see org.springframework.data.elasticsearch.client.ClusterNodes
 */
@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchConfig {

    private final ElasticsearchProperties properties;

    public ElasticsearchConfig(ElasticsearchProperties properties) {
        this.properties = properties;
    }

    @Bean
    public TransportClient transportClient(){
        return new PreBuiltXPackTransportClient(settings())
                .addTransportAddresses(addresses());
    }

    /**
     * .put("client.transport.sniff", true)
     * .put("client.transport.ignore_cluster_name", false)
     * .put("client.transport.ping_timeout", clientPingTimeout)
     * .put("client.transport.nodes_sampler_interval", clientNodesSamplerInterval)
     *
     * @return Settings
     */
    private Settings settings() {
        Settings.Builder builder = Settings.builder();
        builder.put("cluster.name", properties.getClusterName());
        properties.getProperties().forEach(builder::put);
        return builder.build();
    }

    private TransportAddress[] addresses() {
        String clusterNodesStr = properties.getClusterNodes();
        Assert.hasText(clusterNodesStr, "Cluster nodes source must not be null or empty!");
        String[] nodes = StringUtils.delimitedListToStringArray(clusterNodesStr, ",");

        return Arrays.stream(nodes).map(node -> {
            String[] segments = StringUtils.delimitedListToStringArray(node, ":");
            Assert.isTrue(segments.length == 2,
                    () -> String.format("Invalid cluster node %s in %s! Must be in the format host:port!", node, clusterNodesStr));
            String host = segments[0].trim();
            String port = segments[1].trim();
            Assert.hasText(host, () -> String.format("No host name given cluster node %s!", node));
            Assert.hasText(port, () -> String.format("No port given in cluster node %s!", node));
            return new TransportAddress(toInetAddress(host), Integer.valueOf(port));
        }).toArray(TransportAddress[]::new);
    }

    private static InetAddress toInetAddress(String host) {
        try {
            return InetAddress.getByName(host);
        } catch (UnknownHostException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
```

### Article.java

> 主要看点是我时间格式的处理解决上篇【[elasticsearch入门到放弃之elasticsearch-in-java](https://www.jianshu.com/p/9f6f7f67df4e)】遗留的问题

```java
/**
 * @author zhaoyunxing
 * @date: 2019-06-28 14:38
 * @des:
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "blog", type = "article")
public class Article {
    @Id
    @Field(type = FieldType.Text, store = false)
    private String id;
    /**
     * 文章名称
     */
    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_max_word")
    private String name;
    /**
     * 内容
     */
    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_max_word")
    private String content;
    /**
     * 创建时间 采用自定义的时间格式
     */
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss:SSS||yyyy-MM-dd||epoch_millis||date_optional_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "GMT+8")
    private Date createTime;
}
```

### CURD操作

这里你只要熟悉[spring-data-elasticsearch](https://docs.spring.io/spring-data/elasticsearch/docs/2.1.22.RELEASE/reference/html/)的命名规则就很嗨皮的编写代码的，不了解就去翻文档吧,`IDAE`编辑器也会提示你怎么写。我就直接贴代码了

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchCase {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 创建文章,更新文档只有保证id一直就可以了
     */
    @Test
    public void addDocument() {
        System.out.println("***********************一次性插入30条数据***********************");
        List<Article> articles = new ArrayList<>(30);
        for (int i = 1; i <= 30; i++) {
            Article article = new Article();
            article.setName("elasticsearch入门到放弃之docker搭建" + i);
            article.setContent("在我的观念里elasticsearch是" + i + "大数据的产物");
            article.setCreateTime(new Date());
            article.setId(String.valueOf(i));
            articles.add(article);
        }
        articleService.saveAll(articles);
    }

    /**
     * 查询全部 默认是查询10条数据的，但是findAll()查询的是全部数据，说明它真的是findAll()
     */
    @Test
    public void findAll() {
        System.out.println("***********************获取全部数据***********************");
        articleService.findAll().forEach(System.out::println);
    }

    /**
     * 根据id查询
     */
    @Test
    public void findById() {
        System.out.println("***********************根据id查询***********************");
        Article article = articleService.findById(String.valueOf(27)).orElseGet(Article::new);
        System.out.println(article);
    }

    /**
     * 根据名称查询 默认值返回10条数据
     */
    @Test
    public void findByName() {
        System.out.println("***********************根据名称查询***********************");
        articleService.findArticleByName("docker搭建").forEach(System.out::println);
    }

    /**
     * 标题中包含或者内容中包含,设置分页 三条数据分页
     */
    @Test
    public void findArticleByNameOrContent() {
        System.out.println("***********************根据名称和内容查询***********************");
        articleService.findArticleByNameOrContent("docker搭建", "30", PageRequest.of(0, 3)).forEach(System.out::println);
    }

    /**
     * 指定时间域名并且根据id进行deac排序(aec)类似不写了
     */
    @Test
    public void findArticlesByCreateTimeBetweenAndOrderByCreateTimeDesc() {
        System.out.println("***********************指定时间域名并且根据id进行deac排序***********************");

        articleService.findArticlesByCreateTimeBetweenOrderByIdDesc("2019-07-07 14:41:39:998", "2019-07-07 16:33:29:175", PageRequest.of(0, 3)).forEach(System.out::println);
    }

    /**
     * 模糊匹配查询对应的是QueryString方法这个可以参考：<a>https://www.jianshu.com/p/9f6f7f67df4e</a>
     */
    @Test
    public void nativeSearchQuery() {
        System.out.println("***********************模糊查询***********************");
        // 构建查询对象
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("sunny在学用docker搭建elasticsearch环境").defaultField("content"))
                .withPageable(PageRequest.of(0, 3))
                .build();

        elasticsearchTemplate.queryForList(query, Article.class).forEach(System.out::println);
    }

    /**
     * 删除文章 deleteAll()、delete() 这些方法类似不写了
     */
    @Test
    public void deleteDocumentById() {
        System.out.println("***********************根据id删除***********************");
        articleService.deleteById("1");
    }
}
```

## 可能遇到的问题

* `Caused by: java.lang.ClassNotFoundException: org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse`
 
  出现这个问题是因为你选择的`spring-boot-starter-data-elasticsearch`版本没有对`elasticsearch`兼容,你找一个有`PutMappingResponse`的版本，我选择的是`6.8.2`

* `failed to load elasticsearch nodes : org.elasticsearch.index.mapper.MapperParsingException: analyzer [ik_smart] not found for field [name]`
  
  出现这个问题说明你的`elasticsearch`没有安装`ik`插件，可以看我的[elasticsearch-in-java](https://www.jianshu.com/p/9f6f7f67df4e)

* `Constructor threw exception; nested exception is java.lang.IllegalArgumentException: Rejecting mapping update to [elastic] as the final mapping would have more than 1 type: [sunny, user]`
 
  出现这个问题是以为你的索引`elastic`存在两个mapping,在[elasticsearch-head](https://www.jianshu.com/p/80bb53bc1256)删除索引重新开始
  
* `IllegalArgumentException[Parse failure at index [0] of [Sun Jul 07 06:41:39 UTC 2019]]`
  
  出现这个问题主要是你设置了日期类型跟参数类型不对称导致，入参改为string即可,可以参考[ArticleService#findArticlesByCreateTimeBetweenOrderByIdDesc](https://github.com/zhaoyunxing92/spring-boot-learn-box/blob/master/spring-boot-elasticsearch/spring-boot-data-elasticsearch/src/main/java/io/github/xyz/spring/boot/data/elasticsearch/service/ArticleService.java)是怎么处理的