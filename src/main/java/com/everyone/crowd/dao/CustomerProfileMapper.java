package com.everyone.crowd.dao;

import com.everyone.crowd.dao.sqlprovider.CustomerProfileSQLProvider;
import com.everyone.crowd.entity.CustomerProfile;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerProfileMapper {
    @Select("SELECT * FROM t_customerprofiles WHERE user_id = #{user_id}")
    CustomerProfile findById(@Param("user_id") Integer user_id);

    @Select("SELECT * FROM t_customerprofiles WHERE name LIKE '%${name}%' LIMIT #{offset}, #{size}")
    List<CustomerProfile> findByName(@Param("name") String name, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_customerprofiles LIMIT #{offset}, #{size}")
    List<CustomerProfile> findAll(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_customerprofiles")
    List<CustomerProfile> findAllMap();

    @Insert("INSERT INTO t_customerprofiles (user_id, name, email, phone, photo, addr, alipay, bio, cert, status) VALUES (#{userId}, #{name}, #{email}, #{phone}, #{photo},#{addr},#{alipay},#{bio},#{cert}, #{status})")
    int insert(CustomerProfile customerProfile);

    @Update("UPDATE t_customerprofiles SET name = #{name}, email = #{email}, phone = #{phone}, photo = #{photo}, addr = #{addr}, alipay = #{alipay}, bio = #{bio}, level = #{level},cert = #{cert},status = #{status} WHERE user_id = #{userId}")
    int update(CustomerProfile customerProfile);

    @Delete("DELETE  from t_customerprofiles WHERE user_id=#{user_id}")
    int delete(@Param("user_id") Integer user_id);

    @Update("UPDATE t_customerprofiles set status=#{status} WHERE user_id = #{user_id}")
    int updateStatus(@Param("user_id") Integer user_id, @Param("status") String status);

    @Update("UPDATE t_customerprofiles set level = level + 1 WHERE user_id = #{user_id}")
    int updateLevel(@Param("user_id") Integer user_id);

    @Update("UPDATE t_customerprofiles SET cert = #{cert} WHERE user_id = #{userId}")
    int updateCert(@Param("userId") Integer userId, @Param("cert") String cert);

    @Select("SELECT COUNT(user_id) FROM t_customerprofiles")
    int countAll();

    @Select("SELECT COUNT(user_id) FROM t_customerprofiles WHERE status = #{status}")
    int countByStatus(String status);

    @Select("SELECT COUNT(user_id) FROM t_customerprofiles WHERE name LIKE '%${name}%'")
    int countByName(@Param("name") String name);

    @SelectProvider(type = CustomerProfileSQLProvider.class, method = "findByIds")
    List<CustomerProfile> findByIds(@Param("ids") List<Integer> ids);
}
