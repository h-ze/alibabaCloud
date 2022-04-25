package com.qdone.support.async.log.db.handle;

/**
 * 日志处理基类
 * @author 付为地
 */
public abstract class LogHandler {

    /**
     * 执行日志打印
      * @param logData
     * @return 日志结果
     */
  public abstract SysActionLog handle(SysActionLog logData);

}
