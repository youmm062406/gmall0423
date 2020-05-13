package com.atguigu.gmall0423.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0423.bean.SpuInfo;
import com.atguigu.gmall0423.service.ManageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class SpuManageController {
    @Reference
    ManageService manageService;

//    @RequestMapping("spuList")
//    public List<SpuInfo> spuList(String catalog3Id){
//        SpuInfo spuInfo = new SpuInfo();
//        spuInfo.setCatalog3Id(catalog3Id);
//        List<SpuInfo> spuInfoList = manageService.getSpuInfoList(spuInfo);
//        return  spuInfoList;
//    }
    //http://localhost:8082/spuList?catalog3Id=100  实体类对象封装
    @RequestMapping("spuList")
    public List<SpuInfo> spuList(SpuInfo spuInfo){
        return  manageService.getSpuInfoList(spuInfo);
    }


    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public void saveSpuInfo(@RequestBody SpuInfo spuInfo){
        manageService.saveSpuInfo(spuInfo);
    }
}
