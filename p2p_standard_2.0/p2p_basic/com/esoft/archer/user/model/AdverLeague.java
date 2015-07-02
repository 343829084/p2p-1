package com.esoft.archer.user.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * cpa 广告联盟
 * @author Administrator
 *
 */
@Entity
@Table(name = "adver_league")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AdverLeague implements java.io.Serializable{
	
	private int id;

	//联盟ID
	private String mid;
	//联盟用户ID
	private String uid;
	//注册日期
	private Date regDate;
	//查询状态  默认值2 查询注册成功
	private String status;
	//用户是否已经认证，1：已认证，2：未认证
	private String userName;
	
	
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 11)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="mid",length=255)
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	@Column(name="uid",length=255)
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@Column(name="status",length=255)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name="userName",length=255)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name="regDate")
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	
}
