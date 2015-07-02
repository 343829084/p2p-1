package com.esoft.jdp2p.repay.controller;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.model.LoanRepay;

@Component
@Scope(ScopeType.VIEW)
public class LoanRepayList extends EntityQuery<LoanRepay> {
	@Logger
	static Log log;

	private Date searchMinTime;
	private Date searchMaxTime;

	public LoanRepayList() {
		final String[] RESTRICTIONS = { "id like #{loanRepayList.example.id}",
				"loan.id like #{loanRepayList.example.loan.id}",
				"loan.user.id = #{loanRepayList.example.loan.user.id}",
				"repayDay >= #{loanRepayList.searchMinTime}",
				"repayDay <= #{loanRepayList.searchMaxTime}",
				"status like #{loanRepayList.example.status}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	@Override
	protected void initExample() {
		LoanRepay example = new LoanRepay();
		Loan loan = new Loan();
		loan.setUser(new User());
		example.setLoan(loan);
		setExample(example);
	}

	public Date getSearchMinTime() {
		return searchMinTime;
	}

	public void setSearchMinTime(Date searchMinTime) {
		this.searchMinTime = searchMinTime;
	}

	public Date getSearchMaxTime() {
		return searchMaxTime;
	}

	public void setSearchMaxTime(Date searchMaxTime) {
		this.searchMaxTime = searchMaxTime;
	}
}
