package com.esoft.jdp2p.message.service;

import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.model.WithdrawCash;
import com.esoft.jdp2p.message.model.UserMessageTemplate;

public interface FwSmsTemplate {
	/*
	 * 发送"好友币"充值成功的消息
	 */
	public abstract void sendRechargeSuccess(String mobileNumber,Double money);

	/*
	 * 发送提现成功的消息
	 */
	public abstract void sendWithdrawSuccess(String mobileNumber,Double money);
	
	/*
	 * 发送投资成功的消息
	 * 
	 */
	public abstract void sendInvestSuccess(String mobileNumber,Double investMoney,Double ratePercent,Double income);
	
	/*
	 * 给投资人发送满标的消息提醒
	 */
	public abstract void sendInvestorFullRemind(String name, String mobileNumber);
	
	/*
	 * 给投资人发送回款的消息提醒
	 */
	public abstract void sendInvestorReceiveRemind(String name, String mobileNumber);
	
	/*
	 * 给投资人发送标的延期消息提醒
	 */
	public abstract void sendInvestorUnderlyDelyRemind(String name, String mobileNumber);
	
	/*
	 * 给投资人发送流标的消息提醒
	 */
	public abstract void sendInvestorFlowRemind(String name, String mobileNumber,Double money);
	
	/*
	 * 给借款人发送还款的消息提醒
	 */
	public abstract void sendBorrowerReimburseRemind(String name, String mobileNumber);
	
	/*
	 * 给借款人发送收款的消息提醒
	 */
	public abstract void sendBorrowerReceiveRemind(String name, String mobileNumber);

}
