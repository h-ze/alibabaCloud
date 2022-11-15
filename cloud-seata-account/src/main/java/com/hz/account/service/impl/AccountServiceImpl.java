package com.hz.account.service.impl;

import com.hz.account.dao.AccountDao;
import com.hz.account.service.AccountService;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Resource
    AccountDao accountDao;

    /**
     * 扣减账户余额
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(Long userId, BigDecimal money) {
        try {
            log.info("xId:{}", RootContext.getXID());
            log.info("userId:{}",userId);
            log.info("money:{}",money);
            log.info("------->account-service中扣减账户余额开始");
            //模拟超时异常，全局事务回滚
            //暂停几秒钟线程
            accountDao.decrease(userId,money);
            //int i=1/0;
            /*try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            log.info("结束时间:{}",System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
            try {
                GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            } catch (TransactionException transactionException) {
                transactionException.printStackTrace();
            }
        }

        log.info("------->account-service中扣减账户余额结束");
    }
}
