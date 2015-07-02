package com.esoft.ipspay.loan.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.ArithUtil;
import com.esoft.ipspay.loan.service.impl.IpsPayCancelLoanOperation;
import com.esoft.ipspay.loan.service.impl.IpsPayLoaningOperation;
import com.esoft.ipspay.loan.service.impl.IpsPayPassLoanApplyOperation;
import com.esoft.ipspay.trusteeship.exception.IpsPayOperationException;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.LoanConstants.LoanStatus;
import com.esoft.jdp2p.loan.controller.LoanHome;
import com.esoft.jdp2p.loan.exception.BorrowedMoneyTooLittle;
import com.esoft.jdp2p.loan.exception.ExistWaitAffirmInvests;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanService;

public class IpsPayLoanHome extends LoanHome {

	@Resource
	IpsPayPassLoanApplyOperation passLoanApplyOperation;
	
	@Resource
	IpsPayCancelLoanOperation cancelLoanOperation;
	
	@Resource
	IpsPayLoaningOperation loaningOperation;
	
	@Resource
	LoginUserInfo loginUserInfo;
	
	@Resource
	LoanService loanService;
	
	@Override
	public String failByManager() {
		Loan loan = getBaseService().get(Loan.class, getInstance().getId());
		if (loan.getStatus().equals(LoanStatus.WAITING_VERIFY)) {
			return super.failByManager();
		}
		try {
			for (Invest invest : loan.getInvests()) {
				if (invest.getStatus().equals(
						InvestConstants.InvestStatus.WAIT_AFFIRM)) {
					// 流标时候，需要检查是否要等待确认的投资，如果有，则不让放款。
					throw new ExistWaitAffirmInvests("investID:" + invest.getId());
				}
			}
			cancelLoanOperation.createOperation(loan, FacesUtil.getCurrentInstance());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExistWaitAffirmInvests e) {
			FacesUtil.addInfoMessage("流标失败，存在等待第三方资金托管确认的投资。");
		}
		return null;
	}

	@Override
	public String verify() {
		if (getIspass()) {
			if (this.getInstance().getExpectTime() == null) {
				FacesUtil.addErrorMessage("请填写预计执行时间。");
				setIspass(false);
				return null;
			}
			try {
				this.getInstance().setVerifyUser(new User(loginUserInfo.getLoginUserId()));
				passLoanApplyOperation.createOperation(getInstance(),
						FacesContext.getCurrentInstance());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			loanService.refuseApply(this.getInstance().getId(), this
					.getInstance().getVerifyMessage(), loginUserInfo
					.getLoginUserId());
			FacesUtil.addInfoMessage("拒绝借款申请");
		}
		return null;
	}

	@Override
	public String recheck() {
		try {
			Loan loan = getInstance();
			loan.setActualRate(getLoanActualMoney(loan));
			loaningOperation.createOperation(loan, null);
			FacesUtil.addInfoMessage("放款成功，等待资金托管方确认");
		} catch (IpsPayOperationException spoe) {
			FacesUtil.addErrorMessage(spoe.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("unexpected invocation", e);
		} catch (ExistWaitAffirmInvests e) {
			FacesUtil.addInfoMessage("放款失败，存在等待第三方资金托管确认的投资。");
			return null;
		} catch (BorrowedMoneyTooLittle e) {
			FacesUtil.addInfoMessage("放款失败，募集到的资金太少。");
			return null;
		}
		return FacesUtil.redirect(loanListUrl);
	}

	private Double getLoanActualMoney(Loan loan) throws ExistWaitAffirmInvests,
			BorrowedMoneyTooLittle {
		// 实际到借款账户的金额
		double actualMoney = 0D;
		loan = getBaseService().get(Loan.class, loan.getId());
		for (Invest invest : loan.getInvests()) {
			if (invest.getStatus().equals(
					InvestConstants.InvestStatus.WAIT_AFFIRM)) {
				// 放款时候，需要检查是否要等待确认的投资，如果有，则不让放款。
				throw new ExistWaitAffirmInvests("investID:" + invest.getId());
			}
			if (invest.getStatus().equals(
					InvestConstants.InvestStatus.BID_SUCCESS)) {
				// 放款时候，需要只更改BID_SUCCESS 的借款。
				actualMoney = ArithUtil.add(actualMoney,
						invest.getInvestMoney());
			}
		}
		// 借款手续费-借款保证金
		double subR = ArithUtil.sub(loan.getLoanGuranteeFee(),
				loan.getDeposit());

		double tooLittle = ArithUtil.sub(actualMoney, subR);
		// 借到的钱，可能不足以支付借款手续费
		if (tooLittle <= 0) {
			throw new BorrowedMoneyTooLittle("actualMoney：" + tooLittle);
		}
		return actualMoney;
	}

	@Override
	public Class<Loan> getEntityClass() {
		return Loan.class;
	}

}
