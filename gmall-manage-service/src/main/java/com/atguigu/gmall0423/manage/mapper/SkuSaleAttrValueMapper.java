package com.atguigu.gmall0423.manage.mapper;

import com.atguigu.gmall0423.bean.SkuSaleAttrValue;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuSaleAttrValueMapper extends Mapper<SkuSaleAttrValue>{
    //  根据spuId 查询数据
    List<SkuSaleAttrValue> selectSkuSaleAttrValueListBySpu(String spuId);
}
