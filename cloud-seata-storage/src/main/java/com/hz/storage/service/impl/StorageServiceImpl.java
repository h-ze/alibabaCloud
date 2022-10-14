package com.hz.storage.service.impl;

import com.hz.storage.dao.StorageDao;
import com.hz.storage.service.StorageService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {


    @Resource
    private StorageDao storageDao;

    /**
     * 扣减库存
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(Long productId, Integer count) {
        log.info("xId:{}", RootContext.getXID());
        log.info("------->storage-service中扣减库存开始");
        storageDao.decrease(productId,count);
        log.info("------->storage-service中扣减库存结束");
    }
}
