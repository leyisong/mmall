package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    //根据用户名查询返回一个数值
    int checkUsername(String username);
    //根据用户名密码查找用户@Param("username")多参数传入
    User selectLogin(@Param("username") String username, @Param("password") String password);
    //按邮箱查询
    int checkEmail(String email);
    //密码问题找回
    String selectQuestionByUsername(String username);
    //校验答案
    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);
    //修改密码
    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew")String passwordNew);
    //校验密码
    int checkPassword(@Param("password")String password,@Param("userId")Integer userId);
    //根据userid校验邮箱
    int checkEmailByUserId(@Param("email")String email,@Param("userId")Integer userId);

}