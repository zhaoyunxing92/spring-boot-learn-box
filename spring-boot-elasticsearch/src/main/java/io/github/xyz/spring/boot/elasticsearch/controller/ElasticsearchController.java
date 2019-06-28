package io.github.xyz.spring.boot.elasticsearch.controller;

import io.github.xyz.spring.boot.elasticsearch.entity.User;
import io.github.xyz.spring.boot.elasticsearch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhaoyunxing
 * @date: 2019-06-28 14:37
 * @des:
 */
@RestController
@RequestMapping("/user")
public class ElasticsearchController {
    private final UserService userService;

    @Autowired
    public ElasticsearchController(UserService userService) {this.userService = userService;}

    @PostMapping
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping
    public User findeUserById(String id) {
        return userService.findById(id).orElse(new User());
    }
}
