package com.esoft.archer.user.model;

import java.util.Date;

/**
 * 广告模型
 * @author Administrator
 *
 */
public class AdverModel implements java.io.Serializable {


	private Date regDate;//注册日期
	private String fromWhere;//来源==MID
	private long regCount;//注册数
	private int authCount;//认证数
	
	
	
	
	public AdverModel(Date regDate, String fromWhere, long regCount,
			int authCount) {
		super();
		this.regDate = regDate;
		this.fromWhere = fromWhere;
		this.regCount = regCount;
		this.authCount = authCount;
	}
	
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getFromWhere() {
		return fromWhere;
	}
	public void setFromWhere(String fromWhere) {
		this.fromWhere = fromWhere;
	}
	public long getRegCount() {
		return regCount;
	}
	public void setRegCount(long regCount) {
		this.regCount = regCount;
	}
	public int getAuthCount() {
		return authCount;
	}
	public void setAuthCount(int authCount) {
		this.authCount = authCount;
	}
}
