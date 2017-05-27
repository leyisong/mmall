package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Administrator on 2017/5/21.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServiceResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServiceResponse.createByErrorMessage("用户名不存在");
        }
        //  密码登录 md5加密
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServiceResponse.createByErrorMessage("密码错误");
        }
        //登录成功,将密码设置为空,因为我们要将user信息返回,所以密码要设置为空
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess("登录成功", user);
    }


    public ServiceResponse<String> register(User user) {
        //校验用户名,email是否存在
      /*  int resultCount = userMapper.checkUsername(user.getUsername());
        if (resultCount > 0) {
            return ServiceResponse.createByErrorMessage("用户名已存在");
        }*/
        //复用下面的校验方法 故注释掉上面原始的方法
        ServiceResponse<String> validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

       /* resultCount = userMapper.checkEmail(user.getEmail());
        if (resultCount > 0) {
            return ServiceResponse.createByErrorMessage("邮箱已存在");
        }*/

        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }


        user.setRole(Const.Role.ROLE_CUSTOMER);
        //md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServiceResponse.createByErrorMessage("注册失败");
        }
        return ServiceResponse.createBySuccessMessage("注册成功");
    }

    public ServiceResponse<String> checkValid(String str, String type) {
        /**
         * isNotBlank表示不能为空白字符串;
         * isNotEmpty()可以为空白字符串
         *
         */
        if (StringUtils.isNotBlank(type)) {
            //开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServiceResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServiceResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServiceResponse.createByErrorMessage("参数错误");
        }

        return ServiceResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 查找密码找回问题
     *
     * @param username
     * @return
     */
    public ServiceResponse<String> selectQuestion(String username) {
        ServiceResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            //用户不存在
            return ServiceResponse.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServiceResponse.createBySuccess(question);

        }
        return ServiceResponse.createByErrorMessage("找回密码的问题是空的");
    }

    /**
     * 校验答案
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    public ServiceResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            //说明问题及问题答案是这个用户的且是正确的
            //得到一个uuid的一个forgetToken放到本地cash缓存中并设置有效期
            String forgetToken = UUID.randomUUID().toString();
            //将本地的缓存放进去
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServiceResponse.createBySuccess(forgetToken);
        }
        return ServiceResponse.createByErrorMessage("问题的答案错误");
    }

    /**
     * 修改密码
     *
     * @param username
     * @param password
     * @param forgetToken
     * @return
     */
    public ServiceResponse<String> forgetResetPassword(String username, String password, String forgetToken) {
        //校验token是否存在
        if (StringUtils.isBlank(forgetToken)) {
            return ServiceResponse.createByErrorMessage("参数错误,Token需要传递");
        }
        //为了安全,防止token_拼接,也需要校验username
        ServiceResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            //用户不存在
            return ServiceResponse.createByErrorMessage("用户名不存在");
        }
        //对缓存里的token也进行校验
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        //isBlank判断是否为null,源码里面有详细说明
        if (StringUtils.isBlank(token)) {
            return ServiceResponse.createByErrorMessage("token无效或者过期");
        }
        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(password);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (rowCount > 0) {
                return ServiceResponse.createBySuccessMessage("修改密码成功");

            }
        } else {

            return ServiceResponse.createByErrorMessage("token错误,请重新获取");
        }
        return ServiceResponse.createByErrorMessage("修改密码失败");

    }

    /**
     * 登录状态下的修改密码
     *
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    public ServiceResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
//防止横向越权,一定要校验这个用户的旧密码一定要指定这个用户,因为我们会查询一个count(1)出来,如果不指定id,name结果就是true了count>0;
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServiceResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServiceResponse.createBySuccessMessage("密码更新成功");
        }
        return ServiceResponse.createByErrorMessage("密码更新失败");
    }

    /**
     * 更新个人信息,更新成功后,要将更新后的信息返回给前台
     * @param user
     * @return
     */
    public ServiceResponse<User> update_information(User user) {
        //username是不能被更新的
        //email也要校验,新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServiceResponse.createByErrorMessage("该邮箱已经存在,请更换邮箱重试!");
        }
        //声明一个updateuser用于更新
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServiceResponse.createBySuccess("更新个人信息成功", updateUser);
        }
        return ServiceResponse.createByErrorMessage("更新个人信息失败");

    }

    /**
     * 获取用户个人详情
     * @param userId
     * @return
     */
    public ServiceResponse<User> get_information(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if (user==null){
            return  ServiceResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess(user);

    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServiceResponse checkAdminRole(User user){
        if (user!=null&&user.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return ServiceResponse.createBySuccess();
        }
        return  ServiceResponse.createByError();
    }

}