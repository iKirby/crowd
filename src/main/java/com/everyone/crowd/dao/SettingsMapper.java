package com.everyone.crowd.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsMapper {
    @Select("SELECT value FROM t_settings WHERE `key` = #{key}")
    String get(@Param("key") String key);

    @Delete("DELETE FROM t_settings WHERE `key` = #{key}")
    int delete(@Param("key") String key);

    @Insert("INSERT INTO t_settings VALUES (#{key}, #{value})")
    int put(@Param("key") String key, @Param("value") String value);

    @Update("UPDATE t_settings SET value = #{value} WHERE `key` = #{key}")
    int set(@Param("key") String key, @Param("value") String value);
}
