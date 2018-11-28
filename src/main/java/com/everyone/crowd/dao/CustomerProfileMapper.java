package com.everyone.crowd.dao;

import com.everyone.crowd.entity.CustomerProfile;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerProfileMapper {
    @Select("SELECT * FROM t_customerprofiles WHERE user_id = #{user_id}")
    CustomerProfile findById(@Param("user_id") Integer user_id);

    @Insert("INSERT INTO t_customerprofiles (user_id, name, email, phone, photo, addr, alipay, bio, cert, status) VALUES (#{userId}, #{name}, #{email}, #{phone}, #{photo},#{addr},#{alipay},#{bio},#{cert}, #{status})")
    int insert(CustomerProfile customerProfile);

    @Update("UPDATE t_customerprofiles SET name = #{name}, email = #{email}, phone = #{phone}, photo = #{photo}, addr = #{addr}, alipay = #{alipay}, bio = #{bio}, cert = #{cert} WHERE user_id = #{userId}")
    int update(CustomerProfile customerProfile);

    @Delete("DELETE  from t_customerprofiles where  user_id=#{user_id}")
    int delete(@Param("user_id") Integer user_id);

    @Update("UPDATE t_customerprofiles set status=#{status} where user_id = #{user_id}")
    int updateStatus(@Param("user_id") Integer user_id, @Param("status") String status);

    @Update("UPDATE t_customerprofiles set level=#{level} where user_id = #{user_id}")
    int updateLevel(@Param("user_id") Integer user_id, @Param("level") int level);
}
