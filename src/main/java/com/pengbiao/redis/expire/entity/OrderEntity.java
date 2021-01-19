package com.pengbiao.redis.expire.entity;

import lombok.Data;

@Data
public class OrderEntity {
    private Long id;
    private String orderName;
    /**
     * 0 待支付 1 已经支付
     */
    private Integer orderStatus;

    private String orderToken;
    private String orderId;

    public OrderEntity(Long id, String orderName, String orderId, String orderToken) {
        this.id = id;
        this.orderName = orderName;
        this.orderId = orderId;
        this.orderToken = orderToken;
    }
}

