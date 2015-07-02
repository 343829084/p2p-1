package com.esoft.ipspay.invest.controller;

import java.io.IOException;

import javax.annotation.Resource;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.ipspay.invest.service.impl.IpsPayInvestOperation;
import com.esoft.ipspay.trusteeship.exception.IpsPayOperationException;
import com.esoft.jdp2p.coupon.exception.ExceedDeadlineException;
import com.esoft.jdp2p.coupon.exception.UnreachedMoneyLimitException;
import com.esoft.jdp2p.invest.controller.InvestHome;
import com.esoft.jdp2p.invest.exception.ExceedMaxAcceptableRate;
import com.esoft.jdp2p.invest.exception.ExceedMoneyNeedRaised;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.Loan;

public class IpsPayInvestHome extends InvestHome {
	
	@Resource
	IpsPayInvestOperation investOperation;
	
	@Resource
	LoginUserInfo loginUserInfo;
	
	@Override
	public String save() {
		try {
			Loan loan = getBaseService().get(Loan.class,
					getInstance().getLoan().getId());
			if (loan.getUser().getId()
					.equals(loginUserInfo.getLoginUserId())) {
				FacesUtil.addInfoMessage("你不能投自己的项目！");
				return null;
			} else {
				this.getInstance().setUser(
						new User(loginUserInfo.getLoginUserId()));
				this.getInstance().setIsAutoInvest(false);
			investOperation.createOperation(getInstance(), FacesUtil.getCurrentInstance());}
		} catch (IpsPayOperationException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Class<Invest> getEntityClass() {
		return Invest.class;
	}
}
