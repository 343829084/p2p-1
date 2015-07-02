package com.esoft.jdp2p.trusteeship.exception;

public class UserWealthOperationException extends RuntimeException {
	public UserWealthOperationException(String msg) {
		super(msg);
	}

	public UserWealthOperationException(String msg, Throwable t) {
		super(msg, t);
	}

	public UserWealthOperationException(Throwable e) {
		super(e);
	}
}

