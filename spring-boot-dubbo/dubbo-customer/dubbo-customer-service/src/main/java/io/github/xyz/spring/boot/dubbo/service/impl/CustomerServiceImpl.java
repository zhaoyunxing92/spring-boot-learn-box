package io.github.xyz.spring.boot.dubbo.service.impl;

import io.github.xyz.spring.boot.dubbo.entity.Customer;
import io.github.xyz.spring.boot.dubbo.service.CustomerService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhaoyunxing
 * @date 2020/11/10 19:39
 */
@DubboService
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    @Override
    public List<Customer> getCustomers(Integer size) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(0, size))
                .build();

        SearchHits<Customer> customers = elasticsearchOperations.search(query, Customer.class);

        return customers.get().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public List<SearchHit<Customer>> getSearchHitsCustomers(Integer size) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(0, size))
                .build();

        SearchHits<Customer> customers = elasticsearchOperations.search(query, Customer.class);

        return customers.getSearchHits();
    }

}
