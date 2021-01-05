package com.pengbiao.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ListController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/setListValue")
    public String setValue(){
        try{
            // 在开始操作前判断并删除已存在的list，否则下面的过程会对同一个list进行累计操作
            deleteExistedList("list1");
            deleteExistedList("list2");

            // 从左插入一个链表，名为list1，顺序为,v8, v6, v4, v2
            stringRedisTemplate.opsForList().leftPushAll("list1", "v2", "v4", "v6", "v8");
            printRedisList("list1"); // List: list1, elements: v8, v6, v4, v2,

            // 从右侧插入一个列表，名为list2，顺序为, v1, v3, v7, v9
            stringRedisTemplate.opsForList().rightPushAll("list2", "v1", "v3", "v7", "v9");
            printRedisList("list2"); // List: list2, elements: v1, v3, v7, v9,

            // pop方法会将list中的元素弹出后删除
            System.out.println(stringRedisTemplate.opsForList().leftPop("list1")); // v8
            System.out.println(stringRedisTemplate.opsForList().rightPop("list1")); // v2
            System.out.println(stringRedisTemplate.opsForList().leftPop("list2")); // v1
            System.out.println(stringRedisTemplate.opsForList().rightPop("list2")); // v9

            // 使用BoundOperations绑定名为list2的列表进行多次操作
            BoundListOperations listOps = stringRedisTemplate.boundListOps("list2");
            // 从list2右侧输出一个元素
            Object result1 = listOps.rightPop();
            System.out.println(result1); // v7

            // 输出list2中index=1的元素，（List中从0开始）
            Object result2 = listOps.index(1);
            System.out.println(result2); // null，因为此时list2经过上述的pop操作只剩下一个元素

            //从左侧向list2中加入一个元素为v0
            listOps.leftPush("v0");

            // 输出List2中元素的总个数
            Long size = listOps.size();
            System.out.println(size); // 2

            // 取出list2中【0，size-1】范围内的所有元素
            List elements = listOps.range(0, size - 1);

            System.out.println(elements); // [v0, v3]
            // 测试range方法是否会pop出list中的元素，答案：不会，list中的元素以复制的方式进行返回
            System.out.println(listOps.size());

        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
        return "success";
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

    /**
     * 判断list是否存在并进行删除
     */
    private void deleteExistedList(String listKey) {
        if (stringRedisTemplate.opsForList().size(listKey) != null) {
            stringRedisTemplate.delete(listKey);
        }
    }
}
