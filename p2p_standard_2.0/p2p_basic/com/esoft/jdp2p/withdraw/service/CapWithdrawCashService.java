package com.esoft.jdp2p.withdraw.service;

import com.esoft.archer.user.model.UserWealth;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.WithdrawCash;

public interface CapWithdrawCashService {

	/**
	 * 计算提现费用
	 * 
	 * @param amount
	 *            提现金额
	 * @return double 提现费用
	 */
	public double calculateFee(double amount);

	/**
	 * 通过提现申请
	 * 
	 * @param withdrawCash
	 *            提现instance
	 */
	public void passWithdrawCashApply(WithdrawCash withdrawCash);
	
	/**
	 * 通过提现复核
	 * 
	 * @param withdrawCash
	 *            提现instance
	 */
	public void passWithdrawCashRecheck(WithdrawCash withdrawCash);

	/**
	 * 拒绝提现申请
	 * 
	 * @param withdrawCash
	 *            提现instance
	 */
	public void refuseWithdrawCashApply(WithdrawCash withdrawCash);

	/**
	 * 申请提现
	 * 
	 * @param withdrawCash
	 * @throws InsufficientBalance 
	 */
	public void applyWithdrawCash(WithdrawCash withdraw ) throws InsufficientBalance;
	/**
	 * 管理员提现 资金池
	 * @throws InsufficientBalance 
	 */
	public void withdrawByAdmin(UserWealth uw)throws InsufficientBalance;
	/**
	 * 管理员提现 资金池财务确认
	 * @throws InsufficientBalance  
	 * **/
	public void passWithdrawCashConfirming(WithdrawCash withdrawCash);

}

