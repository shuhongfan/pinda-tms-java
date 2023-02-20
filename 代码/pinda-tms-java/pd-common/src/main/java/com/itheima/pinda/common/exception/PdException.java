package com.itheima.pinda.common.exception;

import lombok.Data;

/**
 * 自定义异常
 */
@Data
public class PdException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    
    public PdException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public PdException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public PdException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public PdException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public static void main(String[] args) {
    	try{
			int i = 1/0;
		}catch (Exception e){
    		throw new PdException("除法中分母不能为空");
		}

	}
}
