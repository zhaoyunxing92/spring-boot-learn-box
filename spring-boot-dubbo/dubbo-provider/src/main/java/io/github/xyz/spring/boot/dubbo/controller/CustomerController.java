package io.github.xyz.spring.boot.dubbo.controller;

import io.github.xyz.spring.boot.dubbo.entity.Customer;
import io.github.xyz.spring.boot.dubbo.service.CustomerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhaoyunxing
 * @date 2020/11/10 20:28
 */
@RestController
public class CustomerController {

    @DubboReference
    private CustomerService customerService;

    @GetMapping("/user")
    public List<Customer> getCustomers(@RequestParam(defaultValue = "10") Integer size) {
        return customerService.getCustomers(size);
    }
}
