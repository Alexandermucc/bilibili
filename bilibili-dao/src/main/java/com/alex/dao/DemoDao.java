package com.alex.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author Alexandermucc
 * @date 2022/5/13 - 22:13 - 周五
 **/
@Mapper
public interface DemoDao {
//    @Select("select id from demo where id=#{id}")
    Integer query(Integer id);

    String queryNameById(Integer id);
}
