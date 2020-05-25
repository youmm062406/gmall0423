package com.atguigu.gmall0423.service;

import com.atguigu.gmall0423.bean.CartInfo;

import java.util.List;

public interface CartService  {

    // 写方法？ skuNum,skuId,userId
    void  addToCart(String skuId,String userId,Integer skuNum);


    /**
     * 根据用户Id 查询购物车数据！
     * @param userId
     * @return
     */
    List<CartInfo> getCartList(String userId);

    /**
     * 合并购物车
     * @param cartListCK
     * @param userId
     * @return
     */
    List<CartInfo> mergeToCartList(List<CartInfo> cartListCK, String userId);

    /**
     * 修改商品状态！
     * @param skuId
     * @param isChecked
     * @param userId
     */
    void checkCart(String skuId, String isChecked, String userId);

    /**
     * 根据userId 查询购物车列表
     * @param userId
     * @return
     */
    List<CartInfo> getCartCheckedList(String userId);

    /**
     * 通过userId 查询实时价格
     * @param userId
     * @return
     */
    List<CartInfo> loadCartCache(String userId);
}
