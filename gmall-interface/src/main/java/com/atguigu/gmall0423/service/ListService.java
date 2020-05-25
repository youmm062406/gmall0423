package com.atguigu.gmall0423.service;

import com.atguigu.gmall0423.bean.SkuLsInfo;
import com.atguigu.gmall0423.bean.SkuLsParams;
import com.atguigu.gmall0423.bean.SkuLsResult;

public interface ListService {

    /**
     * 保存数据到es 中！
     * @param skuLsInfo
     */
    void saveSkuLsInfo(SkuLsInfo skuLsInfo);

    /**
     * 检索数据
     * @param skuLsParams
     * @return
     */
    SkuLsResult search(SkuLsParams skuLsParams);

    /**
     * 记录每个商品被访问的次数
     * @param skuId
     */
    void incrHotScore(String skuId);
}
