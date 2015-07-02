package com.esoft.jdp2p.invest.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;

/**
 * Filename: InvestList.java Description: Copyright: Copyright (c)2013 Company:
 * jdp2p
 * 
 * @author: yinjunlu
 * @version: 1.0 Create at: 2014-1-11 下午4:27:32
 * 
 *           Modification History: Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-11 yinjunlu 1.0 1.0 Version
 */
@Component
@Scope(ScopeType.VIEW)
public class InvestList extends EntityQuery<Invest> {

	private Date searchcommitMinTime;
	private Date searchcommitMaxTime;

	private static final String lazyModelCountHql = "select count(distinct invest) from Invest invest";
	private static final String lazyModelHql = "select distinct invest from Invest invest";
	//private static final String lazyModelCountHql = "select count(distinct invest) from Invest invest left join MotionTracking mk on invest.user.id=mk.who";
	//private static final String lazyModelHql = "select distinct invest from Invest invest left join MotionTracking mk on invest.user.id=mk.who";
	//start
	//主要为了当有推荐人的时候执行关联查询
	private static final String lazyModelCountHql1 = "select count(distinct invest) from Invest invest,MotionTracking mk where invest.user.id=mk.who";
	private static final String lazyModelHql1 = "select distinct invest from Invest invest,MotionTracking mk where invest.user.id=mk.who";
	//end
	private String referee;//投资人
	
	@Override
	public LazyDataModel<Invest> getLazyModel() {
		if(StringUtils.isNotEmpty(referee)){
			setCountHql(lazyModelCountHql1);
			setHql(lazyModelHql1);
			addRestriction("mk.fromWhere=#{investList.referee}");
			System.out.println(lazyModelCountHql1.toString());
		}
		return super.getLazyModel();
	}

	public InvestList() {
		setCountHql(lazyModelCountHql);
		setHql(lazyModelHql);
		final String[] RESTRICTIONS = {
				"invest.id like #{investList.example.id}",
				"invest.status like #{investList.example.status}",
				"invest.loan.user.id like #{investList.example.loan.user.id}",
				"invest.loan.id like #{investList.example.loan.id}",
				"invest.loan.name like #{investList.example.loan.name}",
				"invest.loan.type like #{investList.example.loan.type}",
				"invest.user.id = #{investList.example.user.id}",
				"invest.user.username = #{investList.example.user.username}",
				"invest.time >= #{investList.searchcommitMinTime}",
				"invest.status like #{investList.example.status}",
				"invest.time <= #{investList.searchcommitMaxTime}"};
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	@Override
	protected void initExample() {
		Invest example = new Invest();
		Loan loan = new Loan();
		loan.setUser(new User());
		example.setUser(new User());
		example.setLoan(loan);
		setExample(example);
	}

	public Date getSearchcommitMinTime() {
		return searchcommitMinTime;
	}

	public void setSearchcommitMinTime(Date searchcommitMinTime) {
		this.searchcommitMinTime = searchcommitMinTime;
	}

	public Date getSearchcommitMaxTime() {
		return searchcommitMaxTime;
	}

	public void setSearchcommitMaxTime(Date searchcommitMaxTime) {
		this.searchcommitMaxTime = searchcommitMaxTime;
	}

	/**
	 * 设置查询的起始和结束时间
	 */
	public void setSearchStartEndTime(Date startTime, Date endTime) {
		this.searchcommitMinTime = startTime;
		this.searchcommitMaxTime = endTime;
	}

	/**
	 * @author hch
	 * @param userId(用户id)
	 * @return 获取推荐人名称
	 */
	public String getFromWhere(String userId){
		String hql="select fromWhere from MotionTracking where who=?";
		List<String> list=getHt().find(hql,new Object[]{userId});
		String value="";
		if(list!=null&&list.size()>0){
			value=list.get(0);
		}
		return value;
	}
	/**
	 * hch
	 * @param referee
	 * @return 获取推荐人总投资金额
	 */
	public double getRefereeInvestSum(String referee){
		String hql="select sum(money) from Invest where user.id in(select who from MotionTracking where fromWhere=?)";
		Double value=(Double) getHt().find(hql, new Object[]{referee}).get(0);
		if(value==null){
			value=(double) 0;
		}
		return value;
	}

	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
	}
	
}
