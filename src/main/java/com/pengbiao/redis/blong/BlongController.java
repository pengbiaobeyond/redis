package com.pengbiao.redis.blong;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.pengbiao.redis.expire.entity.OrderEntity;
import com.pengbiao.redis.expire.mapper.OrderMapper;
import com.pengbiao.redis.util.RedisTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class BlongController {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisTemplateUtils redisTemplateUtils;

    BloomFilter<Integer> integerBloomFilter;

    @RequestMapping("/getOrder")
    public OrderEntity getOrder(Integer orderId) {
        if (integerBloomFilter != null) {
            if (!integerBloomFilter.mightContain(orderId)) {
                System.out.println("从布隆过滤器中检测到该key不存在");
                return null;
            }
        }

        // 1.先查询Redis中数据是否存在
        OrderEntity orderRedisEntity = (OrderEntity) redisTemplateUtils.getObject(orderId + "");
        if (orderRedisEntity != null) {
            System.out.println("直接从Redis中返回数据");
            return orderRedisEntity;
        }
        // 2. 查询数据库的内容
        System.out.println("从DB查询数据");
        OrderEntity orderDBEntity = orderMapper.getOrderById(orderId);
        if (orderDBEntity != null) {
            System.out.println("将Db数据放入到Redis中");
            redisTemplateUtils.setObject(orderId + "", orderDBEntity);
        }
        return orderDBEntity;
    }

    @RequestMapping("/dbToBulong")
    public String dbToBulong() {
        List<Integer> orderIds = orderMapper.getOrderIds();
        integerBloomFilter = BloomFilter.create(Funnels.integerFunnel(), orderIds.size(), 0.01);
        for (int i = 0; i < orderIds.size(); i++) {
            integerBloomFilter.put(orderIds.get(i));
        }

        return "success";
    }

}
