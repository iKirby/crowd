package com.everyone.crowd.dao;

import com.everyone.crowd.entity.Bid;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidMapper {
    @Select("SELECT * FROM t_bids WHERE id = #{id}")
    Bid findById(@Param("id") Integer id);

    @Select("SELECT * FROM t_bids WHERE dev_id = #{devId} LIMIT #{offset}, #{size}")
    List<Bid> findByDevId(@Param("devId") Integer devId, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM  t_bids LIMIT #{offset}, #{size}")
    List<Bid> findAll(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM t_bids WHERE demand_id = #{demandId} LIMIT #{offset}, #{size}")
    List<Bid> findByDemandId(@Param("demandId") Integer demandId, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(id) FROM t_bids WHERE dev_id = #{devId}")
    int countByDevId(@Param("devId") Integer devId);

    @Select("SELECT COUNT(id) FROM t_bids WHERE dev_id = #{demandId}")
    int countByDemandId(@Param("demandId") Integer demandId);

    @Insert("INSERT INTO t_bids (demand_id, dev_id, price, msg, attachment) VALUES (#{demandId}, #{devId}, #{price}, #{msg}, #{attachment})")
    @Options(useGeneratedKeys = true)
    int insert(Bid bid);

    @Delete("DELETE FROM t_bids WHERE id = #{id}")
    int delete(@Param("id") Integer id);

    @Select("SELECT COUNT(*) FROM t_bids")
    int countAll();

}
