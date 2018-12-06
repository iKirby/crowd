package com.everyone.crowd.dao;

import com.everyone.crowd.entity.Feedback;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackMapper {

    @Select("SELECT * FROM t_feedbacks WHERE id = #{id}")
    Feedback findById(@Param("id") Integer id);

    @Select("SELECT * FROM t_feedbacks LIMIT #{offset}, #{size}")
    List<Feedback> findAll(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_feedbacks WHERE reply IS NULL LIMIT #{offset}, #{size}")
    List<Feedback> findNotReplied(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_feedbacks WHERE user_id = #{userId} LIMIT #{offset}, #{size}")
    List<Feedback> findByUserId(@Param("userId") Integer userId, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(id) FROM t_feedbacks")
    int count();

    @Select("SELECT COUNT(id) FROM t_feedbacks WHERE reply IS NULL")
    int countNotReplied();

    @Select("SELECT COUNT(id) FROM t_feedbacks WHERE user_id = #{userId}")
    int countByUserId(Integer userId);

    @Insert("INSERT INTO t_feedbacks (user_id, url, title, submit_time, content) VALUES (#{userId}, #{url}, #{title}, #{submitTime}, #{content})")
    @Options(useGeneratedKeys = true)
    int insert(Feedback feedback);

    @Update("UPDATE t_feedbacks SET reply = #{reply} WHERE id = #{id}")
    int reply(@Param("id") Integer id, @Param("reply") String reply);
}
