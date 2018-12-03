package com.everyone.crowd.dao;

import com.everyone.crowd.dao.sqlprovider.DevProfileSQLProvider;
import com.everyone.crowd.entity.DevProfile;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevProfileMapper {
    @Select("SELECT * FROM t_devprofiles WHERE user_id = #{user_id}")
    DevProfile findById(@Param("user_id") Integer user_id);

    @Insert("INSERT INTO t_devprofiles (user_id, name, email, phone, photo, addr, alipay, bio, cert, status) VALUES (#{userId}, #{name}, #{email}, #{phone}, #{photo},#{addr},#{alipay},#{bio},#{cert},#{status})")
    int insert(DevProfile devProfile);

    @Update("UPDATE t_devprofiles SET name = #{name}, email = #{email}, phone = #{phone}, photo = #{photo}, addr = #{addr}, alipay = #{alipay}, bio = #{bio}, cert = #{cert} WHERE user_id = #{userId}")
    int update(DevProfile devProfile);

    @Delete("DELETE  from t_devprofiles where  user_id=#{user_id}")
    int delete(@Param("user_id") Integer user_id);

    @Update("UPDATE t_devprofiles set status=#{status} where user_id = #{user_id}")
    int updateStatus(@Param("user_id") Integer user_id, @Param("status") String status);

    @Update("UPDATE t_devprofiles set level=#{level} where user_id = #{user_id}")
    int updateLevel(@Param("user_id") Integer user_id, @Param("level") int level);

    @SelectProvider(type = DevProfileSQLProvider.class, method = "findByIds")
    List<DevProfile> findByIds(@Param("ids") List<Integer> ids);
}
