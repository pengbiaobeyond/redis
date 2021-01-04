package com.pengbiao.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class SetController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("/setSetValue")
    public String setValue(){
        try{
            emptySet("set1");
            emptySet("set2");

            // 分别向set1和set2集合添加元素
            stringRedisTemplate.opsForSet().add("set1", "v1", "v1", "v2", "v3", "v4", "v5");
            stringRedisTemplate.opsForSet().add("set2", "v2", "v4", "v6", "v8");
            System.out.println(stringRedisTemplate.opsForSet().members("set1")); // [v4, v2, v3, v1, v5]
            System.out.println(stringRedisTemplate.opsForSet().members("set2")); // [v6, v4, v2, v8]

            // 绑定一个set1集合进行多次操作
            BoundSetOperations setOps = stringRedisTemplate.boundSetOps("set1");
            // 添加元素
            setOps.add("v6", "v7");
            // 删除集合中的元素
            setOps.remove("v1", "v7");
            System.out.println(setOps.members()); // [v2, v5, v3, v6, v4]
            System.out.println(setOps.size()); // 5

            // 求交集
            Set inter = setOps.intersect("set2");
            System.out.println(inter); // [v6, v4, v2]

            // 求差集
            Set diff = setOps.diff("set2");
            System.out.println(diff); // [v5, v3]
            // 测试求差集后是否会影响原set中的元素
            System.out.println(setOps.members()); // [v2, v5, v3, v6, v4]

            // 求差集并使用新的集合保存
            setOps.diffAndStore("set2", "diff1");
            printRedisList("diff1");

            // 求并集
            Set union = setOps.union("set2");
            System.out.println(union); // [v2, v5, v3, v6, v4, v8]

            // 求并集并使用新的集合保存
            setOps.unionAndStore("set2", "union1");
            printSetMembers("union1");

        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }


    /**
     * 清空已存在的set集合，避免多次操作过程中历史数据对示例操作的影响
     */
    private void emptySet(String setKey) {
        if (!stringRedisTemplate.opsForSet().members(setKey).isEmpty()) {
            stringRedisTemplate.opsForSet().members(setKey).clear();
        }
    }

    /**
     * 打印set结合中的所有元素
     */
    private void printSetMembers(String setKey) {
        BoundSetOperations setOps = stringRedisTemplate.boundSetOps(setKey);
        System.out.println(setOps.members());
    }

    /**
     * 使用List中的range方法来获取list中的所有元素
     * range方法不会像pop方法一样将元素从redis中删除
     */
    private void printRedisList(String listKey) {
        BoundListOperations listOps = stringRedisTemplate.boundListOps(listKey);
        Long size = listOps.size();
        List elemetns = listOps.range(0, size - 1);

        StringBuilder sb = new StringBuilder();
        sb.append("List: " + listKey + ", elements: ");
        for (int i = 0; i < elemetns.size(); i++) {
            sb.append(elemetns.get(i) + ", ");
        }
        System.out.println(sb.toString());
    }
    //https://blog.csdn.net/yitian_z/article/details/104265069
}
