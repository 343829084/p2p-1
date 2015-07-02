package com.esoft.archer.user.service;

import java.util.Date;
import java.util.List;

import org.primefaces.model.LazyDataModel;

import com.esoft.archer.user.model.AdverLeague;
import com.esoft.archer.user.model.AdverModel;

/**
 * 广告联盟接口
 * @author Administrator
 *
 */
@SuppressWarnings("unused")
public interface AdverService {
	
	/**
	 * 保存
	 * @param adverLeague
	 */
	public abstract void save(AdverLeague adverLeague);
	
	/**
	 * 查询列表
	 * @return
	 */
	public abstract List<AdverLeague> queryList();
	
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public abstract AdverLeague getAdverByID(int id);
	
	/**
	 * 统计MID下的注册的用户数
	 * @param mid
	 * @return
	 */
	public abstract int getCountByMid(String mid);
	
	
	/**
	 * 查询某个时间段下，MID下的注册用户数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public abstract List<AdverModel> getCoungGroupMid(final String regStartDate, final String regEndDate);
	
	/**
	 * 反馈给联盟查询
	 * @param mid 联盟Id
	 * @param uid 联盟用户Id
	 * @param stime 开始日期
	 * @param etime 结束日期
	 * @return
	 */
	public abstract List<AdverLeague> getAlList(String mid,String uid,String stime,String etime);
	
	
}
