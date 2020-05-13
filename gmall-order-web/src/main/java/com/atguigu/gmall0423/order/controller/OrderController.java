package com.atguigu.gmall0423.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0423.bean.UserAddress;
import com.atguigu.gmall0423.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrderController {

    //@Autowired
    @Reference
    UserService userService;


//    @RequestMapping("trade")
//    public String trade(){
//
//        //返回一个视图名称index.html
//        return "index";
//    }

    @RequestMapping("trade")
    @ResponseBody
    public List<UserAddress> trade(String userId){

        return userService.getUserAddressList(userId);
    }
}
