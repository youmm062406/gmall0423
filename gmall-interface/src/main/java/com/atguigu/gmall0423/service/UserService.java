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
     * 根据userId 查询用户地址列表
     * @param userId
     * @return
     */
    List<UserAddress> getUserAddressList(String userId);

    /**
     * 登录方法
     * @param userInfo
     * @return
     */
    UserInfo login(UserInfo userInfo);

    /**
     * 根据用户Id 查询数据
     * @param userId
     * @return
     */
    UserInfo verify(String userId);
}
