package io.github.xyz.spring.boot.dubbo.repository;

import io.github.xyz.spring.boot.dubbo.entity.Customer;
import org.springframework.data.repository.Repository;

/**
 * @author zhaoyunxing
 * @date 2020/11/10 19:45
 */
//@Repository
public interface CustomerRepository extends Repository<Customer, String> {
}
