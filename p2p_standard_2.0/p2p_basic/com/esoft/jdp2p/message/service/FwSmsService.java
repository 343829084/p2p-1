package com.esoft.jdp2p.message.service;

import com.esoft.jdp2p.message.exception.SmsSendErrorException;

public interface FwSmsService {

	/**
	 * 发送短信
	 * @param content
	 * @param mobileNumber
	 * @throws SmsSendErrorException
	 */
	public abstract void send(String content, String mobileNumber) throws SmsSendErrorException;
}
