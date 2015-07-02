package com.esoft.archer.testcase;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TestAOP {

	/**
	 * 用户角色添加
	 * 
	 * @param user
	 * @param role
	 */
	@AfterReturning(value = "execution(public void com.esoft.archer.testcase.TestService.test3())")
	public void addRole() {
		System.out.println(123123);
	}
	
	public static void main(String[] args) {
		System.out.println(StringUtils.leftPad("15", 3, "0"));
	}

}
