package com.atguigu.gmall0423.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0423.bean.*;
import com.atguigu.gmall0423.service.ManageService;
import com.sun.javafx.collections.MappingChange;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin//跨域请求
public class ManageController {

    @Reference
    ManageService manageService;

    @RequestMapping("getCatalog1")
    public List<BaseCatalog1> getCatalog1 (){
        return  manageService.getCatalog1();
    }

    //http://localhost:8082/getCatalog2?catalog1Id=6
    @RequestMapping("getCatalog2")
    public List<BaseCatalog2> getCatalog2 (String catalog1Id){
        return  manageService.getCatalog2(catalog1Id);
    }

    //http://localhost:8082/getCatalog3?catalog2Id=37
    @RequestMapping("getCatalog3")
    public List<BaseCatalog3> getCatalog3 (String catalog2Id){
        return  manageService.getCatalog3(catalog2Id);
    }


    //http://localhost:8082/attrInfoList?catalog3Id=282
    @RequestMapping("attrInfoList")
    public List<BaseAttrInfo> attrInfoList (String catalog3Id){
        return  manageService.getAttrList(catalog3Id);
    }

    //http://localhost:8082/saveAttrInfo
    //将前台传递的json数据转换成对象  @RequestBody
    @RequestMapping("saveAttrInfo")
    public void saveAttrInfo (@RequestBody BaseAttrInfo baseAttrInfo){
        //传递的是
        manageService.saveAttrInfo(baseAttrInfo);
    }


    @RequestMapping(value = "getAttrValueList",method = RequestMethod.POST)
    public List<BaseAttrValue> getAttrValueList(String attrId){
        //select * from BaseAttrValue where attrId = ?
        BaseAttrInfo attrInfo = manageService.getAttrInfo(attrId);
        return attrInfo.getAttrValueList();
    }

    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<BaseSaleAttr> getBaseSaleAttrList(){
        return   manageService.getBaseSaleAttrList();
    }
}
