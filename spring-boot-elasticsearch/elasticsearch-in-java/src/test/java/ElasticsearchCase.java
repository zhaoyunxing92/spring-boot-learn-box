import com.alibaba.fastjson.JSONObject;
import io.github.xyz.spring.boot.elasticsearch.java.entity.User;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     *                   https://blog.csdn.net/chengyuqiang/article/details/79048800
     *
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
                             .field("analyzer","ik_smart") //采用ik_smart分词 "search_analyzer": "ik_smart"
                        .endObject()
                        .startObject("age")
                            .field("type","long")
                            .field("store",true)
                        .endObject()
                            .startObject("desc")
                            .field("type","text")
                            .field("store",true)
                            .field("analyzer","ik_max_word")
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
                    .field("id",2L)
                    .field("name","zhaoyunxing")
                    .field("age",28L)
                    .field("desc","中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首")
                .endObject();

        client.prepareIndex("elastic","user")
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
        User user = new User(3l, "张三", 28l, "掌握ES使用IK中文分词器");
        String json = JSONObject.toJSONString(user);

        client.prepareIndex("elastic","user","3")
                .setSource(json, XContentType.JSON)
                .get();
        client.close();
    }
}
