package com.esoft.weixinapp.service.impl;

import com.esoft.weixinapp.message.request.BaseMessage;
import com.esoft.weixinapp.service.BaseManager;

public class BaseManagerImpl<T> implements BaseManager<T> {

	protected Class entityClass;
	private 	BaseMessage baseMessage;
	@Override
	public T setBaseMessage(T t) {

		return t;
	}
	

}
