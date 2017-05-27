package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServiceResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/25.
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    public ServiceResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            //********跟前台约定好,将添加的地址id返回去********//
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServiceResponse.createBySuccess("新建地址成功", result);
        }
        return ServiceResponse.createByErrorMessage("新建地址失败");
    }

    public ServiceResponse<String> delete(Integer userId, Integer shippingId) {
        //为了防止横向越权的问题,需要将删除的id和用户的id绑定
        // int rowCount = shippingMapper.deleteByPrimaryKey(shippingId);
        int rowCount = shippingMapper.deleteByPrimaryKeyUserId(userId, shippingId);
        if (rowCount > 0) {
            return ServiceResponse.createBySuccessMessage("删除地址成功");
        }
        return ServiceResponse.createByErrorMessage("删除地址失败");
    }

    public ServiceResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        //为了防止横向越权的问题,需要将删除的id和用户的id绑定
        // int rowCount = shippingMapper.deleteByPrimaryKey(shippingId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return ServiceResponse.createBySuccessMessage("更新地址成功");
        }
        return ServiceResponse.createByErrorMessage("更新地址失败");
    }

    public ServiceResponse<Shipping> select(Integer userId, Integer shippingId) {
        //为了防止横向越权的问题,需要将删除的id和用户的id绑定
        // int rowCount = shippingMapper.deleteByPrimaryKey(shippingId);
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping == null) {
            return ServiceResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServiceResponse.createBySuccess("删除地址成功", shipping);
    }

    public ServiceResponse<PageInfo> list(Integer pageNum, Integer pageSize, Integer userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserIdAll(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServiceResponse.createBySuccess(pageInfo);
    }
}
