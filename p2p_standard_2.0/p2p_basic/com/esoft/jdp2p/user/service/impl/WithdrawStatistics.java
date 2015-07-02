package com.esoft.jdp2p.user.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.user.UserConstants.RechargeStatus;
import com.esoft.archer.user.UserConstants.WithdrawStatus;

@Service
public class WithdrawStatistics {

	@Resource
	HibernateTemplate ht;

	/**
	 * 累计提现
	 */
	public double getSuccessWithdrawMoney(String userId) {
		String hql = "select sum(withdraw.money) from WithdrawCash withdraw "
				+ "where withdraw.status =? and withdraw.user.id=?";
		List<Object> oos = ht.find(hql, new String[] { WithdrawStatus.SUCCESS,
				userId });
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Double) o;
	}

	/**
	 * ips环讯累计提现
	 */
	public double getSuccessIpsWithdrawMoney(String userId) {
		String hql = "select sum(withdraw.money) from WithdrawCash withdraw "
				+ "where withdraw.status =? and withdraw.user.id=? and withdraw.type='ips'";
		List<Object> oos = ht.find(hql, new String[] { WithdrawStatus.SUCCESS,
				userId });
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Double) o;
	}
	/**
	 * cap资金池累计提现
	 */
	public double getSuccessCapWithdrawMoney(String userId) {
		String hql = "select sum(withdraw.money) from WithdrawCash withdraw "
				+ "where withdraw.status =? and withdraw.user.id=? and withdraw.type='cap'";
		List<Object> oos = ht.find(hql, new String[] { WithdrawStatus.SUCCESS,
				userId });
		Object o = oos.get(0);
		if (o == null) {
			return 0;
		}
		return (Double) o;
	}
}
