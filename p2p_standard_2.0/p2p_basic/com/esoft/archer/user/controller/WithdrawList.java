package com.esoft.archer.user.controller;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.loan.model.WithdrawCash;

/**
 * 提现查询
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class WithdrawList extends EntityQuery<WithdrawCash> implements java.io.Serializable{
	
	private static final long serialVersionUID = 9057256750216810237L;
	
	@Logger static Log log ;
	
	public WithdrawList(){
		final String[] RESTRICTIONS = 
				{"id like #{withdrawList.example.id}",
				"user.username like #{withdrawList.example.user.username}",
				"status like #{withdrawList.example.status}"};
				
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		
	}
	
	@Override
	protected void initExample() {
		super.initExample();
		getExample().setUser(new User());
	}
	
	
}
