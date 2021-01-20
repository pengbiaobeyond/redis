package com.pengbiao.redis.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 蚂蚁课堂创始人-余胜军QQ644064779
 * @title: RedisTemplateUtils
 * @description: 每特教育独创第五期互联网架构课程
 * @date 2019/11/1620:45
 */
@Component
public class RedisTemplateUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void setObject(String key, Object value) {
        setObject(key, value, null);
    }

    public void setObject(String key, Object value, Long timeOut) {
        redisTemplate.opsForValue().set(key, value);
        if (timeOut != null) {
            redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
        }
    }


    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
