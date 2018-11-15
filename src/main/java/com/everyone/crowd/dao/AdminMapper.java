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

    @Update("UPDATE t_admins SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Integer id, @Param("password") String password);

}
