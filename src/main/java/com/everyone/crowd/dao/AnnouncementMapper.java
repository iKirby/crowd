package com.everyone.crowd.dao;

import com.everyone.crowd.entity.Announcement;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AnnouncementMapper {
    @Insert("INSERT INTO t_announcements (title, publish_time, content) VALUES (#{title}, #{publishTime}, #{content})")
    @Options(useGeneratedKeys = true)
    int insert(Announcement announcement);

    @Delete("delete from t_announcements where id = #{id}")
    int delete(@Param("id") Integer id);

    @Update("UPDATE t_announcements SET title = #{title}, publish_time = #{publishTime}, content = #{content} WHERE id = #{id}")
    int update(Announcement announcement);

    @Select("SELECT * FROM t_announcements WHERE id = #{id}")
    Announcement findById(@Param("id") Integer id);

    @Select("SELECT * FROM t_announcements ORDER BY publish_time DESC LIMIT #{offset}, #{size}")
    List<Announcement> findAll(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(id) FROM t_announcements")
    int count();

    @Select("SELECT * FROM t_announcements WHERE publish_time <= #{publishTime} ORDER BY publish_time DESC LIMIT #{offset}, #{size}")
    List<Announcement> findAllTill(@Param("offset") int offset, @Param("size") int size, @Param("publishTime") Date publishTime);

    @Select("SELECT COUNT(id) FROM t_announcements WHERE publish_time <= #{publishTime}")
    int countTill(@Param("publishTime") Date publishTime);
}
