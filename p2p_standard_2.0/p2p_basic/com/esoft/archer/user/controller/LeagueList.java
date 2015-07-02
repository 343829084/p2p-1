package com.esoft.archer.user.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.AdverLeague;
import com.esoft.archer.user.model.AdverLeagueDTO;
import com.esoft.archer.user.service.AdverService;
import com.esoft.archer.user.util.ClassUtil;
import com.esoft.archer.user.util.DozerMapperSingleton;
import com.esoft.archer.user.util.JsonDateValueProcessor;
import com.esoft.archer.user.valide.LeagueListValidator;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.JSONUtils;

/**
 * 联盟反馈
 * 
 * @author gxz
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class LeagueList extends EntityQuery<AdverLeague> implements
		java.io.Serializable {
	static Logger logger = Logger.getLogger(LeagueList.class.getName());

	public Map<String, Object> getMapData() {
		return mapData;
	}

	public void setMapData(Map<String, Object> mapData) {
		this.mapData = mapData;
	}

	@Resource
	private AdverService adverService;

	@Resource
	private LeagueListValidator leagueListValidator;
	
	private Map<String, String> mapPara = new HashMap<String, String>();
	private Map<String, Object> mapData = new HashMap<String, Object>();

	@ResponseBody
	public String getleagueList() {

		try {

			ClassUtil.convertMapValueDecord(mapPara, "utf-8");
			leagueListValidator.getleagueList(mapPara);
			List alist = adverService.getAlList(mapPara.get("mid"),mapPara.get("uid"), mapPara.get("stime"),mapPara.get("eid"));
			List listDTO = null;
			if (null != alist && alist.size() > 0) {

				listDTO = new ArrayList();
				DozerMapperSingleton.listCopy(alist, listDTO,
						"com.esoft.archer.user.model.AdverLeagueDTO");

			}
			int total = 10;// 总页数
			mapData.put("recordTotal", total);
			JSONUtils.saveMap(mapData, "0", "操作成功", listDTO);
		
			
			
		} catch (Exception ex) {

			JSONUtils.saveMap(mapData, "1", "query方法" + ex.getMessage(),	null);
		}

		nowStatusClearAndSetResponseValue();
		return JSONUtils.getResponseStringJSON(mapData);
		
	}

	/**
	 * 单个对象查询
	 * 
	 * */
	public String getleagueByid() {

		try {

			ClassUtil.convertMapValueDecord(mapPara, "utf-8");
			leagueListValidator.getleagueByid(mapPara);
			AdverLeague adverLeague = adverService.getAdverByID(Integer.parseInt(mapPara.get("id")));
			AdverLeagueDTO adverLeagueDTO=null;
			
			if(null!=adverLeague){
				adverLeagueDTO=new AdverLeagueDTO();
				 DozerMapperSingleton.getInstance().map(adverLeague,
						 adverLeagueDTO);
		    }	
			List list=new ArrayList();
			list.add(adverLeagueDTO);		
			JSONUtils.saveMap(mapData, "0", "操作成功", list);

		} catch (Exception ex) {

			logger.error("getleagueByid方法执行异常"+ex.getMessage());
			JSONUtils.saveMap(mapData, "1", "getleagueByid方法执行异常" + ex.getMessage(), null);
		}
		nowStatusClearAndSetResponseValue();
		return JSONUtils.getResponseStringJSON(mapData);
	}

	
	
	private void fillPara(AdverLeague adverLeague){
		
		if(null!=adverLeague){
			
			adverLeague.setMid(mapPara.get("mid"));
			adverLeague.setUid(mapPara.get("uid"));
			adverLeague.setStatus("1");
			adverLeague.setUserName(mapPara.get("userName"));
			adverLeague.setRegDate(new Date());
		}
		
		
	}
	public String save(){

		try {

			ClassUtil.convertMapValueDecord(mapPara, "utf-8");
			leagueListValidator.save(mapPara);
			AdverLeague adverLeague = new AdverLeague();
			fillPara(adverLeague);
			adverService.save(adverLeague);
				
			JSONUtils.saveMap(mapData, "0","保存成功", null);

		} catch (Exception ex) {

			logger.error("getleagueByid方法执行异常"+ex.getMessage());
			JSONUtils.saveMap(mapData, "1","league.save保存失败:"+ex.getMessage(), null);
		}
		nowStatusClearAndSetResponseValue();
		return "";
	}
	
	
	
	private void nowStatusClearAndSetResponseValue(){
	
		FacesUtil.setRequestAttribute("data",JSONUtils.getResponseStringJSON(mapData));
		mapPara.clear();
		mapData.clear();
		
	}
	/**
	 * 判断日期格式是否正确
	 * 
	 * @param date
	 * @return
	 */
	public boolean isValidDate(String date) {
		String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
		String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
				+ "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
				+ "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
				+ "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
				+ "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		if ((date != null)) {
			Pattern pattern = Pattern.compile(datePattern1);
			Matcher match = pattern.matcher(date);
			if (match.matches()) {
				pattern = Pattern.compile(datePattern2);
				match = pattern.matcher(date);
				return match.matches();
			} else {
				return false;
			}
		}
		return false;
	}

	public void setAdverService(AdverService adverService) {
		this.adverService = adverService;
	}

	public Map<String, String> getMapPara() {
		return mapPara;
	}

	public void setMapPara(Map<String, String> mapPara) {
		this.mapPara = mapPara;
	}
}
