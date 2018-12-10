package com.everyone.crowd.dao;

import com.everyone.crowd.entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper {
    @Select("SELECT * FROM t_admins WHERE username = #{username}")
    Admin findByUsername(@Param("username") String username);

    @Select("SELECT * FROM t_admins WHERE id = #{id}")
    Admin findById(@Param("id") Integer id);

    @Select("SELECT * FROM t_admins WHERE cookie = #{cookie}")
    Admin findByCookie(@Param("cookie") String cookie);

    @Update("UPDATE t_admins SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Integer id, @Param("password") String password);

    @Update("UPDATE t_admins SET cookie = #{cookie} WHERE id = #{id}")
    int updateCookie(@Param("id") Integer id, @Param("cookie") String cookie);

}
