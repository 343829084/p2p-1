package com.esoft.ipspay.trusteeship.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.ipspay.invest.service.impl.IpsPayInvestOperation;
import com.esoft.ipspay.loan.service.impl.IpsPayCancelLoanOperation;
import com.esoft.ipspay.loan.service.impl.IpsPayLoaningOperation;
import com.esoft.ipspay.loan.service.impl.IpsPayPassLoanApplyOperation;
import com.esoft.ipspay.recharge.service.impl.IpsPayRechargeOperation;
import com.esoft.ipspay.repay.service.impl.IpsPayAdvanceRepayOperation;
import com.esoft.ipspay.repay.service.impl.IpsPayNormalRepayOperation;
import com.esoft.ipspay.repay.service.impl.IpsPayOverdueRepayOperation;
import com.esoft.ipspay.trusteeship.IpsPayConstans;
import com.esoft.ipspay.trusteeship.service.impl.IpsPayOperationServiceAbs;
import com.esoft.ipspay.user.service.impl.IpsPayCreateAccountOperation;
import com.esoft.ipspay.withdraw.service.impl.IpsPayWithdrawOperation;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;

public class TrusteeshipHome {
	@Logger
	protected static Log log;

	private String operationType = "";

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	@Resource
	LoginUserInfo loginUserInfo;

	@Resource
	IpsPayCreateAccountOperation createAccountOperation;

	@Resource
	IpsPayRechargeOperation rechargeOperation;

	@Resource
	IpsPayPassLoanApplyOperation passLoanApplyOperation;

	@Resource
	IpsPayCancelLoanOperation cancelLoanOperation;

	@Resource
	IpsPayInvestOperation investOperation;

	@Resource
	IpsPayLoaningOperation loaningOperation;

	@Resource
	IpsPayNormalRepayOperation normalRepayOperation;
	
	@Resource
	IpsPayAdvanceRepayOperation advanceRepayOperation;
	
	@Resource
	IpsPayOverdueRepayOperation overdueRepayOperation;

	@Resource
	IpsPayWithdrawOperation withdrawOperation;

	public String handleWebReturn() {
		if (log.isInfoEnabled()) {
			log.info("web call back: " + this.operationType + "  "
					+ getRequestParameters(FacesUtil.getHttpServletRequest()));
		}
		if (this.operationType
				.equals(TrusteeshipConstants.OperationType.CREATE_ACCOUNT)) {
			return this.web(createAccountOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.RECHARGE)) {
			return this.web(rechargeOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.INVEST)) {
			return this.web(investOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.REPAY)) {
			return this.web(normalRepayOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.ADVANCE_REPAY)) {
			return this.web(advanceRepayOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.OVERDUE_REPAY)) {
			return this.web(overdueRepayOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.WITHDRAW_CASH)) {
			return this.web(withdrawOperation);
		} else if (this.operationType
				.equals(IpsPayConstans.OperationType.LOAN)) {
			return this.loanWeb();
		}
		// FIXME:跳转到404页面
		return "404";
	}

	private String loanWeb() {
		try {
			passLoanApplyOperation.receiveOperationPostCallback(FacesUtil
					.getHttpServletRequest());
		} catch (TrusteeshipReturnException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
		FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
		return FacesUtil.redirect("/admin/loan/loanList");
	}

	public void handleS2SWebReturn() {
		if (log.isInfoEnabled()) {
			log.info("S2S call back: " + this.operationType + "  "
					+ getRequestParameters(FacesUtil.getHttpServletRequest()));
		}
		if (this.operationType
				.equals(TrusteeshipConstants.OperationType.CREATE_ACCOUNT)) {
			this.s2s(createAccountOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.RECHARGE)) {
			this.s2s(rechargeOperation);
		} else if (this.operationType
				.equals(IpsPayConstans.OperationType.LOAN)) {
			this.s2s(passLoanApplyOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.INVEST)) {
			this.s2s(investOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.REPAY)) {
			this.s2s(normalRepayOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.ADVANCE_REPAY)) {
			this.s2s(advanceRepayOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.OVERDUE_REPAY)) {
			this.s2s(overdueRepayOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.GIVE_MOENY_TO_BORROWER)) {
			this.s2s(loaningOperation);
		} else if (this.operationType
				.equals(TrusteeshipConstants.OperationType.WITHDRAW_CASH)) {
			this.s2s(withdrawOperation);
		}
	}

	private void s2s(IpsPayOperationServiceAbs operation) {
		operation.receiveOperationS2SCallback(
				FacesUtil.getHttpServletRequest(),
				FacesUtil.getHttpServletResponse());
		FacesUtil.getCurrentInstance().responseComplete();
	}

	private String web(IpsPayOperationServiceAbs operation) {
		try {
			operation.receiveOperationPostCallback(FacesUtil
					.getHttpServletRequest());
			FacesUtil.addInfoMessage("操作成功！");
		} catch (TrusteeshipReturnException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
		FacesUtil.addMessagesOutOfJSFLifecycle(FacesUtil.getCurrentInstance());
		return "pretty:userCenter";
	}

	private String getRequestParameters(ServletRequest request) {
		StringBuffer sb = new StringBuffer();
		Map map = request.getParameterMap();
		for (Object str : map.keySet()) {
			sb.append(str);
			sb.append(":");
			sb.append(request.getParameter(str.toString()));
			sb.append("  ");
		}
		return sb.toString();
	}
}
