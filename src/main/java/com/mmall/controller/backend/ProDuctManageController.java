package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 后台产品
 */
@Controller
@RequestMapping("/manage/product")
public class ProDuctManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    /**
     * 在业务逻辑层判断是更新产品还是增加产品
     *
     * @param session
     * @param product
     * @return
     */
    @RequestMapping("add_product.do")
    @ResponseBody
    public ServiceResponse addProduct(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //进行产品的业务逻辑
            return iProductService.addOrUpdateProdunt(product);
        } else {
            return ServiceResponse.createByErrorMessage("没有权限操作");
        }
    }

    /**
     * 产品上下架的方法
     *
     * @param session
     * @param productId
     * @param stauts
     * @return
     */
    @RequestMapping("set_sale_stauts.do")
    @ResponseBody
    public ServiceResponse setSaleStauts(HttpSession session, Integer productId, Integer stauts) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //进行产品的业务逻辑
            return iProductService.setSaleStauts(productId, stauts);
        } else {
            return ServiceResponse.createByErrorMessage("没有权限操作");
        }
    }

    /**
     * 获取产品详情
     *
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("get_detail.do")
    @ResponseBody
    public ServiceResponse getDtails(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //进行产品的业务逻辑
            //
            return iProductService.manageProductDtail(productId);
        } else {
            return ServiceResponse.createByErrorMessage("没有权限操作");
        }
    }

    /**
     * 利用插件的分页操作
     *
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("get_list.do")
    @ResponseBody
    public ServiceResponse getList(HttpSession session,
                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //进行产品的业务逻辑
            //
            return iProductService.getList(pageNum, pageSize);
        } else {
            return ServiceResponse.createByErrorMessage("没有权限操作");
        }
    }

    /**
     * 根据商品名和id进行搜索
     *
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("product_search.do")
    @ResponseBody
    public ServiceResponse productSearch(HttpSession session, String productName, Integer productId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //进行产品的业务逻辑
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return ServiceResponse.createByErrorMessage("没有权限操作");
        }
    }

    /**
     * 上传文件
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServiceResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        System.out.println(user);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,请先登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            String uploadPath = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, uploadPath);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServiceResponse.createBySuccess(fileMap);
        } else {
            return ServiceResponse.createByErrorMessage("没有权限操作");
        }
    }

    /**
     * 富文本上传
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("richtest_img_upload.do")
    @ResponseBody
    public Map richtestImgupload(HttpSession session,
                                 @RequestParam(value = "upload_file",required = false) MultipartFile file,
                                 HttpServletRequest request, HttpServletResponse response) {

        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success",false);
            resultMap.put("msg","请先登录");
            return resultMap;
        }
//simditor要求的json返回值
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
        if (iUserService.checkAdminRole(user).isSuccess()) {
            String uploadPath = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, uploadPath);
            if (StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success",false);
            resultMap.put("msg","抱歉!!您没有权限");
            return resultMap;
        }
    }
}
