package com.qdone.support.rate.redisson.exception;

/**
 * 限流异常
 */
public class RateLimitException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String msg;
	private int code = 500;

	public RateLimitException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public RateLimitException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public RateLimitException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public RateLimitException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
