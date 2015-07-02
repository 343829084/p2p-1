package com.esoft.weixinapp.commons;

import com.esoft.archer.theme.controller.TplVars;

public class URLUtil {
public static void main(String[] args) {
	String path= Thread.currentThread().getContextClassLoader().getResource("").getPath();
	
	System.out.println("userhome:"+path);
}

}