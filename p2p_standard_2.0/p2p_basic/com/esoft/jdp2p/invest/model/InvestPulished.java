package com.esoft.jdp2p.invest.model;


/** 投资榜
 * @author Yang
 *
 */
public class InvestPulished {
	/**
	 * 个人头像
	 */
	private String photo;
	private String username;
	/**
	 * 投资总额
	 */
	private double allInvestMoney;
	/**
	 * 投资总收益
	 */
	private double allPaidInterest;
	public String getPhoto(){
		return photo;
	}
	public void setPhoto(String photo){
		this.photo=photo;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public double getAllInvestMoney() {
		return allInvestMoney;
	}
	public void setAllInvestMoney(double allInvestMoney) {
		this.allInvestMoney = allInvestMoney;
	}
	public double getAllPaidInterest() {
		return allPaidInterest;
	}
	public void setAllPaidInterest(double allPaidInterest) {
		this.allPaidInterest = allPaidInterest;
	}
	public InvestPulished(String photp,String username, double allInvestMoney,
			double allPaidInterest) {
		this.photo=photp;
		this.username = username;
		this.allInvestMoney = allInvestMoney;
		this.allPaidInterest = allPaidInterest;
	}
	
	
}
