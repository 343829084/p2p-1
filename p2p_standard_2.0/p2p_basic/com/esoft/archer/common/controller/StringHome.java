package com.esoft.archer.common.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.ScopeType;

/**
 * 一些字符串工具，在页面上用
 * 
 * @author Administrator
 * 
 */
@Component
@Scope(ScopeType.REQUEST)
public class StringHome {
	/**
	 * 字符串切割，加上“...”
	 */
	public String ellipsis(String str, int length) {
		char[] strs = str.toCharArray();
		String ellipsisStr = "...";
		if (length > strs.length) {
			length = strs.length;
			ellipsisStr = "";
		}
		char[] aimStrs = new char[length];
		System.arraycopy(strs, 0, aimStrs, 0, length);
		return String.valueOf(aimStrs) + ellipsisStr;
	}
}
