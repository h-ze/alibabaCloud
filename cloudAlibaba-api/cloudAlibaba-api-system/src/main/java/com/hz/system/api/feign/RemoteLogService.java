package com.hz.system.api.feign;


import com.hz.system.api.factory.RemoteLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(contextId = "remoteLogService", value = "system-log",fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService {

    @PostMapping("operLog")
    String saveLog(String log);

}
