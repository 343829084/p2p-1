package com.esoft.ipspay.repay.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import com.esoft.archer.user.service.UserBillService;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.ArithUtil;
import com.esoft.ipspay.repay.service.impl.IpsPayAdvanceRepayOperation;
import com.esoft.ipspay.repay.service.impl.IpsPayNormalRepayOperation;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.controller.RepayHome;
import com.esoft.jdp2p.repay.exception.AdvancedRepayException;
import com.esoft.jdp2p.repay.exception.NormalRepayException;
import com.esoft.jdp2p.repay.model.LoanRepay;
import com.esoft.jdp2p.repay.model.Repay;

public class IpsPayRepayHome extends RepayHome {

	@Resource
	private IpsPayNormalRepayOperation normalRepayOperation;

	@Resource
	IpsPayAdvanceRepayOperation advanceRepayOperation;

	@Resource
	UserBillService userBillService;

	@Override
	public void normalRepay(String repayId) {
		try {
			LoanRepay lr = getBaseService().get(LoanRepay.class, repayId);
			// 判断是否可还款
			checkNormalRepay(lr);
			normalRepayOperation.createOperation(lr,
					FacesContext.getCurrentInstance());
		} catch (InsufficientBalance e) {
			// 余额不足
			FacesUtil.addErrorMessage("您的账户余额不足，无法完成还款，请充值。");
			return;
		} catch (IOException e) {
			throw new RuntimeException("unexpected invocation", e);
		} catch (NormalRepayException e) {
			FacesUtil.addErrorMessage(e.getMessage());
			return;
		}
	}

	@Override
	public void advanceRepay(String loanId) {
		try {
			Loan loan = getBaseService().get(Loan.class, loanId);
			advanceRepayOperation.createOperation(loan,
					FacesContext.getCurrentInstance());
		} catch (IOException e) {
			throw new RuntimeException("unexpected invocation", e);
		} catch (AdvancedRepayException e) {
			FacesUtil.addErrorMessage(e.getMessage());
			return;
		}
	}

	/**
	 * 检查是否可还款
	 * 
	 * @param repay
	 * @throws InsufficientBalance
	 * @throws NormalRepayException
	 */
	private void checkNormalRepay(LoanRepay repay) throws InsufficientBalance,
			NormalRepayException {
		if (!repay.getStatus().equals(LoanConstants.RepayStatus.REPAYING)) {
			// 该还款不处于正常还款状态。
			throw new NormalRepayException("还款：" + repay.getId() + "不处于正常还款状态。");
		}

		double repayAllMoney = ArithUtil
				.add(repay.getCorpus(), repay.getDefaultInterest(),
						repay.getFee(), repay.getInterest());
		double balance = userBillService.getBalance(repay.getLoan().getUser()
				.getId());
		if (balance < repayAllMoney) {
			throw new InsufficientBalance("balance:" + balance
					+ "  repayAllMoney:" + repayAllMoney);
		}
	}

	@Override
	public Class<Repay> getEntityClass() {
		return Repay.class;
	}

}
