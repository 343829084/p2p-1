package com.esoft.jdp2p.bankcard.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.comment.controller.CommentHome;
import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.controller.UserHome;
import com.esoft.archer.user.model.Role;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.archer.user.service.impl.UserBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.bankcard.BankCardConstants;
import com.esoft.jdp2p.bankcard.BankCardConstants.BankCardStatus;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.user.service.RechargeService;
import com.sun.corba.se.impl.orbutil.closure.Constant;

@Component
@Scope(ScopeType.VIEW)
public class BankCardHome extends EntityHome<BankCard> implements java.io.Serializable{

	@Logger
	static Log log;
	@Resource
	HibernateTemplate ht;
	
	@Resource
	private UserBO userBO;
	
	@Resource
	private UserService userService;
	
	@Resource
	private UserHome userHome;
	
	@Resource
	private LoginUserInfo loginUserInfo;
	
	@Resource
	private RechargeService rechargeService ;
	
	@Resource
	private CommentHome commenHome;

	private String realNameString;
	private String idCardString;
	
	private String sexString;
	private Date birthdate;
	
	@Override
	@Transactional(readOnly = false)
	public String save() {
		User loginUser = getBaseService().get(User.class,
				loginUserInfo.getLoginUserId());
		if (loginUser == null) {
			FacesUtil.addErrorMessage("用户未登录");
			return null;
		}
		if (StringUtils.isEmpty(this.getInstance().getId())) {
			getInstance().setId(IdGenerator.randomUUID());
			getInstance().setUser(loginUser);
			getInstance().setStatus(BankCardConstants.BankCardStatus.BINDING);		
			getInstance().setBank(rechargeService.getBankNameByNo(getInstance().getBankNo()));
			
		}
		getInstance().setBankCardType(this.getInstance().getBankCardType());
		getInstance().setBankServiceType(this.getInstance().getBankServiceType());
		getInstance().setTime(new Date());
		super.save(false);
		this.setInstance(null);
		FacesUtil.addInfoMessage("保存银行卡成功！");

		return "pretty:bankCardList";
	}
	private final String WEALTHROLE="user_wealth_role";
	@Transactional(readOnly = false)
	public String saveByWealth() {
		User loginUser = getBaseService().get(User.class,
				loginUserInfo.getLoginUserId());
		if (loginUser == null) {
			FacesUtil.addErrorMessage("用户未登录");
			return null;
		}
		//if (!commenHome.hasCustomRole(loginUser, WEALTHROLE)) {
			loginUser.setRealname(userHome.getInstance().getRealname());
			loginUser.setSex(userHome.getInstance().getSex());
			loginUser.setIdCard(userHome.getInstance().getIdCard());
			loginUser.setBirthday(userHome.getInstance().getBirthday());
			
			
		//}
		userBO.addRole(loginUser, new Role(WEALTHROLE));
			this.save();
			
		FacesUtil.addInfoMessage("保存银行卡成功！");
		// 刷新登录用户权限
		userService.refreshAuthorities(loginUser.getId());
		return "pretty:bankCardList";
	}

	@Override
	@Transactional(readOnly = false)
	public String delete() {
		// 银行卡标记为删除状态
		this.getInstance().setStatus(BankCardStatus.DELETED);
		getBaseService().update(this.getInstance());
		return "pretty:bankCardList";
	}

	@Transactional(readOnly = false)
	public String delete(String bankCardId) {
		BankCard bc = getBaseService().get(BankCard.class, bankCardId);
		if (bc == null) {
			FacesUtil.addErrorMessage("未找到编号为" + bankCardId + "的银行卡！");
		} else {
			// 银行卡标记为删除状态
			this.setInstance(bc);
			this.getInstance().setStatus(BankCardStatus.DELETED);
			getBaseService().update(this.getInstance());
			this.setInstance(null);
		}
		return "pretty:bankCardList";
	}

	/**
	 * 删除银行卡，资金托管
	 * 
	 * @return
	 */
	public String deleteTrusteeship() {
		throw new RuntimeException("you must override this method!");
	}

	public String getRealNameString() {
		return realNameString;
	}

	public void setRealNameString(String realNameString) {
		this.realNameString = realNameString;
	}

	public String getIdCardString() {
		return idCardString;
	}

	public void setIdCardString(String idCardString) {
		this.idCardString = idCardString;
	}

	public String getSexString() {
		return sexString;
	}

	public void setSexString(String sexString) {
		this.sexString = sexString;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	
	
}
