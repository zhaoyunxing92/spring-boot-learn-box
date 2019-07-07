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

## 准备工作

下面可能用到了中文分词[elasticsearch-analysis-ik](https://github.com/medcl/elasticsearch-analysis-ik),由于很简单我就不单独写了简单说下流程吧

* docker 进入容器
```shell
docker exec -it elasticsearch /bin/bash
```
* 进入`bin`目录下

在github：https://github.com/medcl/elasticsearch-analysis-ik/releases 找到跟你elasticsearch匹配的版本这里6.5.4为列

```shell
elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.5.4/elasticsearch-analysis-ik-6.5.4.zip
```  
  
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

可以理解为创建一个mysql数据库表

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

`mappings`可以理解为mysql的表字段(json数据)

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
XContentBuilder builder = XContentFactory.jsonBuilder()
            .startObject() // 相当于json的'{'
                .startObject("properties")
                    .startObject("id")
                        .field("type", "long") //字段类型
                        .field("store", true) //是否存储
                    .endObject() //相当于json的'}'
                    .startObject("name")
                        .field("type", "text")
                        .field("store", true)
                        .field("analyzer", "ik_smart") //采用ik_smart分词 "search_analyzer": "ik_smart"
                    .endObject()
                    .startObject("age")
                        .field("type", "integer")
                        .field("store", true)
                    .endObject()
                    .startObject("desc")
                        .field("type", "text")
                        .field("store", true)
                        .field("analyzer", "ik_max_word")
                    .endObject()
                    .startObject("registerTime")
                        .field("type", "date")
                        .field("store", true)
                        .field("format", "yyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
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

由于我抽取了公共的`search()`方法，那么下面我只写一遍

#### 根据id查询

```java
@Test
public void searchById(){
     QueryBuilder query = QueryBuilders.idsQuery().addIds("1","2");
     search(query);
 }
private void search(QueryBuilder query) {
    SearchResponse searchResponse = client.prepareSearch("elastic")
            .setTypes("user")
            .setQuery(query)
            .get();
    //查询命中缓存
    SearchHits searchHits = searchResponse.getHits();
    System.out.println("查询结果总记录数："+searchHits.getTotalHits());

    Arrays.stream(searchHits.getHits()).forEach(doc-> System.out.println(doc.getSourceAsString()));

    client.close();
}
```

#### 根据关键字查询

```java
@Test
public void searchByTerm(){
    /*
     * * 搜索的字段名称
     * * 关键字
     */
    QueryBuilder query = QueryBuilders.termQuery("desc","es");
    search(query);
}
```
#### 模糊查询

```java
@Test
public void searchByStringQuery(){
    QueryBuilder query = QueryBuilders.queryStringQuery("sunny爱中国，最近在学es")
            // 可以指定作用域，不指定全部字段匹配
            .defaultField("name");
    search(query);
}
```
 
#### 分页设置

```java
SearchResponse searchResponse = client.prepareSearch("elastic")
            .setTypes("user")
            .setQuery(query)
            //从零开始
            .setFrom(0)
            //每页显示5条
            .setSize(5)
            .get();
```

#### 根据字段排序

只写下关键代码 `addSort("id", SortOrder.DESC)`

```java
SearchResponse searchResponse = client.prepareSearch("elastic")
                .setTypes("user")
                .setQuery(query)
                //从零开始
                .setFrom(from)
                //每页显示5条
                .setSize(size)
                .addSort("id", SortOrder.DESC) //设置字段排序规则
                .highlighter(highlightBuilder)
                .get();
```

#### 根据时间域查询

这里也只写伪代码，我数据插入进去的是时间戳，可能是我设置的时间不对，但是能说明问题即可

```java
SearchResponse searchResponse = client.prepareSearch("elastic")
                .setTypes("user")
                .setQuery(query)
                .setQuery(new RangeQueryBuilder("registerTime").from("1562324622115").to("1562324622260"))
                .get();
```

#### 高亮显示

高亮显示原理即在匹配到的关键字前后添加上特殊标签，然后前端通过css识别

```java
HighlightBuilder highlightBuilder = new HighlightBuilder();
    highlightBuilder.field(highlight); // 高亮字段
    highlightBuilder.preTags("<b>"); //前字段
    highlightBuilder.postTags("</b>"); //后字段

    SearchResponse searchResponse = client.prepareSearch("elastic")
            .setTypes("user")
            .setQuery(query)
            //从零开始
            .setFrom(from)
            //每页显示5条
            .setSize(size)
            .highlighter(highlightBuilder)
            .get();
    //查询命中缓存
    SearchHits searchHits = searchResponse.getHits();
    System.out.println("查询结果总记录数：" + searchHits.getTotalHits());

    Arrays.stream(searchHits.getHits()).forEach(doc ->{
        System.out.println(doc.getSourceAsString());
        System.out.println("*******************高亮结果********************");
        Map<String, HighlightField> highlightFields = doc.getHighlightFields();
        HighlightField highlightField = highlightFields.get(highlight);
        Arrays.stream(highlightField.getFragments()).forEach(System.out::println);
        System.out.println();
    });
    client.close();
```



## 可能遇到的问题

* java.lang.NoSuchMethodError: org.elasticsearch.common.settings.Settings$Builder.put([Ljava/lang/Object;)Lorg/elasticsearch/common/settings/Settings$Builder;

 这个是elasticsearch和x-pack版本不一致导致的，保持版本一致即可
