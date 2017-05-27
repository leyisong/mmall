package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId")Integer  productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProductIds(@Param("userId") Integer userId,@Param("productList")List<String> productList);

    int checkedOrUncheckedProduct(@Param("userId") Integer userId,@Param("productId")Integer  productId,@Param("checked")Integer  checked);

    int selectCartProductCount(Integer useId);

    //查询勾选的
    List<Cart> selectCheckedCartByUserId(Integer userId);


}