package com.esoft.archer.user.controller;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.AdverLeague;
import com.esoft.archer.user.model.AdverModel;
import com.esoft.archer.user.service.AdverService;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.util.DateUtil;


@Component
@Scope(ScopeType.VIEW)
public class AdverHome extends EntityHome<AdverLeague> implements java.io.Serializable {

	//广告联盟
	@Resource
	private AdverService adverService;
	
	private Date regStartDate;
	private Date regEndDate;
	
	public Date getRegStartDate() {
		return regStartDate;
	}

	public void setRegStartDate(Date regStartDate) {
		this.regStartDate = regStartDate;
	}

	public Date getRegEndDate() {
		return regEndDate;
	}

	public void setRegEndDate(Date regEndDate) {
		this.regEndDate = regEndDate;
	}

	public List<AdverLeague> queryList(){
		return this.adverService.queryList();
	}
	
	public List<AdverModel> getCountGroupByMid(){
		//System.out.println(regStartDate);
		return this.adverService.getCoungGroupMid(DateUtil.DateToString(regStartDate, "yyyy-MM-dd hh:mm:ss"), DateUtil.DateToString(regEndDate, "yyyy-MM-dd hh:mm:ss"));
		
	}

	public void setAdverService(AdverService adverService) {
		this.adverService = adverService;
	}

	public AdverService getAdverService() {
		return adverService;
	}
	
	public String getStr(){
		String mid = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("mid");
		System.out.println(mid);
		return mid;
	}
	
	
	
	
	
	
}
