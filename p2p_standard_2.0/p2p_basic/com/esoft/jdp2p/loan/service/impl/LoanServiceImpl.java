package com.esoft.jdp2p.loan.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.hibernate.collection.AbstractPersistentCollection;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdScheduler;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.banner.model.BannerPicture;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.config.ConfigConstants;
import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.InvestConstants.InvestStatus;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.LoanConstants.LoanStatus;
import com.esoft.jdp2p.loan.exception.BorrowedMoneyTooLittle;
import com.esoft.jdp2p.loan.exception.ExistWaitAffirmInvests;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.exception.InvalidExpectTimeException;
import com.esoft.jdp2p.loan.model.ApplyEnterpriseLoan;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanCalculator;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.jdp2p.repay.model.LoanRepay;
import com.esoft.jdp2p.repay.service.RepayService;
import com.esoft.jdp2p.risk.service.SystemBillService;
import com.esoft.jdp2p.schedule.ScheduleConstants;
import com.esoft.jdp2p.schedule.job.AutoInvestAfterLoanPassed;
import com.esoft.jdp2p.schedule.job.CheckLoanOverExpectTime;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-14 下午7:45:48
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-14 wangzhi 1.0
 */
@Service("loanService")
public class LoanServiceImpl implements LoanService {

	@Logger
	private static Log log;

	@Resource
	LoanBO loanBO;

	@Resource
	RepayService repayService;

	@Resource
	UserBillBO ubs;

	@Resource
	SystemBillService sbs;

	@Resource
	HibernateTemplate ht;

	@Resource
	StdScheduler scheduler;

	@Resource
	ConfigService configService;

	@Resource
	LoanCalculator loanCalculator;

	/**
	 * 申请借款
	 * 
	 * @param loan
	 * @throws InsufficientBalance
	 *             余额不足以支付保证金
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void applyLoan(Loan loan) throws InsufficientBalance {
		// 借款保证金费率
		double cashDepositMoney = loanCalculator.calculateCashDeposit(loan
				.getLoanMoney());
		// 如果保证金不够，需要先进行充值
		if (cashDepositMoney > ubs.getBalance(loan.getUser().getId())) {
			throw new InsufficientBalance("用户余额不足以支付保证金。");
		}
		loan.setDeposit(cashDepositMoney);

		loan.setCommitTime(new Date());
		// 设置借款状态
		loan.setStatus(LoanConstants.LoanStatus.WAITING_VERIFY);
		loan.setId(loanBO.generateId());
		ht.save(loan);

		// 冻结保证金
		ubs.freezeMoney(loan.getUser().getId(), cashDepositMoney,
				OperatorInfo.APPLY_LOAN, "借款ID:" + loan.getId() + "申请，冻结保证金");
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void fail(String loanId, String operatorId)
			throws ExistWaitAffirmInvests {
		// 流标
		Loan loan = ht.get(Loan.class, loanId);
		ht.evict(loan);
		loan = ht.get(Loan.class, loanId, LockMode.UPGRADE);
		loan.setCancelTime(new Date());
		loan.setStatus(LoanConstants.LoanStatus.CANCEL);
		List<Invest> invests = loan.getInvests();

		try {
			for (Invest investment : invests) {
				if (investment.getStatus().equals(
						InvestConstants.InvestStatus.WAIT_AFFIRM)) {
					throw new ExistWaitAffirmInvests("investID:"
							+ investment.getId());
				}
				if (investment.getStatus().equals(
						InvestConstants.InvestStatus.BID_SUCCESS)) {
					// FIXME：investMoney-代金券金额，优惠券变为可用
					ubs.unfreezeMoney(investment.getUser().getId(),
							investment.getMoney(), OperatorInfo.CANCEL_LOAN,
							"借款" + loan.getId() + "流标，解冻投资金额");
				}
				// 更改投资状态
				investment.setStatus(InvestConstants.InvestStatus.CANCEL);
				ht.update(investment);
			}

			if (loan.getStatus().equals(LoanStatus.RAISING)
					|| loan.getStatus().equals(LoanStatus.RECHECK)) {
				ubs.unfreezeMoney(loan.getUser().getId(), loan.getDeposit(),
						OperatorInfo.CANCEL_LOAN, "借款" + loan.getId()
								+ "流标，解冻保证金");
			}

			ht.merge(loan);
		} catch (InsufficientBalance ib) {
			throw new RuntimeException(ib);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void delayExpectTime(String loanId, Date newExpectTime)
			throws InvalidExpectTimeException {
		// 预计执行时间是否在当前时间之前，如果是，抛异常
		if (!newExpectTime.after(new Date())) {
			throw new InvalidExpectTimeException();
		} else {
			Loan loan = ht.get(Loan.class, loanId);
			ht.evict(loan);
			loan = ht.get(Loan.class, loanId, LockMode.UPGRADE);
			// FIXME:loan不存在
			// 添加项目到期调度任务
			loan.setExpectTime(newExpectTime);
			loan.setStatus(LoanConstants.LoanStatus.RAISING);
			ht.merge(loan);
			try {
				SimpleTrigger trigger = (SimpleTrigger) scheduler
						.getTrigger(TriggerKey
								.triggerKey(
										loanId,
										ScheduleConstants.TriggerGroup.CHECK_LOAN_OVER_EXPECT_TIME));
				if (trigger != null) {
					// 修改时间
					Trigger newTrigger = trigger
							.getTriggerBuilder()
							.withSchedule(
									SimpleScheduleBuilder.simpleSchedule())
							.startAt(newExpectTime).build();
					// 重启触发器
					scheduler.rescheduleJob(trigger.getKey(), newTrigger);
				} else {
					JobDetail jobDetail = JobBuilder
							.newJob(CheckLoanOverExpectTime.class)
							.withIdentity(
									loanId,
									ScheduleConstants.JobGroup.CHECK_LOAN_OVER_EXPECT_TIME)
							.build();// 任务名，任务组，任务执行类
					jobDetail.getJobDataMap().put(
							CheckLoanOverExpectTime.LOAN_ID, loanId);
					trigger = TriggerBuilder
							.newTrigger()
							.forJob(jobDetail)
							.startAt(newExpectTime)
							.withSchedule(
									SimpleScheduleBuilder.simpleSchedule())
							.withIdentity(
									loanId,
									ScheduleConstants.TriggerGroup.CHECK_LOAN_OVER_EXPECT_TIME)
							.build();
					scheduler.scheduleJob(jobDetail, trigger);
				}
			} catch (SchedulerException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void giveMoneyToBorrower(String loanId)
			throws ExistWaitAffirmInvests, BorrowedMoneyTooLittle {
		Loan loan = ht.get(Loan.class, loanId);
		ht.evict(loan);
		loan = ht.get(Loan.class, loanId, LockMode.UPGRADE);
		// FIXME:loan不存在
		// 有两种放款，一种是项目募集完成了，放款；一种是项目未募集满额，得根据项目的实际募集金额，修改项目借款金额，然后进行放款。

		// 更改项目状态，放款。
		loan.setStatus(LoanConstants.LoanStatus.REPAYING);
		// 获取当前日期
		Date dateNow = new Date();
		// 设置放款日期
		loan.setGiveMoneyTime(dateNow);
		if (loan.getInterestBeginTime() == null) {
			loan.setInterestBeginTime(dateNow);
		}

		// 实际到借款账户的金额
		double actualMoney = 0D;

		List<Invest> invests = loan.getInvests();

		for (Invest invest : invests) {
			if (invest.getStatus().equals(
					InvestConstants.InvestStatus.WAIT_AFFIRM)) {
				// 放款时候，需要检查是否要等待确认的投资，如果有，则不让放款。
				throw new ExistWaitAffirmInvests("investID:" + invest.getId());
			}
			if (invest.getStatus().equals(
					InvestConstants.InvestStatus.BID_SUCCESS)) {
				// 放款时候，需要只更改BID_SUCCESS 的借款。
				try {
					// investMoney-代金券金额
					if (invest.getUserCoupon() != null) {
						double realMoney = ArithUtil.sub(invest.getMoney(),
								invest.getUserCoupon().getCoupon().getMoney());
						if (realMoney < 0) {
							realMoney = 0;
						}
						ubs.transferOutFromFrozen(invest.getUser().getId(),
								realMoney, OperatorInfo.GIVE_MONEY_TO_BORROWER,
								"投资成功，取出投资金额, 借款ID：" + loan.getId());
					} else {
						ubs.transferOutFromFrozen(invest.getUser().getId(),
								invest.getMoney(),
								OperatorInfo.GIVE_MONEY_TO_BORROWER,
								"投资成功，取出投资金额, 借款ID：" + loan.getId());
					}
					actualMoney = ArithUtil.add(actualMoney,
							invest.getInvestMoney());
				} catch (InsufficientBalance e) {
					throw new RuntimeException(e);
				}
				// 更改投资状态
				invest.setStatus(InvestConstants.InvestStatus.REPAYING);
				ht.update(invest);
			}
		}
		// 设置借款实际借到的金额
		loan.setMoney(actualMoney);
		// 根据借款期限产生还款信息
		repayService.generateRepay(loan.getId());

		// 借款手续费-借款保证金
		double subR = ArithUtil.sub(loan.getLoanGuranteeFee(),
				loan.getDeposit());

		double tooLittle = ArithUtil.sub(actualMoney, subR);
		// 借到的钱，可能不足以支付借款手续费
		if (tooLittle <= 0) {
			throw new BorrowedMoneyTooLittle("actualMoney：" + tooLittle);
		}
		// 把借款转给借款人账户
		ubs.transferIntoBalance(loan.getUser().getId(), actualMoney,
				OperatorInfo.GIVE_MONEY_TO_BORROWER,
				"借款到账, 借款ID：" + loan.getId());
		try {
			ubs.unfreezeMoney(loan.getUser().getId(), loan.getDeposit(),
					OperatorInfo.GIVE_MONEY_TO_BORROWER, "借款成功，解冻借款保证金, 借款ID："
							+ loan.getId());
			ubs.transferOutFromBalance(loan.getUser().getId(),
					loan.getLoanGuranteeFee(),
					OperatorInfo.GIVE_MONEY_TO_BORROWER, "取出借款管理费, 借款ID："
							+ loan.getId());
			sbs.transferInto(loan.getLoanGuranteeFee(),
					OperatorInfo.GIVE_MONEY_TO_BORROWER,
					"借款管理费, 借款ID：" + loan.getId());
		} catch (InsufficientBalance e) {
			throw new RuntimeException(e);
		}
		ht.merge(loan);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void passApply(Loan loan) throws InvalidExpectTimeException {
		// FIXME:验证
		// 预计执行时间是否在当前时间之前，如果是，抛异常
		if (!loan.getExpectTime().after(new Date())) {
			throw new InvalidExpectTimeException();
		}
		setPics(loan);
		addDealLoanStatusJob(loan);
		// 审核通过
		loan.setVerified(LoanConstants.LoanVerifyStatus.PASSED);
		loan.setStatus(LoanConstants.LoanStatus.RAISING);
		loan.setVerifyTime(new Date());
		ht.merge(loan);
		if (log.isDebugEnabled())
			log.debug(MessageFormat.format("借款[编号：{0},名称：{1}]审核通过!审核人：{2}",
					loan.getId(), loan.getName(), "//FIXME:"));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void refuseApply(String loanId, String refuseInfo,
			String verifyUserId) {
		Loan loan = ht.get(Loan.class, loanId);
		ht.evict(loan);
		loan = ht.get(Loan.class, loanId, LockMode.UPGRADE);
		User verifyUser = ht.get(User.class, verifyUserId);
		// FIXME:loan不存在，用户不存在的验证
		// 审核未通过
		loan.setVerified(LoanConstants.LoanVerifyStatus.FAILED);
		loan.setStatus(LoanConstants.LoanStatus.VERIFY_FAIL);
		loan.setExpectTime(null);
		// 审核人
		loan.setVerifyUser(verifyUser);
		loan.setVerifyTime(new Date());
		// 借款保证金费率
		ht.merge(loan);
		double cashDepositMoney = loan.getDeposit();
		try {
			ubs.unfreezeMoney(loan.getUser().getId(), cashDepositMoney,
					OperatorInfo.REFUSE_APPLY_LOAN, "借款审核未通过，解冻保证金。借款ID:"
							+ loan.getId());
		} catch (InsufficientBalance e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isCompleted(String loanId) {
		List<LoanRepay> repays = ht.find(
				"from LoanRepay repay where repay.loan.id=?", loanId);
		for (LoanRepay repay : repays) {
			// 如果有一笔还款状态不是“完成”，则返回false
			if (!repay.getStatus().equals(LoanConstants.RepayStatus.COMPLETE)) {
				return false;
			}
		}
		return true;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void dealComplete(String loanId) {
		// 所有还款都完成了，则借款状态为“完成”
		if (isCompleted(loanId)) {
			Loan loan = ht.get(Loan.class, loanId);
			ht.evict(loan);
			loan = ht.get(Loan.class, loanId, LockMode.UPGRADE);
			loan.setCompleteTime(new Date());
			loan.setStatus(LoanConstants.LoanStatus.COMPLETE);
			ht.merge(loan);
			List<Invest> is = ht
					.find("from Invest invest where invest.loan.id=? and invest.status in (?,?,?)",
							new String[] { loanId, InvestStatus.REPAYING,
									InvestStatus.OVERDUE, InvestStatus.BAD_DEBT });
			for (Invest invest : is) {
				invest.setStatus(InvestConstants.InvestStatus.COMPLETE);
				ht.update(invest);
			}
		}
	}

	@Override
	public boolean isRaiseCompleted(String loanId)
			throws NoMatchingObjectsException {
		if (loanCalculator.calculateMoneyNeedRaised(loanId) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public void dealRaiseComplete(String loanId)
			throws NoMatchingObjectsException {
		if (loanCalculator.calculateMoneyNeedRaised(loanId) == 0) {
			// 项目募集完成
			Loan loan = ht.get(Loan.class, loanId);
			loan.setStatus(LoanConstants.LoanStatus.RECHECK);
			ht.update(loan);
		}
	}

	@Override
	public boolean isOverExpectTime(String loanId) {
		Loan loan = ht.get(Loan.class, loanId);
		// FIXME:loan为空验证
		if (new Date().before(loan.getExpectTime())) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void dealOverExpectTime(String loanId) {
		// FIXME loan需要验证
		Loan loan = ht.get(Loan.class, loanId);
		ht.evict(loan);
		loan = ht.get(Loan.class, loanId, LockMode.UPGRADE);
		// 只有筹款中的借款，才能通过调度改成等待复核
		if (isOverExpectTime(loanId)
				&& LoanConstants.LoanStatus.RAISING.equals(loan.getStatus())) {
			loan.setStatus(LoanConstants.LoanStatus.RECHECK);
			try {
				ht.merge(loan);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void createLoanByAdmin(Loan loan) throws InsufficientBalance,
			InvalidExpectTimeException {
		// FIXME:开始计息时间，必须在（当前时间往前一个还款阶段）之后。
		// 预计执行时间是否在当前时间之前，如果是，抛异常
		if (!loan.getExpectTime().after(new Date())) {
			throw new InvalidExpectTimeException();
		}

		loan.setCommitTime(new Date());
		loan.setMoney(0D);
		// 设置借款状态
		loan.setStatus(LoanConstants.LoanStatus.WAITING_VERIFY);
		loan.setId(loanBO.generateId());
		setPics(loan);
		ht.save(loan);
		// 冻结保证金
		if (loan.getDeposit() != null && loan.getDeposit() > 0) {
			ubs.freezeMoney(loan.getUser().getId(), loan.getDeposit(),
					OperatorInfo.APPLY_LOAN, "发起借款，冻结保证金，借款ID:" + loan.getId());
		}

		if (log.isDebugEnabled())
			log.debug("添加项目成功，编号[" + loan.getId() + "],名称：[" + loan.getName()
					+ "]");

	}

	private void addDealLoanStatusJob(Loan loan) {
		// 调度，到期自动改项目状态
		JobDetail jobDetail = JobBuilder
				.newJob(CheckLoanOverExpectTime.class)
				.withIdentity(loan.getId(),
						ScheduleConstants.JobGroup.CHECK_LOAN_OVER_EXPECT_TIME)
				.build();// 任务名，任务组，任务执行类
		jobDetail.getJobDataMap().put(CheckLoanOverExpectTime.LOAN_ID,
				loan.getId());
		SimpleTrigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(
						loan.getId(),
						ScheduleConstants.TriggerGroup.CHECK_LOAN_OVER_EXPECT_TIME)
				.forJob(jobDetail)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule())
				.startAt(loan.getExpectTime()).build();
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}

		if (log.isDebugEnabled())
			log.debug("添加[到期自动修改项目状态]调度成功，项目编号[" + loan.getId() + "]");
	}

	/**
	 * 赋值项目资料和抵押相关物资的图片
	 * 
	 * @param loan
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	private void setPics(Loan loan) {
		List<BannerPicture> lips = loan.getLoanInfoPics();
		List<BannerPicture> gips = loan.getGuaranteeInfoPics();
		if (lips != null && !(lips instanceof AbstractPersistentCollection)) {
			for (BannerPicture lip : lips) {
				ht.saveOrUpdate(lip);
			}
		}
		if (gips != null && !(gips instanceof AbstractPersistentCollection)) {
			for (BannerPicture gip : gips) {
				ht.saveOrUpdate(gip);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void applyEnterpriseLoan(ApplyEnterpriseLoan ael) {
		ael.setId(IdGenerator.randomUUID());
		ael.setStatus(LoanConstants.ApplyEnterpriseLoanStatus.WAITING_VERIFY);
		ael.setApplyTime(new Date());
		ht.save(ael);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void verifyEnterpriseLoan(ApplyEnterpriseLoan ael) {
		ael.setStatus(LoanConstants.ApplyEnterpriseLoanStatus.VERIFIED);
		ht.update(ael);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void update(Loan loan) {
		// 只能更改不影响流程的字段
		setPics(loan);
		ht.update(loan);

		if (log.isDebugEnabled())
			log.debug("修改项目成功，编号[" + loan.getId() + "]");
	}

	@Override
	public List<Invest> getSuccessfulInvests(String loanId) {
		return ht
				.find("select im from Invest im where im.loan.id=? and im.status in (?,?,?,?,?)",
						new String[] { loanId,
								InvestConstants.InvestStatus.BID_SUCCESS,
								InvestConstants.InvestStatus.OVERDUE,
								InvestConstants.InvestStatus.COMPLETE,
								InvestConstants.InvestStatus.BAD_DEBT,
								InvestConstants.InvestStatus.REPAYING });
	}

}
