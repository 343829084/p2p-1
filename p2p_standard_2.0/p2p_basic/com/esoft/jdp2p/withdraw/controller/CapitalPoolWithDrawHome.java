package com.esoft.jdp2p.withdraw.controller;
import javax.annotation.Resource;

import com.esoft.archer.user.controller.CapitalPoolWithdrawHome;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.loan.model.WithdrawCash;
import com.esoft.jdp2p.trusteeship.exception.UserWealthOperationException;
import com.esoft.jdp2p.withdraw.service.impl.CapitalPoolWithdrawOperation;


/**
 * 资金池提现入口
 *
 */

public class CapitalPoolWithDrawHome  extends CapitalPoolWithdrawHome{
	

	@Resource
	private CapitalPoolWithdrawOperation CapitalPool;
	@Override
	public String withdraw() {
		try {
			if (!calculateFee()) {
				return null;
			}
            if(!checkCap()){
            	FacesUtil.addErrorMessage("为了你的资金安全,请先进行好有钱实名认证！");
				return "pretty:bankCardList";
				
			}
			CapitalPool.createOperation(getInstance(),
					FacesUtil.getCurrentInstance());
			FacesUtil.addInfoMessage("您的提现申请已经提交成功，请等待审核！");
			return "pretty:myCashFlow";
		} catch (UserWealthOperationException spoe) {
			FacesUtil.addErrorMessage(spoe.getMessage());
		}
		return null;
	}
	@Override
	public Class<WithdrawCash> getEntityClass() {
		return WithdrawCash.class;
	}
}
