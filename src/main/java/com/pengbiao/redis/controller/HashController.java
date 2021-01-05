package com.pengbiao.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HashController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("/setHashValue")
    public String setValue(){
        try{
            // 存入一个Hash类型数据，第一个为存入Hash数据结构的整体key，后面为对应存入的值，也就是如下结构：
            // Map<Key, Map<field, value>>
            Map<String, Object> hash = new HashMap<>();
            hash.put("field1", "value1");
            hash.put("field2", "value2");
            stringRedisTemplate.opsForHash().putAll("hash", hash);
            System.out.println(stringRedisTemplate.opsForHash().get("hash", "field1")); // output: value1

            // 向key为hash的散列表中存入单条数据
            stringRedisTemplate.opsForHash().put("hash", "field3", "value3");
            System.out.println(stringRedisTemplate.opsForHash().get("hash", "field3")); // output: value3
            // 绑定散列表Hash进行操作，可以对一个hash进行多次操作
            BoundHashOperations hashOps = stringRedisTemplate.boundHashOps("hash");
            hashOps.put("field4", "value4");
            hashOps.delete("field4");
            // 输出名为hash散列表中的所有key和value
            System.out.println(hashOps.keys()); // output: [filed, field1, field2, field3]
            System.out.println(hashOps.values()); // output: [hvalue, value1, value2, value3]

            // 创建另一个散列表进行操作
            stringRedisTemplate.opsForHash().put("hash1", "hash1_key1", "hash1_value1");
            System.out.println(stringRedisTemplate.opsForHash().get("hash1", "hash1_key1"));
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }

        return "success";
    }
}
