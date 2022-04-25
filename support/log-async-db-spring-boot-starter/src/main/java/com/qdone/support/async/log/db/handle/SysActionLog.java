package com.qdone.support.async.log.db.handle;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
* 用户日志
*
* @author 付为地
* @email 1335157415@qq.com
* @date 2021-09-01 15:38:26
*/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SysActionLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 编号
	*/
	private Long id;
	/**
	* 用户令牌
	*/
	private String token;
	/**
	* 链路编号
	*/
	private String trace;
	/**
	* 项目名称
	*/
	private String project;
	/**
	 * 项目版本
	 */
	private String version;
	/**
	 * 项目环境
	 */
	private String profile;
	/**
	* 模块名称
	*/
	private String moudle;
	/**
	* 操作类型
	*/
	private String actionType;
	/**
	* 日志类型 1:正常 0：异常
	*/
	private char type =1;
	/**
	* 请求URI
	*/
	private String requestUri;
	/**
	* 执行类名
	*/
	private String className;
	/**
	* 执行方法名称
	*/
	private String methodName;
	/**
	* 用户代理
	*/
	private String userAgent;
	/**
	* 操作IP地址
	*/
	private String remoteIp;
	/**
	* 操作方式
	*/
	private String requestMethod;
	/**
	* 请求参数
	*/
	private String requestParams;
	/**
	* 返回参数
	*/
	private String responseParams;
	/**
	* 设备MAC
	*/
	private String requestMac;
	/**
	* 异常信息
	*/
	private String exception;
	/**
	* 执行线程
	*/
	private String actionThread;
	/**
	* 开始执行时刻
	*/
	private Instant actionStartTime;
	/**
	* 结束执行时刻
	*/
	private Instant actionEndTime;
	/**
	* 执行耗时 单位(毫秒)
	*/
	private Long actionTime;
	/**
	* 创建日志时间
	*/
	private Instant createTime;
}
