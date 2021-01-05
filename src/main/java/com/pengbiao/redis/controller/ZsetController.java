package com.pengbiao.redis.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class ZsetController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("/setZsetValue")
    public String setValue(){
        try{
            stringRedisTemplate.delete("zset1");
            // 使用TypedTuple创建ZSET
            Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
            for (int i = 1; i <= 9; i++) {
                double score = i * 0.1;
                ZSetOperations.TypedTuple<String> typedTuple = new DefaultTypedTuple<>("value" + i, score);
                typedTuples.add(typedTuple);
            }
            stringRedisTemplate.opsForZSet().add("zset1", typedTuples);

            // 输出Zset的集合大小
            BoundZSetOperations zsetOps = stringRedisTemplate.boundZSetOps("zset1");
            Long size = zsetOps.size();
            System.out.println(size); // 8

            // 输出Zset结合中的所有元素
            Set members = zsetOps.range(0, size - 1);
            System.out.println(members); // [value1, value2, value3, value4, value5, value6, value7, value8]

            // 加入一个元素
            zsetOps.add("value10", 0.26);

            // 输出score在该范围中的所有元素
            Set setScore = zsetOps.rangeByScore(0.2, 0.6);
            System.out.println(setScore); // [value2, value10, value3, value4, value5]

            // 定义值范围
            RedisZSetCommands.Range range = new RedisZSetCommands.Range();
//        range.gt("value3"); // 大于value3
//        range.lt("value8"); // 小于value8
            range.lte("value8"); // 小于等于value8
            // 按值排序
            Set setLex = zsetOps.rangeByLex(range);
            System.out.println(setLex); // [value1, value2, value10, value3, value4, value5, value6, value7, value8]

            // 删除元素
            zsetOps.remove("value9", "value2");
            System.out.println(zsetOps.range(0, zsetOps.size() - 1)); // [value1, value10, value3, value4, value5, value6, value7, value8]

            // 得到value值对应的分数
            Double score = zsetOps.score("value8");
            System.out.println(score); // 0.8

//            zsetOps.

            // 在下标区间内，按分数排序，同时返回value和score
            Set<ZSetOperations.TypedTuple<String>> rangeSet = zsetOps.rangeWithScores(1, 6);
            System.out.println(JSONObject.toJSONString(rangeSet.toArray()));

            // 在分数区间内，按分数排序，同时返回value和score
            Set<ZSetOperations.TypedTuple<String>> scoreSet = zsetOps.rangeByScoreWithScores(0.1, 0.6);
            System.out.println(JSONObject.toJSONString(scoreSet)); // []

            // 从大到小排序，默认为从小到达排序
            Set<String> reverseSet = zsetOps.reverseRange(2, 8);
            System.out.println(reverseSet);

        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
        return "success";
    }

    /**
     * 打印set结合中的所有元素
     */
    private void printSetMembers(String setKey) {
        BoundSetOperations setOps = stringRedisTemplate.boundSetOps(setKey);
        System.out.println(setOps.members());
    }


}
