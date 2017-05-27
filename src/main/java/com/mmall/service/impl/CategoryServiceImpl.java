package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServiceResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/22.
 */
@Service("ICategoryService")
public class CategoryServiceImpl implements ICategoryService {


    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;

    public ServiceResponse addCategory(String categoryName, Integer parent_id) {
        //校验参数
        if (parent_id == null || categoryName == null) {
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parent_id);
        category.setStatus(true);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServiceResponse.createBySuccess("添加商品成功");
        }
        return ServiceResponse.createByErrorMessage("添加失败");
    }

    public ServiceResponse updateCategory(String categoryName, Integer categoryId) {
        //校验参数
        if (categoryId == null || categoryName == null) {
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServiceResponse.createBySuccess("更新商品名字成功");
        }
        return ServiceResponse.createByErrorMessage("更新商品名字失败");

    }

    public ServiceResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {

        List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        //isEmpty判断了是否为空,还判断了是否为空集合
        if (CollectionUtils.isEmpty(categories)) {
            //打印日志
            logger.info("为找到当前分类的子分类");
        }
        return ServiceResponse.createBySuccess(categories);
    }

    /**
     * 递归查询子节点及孩子节点
     * @param categoryId
     * @return
     */

    public ServiceResponse<List<Integer>> getCategoryAndDeepChildrenCategory(Integer categoryId){
        //guawa 里面的方法  初始化
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);

       List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId!=null){

            for (Category category1:categorySet){
                categoryIdList.add(category1.getId());

            }
        }
        return ServiceResponse.createBySuccess(categoryIdList);


    }

    /**
     * 递归算法算出子节点,并重写category的hashcode和equals
     * @return
     */
    private Set<Category> findChildCategory(Set<Category> categorySet ,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category!=null){
                categorySet.add(category);
            }
        //递归算法一定要有一个退出条件
        List<Category> categories = categoryMapper.selectCategoryChildrenByParentId(categoryId);
            for (Category category1:categories){
                findChildCategory(categorySet,category1.getId());
            }
        return categorySet;
    }

}
