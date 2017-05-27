package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * Created by Administrator on 2017/5/22.
 */
public interface IProductService {
    ServiceResponse addOrUpdateProdunt(Product product);
    ServiceResponse<String> setSaleStauts(Integer productId,Integer stauts);
    ServiceResponse<ProductDetailVo> manageProductDtail(Integer productId);
    ServiceResponse<PageInfo> getList(int pageNum, int pageSize);
    ServiceResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);
    ServiceResponse<ProductDetailVo> getDetail(Integer productId);
    ServiceResponse<PageInfo> getProductByKeywordCategoryId(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
