package com.hz.server.service.impl;

import com.common.entity.EnterpriseInfo;
import com.hz.server.dao.EnterpriseDao;
import com.hz.server.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("enterprise")
@Transactional
public class EnterpriseServiceImpl implements EnterpriseService {

    @Autowired
    private EnterpriseDao enterpriseDao;
    @Override
    public int addEnterpriseInfo(EnterpriseInfo enterpriseInfo) {

        return enterpriseDao.addEnterpriseInfo(enterpriseInfo);
    }

    @Override
    public int updateEnterpriseInfo(EnterpriseInfo enterpriseInfo) {

        return enterpriseDao.updateEnterpriseInfo(enterpriseInfo);
    }

    @Override
    public EnterpriseInfo getEnterpriseInfo(String enterpriseId) {
        return enterpriseDao.getEnterpriseInfo(enterpriseId);
    }

    @Override
    public int deleteEnterpriseInfo(String enterpriseId) {
        return enterpriseDao.deleteEnterpriseInfo(enterpriseId);
    }

    @Override
    public EnterpriseInfo getEnterpriseInfoByUserId(String userId) {
        EnterpriseInfo enterpriseInfoByUserId = enterpriseDao.getEnterpriseInfoByUserId(userId);
        return enterpriseInfoByUserId;
    }
}
