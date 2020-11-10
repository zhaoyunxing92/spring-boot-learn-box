package io.github.xyz.spring.boot.dubbo.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品信息
 *
 * @author zhaoyunxing
 * @date 2020/11/10 19:43
 */
@Getter
@Setter
public class Product implements Serializable {
    /**
     * 产品id
     */
    private String productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 价格
     */
    private Double price;

    /**
     * 含税价格
     */
    private Double taxfulPprice;

    /**
     * 基本单价
     */
    private Double baseUnitPrice;

    /**
     * 基础价格
     */
    private Double basePrice;

    /**
     * 折扣
     */
    private Integer discountPercentage;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 制造商
     */
    private String manufacturer;
    /**
     * 税额
     */
    private Integer taxAmount;
    /**
     * 类别
     */
    private String category;

    private String sku;

    /**
     * 免税价格
     */
    private Double taxlessprice;

    /**
     * 单位折扣金额
     */
    private Integer unitDiscountAmount;

    /**
     * 最低价
     */
    private Integer minPrice;

    /**
     * 折扣金额
     */
    private Integer discountAmount;

    /**
     * 创建时间
     */
    private Date createdOn;
}
