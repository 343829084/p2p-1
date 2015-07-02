package com.esoft.archer.user.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.system.controller.DictUtil;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserWealth;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateUtil;

@Component
@Scope(ScopeType.VIEW)
public class UserWealthList extends EntityQuery<UserWealth> {

	private Date startTime;
	private Date endTime;
	@Logger
	Log log;

	@Autowired
	private DictUtil dictUtil;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public UserWealthList() {
		final String[] RESTRICTIONS = { "id like #{userWealthList.example.id}",
				"user.id like #{userWealthList.example.user.id}",
				"type like #{userWealthList.example.type}",
				"typeInfo like #{userWealthList.example.typeInfo}",
				"time >= #{userWealthList.startTime}",
				"time <= #{userWealthList.endTime}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		// addOrder("time", super.DIR_DESC);
	}

	@Override
	protected void initExample() {
		UserWealth example = new UserWealth();
		example.setUser(new User());
		setExample(example);
	}

	public Date getSearchcommitMinTime() {
		return startTime;
	}

	public void setSearchcommitMinTime(Date searchcommitMinTime) {
		this.startTime = searchcommitMinTime;
	}

	public Date getSearchcommitMaxTime() {
		return endTime;
	}

	public void setSearchcommitMaxTime(Date searchcommitMaxTime) {
		if (searchcommitMaxTime != null) {
			searchcommitMaxTime = DateUtil.addDay(searchcommitMaxTime, 1);
		}
		this.endTime = searchcommitMaxTime;
	}

	/**
	 * 设置查询的起始和结束时间
	 */
	public void setSearchStartEndTime(Date startTime, Date endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * 导出
	 */
	public void export() {
		UserWealth userWealth = getExample();
		StringBuilder hql = new StringBuilder("from UserWealth where 1=1");
		if (userWealth.getUser().getId() != null
				&& userWealth.getUser().getId().length() > 0) {
			hql.append(" and user.id='").append(userWealth.getUser().getId())
					.append("'");
		}
		if (userWealth.getTypeInfo() != null
				&& userWealth.getTypeInfo().length() > 0) {
			hql.append(" and typeInfo='").append(userWealth.getTypeInfo())
					.append("'");
		}
		if (startTime != null && endTime != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			hql.append(" and time between ").append(format.format(startTime))
					.append(" and ").append(format.format(endTime));
		}
		List<UserWealth> list = getHt().find(hql.toString());
		if (list != null && list.size() > 0) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			DecimalFormat numberFormat = new DecimalFormat("0.00");
			String[] excelHeader = { "时间", "类型|明细", "金额", "可用金额", "冻结金额", "备注" };
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("user_wealth");
			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < excelHeader.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(excelHeader[i]);
				sheet.autoSizeColumn(i);
			}
			for (int i = 0; i < list.size(); i++) {
				UserWealth bill = list.get(i);
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(format.format(bill.getTime()));
				row.createCell(1).setCellValue(
						dictUtil.getValue("bill_operator", bill.getTypeInfo()));
				row.createCell(2).setCellValue(
						numberFormat.format(bill.getMoney()));
				row.createCell(3).setCellValue(
						numberFormat.format(bill.getBalance()));
				row.createCell(4).setCellValue(
						numberFormat.format(bill.getFrozenMoney()));
				row.createCell(5).setCellValue(bill.getDetail());
			}
			HttpServletResponse response = FacesUtil.getHttpServletResponse();
			response.setContentType("application/vnd.ms-excel");
			OutputStream stream = null;
			try {
				String filename = "账户流水_" + userWealth.getUser().getId() + ".xls";
				String agent = FacesUtil.getHttpServletRequest().getHeader(
						"USER-AGENT");
				if (null != agent && -1 != agent.indexOf("MSIE")) {
					filename = URLEncoder.encode(filename, "utf-8");
				} else {
					filename = new String(filename.getBytes("utf-8"),
							"iso8859-1");
				}
				response.setHeader("Content-disposition",
						"attachment;filename=" + filename);
				stream = response.getOutputStream();
				wb.write(stream);
				stream.flush();
				stream.close();
				stream = null;
				response.flushBuffer();
				FacesUtil.getCurrentInstance().responseComplete();
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
