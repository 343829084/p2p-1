package com.esoft.weixinapp.test;


import com.esoft.weixinapp.message.request.user;
import com.esoft.weixinapp.message.respone.BaseMessage;

public class test {
	
		public static void main(String[] args) {
			
			BaseMessage u = new user();
			user u1=(user) u; 
		u.setFromUserName("aaaa");
		u.setToUserName("bbbb");
		u1.setName("cccc");
		u1.setPw("ddddd");
		System.out.println(u1.getFromUserName());
		System.out.println(u1.getToUserName());
		System.out.println(u1.getName());
		System.out.println(u1.getPw());
		
		
		}
}
