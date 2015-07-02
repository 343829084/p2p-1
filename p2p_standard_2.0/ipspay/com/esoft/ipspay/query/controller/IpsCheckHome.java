package com.esoft.ipspay.query.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.ipspay.query.bean.CheckBalanceResult;
import com.esoft.ipspay.query.service.impl.IpsCheckBalanceServiceImpl;

@Component
@Scope(ScopeType.VIEW)
public class IpsCheckHome {

	@Resource
	IpsCheckBalanceServiceImpl ipsCheckBalanceServiceImpl;

	private String userId;

	private CheckBalanceResult cbr;

	public void chekBalance() {
		try {
			cbr = ipsCheckBalanceServiceImpl.CheckBalanceByUserId(userId);
		} catch (UserNotFoundException e) {
			cbr=null;
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public CheckBalanceResult getCbr() {
		return cbr;
	}

	public void setCbr(CheckBalanceResult cbr) {
		this.cbr = cbr;
	}

}
