package com.mmall.dao;

import com.mmall.pojo.Cart;

public interface CartMapper {
    //根据主键删除
    int deleteByPrimaryKey(Integer id);
    //插入
    int insert(Cart record);
    //插入;对传入的参数判断是否为空在插入
    int insertSelective(Cart record);
    //查询根据主键
    Cart selectByPrimaryKey(Integer id);
    //根据主键更新有字段的空判断
    int updateByPrimaryKeySelective(Cart record);
    //没有空判断的更新
    int updateByPrimaryKey(Cart record);
}