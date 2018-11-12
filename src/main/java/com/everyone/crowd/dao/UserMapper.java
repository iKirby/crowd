package com.everyone.crowd.dao;

import com.everyone.crowd.entity.User;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM t_users WHERE username = #{username}")
    User getByUsername(User user);

    @Select("SELECT * FROM t_users WHERE cookie = #{value}")
    User getByCookie(String cookie);

    @Insert("INSERT INTO t_users (username, password, email, activate_code) VALUES (#{username}, #{password}, #{email}, #{activateCode})")
    @Options(useGeneratedKeys = true)
    Integer insert(User user);

    @Update("UPDATE t_users SET username = #{username}, password = #{password}, email = #{email}, balance = #{balance}, activated = #{activated}")
    Integer update(User user);

    @Update("UPDATE t_users SET activated = TRUE, activate_code = NULL WHERE activate_code = #{activateCode}")
    Integer activate(@Param("activateCode") String activateCode);

    @Update("UPDATE t_users SET email = #{email} WHERE id = #{id}")
    Integer updateEmail(@Param("id") Integer id, @Param("email") String email);

    @Update("UPDATE t_users SET password = #{password} WHERE id = #{id}")
    Integer updatePassword(@Param("id") Integer id, @Param("password") String password);

    @Update("UPDATE t_users SET two_factor = #{twoFactor} WHERE id = #{id}")
    Integer updateTwoFactor(@Param("id") Integer id, @Param("twoFactor") String twoFactor);

    @Update("UPDATE t_users SET cookie = #{cookie} WHERE id = #{id}")
    Integer updateCookie(@Param("id") Integer id, @Param("cookie") String cookie);

    @Update("UPDATE t_users SET balance = #{balance} WHERE id = #{id}")
    Integer updateBalance(@Param("id") Integer id, @Param("balance") BigDecimal balance);
}
