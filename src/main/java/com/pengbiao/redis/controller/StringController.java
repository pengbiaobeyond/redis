package com.pengbiao.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StringController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("/setStringValue")
    public String setValue(){
        try{
            redisTemplate.opsForValue().set("key1", "value1");
            redisTemplate.opsForValue().set("int_key", "1");
            stringRedisTemplate.opsForValue().set("int", "1");
            stringRedisTemplate.opsForValue().increment("int", 5);
            System.out.println(redisTemplate.opsForValue().get("key1")); // output: value1
            System.out.println(redisTemplate.opsForValue().get("int_key")); // output: 1
            System.out.println(stringRedisTemplate.opsForValue().get("int")); // output: 6, 这里如果使用redisTemplate会存在NPE
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
        return "success";
    }

    @GetMapping("/deleteStringValue")
    public String deleteStringValue(){
        try{
            stringRedisTemplate.delete("int");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }
}
