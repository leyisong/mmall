package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimaUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;

    public ServiceResponse addOrUpdateProdunt(Product product) {
        if (product != null) {
            //如果子图不为空,将子图赋值给主图,也就是默认子图的第一张作为主图
            if (StringUtils.isNotBlank(product.getSubImages())) {
                //和前台约定以逗号隔开
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }

            }
            //和前台约定更新产品的时候要传id过来,故下面是产品更新的方法
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServiceResponse.createBySuccess("更新产品成功");
                }
                return ServiceResponse.createBySuccessMessage("更新产品失败");
            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServiceResponse.createBySuccess("新增产品成功");
                }
                return ServiceResponse.createBySuccessMessage("新增产品失败");
            }
        }
        return ServiceResponse.createByErrorMessage("新增或更新产品参数不正确");
    }


    public ServiceResponse<String> setSaleStauts(Integer productId, Integer stauts) {
        if (productId == null || stauts == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(stauts);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServiceResponse.createBySuccess("修改产品状态成功");
        }
        return ServiceResponse.createByErrorMessage("修改产品状态失败");
    }

    public ServiceResponse<ProductDetailVo> manageProductDtail(Integer productId) {
        if (productId == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServiceResponse.createByErrorMessage("产品已下架或者已删除");
        }
        //VO对象-->value object
        //pojo-->bo(business object)-->vo(view object)
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);

        return ServiceResponse.createBySuccess(productDetailVo);
    }


    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        //imageHost;//图片服务器url图片的前缀
        //   parentCategoryId;//副分类的的分类id
        //createtime
        //updatetime
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            //如果为空,默认设置为根节点0
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        productDetailVo.setCreateTime(DateTimaUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimaUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    /**
     * 分页操作
     *
     * @return
     */

    public ServiceResponse<PageInfo> getList(int pageNum, int pageSize) {

        //startPage -->start
        //填充自己的sql查询结果
        //pageHelper收尾
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectList();

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : products) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo productPageInfo = new PageInfo(products);
        productPageInfo.setList(productListVoList);
        return ServiceResponse.createBySuccess(productPageInfo);

    }

    /**
     * pojo转换为vo
     *
     * @param product
     * @return
     */
    private ProductListVo assembleProductListVo(Product product) {

        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setSubImages(product.getSubImages());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        return productListVo;
    }

    public ServiceResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> products = productMapper.selectListByNameOrId(productName, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : products) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo productPageInfo = new PageInfo(products);
        productPageInfo.setList(productListVoList);
        return ServiceResponse.createBySuccess(productPageInfo);
    }


    public ServiceResponse<ProductDetailVo> getDetail(Integer productId) {
        if (productId == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServiceResponse.createByErrorMessage("产品已下架或者已删除");
        }
        if (product.getStatus() != Const.ProductStautsEnum.ON_SALE.getCode()) {
            return ServiceResponse.createByErrorMessage("产品已下架或者已删除");
        }
        //VO对象-->value object
        //pojo-->bo(business object)-->vo(view object)
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServiceResponse.createBySuccess(productDetailVo);
    }


    public ServiceResponse<PageInfo> getProductByKeywordCategoryId(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //没有改分类,并且还没有关键字,这时候返回一个空的结果集
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVos = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVos);
                return ServiceResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.getCategoryAndDeepChildrenCategory(category.getId()).getData();
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        //排序处理
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRIVE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                //拼接传递数据
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        //搜索product
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword, categoryIdList.size()==0?null:categoryIdList);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo productPageInfo = new PageInfo(productList);
        productPageInfo.setList(productListVoList);
        return ServiceResponse.createBySuccess(productPageInfo);
    }
}





