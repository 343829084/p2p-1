package com.esoft.ipspay.withdraw.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.user.controller.WithdrawHome;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.ipspay.trusteeship.exception.IpsPayOperationException;
import com.esoft.ipspay.withdraw.service.impl.IpsPayWithdrawOperation;
import com.esoft.jdp2p.loan.model.WithdrawCash;
@Component
@Scope(ScopeType.VIEW)
public class IpsPayWithdrawHome extends WithdrawHome {

	@Resource
	private IpsPayWithdrawOperation operation;

	@Override
	public String withdraw() {
		try {
			if (!calculateFee()) {
				return null;
			}
			
			if(!checkInvest()){
				FacesUtil.addErrorMessage("为了你的资金安全,请先进行账户余额实名认证！");
				return "pretty:get_investor_permission";
			};
			
			operation.createOperation(getInstance(),
					FacesUtil.getCurrentInstance());
		} catch (IpsPayOperationException spoe) {
			FacesUtil.addErrorMessage(spoe.getMessage());
		}
		return null;
	}

	@Override
	public Class<WithdrawCash> getEntityClass() {
		return WithdrawCash.class;
	}

}
