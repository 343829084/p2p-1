package com.esoft.jdp2p.withdraw.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.UserConstants.WithdrawStatus;
import com.esoft.archer.user.model.UserBill;
import com.esoft.archer.user.model.UserWealth;
import com.esoft.archer.user.service.impl.UserWealthBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.WithdrawCash;
import com.esoft.jdp2p.message.service.FwSmsService;
import com.esoft.jdp2p.message.service.FwSmsTemplate;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeePoint;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeeType;
import com.esoft.jdp2p.risk.service.SystemBillService;
import com.esoft.jdp2p.risk.service.impl.FeeConfigBO;
import com.esoft.jdp2p.user.service.WithdrawCashService;
import com.esoft.jdp2p.withdraw.service.CapWithdrawCashService;
@Service(value = "capitalPoolwithdrawCashService")
public class CapitalPoolWitdrawCashServiceImpl implements CapWithdrawCashService {
	@Logger
	private static Log log;

	@Resource
	HibernateTemplate ht;

	@Resource
	private FeeConfigBO feeConfigBO;

	@Resource
	UserWealthBO userWealthBO;

	@Resource
	SystemBillService sbs;
	@Resource
	FwSmsTemplate fwSmsTemplate;
	@Override
	public double calculateFee(double amount) {
		return feeConfigBO.getFee(FeePoint.WITHDRAW, FeeType.FACTORAGE, null,
				null, amount);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void passWithdrawCashApply(WithdrawCash withdrawCash) {
		// 更新提现审核状态，到等待复核状态
		WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId());
		ht.evict(wdc);
		wdc = ht.get(WithdrawCash.class, wdc.getId(), LockMode.UPGRADE);
		wdc.setVerifyTime(new Date());
		wdc.setStatus(UserConstants.WithdrawStatus.RECHECK);
		wdc.setVerifyMessage(withdrawCash.getVerifyMessage());
		wdc.setVerifyUser(withdrawCash.getVerifyUser());
		ht.merge(wdc);

		if (log.isInfoEnabled())
			log.info("好友钱提现审核初审通过，提现编号：" + wdc.getId() + "，审核人："
					+ withdrawCash.getVerifyUser().getId() + "，审核时间:"
					+ wdc.getVerifyTime());
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void passWithdrawCashRecheck(WithdrawCash withdrawCash) {
		WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId());
		ht.evict(wdc);
		wdc = ht.get(WithdrawCash.class, wdc.getId(), LockMode.UPGRADE);
		wdc.setVerifyTime(new Date());
		wdc.setStatus(UserConstants.WithdrawStatus.CONFIRM);
		wdc.setVerifyMessage(withdrawCash.getVerifyMessage());
		wdc.setVerifyUser(withdrawCash.getVerifyUser());
		ht.merge(wdc);

		if (log.isInfoEnabled())
			log.info("好友钱提现审核复审通过，提现编号：" + wdc.getId() + "，审核人："
					+ withdrawCash.getVerifyUser().getId() + "，审核时间:"
					+ wdc.getVerifyTime());
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void refuseWithdrawCashApply(WithdrawCash withdrawCash) {
		// 解冻申请时候冻结的金额
		WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId());
		ht.evict(wdc);
		wdc = ht.get(WithdrawCash.class, wdc.getId(), LockMode.UPGRADE);
		wdc.setStatus(UserConstants.WithdrawStatus.VERIFY_FAIL);
		wdc.setVerifyMessage(withdrawCash.getVerifyMessage());
		wdc.setVerifyUser(withdrawCash.getVerifyUser());
		ht.merge(wdc);
		try {
			userWealthBO.unfreezeMoney(wdc.getUser().getId(), wdc.getMoney(),
					OperatorInfo.REFUSE_APPLY_WITHDRAW, "好友钱提现申请被拒绝，解冻提现金额, 提现ID:"
							+ withdrawCash.getId());
			userWealthBO.unfreezeMoney(wdc.getUser().getId(), wdc.getFee(),
					OperatorInfo.REFUSE_APPLY_WITHDRAW, "好友钱提现申请被拒绝，解冻手续费, 提现ID:"
							+ withdrawCash.getId());
		} catch (InsufficientBalance e) {
			throw new RuntimeException(e);
		}
		if (log.isInfoEnabled())
			log.info("好友钱提现审核未通过，提现编号：" + wdc.getId());

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void applyWithdrawCash(WithdrawCash withdraw)
			throws InsufficientBalance {
		// FIXME:缺验证
		withdraw.setFee(calculateFee(withdraw.getMoney()));
		withdraw.setCashFine(0D);

		withdraw.setId(generateId());
		withdraw.setTime(new Date());
		userWealthBO.freezeMoney(withdraw.getUser().getId(), withdraw.getMoney(),
				OperatorInfo.APPLY_WITHDRAW,
				"好友钱申请提现，冻结提现金额, 提现编号:" + withdraw.getId());
		userWealthBO.freezeMoney(withdraw.getUser().getId(), withdraw.getFee(),
				OperatorInfo.APPLY_WITHDRAW,
				"好友钱申请提现，冻结提现手续费, 提现编号:" + withdraw.getId());
		// 等待审核
		withdraw.setStatus(UserConstants.WithdrawStatus.WAIT_VERIFY);
		ht.save(withdraw);
	}

	/**
	 * 生成id
	 * 
	 * @return
	 */
	private String generateId() {
		String gid = DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD);
		String hql = "select withdraw from WithdrawCash withdraw where withdraw.id = (select max(withdrawM.id) from WithdrawCash withdrawM where withdrawM.id like ?)";
		List<WithdrawCash> contractList = ht.find(hql, gid + "%");
		Integer itemp = 0;
		if (contractList.size() == 1) {
			WithdrawCash withdrawCash = contractList.get(0);
			ht.lock(withdrawCash, LockMode.UPGRADE);
			String temp = withdrawCash.getId();
			temp = temp.substring(temp.length() - 6);
			itemp = Integer.valueOf(temp);
		}
		itemp++;
		gid += String.format("%08d", itemp);
		return gid;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)

	public void withdrawByAdmin(UserWealth uw)throws InsufficientBalance{
		WithdrawCash wc = new WithdrawCash();
		wc.setCashFine(0D);
		wc.setFee(0D);
		wc.setId(generateId());
		wc.setIsWithdrawByAdmin(true);
		wc.setMoney(uw.getMoney());
		wc.setStatus(WithdrawStatus.SUCCESS);
		wc.setTime(new Date());
		wc.setUser(uw.getUser());
		ht.save(wc);
		userWealthBO.transferOutFromBalance(uw.getUser().getId(), uw.getMoney(),
				OperatorInfo.ADMIN_OPERATION, uw.getDetail());
	}
	/*财务确认*/
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void passWithdrawCashConfirming(WithdrawCash withdrawCash) {
		WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId());
		ht.evict(wdc);
		wdc = ht.get(WithdrawCash.class, wdc.getId(), LockMode.UPGRADE);
		wdc.setRecheckTime(new Date());
		wdc.setStatus(UserConstants.WithdrawStatus.SUCCESS);
		wdc.setVerifyMessage(withdrawCash.getRecheckMessage());
		wdc.setVerifyUser(withdrawCash.getRecheckUser());
		ht.merge(wdc);
		try {
			userWealthBO.transferOutFromFrozen(wdc.getUser().getId(),
					wdc.getMoney(), OperatorInfo.WITHDRAW_SUCCESS,
					"好友钱提现申请通过，取出冻结金额, 提现ID:" + withdrawCash.getId());
			userWealthBO.transferOutFromFrozen(wdc.getUser().getId(),
					wdc.getFee(), OperatorInfo.WITHDRAW_SUCCESS,
					"好友钱提现申请通过，取出冻结手续费, 提现ID:" + withdrawCash.getId());
			sbs.transferInto(wdc.getFee(), OperatorInfo.WITHDRAW_SUCCESS,
					"好友钱提现申请通过, 扣除手续费。提现ID:" + withdrawCash.getId());
			fwSmsTemplate.sendWithdrawSuccess(wdc.getUser().getMobileNumber(),wdc.getMoney());
			
		} catch (InsufficientBalance e) {
			throw new RuntimeException(e);
		}

		if (log.isInfoEnabled())
			log.info("好友钱提现已确认转款，提现编号：" + wdc.getId());
		
	}

}
