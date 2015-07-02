package com.esoft.archer.user.service.impl;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.service.UserBillService;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;

@Service(value = "userBillService")
public class UserBillServiceImpl implements UserBillService {

	@Resource
	private HibernateTemplate ht;

	@Resource
	private UserBO userBO;

	@Resource
	private UserBillBO userBillBO;

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void freezeMoney(String userId, double money, String operatorInfo,
			String operatorDetail) throws InsufficientBalance {
		// FIXME:验证用户不存在。其他方法同样需要验证。
		userBillBO.freezeMoney(userId, money, operatorInfo, operatorDetail);
	}

	@Override
	public double getBalance(String userId) {
		return userBillBO.getBalance(userId);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void transferOutFromFrozen(String userId, double money,
			String operatorInfo, String operatorDetail)
			throws InsufficientBalance {
		userBillBO.transferOutFromFrozen(userId, money, operatorInfo,
				operatorDetail);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void unfreezeMoney(String userId, double money, String operatorInfo,
			String operatorDetail) throws InsufficientBalance {
		userBillBO.unfreezeMoney(userId, money, operatorInfo, operatorDetail);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void transferOutFromBalance(String userId, double money,
			String operatorInfo, String operatorDetail)
			throws InsufficientBalance {
		userBillBO.transferOutFromBalance(userId, money, operatorInfo, operatorDetail);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void transferIntoBalance(String userId, double money,
			String operatorInfo, String operatorDetail) {
		userBillBO.transferIntoBalance(userId, money, operatorInfo, operatorDetail);
	}

	@Override
	public double getFrozenMoney(String userId) {
		return userBillBO.getFrozenMoney(userId);
	}
}
