package com.pengbiao.redis.expire.controller;

import com.pengbiao.redis.expire.entity.OrderEntity;
import com.pengbiao.redis.expire.mapper.OrderMapper;
import com.pengbiao.redis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisUtil redisUtils;

    @RequestMapping("/saveOrder")
    public String saveOrder() {
        // 1.生成token
        String orderToken = UUID.randomUUID().toString();
        String orderId = System.currentTimeMillis() + "";
        //2. 将该token存放到redis中
        redisUtils.setString(orderToken, orderId, 5L);
        OrderEntity orderEntity = new OrderEntity(null, "蚂蚁课堂永久会员", orderId, orderToken);
        int result = orderMapper.insertOrder(orderEntity);
        return result > 0 ? "success" : "fail";
    }
}

