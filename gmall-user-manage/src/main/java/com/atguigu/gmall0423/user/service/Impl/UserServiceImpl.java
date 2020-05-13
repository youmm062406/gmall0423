package com.atguigu.gmall0423.user.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall0423.bean.UserAddress;
import com.atguigu.gmall0423.bean.UserInfo;
import com.atguigu.gmall0423.service.UserService;
import com.atguigu.gmall0423.user.mapper.UserAddressMapper;
import com.atguigu.gmall0423.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserAddressMapper userAddressMapper;
    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }


}
