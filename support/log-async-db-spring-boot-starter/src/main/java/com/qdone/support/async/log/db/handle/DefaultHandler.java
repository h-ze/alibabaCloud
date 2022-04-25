package com.qdone.support.async.log.db.handle;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 默认的日志处理
 * @author 付为地
 */
@Slf4j
public class DefaultHandler extends LogHandler {

    /**
     * 执行日志打印
     * @param logData
     * @return
     */
    @Override
    public SysActionLog handle(SysActionLog logData) {
        String type= ObjectUtils.isNotEmpty(logData.getType())&&logData.getType()==0?"异常":"正常";
        if(ObjectUtils.isNotEmpty(logData.getType())&&logData.getType()==0){
            log.error("全局日志打印 令牌[{}],链路[{}],项目[{}],模块[{}],业务[{}],类型[{}],类名[{}],方法名[{}],方式[{}],URL[{}],参数[{}],返回[{}],异常[{}],开始[{}],结束[{}],耗时[{}]",
                    logData.getToken(),logData.getTrace(),logData.getProject(),logData.getMoudle(), logData.getActionType(),type,logData.getClassName(), logData.getMethodName(),logData.getRequestMethod(),logData.getRequestUri()
                    ,logData.getRequestParams(),logData.getResponseParams(),logData.getException(),logData.getActionStartTime(),logData.getActionEndTime(),logData.getActionTime());
        }else{
            log.info("全局日志打印 令牌[{}],链路[{}],项目[{}],模块[{}],业务[{}],类型[{}],类名[{}],方法名[{}],方式[{}],URL[{}],参数[{}],返回[{}],开始[{}],结束[{}],耗时[{}]",
                    logData.getToken(),logData.getTrace(),logData.getProject(),logData.getMoudle(), logData.getActionType(),type,logData.getClassName(), logData.getMethodName(),logData.getRequestMethod(),logData.getRequestUri()
                    ,logData.getRequestParams(),logData.getResponseParams(),logData.getActionStartTime(),logData.getActionEndTime(),logData.getActionTime());
        }
        return logData;
    }

}
