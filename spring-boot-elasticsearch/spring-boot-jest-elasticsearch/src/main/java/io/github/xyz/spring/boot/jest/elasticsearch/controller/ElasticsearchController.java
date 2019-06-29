package io.github.xyz.spring.boot.jest.elasticsearch.controller;

import io.github.xyz.spring.boot.jest.elasticsearch.entity.User;
import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author zhaoyunxing
 * @date: 2019-06-28 14:37
 * @des:
 */
@RestController
@RequestMapping("/user")
public class ElasticsearchController {
    private final JestClient jestClient;

    @Autowired
    public ElasticsearchController(JestClient jestClient) {
        this.jestClient = jestClient;
    }

    @GetMapping
    public User add() throws IOException {
        User user = new User();
        user.setAge(30);
        user.setName("李四");
        Index index = new Index.Builder(user).index("user").type("user").build();
        DocumentResult result = jestClient.execute(index);
        return result.getSourceAsObject(User.class);
    }

    @GetMapping("/get")
    public List<User> get() throws IOException {
        String json = "{\n" +
                " \"query\" : {\n" +
                "  \"match\" : {\n" +
                "   \"name\" : \"李四\"\n" +
                "  }\n" +
                " }\n" +
                "}";
        Search search = new Search.Builder(json).addIndex("user").addType("user").build();
        SearchResult result = jestClient.execute(search);
        return result.getSourceAsObjectList(User.class, true);
    }
}
