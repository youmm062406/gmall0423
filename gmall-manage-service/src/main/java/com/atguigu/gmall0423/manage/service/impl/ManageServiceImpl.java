package com.atguigu.gmall0423.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall0423.bean.*;
import com.atguigu.gmall0423.config.RedisUtil;
import com.atguigu.gmall0423.manage.constant.ManageConst;
import com.atguigu.gmall0423.manage.mapper.*;
import com.atguigu.gmall0423.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class ManageServiceImpl implements ManageService {
    @Autowired
    BaseCatalog1Mapper baseCatalog1Mapper;
    @Autowired
    BaseCatalog2Mapper baseCatalog2Mapper;
    @Autowired
    BaseCatalog3Mapper baseCatalog3Mapper;
    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;
    @Autowired
    SpuInfoMapper spuInfoMapper;

    @Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    SpuImageMapper spuImageMapper;
    @Autowired
    SpuSaleAttrMapper  spuSaleAttrMapper;

    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    RedisUtil redisUtil;
    @Override
    public List<BaseCatalog1> getCatalog1() {
        return baseCatalog1Mapper.selectAll();
    }

    @Override
    public List<BaseCatalog2> getCatalog2(String catalog1Id) {
        BaseCatalog2 baseCatalog2 = new BaseCatalog2();
        baseCatalog2.setCatalog1Id(catalog1Id);
        return baseCatalog2Mapper.select(baseCatalog2);
    }

    @Override
    public List<BaseCatalog3> getCatalog3(String catalog2Id) {
        BaseCatalog3 baseCatalog3 = new BaseCatalog3();
        baseCatalog3.setCatalog2Id(catalog2Id);
        return baseCatalog3Mapper.select(baseCatalog3);
    }

    @Override
    public List<BaseAttrInfo> getAttrList(String catalog3Id) {
        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        return baseAttrInfoMapper.select(baseAttrInfo);
    }
    @Transactional
    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        //修改操作
        if(baseAttrInfo.getId()!= null && baseAttrInfo.getId().length()>0){
            baseAttrInfoMapper.updateByPrimaryKeySelective(baseAttrInfo);
        }else{
            //保存数据baseAttrInfo
            baseAttrInfoMapper.insertSelective(baseAttrInfo);
        }

        //baseAttrValue  ？ 先清空数据，再插入数据即可
        //清空数据的条件  根据attrId 为依据
        //delete from baseAttrValue where attrId = baseAttrInfo.getId()
        BaseAttrValue baseAttrValueDel = new BaseAttrValue();
        baseAttrValueDel.setAttrId(baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValueDel);


        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();


        if(attrValueList != null && attrValueList.size() > 0){
            for(BaseAttrValue baseAttrValue : attrValueList){
                // private String id;
                // private String valueName;   前台页面传递
                // private String attrId;   attrId = baseAttrInfo.getId()；
                //前提条件baseAttrInfo对象中的主键必须能够获取到自增的值
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insertSelective(baseAttrValue);
            }
        }
    }

    @Override
    public BaseAttrInfo getAttrInfo(String attrId) {
        // 创建属性对象
        //baseAttrInfo.id = baseAttrValue.attrId
        BaseAttrInfo attrInfo = baseAttrInfoMapper.selectByPrimaryKey(attrId);
        // 创建属性值对象
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        // 根据attrId字段查询对象
        baseAttrValue.setAttrId(attrInfo.getId());
        List<BaseAttrValue> attrValueList = baseAttrValueMapper.select(baseAttrValue);
        // 给属性对象中的属性值集合赋值
        attrInfo.setAttrValueList(attrValueList);
        // 将属性对象返回
        return attrInfo;
    }

    @Override
    public List<SpuInfo> getSpuInfoList(String catalog3Id) {
        return null;
    }

    @Override
    public List<SpuInfo> getSpuInfoList(SpuInfo spuInfo) {
        return spuInfoMapper.select(spuInfo);
    }
    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectAll();
    }


    @Override
    @Transactional
    public void saveSpuInfo(SpuInfo spuInfo) {
        // 保存数据
        // spuInfo
        // spuImage
        // spuSaleAttr
        // spuSaleAttrValue

        // 什么情况下是保存，什么情况下是更新 spuInfo
        if (spuInfo.getId()==null || spuInfo.getId().length()==0){
            //保存数据
            spuInfo.setId(null);
            spuInfoMapper.insertSelective(spuInfo);
        }else {
            spuInfoMapper.updateByPrimaryKeySelective(spuInfo);
        }

        //  spuImage 图片列表 先删除，在新增
        //  delete from spuImage where spuId =?
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuInfo.getId());
        spuImageMapper.delete(spuImage);

        // 保存数据，先获取数据
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if (spuImageList!=null && spuImageList.size()>0){
            // 循环遍历
            for (SpuImage image : spuImageList) {
                image.setId(null);
                image.setSpuId(spuInfo.getId());
                spuImageMapper.insertSelective(image);
            }
        }
        // 销售属性 删除，插入
        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuInfo.getId());
        spuSaleAttrMapper.delete(spuSaleAttr);

        // 销售属性值 删除，插入
        SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
        spuSaleAttrValue.setSpuId(spuInfo.getId());
        spuSaleAttrValueMapper.delete(spuSaleAttrValue);

        // 获取数据
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if (spuSaleAttrList!=null && spuSaleAttrList.size()>0){
            // 循环遍历
            for (SpuSaleAttr saleAttr : spuSaleAttrList) {
                saleAttr.setId(null);
                saleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insertSelective(saleAttr);

                // 添加销售属性值
                List<SpuSaleAttrValue> spuSaleAttrValueList = saleAttr.getSpuSaleAttrValueList();
                if (spuSaleAttrValueList!=null && spuSaleAttrValueList.size()>0){
                    // 循环遍历
                    for (SpuSaleAttrValue saleAttrValue : spuSaleAttrValueList) {
                        saleAttrValue.setId(null);
                        saleAttrValue.setSpuId(spuInfo.getId());
                        spuSaleAttrValueMapper.insertSelective(saleAttrValue);
                    }
                }

            }
        }
    }

    @Override
    public List<SpuImage> getSpuImageList(SpuImage spuImage) {
        // sql：select * from spuImage where spuId=spuImage.getSpuId();
        List<SpuImage> spuImageList = spuImageMapper.select(spuImage);
        return spuImageList;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId) {
        // 调用mapper
        // 涉及两张表关联查询！
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
        return spuSaleAttrList;
    }

    @Override
    @Transactional
    public void saveSkuInfo(SkuInfo skuInfo) {
        //skuInfo:
        skuInfoMapper.insertSelective(skuInfo);

        //skuImage:
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (skuImageList!=null &&skuImageList.size()>0){
            for (SkuImage skuImage : skuImageList) {
                // skuImage.skuId = skuInfo.getId();
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insertSelective(skuImage);
            }
        }
        //skuAttrValue:
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();

        // 集合长度：.size(); 字符串：length()  数组 length; 文件长度 length();
        //File file = new File();
        //file.length();
        //byte [] length = new byte[1024];

        if (skuAttrValueList!=null && skuAttrValueList.size()>0){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insertSelective(skuAttrValue);
            }
        }

        //skuSaleAttrValue:
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if (skuSaleAttrValueList!=null && skuSaleAttrValueList.size()>0){
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValueMapper.insertSelective(skuSaleAttrValue);
            }
        }


    }
    @Override
    public SkuInfo getSkuInfo(String skuId){
        //        Jedis jedis = redisUtil.getJedis();
        //jedis.set("ok","没毛病");
        //jedis.close();
        //获取jedis
        Jedis jedis = null;
        SkuInfo skuInfo = null;
        try {
            jedis = redisUtil.getJedis();

            //涉及redis ，必须注意使用哪种数据类型来存储数据
            /**
             * redis五种数据类型使用场景
             * String: 短信验证码，存储一个变量
             * hash:json字符串{对象转换的字符串}
             * list：lpush，pop 队列使用
             * set:去重
             * zset：评分，排序
             */
            //获取缓存中的数据

            //定义key：见名知意  sku:skuId:info
            String skuKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;
            //判断redis中是否有key
            if(jedis.exists(skuKey)){
                //取得key中的value
                String skuJson= jedis.get(skuKey);
                //缓存中没有数据
                if(skuJson == null || skuJson.length() == 0){
                     //试着加锁
                    System.out.println("缓存中没有数据!");
                    //执行set命令
                    //定义上锁的key=sku:skuId:lock
                    String skuLocalKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKULOCK_SUFFIX;
                    String localKey = jedis.set(skuLocalKey, "good", "NX", "PX", ManageConst.SKULOCK_EXPIRE_PX);
                    if("OK".equals(localKey)){
                        //此时加锁成功
                        skuInfo = getSkuInfoDB(skuId);
                        jedis.setex(skuKey,ManageConst.SKUKEY_TIMEOUT,JSON.toJSONString(skuInfo));
                        //删除锁
                        jedis.del(skuLocalKey);
                        return skuInfo;
                    }else{
                        Thread.sleep(1000);
                        //重新调用getSkuInfo
                        return getSkuInfo(skuId);
                    }
                }else{
                    //将字符串转换为对象
                    skuInfo = JSON.parseObject(skuJson,SkuInfo.class);
                    return skuInfo;
                }
            }else{
                //判断缓存中是否有数据，如果有，取缓存中过的数据，如果没有，从db获取并将数据放入缓存
                skuInfo = getSkuInfoDB(skuId);
                jedis.setex(skuKey,ManageConst.SKUKEY_TIMEOUT,JSON.toJSONString(skuInfo));
                return skuInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return getSkuInfoDB(skuId);

    }

    private SkuInfo getSkuInfoDB(String skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        //可以单塞skuImageList，也可以放到skuInfo
        //getSkuImageBySkuId(skuId);
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);
        skuInfo.setSkuImageList(skuImageList);
        return skuInfo;
    }

    @Override
    public List<SkuImage> getSkuImageBySkuId(String skuId) {
        // select * from skuImnage where skuId = ?
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);
        return skuImageList;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(SkuInfo skuInfo) {
        // 使用哪个mapper 调用查询接口！？
        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuInfo.getId(),skuInfo.getSpuId());
    }

    @Override
    public List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId) {
        // 根据spuId 查询数据
        return   skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(spuId);

    }

}
