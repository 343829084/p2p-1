package com.esoft.ipspay.recharge.controller;

import javax.annotation.Resource;

import com.esoft.archer.user.controller.RechargeHome;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.ipspay.recharge.service.impl.IpsPayRechargeOperation;
import com.esoft.ipspay.trusteeship.exception.IpsPayOperationException;
import com.esoft.jdp2p.loan.model.Recharge;

public class IpsPayRechargeHome extends RechargeHome {

	@Resource
	private IpsPayRechargeOperation operation;

	@Override
	public void recharge() {
		try {
			operation.createOperation(getInstance(),
					FacesUtil.getCurrentInstance());
		} catch (IpsPayOperationException spoe) {
			FacesUtil.addErrorMessage(spoe.getMessage());
		}
	}
	
	@Override
	public Class<Recharge> getEntityClass() {
		return Recharge.class;
	}

}
