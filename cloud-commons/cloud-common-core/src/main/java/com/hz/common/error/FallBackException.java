package com.hz.common.error;

/**
 * 降级处理特定异常
 * 不要被全局异常捕获
 * @author 付为地
 */
public class FallBackException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String msg;
	private int code = 500;

	public FallBackException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public FallBackException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public FallBackException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public FallBackException(String msg, int code, Throwable e) {
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
