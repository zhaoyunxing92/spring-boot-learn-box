package io.github.xyz.spring.boot.dubbo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 客户信息
 *
 * @author zhaoyunxing
 * @date 2020/11/10 20:01
 */
@Getter
@Setter
@ToString
@Document(indexName = "kibana_sample_data_ecommerce")
public class Customer implements Serializable {

    private String id;

    /**
     * 类别
     */
    private List<String> category;
    /**
     * 货币
     */
    private String currency;
    /**
     * 客户生日
     */
    private Date customerBirthDate;
    /**
     * 客户名称
     */
    private String customerFirstName;
    /**
     * 客户全称
     */
    private String customerFullName;
    /**
     * 客户性别
     */
    private String customerGender;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 产品
     */
    private List<Product> products;
}
