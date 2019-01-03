package com.everyone.crowd.dao;

import com.everyone.crowd.entity.Order;
import com.everyone.crowd.entity.OrderComment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderMapper {

    @Select("SELECT * FROM  t_orders LIMIT #{offset}, #{size}")
    List<Order> findAll(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_orders WHERE id = #{id}")
    Order findById(@Param("id") Integer id);

    @Select("SELECT * FROM t_orders WHERE dev_id = #{devId} LIMIT #{offset}, #{size}")
    List<Order> findByDevId(@Param("devId") Integer devId, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_orders WHERE customer_id = #{customerId} LIMIT #{offset}, #{size}")
    List<Order> findByCustomerId(@Param("customerId") Integer customerId, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(id) FROM t_orders WHERE dev_id = #{devId}")
    int countByDevId(@Param("devId") Integer devId);

    @Select("SELECT COUNT(id) FROM t_orders WHERE customer_id = #{customerId}")
    int countByCustomerId(@Param("customerId") Integer customerId);

    @Select("SELECT * FROM t_orders WHERE dev_id = #{devId} AND status = #{status} LIMIT #{offset}, #{size}")
    List<Order> findByDevIdAndStatus(@Param("devId") Integer devId, @Param("status") String status, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_orders WHERE customer_id = #{customerId} AND status = #{status} LIMIT #{offset}, #{size}")
    List<Order> findByCustomerIdAndStatus(@Param("customerId") Integer customerId, @Param("status") String status, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(id) FROM t_orders WHERE dev_id = #{devId} AND status = #{status}")
    int countByDevIdAndStatus(@Param("devId") Integer devId, @Param("status") String status);

    @Select("SELECT COUNT(id) FROM t_orders WHERE customer_id = #{customerId} AND status = #{status}")
    int countByCustomerIdAndStatus(@Param("customerId") Integer customerId, @Param("status") String status);

    @Select("SELECT * FROM t_orders WHERE status = #{status} LIMIT #{offset}, #{size}")
    List<Order> findByStatus(@Param("status") String status, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(id) FROM t_orders WHERE status = #{status}")
    int countByStatus(@Param("status") String status);

    @Insert("INSERT INTO t_orders (dev_id, customer_id, demand_id, order_time, price, status) VALUES (#{devId}, #{customerId}, #{demandId}, #{orderTime}, #{price}, #{status})")
    @Options(useGeneratedKeys = true)
    int insert(Order order);

    @Update("UPDATE t_orders SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") String status);

    @Update("UPDATE t_orders SET complete_time = #{completeTime} WHERE id = #{id}")
    int completeOrder(@Param("id") Integer id, @Param("completeTime") Date completeTime);

    @Delete("DELETE FROM t_orders WHERE id = #{id}")
    int delete(@Param("id") Integer id);

    @Select("SELECT count(id) FROM t_orders WHERE order_time >= #{orderTime}")
    int countByTimeAfter(Date orderTime);

    @Select("SELECT * FROM t_ordercomments WHERE order_id = #{orderId}")
    OrderComment findCommentByOrderId(@Param("orderId") Integer orderId);

    @Select("SELECT COUNT(order_id) FROM t_ordercomments WHERE order_id = #{orderId}")
    int commentExists(@Param("orderId") Integer orderId);

    @Insert("INSERT INTO t_ordercomments (order_id, dev_comment) VALUES (#{orderId}, #{devComment})")
    @Options(useGeneratedKeys = true)
    int insertDevComment(OrderComment orderComment);

    @Insert("INSERT INTO t_ordercomments (order_id, customer_comment) VALUES (#{orderId}, #{customerComment})")
    @Options(useGeneratedKeys = true)
    int insertCustomerComment(OrderComment orderComment);

    @Update("UPDATE t_ordercomments SET customer_comment = #{customerComment} WHERE order_id = #{orderId}")
    int updateCustomerComment(@Param("orderId") Integer orderId, @Param("customerComment") String customerComment);

    @Update("UPDATE t_ordercomments SET dev_comment = #{devComment} WHERE order_id = #{orderId}")
    int updateDevComment(@Param("orderId") Integer orderId, @Param("devComment") String devComment);

    @Select("SELECT COUNT(id) FROM t_orders")
    int countAll();
}
