package com.pengbiao.redis.expire.mapper;

import com.pengbiao.redis.expire.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface OrderMapper {

    @Insert("insert into order_number values (null,#{orderName},0,#{orderToken},#{orderId})")
    int insertOrder(OrderEntity OrderEntity);


    @Select("SELECT ID AS ID ,order_name AS ORDERNAME ,order_status AS orderstatus,order_token as ordertoken,order_id as  orderid FROM order_number\n" +
            "where order_token=#{orderToken};")
    OrderEntity getOrderNumber(String orderToken);

    @Update("\n" +
            "\n" +
            "update order_number set order_status=#{orderStatus} where order_token=#{orderToken};")
    int updateOrderStatus(String orderToken, Integer orderStatus);

    @Select("SELECT ID AS ID ,order_name AS ORDERNAME ,order_status AS orderstatus,order_token as ordertoken,order_id as  orderid FROM order_number\n" +
            "where id=#{orderId};")
    OrderEntity getOrderById(Integer orderId);

    @Select("SELECT ID AS ID  FROM order_number\n")
    List<Integer> getOrderIds();
}
