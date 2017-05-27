package com.mmall.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品和购物车的vo包装类
 */
public class CartProductVo {
    //***********************购物车相关的***********************//
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    //*************************产品相关的*******************************//
    private String productNmae;
    private String productSubtitle;//标题
    private String productMainImage;//主图
    private BigDecimal productPrice;//价格
    private Integer productStatus;//状态 是否下架
    private BigDecimal productTotalPrice;//总价
    private Integer productStoke;//库存
    private Integer productChecked;//是否 被选中
    //*******************************************************//
    private String limitQuantity;//限制最大数量的一个返回结果

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductNmae() {
        return productNmae;
    }

    public void setProductNmae(String productNmae) {
        this.productNmae = productNmae;
    }

    public String getProductSubtitle() {
        return productSubtitle;
    }

    public void setProductSubtitle(String productSubtitle) {
        this.productSubtitle = productSubtitle;
    }

    public String getProductMainImage() {
        return productMainImage;
    }

    public void setProductMainImage(String productMainImage) {
        this.productMainImage = productMainImage;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public Integer getProductStoke() {
        return productStoke;
    }

    public void setProductStoke(Integer productStoke) {
        this.productStoke = productStoke;
    }

    public Integer getProductChecked() {
        return productChecked;
    }

    public void setProductChecked(Integer productChecked) {
        this.productChecked = productChecked;
    }

    public String getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(String limitQuantity) {
        this.limitQuantity = limitQuantity;
    }
}
