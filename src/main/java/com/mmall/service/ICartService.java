package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.vo.CartVo;

/**
 * Created by Administrator on 2017/5/24.
 */
public interface ICartService {
    ServiceResponse<CartVo> add(Integer useId, Integer count, Integer productId);
    ServiceResponse<CartVo> update(Integer useId, Integer count, Integer productId);
    ServiceResponse<CartVo> delete(Integer useId, String productIds);
    ServiceResponse<CartVo> list(Integer useId);
    ServiceResponse<CartVo> selectOrUnSelect(Integer useId,Integer productId,Integer checked);
    ServiceResponse<Integer> getCartProductCount(Integer userId);
}
