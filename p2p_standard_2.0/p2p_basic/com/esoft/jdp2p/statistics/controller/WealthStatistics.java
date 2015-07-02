package com.esoft.jdp2p.statistics.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.user.service.UserBillService;
import com.esoft.archer.user.service.UserWealthService;
import com.esoft.core.annotations.ScopeType;

@Component
@Scope(ScopeType.REQUEST)
public class WealthStatistics {
	@Resource
	private UserWealthService uws;
	/**
	 * 获取用户账户余额
	 * 
	 * @return
	 */
	public double getBalanceByUserId(String userId) {
		return uws.getBalance(userId);
	}

	/**
	 * 获取用户账户冻结金额
	 * @param userId
	 * @return
	 */
	public double getFrozenMoneyByUserId(String userId) {
		return uws.getFrozenMoney(userId);
	}
}
