package com.esoft.app.protocol.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.esoft.app.protocol.model.InvestSub;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanCalculator;

public class InvestUtil {
	public static InvestSub getInvestSub(Invest invest,LoanCalculator loanCalculator) throws IllegalAccessException, InvocationTargetException, NoMatchingObjectsException{
		InvestSub sub=new InvestSub();
		BeanUtils.copyProperties(sub,invest);
		sub.getRepayRoadmap();
		Loan loan=invest.getLoan();
		sub.setLoanId(loan.getId());
		sub.setLoanName(loan.getName());
		sub.setJd(loanCalculator.calculateRaiseCompletedRate(loan.getId()));
		return sub;
	}
}
