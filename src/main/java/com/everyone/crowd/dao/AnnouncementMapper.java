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

    @Update("UPDATE t_announcements SET title = #{title}, content = #{content}")
    int update(Announcement announcement);

    @Select("SELECT * FROM t_announcements WHERE title LIKE '%${title}%'")
    List<Announcement> findByTitle(@Param("title") String title);
}
