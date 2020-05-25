package com.atguigu.gmall0423.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0423.bean.SkuInfo;
import com.atguigu.gmall0423.bean.SkuLsInfo;
import com.atguigu.gmall0423.bean.SpuImage;
import com.atguigu.gmall0423.bean.SpuSaleAttr;
import com.atguigu.gmall0423.service.ListService;
import com.atguigu.gmall0423.service.ManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController  //    @ResponseBody+@Controller
@CrossOrigin
public class SkuManageController {

    @Reference
    private ListService listService;

    @Reference
    private ManageService manageService;

//    @RequestMapping("spuImageList")
//    public List<SpuImage> spuImageList(String spuId){
//
//    }
//    http://localhost:8082/spuImageList?spuId=58
    @RequestMapping("spuImageList")
    public List<SpuImage> spuImageList(SpuImage spuImage){
        // 调用service 层
        List<SpuImage> spuImageList = manageService.getSpuImageList(spuImage);
        return spuImageList;
    }

    @RequestMapping("spuSaleAttrList")
    public List<SpuSaleAttr> spuSaleAttrList(String spuId){
        // 调用service 层
        return manageService.getSpuSaleAttrList(spuId);
    }
    @RequestMapping("saveSkuInfo")
    public void saveSkuInfo(@RequestBody SkuInfo skuInfo){
        if (skuInfo!=null){
            manageService.saveSkuInfo(skuInfo);
        }
    }
    // 上传一个商品，如果上传批量！
    @RequestMapping("onSale")
    public void onSale(String skuId){
        // 众筹属性不能拷贝！？
        // 创建一个skuLsInfo 对象
        SkuLsInfo skuLsInfo = new SkuLsInfo();
        // 给skuLsInfo 赋值！
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        // 属性拷贝！
        BeanUtils.copyProperties(skuInfo,skuLsInfo);
//        try {
//            org.apache.commons.beanutils.BeanUtils.copyProperties(skuLsInfo,skuInfo);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
        listService.saveSkuLsInfo(skuLsInfo);
    }
}
