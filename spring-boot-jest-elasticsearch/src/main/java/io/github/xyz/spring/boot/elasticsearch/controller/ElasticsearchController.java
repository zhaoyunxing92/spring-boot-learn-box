package io.github.xyz.spring.boot.elasticsearch.controller;

import io.github.xyz.spring.boot.elasticsearch.entity.User;
import io.github.xyz.spring.boot.elasticsearch.service.UserService;
import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
   // private final UserService userService;
    private final JestClient jestClient;

    @Autowired
    public ElasticsearchController(UserService userService, JestClient jestClient) {
      //  this.userService = userService;
        this.jestClient = jestClient;
    }

    @GetMapping
    public User add() throws IOException {
        User user = new User();
        user.setAge(30);
        user.setName("张三");
        Index index = new Index.Builder(user).index("user").type("user").build();
        DocumentResult result = jestClient.execute(index);
        return result.getSourceAsObject(User.class);
    }

    @GetMapping("/get")
    public List<User> get() throws IOException {
        String json = "{\n" +
                " \"query\" : {\n" +
                "  \"match\" : {\n" +
                "   \"name\" : \"张三\"\n" +
                "  }\n" +
                " }\n" +
                "}";
        Search search = new Search.Builder(json).addIndex("user").addType("user").build();
        SearchResult result = jestClient.execute(search);
        return result.getSourceAsObjectList(User.class, true);
    }

//    @PostMapping
//    public User save(@RequestBody User user) {
//        return userService.save(user);
//    }
//
//    @GetMapping
//    public User findeUserById(String id) {
//        return userService.findById(id).orElse(new User());
//    }
}
