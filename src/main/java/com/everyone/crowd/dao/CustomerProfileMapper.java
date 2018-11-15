package com.everyone.crowd.dao;

import com.everyone.crowd.entity.CustomerProfile;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerProfileMapper {
    @Select("SELECT * FROM t_customerprofiles WHERE user_id = #{user_id}")
    CustomerProfile findById(@Param("user_id") Integer user_id);


    @Insert("INSERT INTO t_customerprofiles (name, email, phone, photo, addr, alipay, bio, cert) VALUES (#{name}, #{email}, #{phone}, #{photo},#{addr},#{alipay},#{bio},#{cert})")
    @Options(useGeneratedKeys = true)
    void insert(CustomerProfile customerProfile);

    @Update("UPDATE t_customerprofiles SET name = #{name}, email = #{email}, phone = #{phone}, photo = #{photo}, addr = #{addr}, alipay = #{alipay}, bio = #{bio}, cert = #{cert}")
    void update(CustomerProfile customerProfile);

    @Delete("DELETE  from t_customerprofiles where  user_id=#{user_id}")
    void delete(@Param("user_id") Integer user_id);

    @Update("UPDATE t_customerprofiles set status=#{status} where user_id = #{user_id}")
     void  updateStatus (@Param("status") String status,@Param("user_id") Integer user_id);

    @Update("UPDATE t_customerprofiles set level=#{level} where user_id = #{user_id}")
    void  updateLevel (@Param("level") int level,@Param("user_id") Integer user_id);
}
