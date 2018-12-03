package com.everyone.crowd.dao;

import com.everyone.crowd.entity.Category;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryMapper {

    @Select("SELECT * FROM t_category")
    List<Category> findAll();

    @Select("SELECT * FROM t_category WHERE id = #{id}")
    Category findById(@Param("id") Integer id);

    @Update("UPDATE t_category SET name=#{name},description=#{description} WHERE id=#{id}")
    int update(@Param("id") Integer id, @Param("name") String name, @Param("description") String description);

    @Insert("INSERT INTO t_category(name, description) VALUES (#{name}, #{description})")
    int insert(Category category);

    @Delete("DELETE FROM t_category WHERE id=#{id}")
    int delete(@Param("id") Integer id);


}
