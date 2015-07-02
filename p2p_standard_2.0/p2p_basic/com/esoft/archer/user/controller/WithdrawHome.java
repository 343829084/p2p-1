package com.esoft.archer.user.controller;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserBillService;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.HashCrypt;
import com.esoft.core.util.StringManager;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.WithdrawCash;
import com.esoft.jdp2p.user.service.WithdrawCashService;

@Component
@Scope(ScopeType.VIEW)
public class WithdrawHome extends EntityHome<WithdrawCash> {

	@Logger
	static Log log;
	private static StringManager userSM = StringManager
			.getManager(UserConstants.Package);
	@Resource
	WithdrawCashService wcs;

	@Resource
	private UserService userService;
	
	@Resource
	LoginUserInfo loginUserInfo;
	
	@Resource
	UserBillService userBillService;

	/** 交易密码 */
	private String cashPassword;

	public WithdrawHome() {
		setUpdateView(FacesUtil.redirect("/admin/withdraw/withdrawList"));
	}

	@Override
	protected WithdrawCash createInstance() {
		
		WithdrawCash withdraw = new WithdrawCash();
		withdraw.setAccount("借款账户");
		withdraw.setFee(0D);
		withdraw.setCashFine(0D);
		withdraw.setUser(new User(loginUserInfo.getLoginUserId()));
		return withdraw;
	}

	/**
	 * 计算手续费和罚金
	 */
	public boolean calculateFee() {
		double fee = wcs.calculateFee(this.getInstance().getMoney());
		if (userBillService.getBalance(loginUserInfo.getLoginUserId())<fee+this.getInstance().getMoney()) {
			FacesUtil.addErrorMessage("余额不足！");
			FacesUtil.getCurrentInstance().validationFailed();
			this.getInstance().setMoney(0D);
			return false;
		} else {
			this.getInstance().setFee(
				wcs.calculateFee(this.getInstance().getMoney()));
			return true;
		}
	}

	/**
	 * 提现
	 */
	public String withdraw() {
		try {
//			User user = getBaseService().get(User.class,
//					this.getInstance().getUser().getId());
//			if (HashCrypt.getDigestHash(cashPassword).equals(
//					user.getCashPassword())) {
//				FacesUtil.addErrorMessage("交易密码错误！");
//				return null;
//			}
			
			

			wcs.applyWithdrawCash(this.getInstance());
			FacesUtil.addInfoMessage("您的提现申请已经提交成功，请等待审核！");
			return "pretty:myCashFlow";
		} catch (InsufficientBalance e) {
			FacesUtil.addErrorMessage("余额不足！");
			return null;
		}
	}

	/**
	 * 提现审核初审通过
	 */
	public String verifyPass() {
		getInstance().setVerifyUser(new User(loginUserInfo.getLoginUserId()));
		wcs.passWithdrawCashApply(this.getInstance());
		FacesUtil.addInfoMessage("审核通过，请等待系统复核");
		return getUpdateView();
	}

	/**
	 * 提现审核复核通过
	 */
	public String recheckPass() {
		getInstance().setRecheckUser(new User(loginUserInfo.getLoginUserId()));
		wcs.passWithdrawCashRecheck(this.getInstance());
		FacesUtil.addInfoMessage("复核通过，用户账户资金会自动解冻并扣除");
		return getUpdateView();
	}

	/**
	 * 提现审核初审不通过
	 */
	public String verifyFail() {
		getInstance().setVerifyUser(new User(loginUserInfo.getLoginUserId()));
		wcs.refuseWithdrawCashApply(this.getInstance());
		FacesUtil.addInfoMessage("初审未通过，用户账户的资金会自动解冻");
		return getUpdateView();
	}

	/**
	 * 提现审核复核不通过
	 */
	public String recheckFail() {
		getInstance().setVerifyUser(new User(loginUserInfo.getLoginUserId()));
		wcs.refuseWithdrawCashApply(this.getInstance());
		FacesUtil.addInfoMessage("复核未通过，用户账户的资金会自动解冻");
		return getUpdateView();
	}

	public String getCashPassword() {
		return cashPassword;
	}

	public void setCashPassword(String cashPassword) {
		this.cashPassword = cashPassword;
	}

	
	public Boolean checkInvest(){
		
		String userId=loginUserInfo.getLoginUserId();
		Boolean isRight=userService.hasRole(userId, "INVEST");
		if(!isRight){
			return false;
		}
		return true;
	}
}
