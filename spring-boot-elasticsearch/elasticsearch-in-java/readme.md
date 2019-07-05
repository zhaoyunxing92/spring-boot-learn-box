# elasticsearch-in-java
 
在java中使用`elasticsearch`自带的api操作`elasticsearch`。你可以先看下[elasticsearch入门到放弃之docker搭建](https://www.jianshu.com/p/ba7caa5bed53)获取一个elasticsearch环境

## 系列文章

* [elasticsearch入门到放弃之docker搭建](https://www.jianshu.com/p/ba7caa5bed53) es环境搭建
* [elasticsearch入门到放弃之x-pack安全认证](https://www.jianshu.com/p/3b01817996c8) x-pack保驾护航你的es
* [elasticsearch入门到放弃之elasticsearch-head](https://www.jianshu.com/p/80bb53bc1256) es-head可视化你的es

## 参考文档
 
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html 官方文档
 * https://discuss.elastic.co 官方论坛
 
## 环境信息

  * jdk 1.8
  * elasticsearch 6.5.4
  * x-pack 6.5.4 正常的私服下载不到,es官方给的解决方案：https://www.elastic.co/guide/en/elasticsearch/reference/6.5/setup-xpack-client.html
  
## pom.xml依赖

> 我这里使用了`x-pack-transport`的包,你可以选择使用`transport`包，如果你没有开启`x-pack`的话

```xml
<properties>
    <elasticsearch.version>6.5.4</elasticsearch.version>
    <log4j.version>2.7</log4j.version>
    <junit.version>4.12</junit.version>
</properties>
<dependencies>
    <dependency>
        <groupId>org.elasticsearch</groupId>
        <artifactId>elasticsearch</artifactId>
        <version>${elasticsearch.version}</version>
    </dependency>
    <dependency>
        <groupId>org.elasticsearch.client</groupId>
        <artifactId>x-pack-transport</artifactId>
        <version>${elasticsearch.version}</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>${log4j.version}</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>${log4j.version}</version>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>
 <!--这个必须设置不然下载不到x-pack-->
<repositories>
    <repository>
        <id>elasticsearch-releases</id>
        <url>https://artifacts.elastic.co/maven</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
```

## 正题
下面代码基于`junit`编写可以直接参考：[ElasticsearchCase](https://github.com/zhaoyunxing92/spring-boot-learn-box/blob/develop/spring-boot-elasticsearch/elasticsearch-in-java/src/test/java/ElasticsearchCase.java)
### 初始化客户端

跟操作数据库一样，写入地址、账号密码，获取一个客户端，有兴趣的可以看`org.springframework.data.elasticsearch.client.ClusterNodes`spring boot是怎么解析集群的

```java
private TransportClient client;
private String[] nodes = new String[]{"127.0.0.1:9200"};
@Before
public void initClint() {
    Settings settings = Settings.builder()
            // es 集群的名称
            .put("cluster.name", "elasticsearch")
            .put("client.transport.sniff", "true")
            //账号密码
            .put("xpack.security.user", "elastic:123456")
            .build();
    client = new PreBuiltXPackTransportClient(settings)
            //添加集群节点
            .addTransportAddresses(parseAddress());
}
```

### 创建索引

可以理解为创建一个mysql数据库

```java
@Test
public void createIndex() {
    client.admin()
            .indices()
            .prepareCreate("elastic").get();
    client.close();
}
```
### 设置mappings

`mappings`可以理解为mysql的建表语句(json数据)

对应的json格式

```json
{
    "properties": {
            "id": {
                "type": "long",
                        "store": true
            },
            "name": {
                "type": "text",
                        "store": true
            },
            "age": {
                "type": "integer",
                        "store": true
           }
    }
}
```
对应的java代码

```java
XContentBuilder builder= XContentFactory.jsonBuilder()
            .startObject() // 相当于json的'{'
                .startObject("properties")
                    .startObject("id")
                        .field("type","long") //字段类型
                        .field("store",true) //是否存储
                    .endObject() //相当于json的'}'
                    .startObject("name")
                         .field("type","text")
                         .field("store",true)
                         .field("analyzer","ik_smart") //采用ik_smart分词
                    .endObject()
                    .startObject("age")
                        .field("type","long")
                        .field("store",true)
                    .endObject()
                .endObject()
            .endObject();

    client.admin().indices()
            .preparePutMapping("elastic")
            .setType("user") //对应数据库的表名称
            .setSource(builder)
            .get();
    client.close();
```

### 添加数据

对应数据库的`insert`,可以用java pojo转换成json对象(fastjson)

json数据
```json
{
	"id": 1,
	"name": "zhaoyunxing",
	"age": 28,
	"desc": "中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首"
}
```
java代码
```java
@Test
public void addDocument() throws IOException {
    //创建文档
    XContentBuilder builder = XContentFactory.jsonBuilder()
            .startObject()
                .field("id",1L)
                .field("name","sunny")
                .field("age",28L)
                .field("desc","中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首")
            .endObject();

    client.prepareIndex("elastic","user")
            .setId("1")
            .setSource(builder)
            .get();
    client.close();
}
```

pojo
```java
@Test
public void addDocumentPojo(){
    User user = new User(3l, "张三", 28l, "掌握ES使用IK中文分词器");
    String json = JSONObject.toJSONString(user);

    client.prepareIndex("elastic", "user","3")
            .setSource(json, XContentType.JSON)
            .get();
    client.close();
}
```
### 查询

#### 根据id查询




## 可能遇到的问题

* java.lang.NoSuchMethodError: org.elasticsearch.common.settings.Settings$Builder.put([Ljava/lang/Object;)Lorg/elasticsearch/common/settings/Settings$Builder;

 这个是elasticsearch和x-pack版本不一致导致的，保持版本一致即可
