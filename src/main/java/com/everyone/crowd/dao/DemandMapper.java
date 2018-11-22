package com.everyone.crowd.dao;

import com.everyone.crowd.entity.Demand;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandMapper {

    @Select("SELECT * FROM t_demands LIMIT #{offset}, #{size}")
    List<Demand> findAll(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_demands WHERE id = #{id}")
    Demand findById(@Param("id") Integer id);

    @Select("SELECT * FROM t_demands WHERE title LIKE '%${title}%' LIMIT #{offset}, #{size}")
    List<Demand> findByTitle(@Param("title") String title, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_demands WHERE customer_id = #{customerId} LIMIT #{offset}, #{size}")
    List<Demand> findByCustomerId(@Param("customerId") Integer customerId, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_demands WHERE category_id = #{categoryId} LIMIT #{offset}, #{size}")
    List<Demand> findByCategoryId(@Param("categoryId") Integer categoryId, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_demands WHERE status = #{status} LIMIT #{offset}, #{size}")
    List<Demand> findByStatus(@Param("status") String status, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(id) FROM t_demands WHERE status = #{status}")
    int countByStatus(@Param("status") String status);

    @Select("SELECT COUNT(customer_id) FROM t_demands WHERE customer_id = #{customerId}")
    int countByCustomerId(@Param("customerId") Integer customerId);

    @Select("SELECT COUNT(category_id) FROM t_demands WHERE category_id = #{categoryId}")
    int countByCategoryId(@Param("categoryId") Integer categoryId);

    @Select("SELECT COUNT(*) FROM t_demands")
    int countAll();

    @Select("SELECT COUNT(title) FROM t_demands WHERE title LIKE '%${title}%'")
    int countByTitle(@Param("title") String title);

    @Insert("INSERT INTO t_demands (customer_id, title, publish_time, category_id, region, price, detail, start_date, end_date, attachment) VALUES (#{customerId}, #{title}, #{publish_time}, #{categoryId}, #{region}, #{price}, #{detail}, #{startDate}, #{endDate}, #{attachment})")
    @Options(useGeneratedKeys = true)
    int insert(Demand demand);

    @Update("UPDATE t_demands SET customer_id = #{customerId},title = #{title},publish_time = #{publishTime},category_id = #{categoryId},region = #{region},price = #{price},detail = #{detail},start_date = #{startDime},end_date = #{endDate},attachment = #{attachment} WHERE id = #{id}")
    int update(Demand demand);

    @Update("UPDATE t_demands SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") String status);

    @Delete("DELETE FROM t_demands WHERE id = #{id}")
    int delete(@Param("id") Integer id);
}