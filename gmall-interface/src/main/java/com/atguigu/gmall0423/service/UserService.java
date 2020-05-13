package com.atguigu.gmall0423.service;

import com.atguigu.gmall0423.bean.UserAddress;
import com.atguigu.gmall0423.bean.UserInfo;

import java.util.List;

public interface UserService {

    /**
     * 查询所有数据
     * @return
     */
    List<UserInfo> findAll();

    /**
     * 根据用户id查询用户地址
     * @param userId
     * @return
     */
    List<UserAddress> getUserAddressList(String userId);



}
