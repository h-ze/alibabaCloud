package com.hz.server.service;

import com.common.entity.EnterpriseInfo;

public interface EnterpriseService {

    int addEnterpriseInfo(EnterpriseInfo enterpriseInfo);

    int updateEnterpriseInfo(EnterpriseInfo enterpriseInfo);

    EnterpriseInfo getEnterpriseInfo(String enterpriseId);

    int deleteEnterpriseInfo(String enterpriseId);

    EnterpriseInfo getEnterpriseInfoByUserId(String userId);
}
