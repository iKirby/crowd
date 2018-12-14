package com.everyone.crowd.dao;

import com.everyone.crowd.dao.sqlprovider.DemandSQLProvider;
import com.everyone.crowd.entity.Demand;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
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

    @Select("SELECT * FROM t_demands WHERE customer_id = #{customerId} AND category_id = #{categoryId} LIMIT #{offset}, #{size}")
    List<Demand> findByCustomerIdAndCategoryId(@Param("customerId") Integer customerId, @Param("categoryId") Integer categoryId, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_demands WHERE category_id = #{categoryId} and status = #{status} LIMIT #{offset}, #{size}")
    List<Demand> findByCategoryIdAndStatus(@Param("categoryId") Integer categoryId, @Param("status") String status, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(id) FROM t_demands WHERE status = #{status}")
    int countByStatus(@Param("status") String status);

    @Select("SELECT COUNT(id) FROM t_demands WHERE customer_id = #{customerId}")
    int countByCustomerId(@Param("customerId") Integer customerId);

    @Select("SELECT COUNT(id) FROM t_demands WHERE category_id = #{categoryId}")
    int countByCategoryId(@Param("categoryId") Integer categoryId);

    @Select("SELECT COUNT(category_id) FROM t_demands WHERE category_id = #{categoryId} AND status = #{status}")
    int countByCategoryIdAndStatus(@Param("categoryId") Integer categoryId, @Param("status") String status);

    @Select("SELECT COUNT(category_id) FROM t_demands WHERE customer_id = #{customerId} AND category_id = #{categoryId}")
    int countByCustomerIdAndCategoryId(@Param("customerId") Integer customerId, @Param("categoryId") Integer categoryId);

    @Select("SELECT COUNT(id) FROM t_demands")
    int countAll();

    @Select("SELECT COUNT(title) FROM t_demands WHERE title LIKE '%${title}%'")
    int countByTitle(@Param("title") String title);

    @Insert("INSERT INTO t_demands (customer_id, title, publish_time, category_id, region, price, detail, start_date, end_date, attachment,status) VALUES (#{customerId}, #{title}, #{publishTime}, #{categoryId}, #{region}, #{price}, #{detail}, #{startDate}, #{endDate}, #{attachment}, #{status})")
    @Options(useGeneratedKeys = true)
    int insert(Demand demand);

    @Update("UPDATE t_demands SET title = #{title},publish_time = #{publishTime},category_id = #{categoryId},region = #{region},price = #{price},detail = #{detail},start_date = #{startDate},end_date = #{endDate},attachment = #{attachment}, status = #{status} WHERE id = #{id}")
    int update(Demand demand);

    @Update("UPDATE t_demands SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") String status);

    @Delete("DELETE FROM t_demands WHERE id = #{id}")
    int delete(@Param("id") Integer id);

    @Select("SELECT COUNT(id) FROM t_demands WHERE publish_time >= #{publishTime}")
    int countByTimeAfter(Date publishTime);

    @SelectProvider(type = DemandSQLProvider.class, method = "countByMultipleConditions")
    int countByMultipleConditions(@Param("keyword") String keyword,
                                  @Param("categoryId") Integer categoryId,
                                  @Param("region") String region,
                                  @Param("lowPrice") BigDecimal lowPrice,
                                  @Param("highPrice") BigDecimal highPrice,
                                  @Param("startDateFrom") Date startDateFrom,
                                  @Param("startDateTo") Date startDateTo,
                                  @Param("endDateFrom") Date endDateFrom,
                                  @Param("endDateTo") Date endDateTo,
                                  @Param("status") String status);

    @SelectProvider(type = DemandSQLProvider.class, method = "findByMultipleConditions")
    List<Demand> findByMultipleConditions(@Param("keyword") String keyword,
                                          @Param("categoryId") Integer categoryId,
                                          @Param("region") String region,
                                          @Param("lowPrice") BigDecimal lowPrice,
                                          @Param("highPrice") BigDecimal highPrice,
                                          @Param("startDateFrom") Date startDateFrom,
                                          @Param("startDateTo") Date startDateTo,
                                          @Param("endDateFrom") Date endDateFrom,
                                          @Param("endDateTo") Date endDateTo,
                                          @Param("status") String status,
                                          @Param("offset") int offset,
                                          @Param("size") int size);
}
