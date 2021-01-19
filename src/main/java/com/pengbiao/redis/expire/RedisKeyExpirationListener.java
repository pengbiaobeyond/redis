package com.pengbiao.redis.expire;

import com.pengbiao.redis.expire.entity.OrderEntity;
import com.pengbiao.redis.expire.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 待支付
     */
    private static final Integer ORDER_STAYPAY = 0;
    /**
     * 失效
     */
    private static final Integer ORDER_INVALID = 2;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * Redis失效事件 key
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiraKey = message.toString();
        // 根据key查询 value 如果还还是为待支付状态 将订单改为已经超时~~
        OrderEntity orderNumber = orderMapper.getOrderNumber(expiraKey);
        System.out.println(expiraKey);
        if (orderNumber == null) {
            return;
        }
        if (orderNumber.getOrderStatus().equals(ORDER_STAYPAY)) {
            // 将订单状态改为已经失效
            orderMapper.updateOrderStatus(expiraKey, ORDER_INVALID);
        }
    }
}

