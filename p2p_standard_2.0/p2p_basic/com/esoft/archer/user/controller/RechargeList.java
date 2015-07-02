package com.esoft.archer.user.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.RechargeBankCard;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.jdp2p.user.service.RechargeService;

/**
 * 充值查询
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class RechargeList extends EntityQuery<Recharge> implements
		java.io.Serializable {

	private static final long serialVersionUID = 9057256750216810237L;

	@Logger
	static Log log;

	@Resource
	private RechargeService rechargeService;

	private List<RechargeBankCard> rechargeBankCards;
	
	private Date startTime ;
	private Date endTime ;

	public RechargeList() {
		final String[] RESTRICTIONS = { "id like #{rechargeList.example.id}",
				"time >= #{rechargeList.startTime}",
				"time <= #{rechargeList.endTime}",
				"status = #{rechargeList.example.status}",
				"rechargeWay like #{rechargeList.example.rechargeWay}",
				"user.username like #{rechargeList.example.user.username}" };

		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
//		addOrder("time", super.DIR_DESC);
	}

	@Override
	protected void initExample() {
		super.initExample();
		getExample().setUser(new User());
	}

	public List<RechargeBankCard> getRechargeBankCards() {
		if (this.rechargeBankCards == null) {
			this.rechargeBankCards = rechargeService.getBankCardsList();
		}
		return this.rechargeBankCards;
	}

	//~~~~~~~~~~~~~~~~
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return endTime;
	}

}
