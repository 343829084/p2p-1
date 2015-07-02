package com.esoft.jdp2p.message.service.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;




import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;

import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
import com.esoft.jdp2p.message.service.FwSmsTemplate;


@Service
public class FwSmsTemplateImpl implements FwSmsTemplate{

@Resource
private HibernateTemplate ht;

@Resource
private MessageFw messageFw;

	@Override
	public void sendRechargeSuccess(String mobileNumber, Double money) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("money", String.valueOf(money));
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		messageFw.sendFWSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.HAOYOUBI_RECHARGE_SUCCESS
						+ "_sms"), params, mobileNumber);
		//System.out.println("好友来投的用户，您好！您的好友币已充值成功，目前账户可用投资金额：#{money}元。客服电话：4008-716-567");
		
	}
	
	@Override
	public void sendWithdrawSuccess(String mobileNumber,Double money) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("money", String.valueOf(money));
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		messageFw.sendFWSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.WITHDRAW_SUCCESS
						+ "_sms"), params, mobileNumber);
		//System.out.println("您的账户提现操作已成功，金额：#{money}元，已转到您绑定的银行账户。如非本人操作，请致电客服：4008-716-567");
		
	}
	
	
	@Override
	public void sendInvestSuccess(String mobileNumber,Double investMoney,Double ratePercent,Double income) {
		Map<String, String> params = new HashMap<String, String>();
		//获取投资金额
		params.put("investMoney",String.valueOf(investMoney));
		//获取年息
		params.put("ratePercent", String.valueOf(ratePercent));
		//预期收益
		params.put("income",String.valueOf(income));
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		messageFw.sendFWSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.INVEST_SUCCESS
						+ "_sms"), params, mobileNumber);
		
	}

	@Override
	public void sendInvestorFullRemind(String name,String mobileNumber) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name",name);
		
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		messageFw.sendFWSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.INVEST_FULL_REMIND
						+ "_sms"), params, mobileNumber);
		//System.out.println("因您投资的#{name}标的未能在招标期限内满标，故此标的进入流标状态，解除冻结金额：XXXX.XX元，感谢您对本标的的支持。好友来投");
	}

	@Override
	public void sendInvestorReceiveRemind(String name,String mobileNumber) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("name",name);
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		messageFw.sendFWSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.INVEST_REPAY_REMIND
						+ "_sms"), params, mobileNumber);
		
	}

	@Override
	public void sendInvestorUnderlyDelyRemind(String name,String mobileNumber) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name",name);
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		messageFw.sendFWSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.INVEST_DELAY_REMIND
						+ "_sms"), params, mobileNumber);
		
	}

	@Override
	public void sendInvestorFlowRemind(String name, String mobileNumber,Double money) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("money",String.valueOf(money));
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		messageFw.sendFWSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.INVEST_FLOW_REMIND
						+ "_sms"), params, mobileNumber);
		
	}

	@Override
	public void sendBorrowerReimburseRemind(String name,String mobileNumber) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		messageFw.sendFWSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.BORROWER_REPAY_REMIND
						+ "_sms"), params, mobileNumber);
	}

	@Override
	public void sendBorrowerReceiveRemind(String name,String mobileNumber) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		messageFw.sendFWSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.BORROWER_RECEIVE_REMIND
						+ "_sms"), params, mobileNumber);
	}




	

	


	


	


	

	

	
}
