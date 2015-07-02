package com.esoft.ipspay.trusteeship.exception;

/**
 * 环迅支付操作异常
 * @author Administrator
 *
 */
public class IpsPayOperationException extends RuntimeException {
	public IpsPayOperationException(String msg) {
		super(msg);
	}

	public IpsPayOperationException(String msg, Throwable t) {
		super(msg, t);
	}

	public IpsPayOperationException(Throwable e) {
		super(e);
	}
}
