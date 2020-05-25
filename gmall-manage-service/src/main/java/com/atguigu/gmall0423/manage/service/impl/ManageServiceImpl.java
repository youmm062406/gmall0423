package com.atguigu.gmall0423.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall0423.bean.*;
import com.atguigu.gmall0423.config.RedisUtil;
import com.atguigu.gmall0423.manage.constant.ManageConst;
import com.atguigu.gmall0423.manage.mapper.*;
import com.atguigu.gmall0423.service.ManageService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
        // select * from baseCatalog2 where catalog1Id =?
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
        // select * from baseAttrInfo where catalog3Id = ?
//        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
//        baseAttrInfo.setCatalog3Id(catalog3Id);
//        return baseAttrInfoMapper.select(baseAttrInfo);
        return  baseAttrInfoMapper.getBaseAttrInfoListByCatalog3Id(catalog3Id);

    }

    // 为什么总是install ！
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

        // baseAttrValue
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        // if ( attrValueList.size()>0  && attrValueList!=null){
        if (attrValueList!=null && attrValueList.size()>0){
            // 循环判断
            for (BaseAttrValue baseAttrValue : attrValueList) {
                //  private String id;
                //  private String valueName; 前台页面传递
                //  private String attrId; attrId=baseAttrInfo.getId();
                //  前提条件baseAttrInfo 对象中的主键必须能够获取到自增的值！
                //  baseAttrValue.setId("122"); 测试事务
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insertSelective(baseAttrValue);
            }
        }
    }

    @Override
    public List<BaseAttrValue> getAttrValueList(String attrId) {

        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(attrId);
        List<BaseAttrValue> baseAttrValueList = baseAttrValueMapper.select(baseAttrValue);
//        new BaseAttrValue().setAttrId(attrId);
        return baseAttrValueList;
//        return baseAttrValueMapper.select();
    }

    @Override
    public BaseAttrInfo getAttrInfo(String attrId) {
        // baseAttrInfo.id = baseAttrValue.getAttrId();
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectByPrimaryKey(attrId);

        // 需要将平台属性值集合放入平台属性中
        //  select * from baseAttrVallue where attrId = ?
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(attrId);
        List<BaseAttrValue> baseAttrValueList = baseAttrValueMapper.select(baseAttrValue);

        baseAttrInfo.setAttrValueList(baseAttrValueList);
        return baseAttrInfo;
    }

    @Override
    public List<SpuInfo> getSpuList(String catalog3Id) {
        return null;
    }

    @Override
    public List<SpuInfo> getSpuList(SpuInfo spuInfo) {
        List<SpuInfo> spuInfoList = spuInfoMapper.select(spuInfo);
        return spuInfoList;
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectAll();
    }

    @Override
    @Transactional
    public void saveSpuInfo(SpuInfo spuInfo) {
        // 保存数据
        //        spuInfo
        //        spuImage
        //        spuSaleAttr
        //        spuSaleAttrValue

        spuInfoMapper.insertSelective(spuInfo);

        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if (spuImageList!=null && spuImageList.size()>0){
            for (SpuImage spuImage : spuImageList) {
                // 设置spuId
                spuImage.setSpuId(spuInfo.getId());
                spuImageMapper.insertSelective(spuImage);
            }
        }


        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if (spuSaleAttrList!=null && spuSaleAttrList.size()>0){
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insertSelective(spuSaleAttr);

                // spuSaleAttrValue
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if (spuSaleAttrValueList!=null && spuSaleAttrValueList.size()>0){
                    for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                        spuSaleAttrValue.setSpuId(spuInfo.getId());
                        spuSaleAttrValueMapper.insertSelective(spuSaleAttrValue);
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
//        skuInfo:
        skuInfoMapper.insertSelective(skuInfo);

//        skuImage:
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (skuImageList!=null &&skuImageList.size()>0){
            for (SkuImage skuImage : skuImageList) {
                // skuImage.skuId = skuInfo.getId();
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insertSelective(skuImage);
            }
        }
//        skuAttrValue:
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();

        // 集合长度：.size(); 字符串：length()  数组 length; 文件长度 length();
//        File file = new File();
//        file.length();
//       byte [] length = new byte[1024];

        if (skuAttrValueList!=null && skuAttrValueList.size()>0){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insertSelective(skuAttrValue);
            }
        }

//        skuSaleAttrValue:
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if (skuSaleAttrValueList!=null && skuSaleAttrValueList.size()>0){
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValueMapper.insertSelective(skuSaleAttrValue);
            }
        }
    }

    @Override
    public SkuInfo getSkuInfo(String skuId) {
//        Jedis jedis = redisUtil.getJedis();
//
//        jedis.set("ok","没毛病");
//
//        jedis.close();

        // ctrl+alt+m 方法提取快捷键
        // redisson 调用
        return getSkuInfoRedisson(skuId);
        // jedis 调用！
        // return getSkuInfoJedis(skuId);


    }

    private SkuInfo getSkuInfoRedisson(String skuId) {
        // 放入业务逻辑代码
        SkuInfo skuInfo =null;
        Jedis jedis = null;
        RLock lock = null;
        // ctrl+alt+t
        try {
            Config config = new Config();
            config.useSingleServer().setAddress("redis://192.168.25.220:6379");

            RedissonClient redissonClient = Redisson.create(config);
//        RedissonClient redissonClient = Redisson.create(new Config().useSingleServer().setAddress("redis://192.168.25.220:6379"));

            // 使用redisson 调用getLock
            lock = redissonClient.getLock("yourLock");

            // 加锁
            lock.lock(10, TimeUnit.SECONDS);

            jedis = redisUtil.getJedis();
            // 定义key： 见名之意： sku：skuId:info
            String skuKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;
            // 判断缓存中是否有数据，如果有，从缓存中获取，没有从db获取并将数据放入缓存！
            // 判断redis 中是否有key
            if (jedis.exists(skuKey)){
                // 取得key 中的value
                String skuJson = jedis.get(skuKey);
                // 将字符串转换为对象
                skuInfo = JSON.parseObject(skuJson, SkuInfo.class);
                //                jedis.close();
                return skuInfo;
            }else {
                skuInfo = getSkuInfoDB(skuId);
                // 放redis 并设置过期时间
                jedis.setex(skuKey,ManageConst.SKUKEY_TIMEOUT,JSON.toJSONString(skuInfo));
                return skuInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 如何解决空指针？  if
            if (jedis!=null){
                jedis.close();
            }
            if (lock!=null){
                lock.unlock();
            }
        }
        return getSkuInfoDB(skuId);
    }

    private SkuInfo getSkuInfoJedis(String skuId) {
        // 获取jedis
        SkuInfo skuInfo =null;
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            // 定义key： 见名之意： sku：skuId:info
            String skuKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;
            // 获取数据
            String skuJson = jedis.get(skuKey);

            if (skuJson==null || skuJson.length()==0){
                // 试着枷锁
                System.out.println("缓存中没有数据");
                // 执行set 命令
                // 定义上锁的key=sku:skuId:lock
                String skuLockKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKULOCK_SUFFIX;
                String lockKey   = jedis.set(skuLockKey, "good", "NX", "PX", ManageConst.SKULOCK_EXPIRE_PX);
                if ("OK".equals(lockKey)){
                    // 此时枷锁成功！
                    skuInfo = getSkuInfoDB(skuId);
                    // 将是数据放入缓存
                    // 将对象转换成字符串
                    String skuRedisStr = JSON.toJSONString(skuInfo);
                    jedis.setex(skuKey,ManageConst.SKUKEY_TIMEOUT,skuRedisStr);
                    // 删除锁！
                    jedis.del(skuLockKey);
                    return skuInfo;
                }else {
                    // 等待
                    Thread.sleep(1000);

                    // 调用getSkuInfo();
                    return getSkuInfo(skuId);
                }
            }else {
                skuInfo = JSON.parseObject(skuJson, SkuInfo.class);
                return skuInfo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return getSkuInfoDB(skuId);
    }

    private SkuInfo getSkuInfoDB(String skuId) {
        // 直接将skuImageList 放入skuInfo 中！
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        skuInfo.setSkuImageList(getSkuImageBySkuId(skuId));
        // 查询平台属性值集合
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuId);
        skuInfo.setSkuAttrValueList(skuAttrValueMapper.select(skuAttrValue));
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
        // 使用哪个mapper 调用查询接口！？ spuMaper ,skuMapper spuAttrValueMapper
        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuInfo.getId(),skuInfo.getSpuId());
    }

    @Override
    public List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId) {
        // 根据spuId 查询数据
      return   skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(spuId);

    }

    @Override
    public List<BaseAttrInfo> getAttrList(List<String> attrValueIdList) {
        // SELECT * FROM base_attr_info bai INNER JOIN base_attr_value bav ON bai.id = bav.attr_id WHERE bav.id in (80,82,83,13);
        // 80,82,83,13 可以看做一个字符串！
        // 将集合编程字符串
        String valueIds = StringUtils.join(attrValueIdList.toArray(), ",");
        System.out.println("valueIds:"+valueIds);
        return   baseAttrInfoMapper.selectAttrInfoListByIds(valueIds);

    }
}
