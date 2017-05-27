package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */
public class CartVo {

    List<CartProductVo> cartProductVoList;
    private BigDecimal cartTotalPrice;//购物车总价
    private  Boolean allChecked;//是否勾选
    private String imageHost;//购物车图片

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
