package com.everyone.crowd.dao;

import com.everyone.crowd.entity.VerifyRequest;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerifyRequestMapper {
    @Select("SELECT * FROM t_verifyrequests WHERE id = #{id}")
    VerifyRequest findById(Integer id);

    @Select("SELECT * FROM t_verifyrequests WHERE processed = #{processed} LIMIT #{offset}, #{size}")
    List<VerifyRequest> findByStatus(@Param("processed") Boolean processed, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_verifyrequests WHERE user_id = #{userId} LIMIT #{offset}, #{size}")
    List<VerifyRequest> findByUserId(@Param("userId") Integer userId, @Param("offset") int offset, @Param("size") int size);

    @Insert("INSERT INTO t_verifyrequests (user_id, type, real_name, cert_type, cert_no, msg, attachment) VALUES (#{userId}, #{type}, #{realName}, #{certType}, #{certNo}, #{msg}, #{attachment})")
    int insert(VerifyRequest verifyRequest);

    @Update("UPDATE t_verifyrequests SET processed = TRUE WHERE id = #{id}")
    int process(@Param("id") Integer id);

    @Select("SELECT COUNT(id) FROM t_verifyrequests WHERE processed = #{processed}")
    int countByStatus(@Param("processed") Boolean processed);

    @Select("SELECT COUNT(id) FROM t_verifyrequests WHERE user_id = #{userId}")
    int countByUserId(@Param("userId") Integer userId);

    @Delete("DELETE FROM t_verifyrequests WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);
}
