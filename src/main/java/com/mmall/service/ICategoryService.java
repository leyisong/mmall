package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */
public interface ICategoryService {

    ServiceResponse addCategory(String categoryName, Integer parent_id);
    ServiceResponse updateCategory(String categoryName, Integer categoryId);
    ServiceResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
    ServiceResponse<List<Integer>> getCategoryAndDeepChildrenCategory(Integer categoryId);
}
