package com.esoft.archer.user.aop;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.user.model.Role;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.impl.UserBO;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
import com.esoft.jdp2p.message.service.impl.MessageBO;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 积分监听器，给用户加积分
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-2-7 下午4:58:10
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-2-7 wangzhi 1.0
 */
//@Component
//@Aspect
public class UserPointMonitor {

	@Resource
	private MessageBO messageBO;

	/**
	 * 用户角色添加
	 * 
	 * @param user
	 * @param role
	 */
	@AfterReturning(argNames = "user, role", value = "execution(public void com.esoft.archer.user.service.impl.addRole(..)) && args(com.esoft.archer.user.model.User, com.esoft.archer.user.model.Role)")
	public void addRole(User user, Role role) {
		if (role.getId().equals("INVESTOR")) {
			// TODO:实名认证
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", user.getUsername());
//			messageBO.sendMsg(user, userMessageNodeId, params);
		}
	}
	
	/**
	 * 用户角色移除
	 * 
	 * @param user
	 * @param role
	 */
	@AfterReturning(argNames = "user, role", value = "execution(public void com.esoft.archer.user.service.impl.removeRole(..)) && args(com.esoft.archer.user.model.User, com.esoft.archer.user.model.Role)")
	public void removeRole(User user, Role role) {
		if (role.getId().equals("INACTIVE")) {
			// TODO:用户激活
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", user.getUsername());
//			messageBO.sendMsg(user, userMessageNodeId, params);
		}
	}
	

}
