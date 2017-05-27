package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/5/22.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加种类
     *
     * @param session
     * @param categoryName
     * @param parent_id
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServiceResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parent_id", defaultValue = "0") Integer parent_id) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        //校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            return iCategoryService.addCategory(categoryName, parent_id);

        } else {
            return ServiceResponse.createByErrorMessage("不是管理员,没有权限操作");
        }
    }

    /**
     * 更新商品名字
     *
     * @param session
     * @param categoryName
     * @param categoryId
     * @return
     */
    @RequestMapping("update_category.do")
    @ResponseBody
    public ServiceResponse updateCategory(HttpSession session, String categoryName, Integer categoryId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        //校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            return iCategoryService.updateCategory(categoryName, categoryId);

        } else {
            return ServiceResponse.createByErrorMessage("不是管理员,没有权限操作");
        }
    }

    /**
     * 查询分类的节点 平级查询
     *
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_children_parallel_category.do")
    @ResponseBody
    public ServiceResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        //校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            //查询子节点的category的信息,并且保持不递归
            return iCategoryService.getChildrenParallelCategory(categoryId);

        } else {
            return ServiceResponse.createByErrorMessage("不是管理员,没有权限操作");
        }
    }

    /**
     * 递归查询分类下面的分类
     *
     * @param session
     * @param categoryId
     * @return
     */


    @RequestMapping("get_category_and_deep_children_category.do")
    @ResponseBody
    public ServiceResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        //校验是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            //查询子节点的category的信息,并且递归子节点的id
            // 0-->100-->1000

            return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);

        } else {
            return ServiceResponse.createByErrorMessage("不是管理员,没有权限操作");
        }
    }


}
