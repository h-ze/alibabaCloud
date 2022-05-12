package com.hz.common.core.log.service;


import com.hz.system.api.feign.RemoteLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncLogService {
    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(String logs)
    {

        log.info("保存系统日志:{}",logs);
        //remoteLogService.saveLog(logs);
    }

}
