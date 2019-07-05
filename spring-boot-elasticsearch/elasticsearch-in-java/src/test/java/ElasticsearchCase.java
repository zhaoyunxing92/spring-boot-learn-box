import com.alibaba.fastjson.JSONObject;
import io.github.xyz.spring.boot.elasticsearch.java.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author zhaoyunxing
 * @date: 2019-07-04 19:46
 * @des:
 */
public class ElasticsearchCase {

    private TransportClient client;

    private String[] nodes = new String[]{"172.26.104.209:9300"};

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

    /**
     * 解析集群节点
     *
     * @return TransportAddress[]
     */
    private TransportAddress[] parseAddress() {
        List<TransportAddress> transportAddresses = new ArrayList<>();
        Arrays.asList(nodes).forEach(node -> {
            try {
                String[] split = node.split(":");
                transportAddresses.add(new TransportAddress(InetAddress.getByName(split[0]), Integer.valueOf(split[1])));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
        return transportAddresses.toArray(new TransportAddress[transportAddresses.size()]);
    }

    /**
     * 创建索引
     */
    @Test
    public void createIndex() {
        client.admin()
                .indices()
                .prepareCreate("elastic").get();
        client.close();
    }

    /**
     * 设置mappings
     * es字段类型可以参考：https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html
     * https://blog.csdn.net/chengyuqiang/article/details/79048800
     */
    @Test
    public void setMappings() throws IOException {
  /*
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
    */
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
    }

    /**
     * 添加数据
     */
    @Test
    public void addDocument() throws IOException {
        //创建文档
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                    .field("id", 2L)
                    .field("name", "zhaoyunxing")
                    .field("age", 28L)
                    .field("desc", "中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首")
                .endObject();

        client.prepareIndex("elastic", "user")
                .setId("2")
                .setSource(builder)
                .get();
        client.close();
    }

    /**
     * pojo
     */
    @Test
    public void addDocumentPojo() {
        User user = new User(3l, "张三", 28, "掌握ES使用IK中文分词器", new Date());
        String json = JSONObject.toJSONString(user);

        client.prepareIndex("elastic", "user", "3")
                .setSource(json, XContentType.JSON)
                .get();
        client.close();
    }

    @Test
    public void addDocumentPojos() {
        for (int i = 1; i <= 15; i++) {
            User user = new User((long) i, "张三", 28, "掌握ES使用IK中文分词器" + i, new Date());
            String json = JSONObject.toJSONString(user);

            client.prepareIndex("elastic", "user", String.valueOf(i))
                    .setSource(json, XContentType.JSON)
                    .get();
        }
        for (int i = 15; i <=30; i++) {
            User user = new User((long) i, "sunny", 28, "到此我就觉得是时候安装 IK 了 ，所以 开始安装 IK 分词了" + i, new Date());
            String json = JSONObject.toJSONString(user);

            client.prepareIndex("elastic", "user", String.valueOf(i))
                    .setSource(json, XContentType.JSON)
                    .get();
        }
        client.close();
    }

    /**
     * 根据id查询
     */
    @Test
    public void searchById() {
        QueryBuilder query = QueryBuilders.idsQuery().addIds("1", "2");
        search(query);
    }

    /**
     * 根据字段查询
     */
    @Test
    public void searchByTerm() {
        /*
         * * 搜索的字段名称
         * * 关键字
         */
        QueryBuilder query = QueryBuilders.termQuery("desc", "es");
        search(query);
    }

    /**
     * 模糊查询
     */
    @Test
    public void searchByStringQuery() {
        QueryBuilder query = QueryBuilders.queryStringQuery("掌握")
                // 可以指定作用域，不指定全部字段匹配
                .defaultField("desc");


        search(query);
    }

    /*
    公共查询
     */
    private void search(QueryBuilder query) {

        search(query, 0, 8, "desc");
    }

    /**
     * @param query     query
     * @param from      当前页
     * @param size      显示条数
     * @param highlight 高亮字段
     */
    private void search(QueryBuilder query, Integer from, Integer size, String highlight) {

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
                .addSort("id", SortOrder.DESC) //设置字段排序规则
                .highlighter(highlightBuilder)
               // .setQuery(new RangeQueryBuilder("registerTime").from("1562324622115").to("1562324622260"))
                .get();
        //查询命中缓存
        SearchHits searchHits = searchResponse.getHits();
        System.out.println("查询结果总记录数：" + searchHits.getTotalHits());

        Arrays.stream(searchHits.getHits()).forEach(doc -> {
            System.out.println(doc.getSourceAsString());
            System.out.println("*******************高亮结果********************");
            Map<String, HighlightField> highlightFields = doc.getHighlightFields();
            if(!highlightFields.isEmpty()){
                HighlightField highlightField = highlightFields.get(highlight);
                Arrays.stream(highlightField.getFragments()).forEach(System.out::println);
            }

            System.out.println();
        });
        client.close();
    }
}
