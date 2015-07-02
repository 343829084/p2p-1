package com.esoft.weixinapp.service;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CoreService{
	public  String processRequest(Map<String, String> requestMap) ;

	/**
	 * emoji表情转换(hex -> utf-16)
	 * 
	 * @param hexEmoji
	 * @return
	 */
	public String emoji(int hexEmoji);
	
}