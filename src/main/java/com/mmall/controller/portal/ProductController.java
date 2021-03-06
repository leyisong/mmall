package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServiceResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/5/24.
 */
@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    /**
     * 前端查看用户详情
     *
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServiceResponse<ProductDetailVo> detail(Integer productId) {
        return iProductService.getDetail(productId);
    }

    /**
     * 产品搜索(根据categoryid以及产品名称模糊查询)
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServiceResponse<PageInfo> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {

        return iProductService.getProductByKeywordCategoryId(keyword, categoryId, pageNum, pageSize, orderBy);

    }
}