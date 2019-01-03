package com.everyone.crowd.dao;

import com.everyone.crowd.entity.Announcement;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementMapper {
    @Insert("INSERT INTO t_announcements (title, content) VALUES (#{title}, #{content})")
    @Options(useGeneratedKeys = true)
    int insert(Announcement announcement);

    @Delete("delete from t_announcements where id = #{id}")
    int delete(@Param("id") Integer id);

    @Update("UPDATE t_announcements SET title = #{title}, content = #{content} WHERE id = #{id}")
    int update(Announcement announcement);

    @Select("SELECT * FROM t_announcements WHERE id = #{id}")
    Announcement findById(@Param("id") Integer id);

    @Select("SELECT * FROM t_announcements WHERE title LIKE '%${title}%' LIMIT #{offset}, #{size}")
    List<Announcement> findByTitle(@Param("title") String title, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_announcements LIMIT #{offset}, #{size}")
    List<Announcement> findAll(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(id) FROM t_announcements WHERE title LIKE '%${title}%'")
    int countByTitle(@Param("title") String title);

    @Select("SELECT COUNT(id) FROM t_announcements")
    int count();
}
