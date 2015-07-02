package com.esoft.app.protocol.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.esoft.app.protocol.model.LoanSub;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanCalculator;
import com.google.gson.GsonBuilder;

public class LoanUtil {
	public static LoanSub getLoanSub(GsonBuilder builder,Loan loan,LoanCalculator loanCalculator) throws IllegalAccessException, InvocationTargetException, NoMatchingObjectsException{
		String statusValue=LoanType.getValue(LoanType.statusNameMap,loan.getStatus());
		LoanSub sub=new LoanSub();
		BeanUtils.copyProperties(sub, loan);
		sub.setJd(loanCalculator.calculateRaiseCompletedRate(loan.getId()));//进度
		sub.setLastStrTime(loanCalculator.calculateRemainTime(loan.getId()));//最后时间
		sub.setSybj(loanCalculator.calculateMoneyNeedRaised(loan.getId()));//剩余本金
		if(statusValue!=null){
			sub.setStatus(statusValue);
		}
		return sub;
	}
}
