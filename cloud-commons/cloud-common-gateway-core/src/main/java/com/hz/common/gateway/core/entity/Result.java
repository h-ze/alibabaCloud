package com.hz.common.gateway.core.entity;

import lombok.*;

import java.io.Serializable;

/**
 * 响应数据
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 3997124446365032582L;
	private Integer code = 200;
	private String msg="请求成功";
	private T data;
	

	/**
     * 服务器接到请求
	 * 业务处理时错误
     * @param data 响应体
     * @return
     */
    public static  <T> Result<T> error(T data){
    	return new Result<T>(500,"服务器异常",data);
	}
    
    /**
     * 服务器接到请求
	 * 业务处理时错误
	 * 自定义错误信息
     * @param data 响应体
     * @param code 业务编码
     * @param msg  业务消息
     * @return
     */
    public static  <T> Result<T> error(T data, int code, String msg){
		return new Result<T>(code,msg,data);
	}

	/**
	 * 服务器接到请求
	 * 业务处理时错误
	 * @param data 响应体
	 * @param code 业务编码
	 * @param msg  业务消息
	 * @return
	 */
	public static  <T> Result<T> error(int code, String msg, T data){
		return new Result<T>(code,msg,data);
	}
	/**
	 * 服务器接到请求
	 * 业务处理时错误
	 * 自定义错误信息
	 * @param code 业务编码
	 * @param msg  业务消息
	 * @param msg  业务消息
	 * @return
	 */
	public static Result<Object> error(int code, String msg){
		return new Result<Object>(code,msg,null);
	}
 
    
    /**
     * 服务器接到请求
	 * 服务器处理成功
     * @param data 响应体
     * @return
     */
    public static <T> Result<T> success(T data){
		return new Result<T>(200,"请求成功",data);
	}

}
