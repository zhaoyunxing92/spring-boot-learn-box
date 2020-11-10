package io.github.xyz.spring.boot.dubbo.service;

import io.github.xyz.spring.boot.dubbo.entity.Customer;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.util.List;

/**
 * @author zhaoyunxing
 * @date 2020/11/10 19:37
 */
public interface CustomerService {

    List<Customer> getCustomers(Integer size);

    List<SearchHit<Customer>> getSearchHitsCustomers(Integer size);
}
