# spring-data-elasticsearch实践

> 代码地址：https://github.com/zhaoyunxing92/spring-boot-learn-box/tree/master/spring-boot-elasticsearch/spring-boot-data-elasticsearch

这篇主要是对上篇[elasticsearch入门到放弃之springboot elasticsearch x-pack](https://www.jianshu.com/p/7019d93219f5)的补充，下面我将按照场景结合官方编写接口

## 系列文章

* [elasticsearch入门到放弃之docker搭建](https://www.jianshu.com/p/ba7caa5bed53) es环境搭建
* [elasticsearch入门到放弃之x-pack安全认证](https://www.jianshu.com/p/3b01817996c8) x-pack保驾护航你的es
* [elasticsearch入门到放弃之elasticsearch-head](https://www.jianshu.com/p/80bb53bc1256) es-head可视化你的es
* [elasticsearch入门到放弃之elasticsearch-in-java](https://www.jianshu.com/p/9f6f7f67df4e) 这个很关键，看完这个，再看这个就很容易理解了
* [elasticsearch入门到放弃之springboot elasticsearch x-pack](https://www.jianshu.com/p/7019d93219f5) spring boot跟elasticsearch整合

## 参考文档

* [https://docs.spring.io/spring-data/elasticsearch/docs/2.1.22.RELEASE/reference/html/](https://docs.spring.io/spring-data/elasticsearch/docs/2.1.22.RELEASE/reference/html/) 官方的文档也是很棒的
* [https://www.elastic.co/guide/en/elasticsearch/reference/current/fielddata.html](https://www.elastic.co/guide/en/elasticsearch/reference/current/fielddata.html) text字段排序问题

## 环境信息、配置

* jdk 1.8
* elasticsearch 6.8.2
* x-pack 6.8.2
* spring-boot-starter-data-elasticsearch 6.8.2
* spring-boot 2.1.0 (它默认带的elasticsearch是6.2.2的)

### pom.xml文件

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

### application.yml配置

```yaml
spring:
  data:
    elasticsearch:
      repositories:
        enabled: true
      cluster-nodes: 127.0.0.1:9300 # 集群模式下用逗号分开
      cluster-name: elasticsearch
      properties:
        xpack.security.user: elastic:123456
```

## 使用场景

### 根据名称获取文章

> 注意：如果返回值是`Page`对象则参数必须添加`Pageable`

```java
// 带分页信息 
Page<Article> articles = articleService.findArticleByName("docker搭建", PageRequest.of(0, 5));

// 只关心数据本身(使用场景还没有想到)
List<Article> articles= articleService.findArticleByName(String name);
```

### 根据名称获取文章,然后根据id和创建时间倒序

> 注意：排序的时候不要用在字段类型是`text`上，具体原因看`可能遇到的问题`第一个

```java
// 按照`org.springframework.data.domain.Sort`来
Page<Article> articles = articleService.findArticleByName("docker搭建", PageRequest.of(0, 5,Sort.by(Sort.Direction.DESC,"id","createTime")));

// 按照命名规则来
Page<Article> articles = articleService.findArticleByNameOrderByCreateTimeDesc("docker搭建", PageRequest.of(0, 5));
```

### 统计文章名称出现次数

使用场景好像没有,了解即可
```java
 Long count = articleService.countArticleByName("docker搭建");
```
### 删除文章

有很多方法，只写使用频率高的

```java
//无返回值删除
articleService.deleteById("40");
// 根据对象删除
articleService.delete(new Article());
//有返回值删除，只能针对主键
List<Article> articles = articleService.deleteArticlesById("40");
```
### 其他未验证的感觉没有使用场景的
 
 * 根据`top`或`first`限制
 * 使用注解`@Query`的
 
## 可能遇到的问题

* `java.lang.IllegalArgumentException: Fielddata is disabled on text fields by default. Set fielddata=true on [name] in order to load fielddata in memory by uninverting the inverted index. Note that this can however use significant memory. Alternatively use a keyword field instead.`

 这个问题是因为`Sort`的字段`name`是`text`类型的,按照官方[fieldata](https://www.elastic.co/guide/en/elasticsearch/reference/current/fielddata.html)的建议，它禁止这样操作，容易出现内存过大
 ```java
 // fielddata = true 可以解决这个问题，但是可能带来oom问题
 @Field(type = FieldType.Text,fielddata = true,store = true, analyzer = "ik_smart", searchAnalyzer = "ik_max_word")
 private String name;
 ```