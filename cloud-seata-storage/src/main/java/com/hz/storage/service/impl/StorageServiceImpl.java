package com.hz.storage.service.impl;

import com.hz.storage.dao.StorageDao;
import com.hz.storage.service.StorageService;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
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
    /**
     * 事务传播特性设置为 REQUIRES_NEW 开启新的事务 重要！！！！一定要使用REQUIRES_NEW
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    //@GlobalTransactional(rollbackFor = Exception.class)
    public void decrease(Long productId, Integer count) {

        log.info("xId:{}", RootContext.getXID());
        log.info("------->storage-service中扣减库存开始");
        storageDao.decrease(productId,count);
        log.info("------->storage-service中扣减库存结束");

        /*try {
            log.info("xId:{}", RootContext.getXID());
            log.info("------->storage-service中扣减库存开始");
            storageDao.decrease(productId,count);
            log.info("------->storage-service中扣减库存结束");

        } catch (Exception e) {
            try {
                GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            } catch (TransactionException transactionException) {
                transactionException.printStackTrace();
            }
            throw e;

        }*/
    }
}
