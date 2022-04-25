package com.hz.service.impl;

import com.common.exception.RRException;
import com.google.gson.Gson;
import com.hz.dao.SysConfigDao;
import com.hz.entity.SysOss;
import com.hz.oss.cloud.CloudStorageConfig;
import com.hz.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigDao sysConfigDao;

    @Override
    public void saveConfig(SysOss sysOss) {
        sysConfigDao.createSysConfig(sysOss);
    }

    @Override
    public void update(SysOss sysOss) {
        sysConfigDao.updateOss(sysOss);
    }

    @Override
    public void updateValueByKey(String key, String value) {

    }

    @Override
    public void deleteBatch(String sysName) {
        sysConfigDao.deleteOss(sysName);
    }

    @Override
    public SysOss getSysOss(String sysName) {
        return sysConfigDao.selectOss(sysName);
    }

    @Override
    public List<SysOss> getSysOssList() {
        return sysConfigDao.selectAllOss();
        //return null;
    }

    @Override
    public String getValue(String key) {
        SysOss sysOss = sysConfigDao.selectOss(key);
        return sysOss.getType();

        //此处需要研究  可能原因是由于从redis里读取的
        /*SysConfigEntity config = sysConfigRedis.get(key);
        if(config == null){
            config = baseMapper.queryByKey(key);
            sysConfigRedis.saveOrUpdate(config);
        }

        return config == null ? null : config.getParamValue();*/
    }

    @Override
    public CloudStorageConfig getConfigObject(String key/*, Class<T> clazz*/) {
        SysOss sysOss = sysConfigDao.selectOss(key);
        CloudStorageConfig cloudStorageConfig = new CloudStorageConfig();
        cloudStorageConfig.setAliyunAccessKeyId(sysOss.getAccessKeyId());
        cloudStorageConfig.setAliyunAccessKeySecret(sysOss.getAccessKeySecret());
        cloudStorageConfig.setAliyunBucketName(sysOss.getBucketName());
        cloudStorageConfig.setAliyunEndPoint(sysOss.getEndpoint());
        cloudStorageConfig.setAliyunDomain("");
        cloudStorageConfig.setType(2);
        //String value = cloudStorageConfig.toString();
        return cloudStorageConfig;
       /* if(StringUtils.isNotBlank(value)){
            return new Gson().fromJson(value, clazz);
        }*/

        /*try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RRException("获取参数失败");
        }*/
        //此处需要研究 可能原因是由于从redis里读取的
        /*String value = getValue(key);
        if(StringUtils.isNotBlank(value)){
            return new Gson().fromJson(value, clazz);
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RRException("获取参数失败");
        }*/
    }
}
