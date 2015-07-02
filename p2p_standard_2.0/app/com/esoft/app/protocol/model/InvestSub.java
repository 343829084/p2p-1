package com.esoft.app.protocol.model;

import com.esoft.jdp2p.invest.model.Invest;

public class InvestSub extends Invest{
	private String loanId;//项目编号
	private String loanName;//项目名称
	private double jd;//进度
	public String getLoanId() {
		return loanId;
	}
	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
	public String getLoanName() {
		return loanName;
	}
	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}
	public double getJd() {
		return jd;
	}
	public void setJd(double jd) {
		this.jd = jd;
	}
}
