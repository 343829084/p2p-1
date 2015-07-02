package com.esoft.weixinapp.message.request;

import com.esoft.weixinapp.message.respone.BaseMessage;

public class user  extends BaseMessage{
	private String name;
	private String pw;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	
}
