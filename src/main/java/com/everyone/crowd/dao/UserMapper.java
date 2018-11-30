package com.everyone.crowd.dao;

import com.everyone.crowd.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserMapper {

    @Select("SELECT * FROM t_users WHERE id = #{id}")
    User findById(@Param("id") Integer id);

    @Select("SELECT * FROM t_users WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM t_users WHERE cookie = #{cookie}")
    User findByCookie(@Param("cookie") String cookie);

    @Insert("INSERT INTO t_users (username, password, email, activate_code) VALUES (#{username}, #{password}, #{email}, #{activateCode})")
    @Options(useGeneratedKeys = true)
    int insert(User user);

    @Update("UPDATE t_users SET username = #{username}, password = #{password}, email = #{email}, balance = #{balance}, activated = #{activated} WHERE id = #{id}")
    int update(User user);

    @Update("UPDATE t_users SET activated = TRUE, activate_code = NULL WHERE activate_code = #{activateCode}")
    int activate(@Param("activateCode") String activateCode);

    @Update("UPDATE t_users SET iscustomer = TRUE WHERE id = #{id}")
    int isCustomer(@Param("id") Integer id);

    @Update("UPDATE t_users SET isdeveloper = TRUE WHERE id = #{id}")
    int isDeveloper(@Param("id") Integer id);

    @Update("UPDATE t_users SET email = #{email}, activate_code = #{activateCode} WHERE id = #{id}")
    int updateEmail(@Param("id") Integer id, @Param("email") String email, @Param("activateCode") String activateCode);

    @Update("UPDATE t_users SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Integer id, @Param("password") String password);

    @Update("UPDATE t_users SET two_factor = #{twoFactor} WHERE id = #{id}")
    int updateTwoFactor(@Param("id") Integer id, @Param("twoFactor") String twoFactor);

    @Update("UPDATE t_users SET cookie = #{cookie} WHERE id = #{id}")
    int updateCookie(@Param("id") Integer id, @Param("cookie") String cookie);

    @Update("UPDATE t_users SET balance = #{balance} WHERE id = #{id}")
    int updateBalance(@Param("id") Integer id, @Param("balance") BigDecimal balance);

    @Select("SELECT COUNT(id) FROM t_users WHERE username = #{username}")
    int countExists(@Param("username") String username);

    @Select("SELECT * FROM t_users LIMIT #{offset}, #{size}")
    List<User> findAllPaged(@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("SELECT COUNT(id) FROM t_users")
    int getUserCount();
}
