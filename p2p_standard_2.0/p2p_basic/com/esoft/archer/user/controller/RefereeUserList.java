package com.esoft.archer.user.controller;

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;

/**
 * 推荐人相关用户模型控制器
 * @author hch 
 */
@Component
@Scope(ScopeType.REQUEST)
public class RefereeUserList extends EntityQuery<User> {
	private static final String lazyModelCount = "select count(user.id) from User user,MotionTracking mt";
	private static final String lazyModel = "select user from User user,MotionTracking mt";

	private String referee;
	
	public RefereeUserList() {
		setCountHql(lazyModelCount);
		setHql(lazyModel);

		final String[] RESTRICTIONS = { 
				"mt.fromWhere=#{refereeUserList.referee}",
				"user.id=mt.who"
		};
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
	}
}
