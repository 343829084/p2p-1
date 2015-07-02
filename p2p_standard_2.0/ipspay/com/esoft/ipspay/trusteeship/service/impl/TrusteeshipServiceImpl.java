package com.esoft.ipspay.trusteeship.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.core.annotations.Logger;
import com.esoft.ipspay.invest.service.impl.IpsPayInvestOperation;
import com.esoft.ipspay.loan.service.impl.IpsPayLoaningOperation;
import com.esoft.ipspay.loan.service.impl.IpsPayPassLoanApplyOperation;
import com.esoft.ipspay.repay.service.impl.IpsPayNormalRepayOperation;
import com.esoft.ipspay.trusteeship.IpsPayConstans;
import com.esoft.ipspay.withdraw.service.impl.IpsPayWithdrawOperation;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.TrusteeshipService;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;

public class TrusteeshipServiceImpl implements TrusteeshipService {

	@Logger
	Log log;
	
	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;
	
	@Resource
	IpsPayPassLoanApplyOperation passLoanApplyOperation;

	@Resource
	IpsPayInvestOperation investOperation;
	
	@Resource
	IpsPayLoaningOperation loaningOperation;
	
	@Resource
	IpsPayNormalRepayOperation normalRepayOperation;
	
	@Resource
	IpsPayWithdrawOperation withdrawOperation;

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void handleSendedOperations() {
		// 任何超过30分钟而无响应的操作，视为失败
		List<TrusteeshipOperation> tos = trusteeshipOperationBO
				.getUnCallbackOperations(15);
		if (log.isInfoEnabled()) {
			log.info("RefreshTrusteeshipOperation start, size:" + tos.size());
		}
		for (TrusteeshipOperation to : tos) {
			if (log.isDebugEnabled()) {
				log.debug("RefreshTrusteeshipOperation id:" + to.getId());
			}
			to.setStatus(TrusteeshipConstants.Status.NO_RESPONSE);
			if (to.getType().equals(IpsPayConstans.OperationType.LOAN)) {
				//标的登记失败
				//FIXME:还有可能是标的流标失败
				passLoanApplyOperation.fail(to);
			} else if (to.getType().equals(
					TrusteeshipConstants.OperationType.INVEST)) {
				investOperation.fail(to);
			} else if (to.getType().equals(
					TrusteeshipConstants.OperationType.REPAY)) {
				normalRepayOperation.fail(to);
			} else if (to.getType().equals(
					TrusteeshipConstants.OperationType.WITHDRAW_CASH)) {
				withdrawOperation.fail(to);
			}
			ht.update(to);
		}
	}

}
