package com.atguigu.gmall0423.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0423.bean.SkuInfo;
import com.atguigu.gmall0423.bean.SpuImage;
import com.atguigu.gmall0423.bean.SpuSaleAttr;
import com.atguigu.gmall0423.service.ManageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  //    @ResponseBody+@Controller
@CrossOrigin
public class SkuManageController {

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
}
