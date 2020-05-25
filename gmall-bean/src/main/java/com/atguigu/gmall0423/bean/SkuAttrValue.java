package com.atguigu.gmall0423.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

// 保存平台属性
@Data
public class SkuAttrValue implements Serializable {
    @Id
    @Column
    String id;

    @Column
    String attrId;

    @Column
    String valueId;

    @Column
    String skuId;

}
