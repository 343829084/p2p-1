package com.esoft.ipspay.user.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.Dom4jUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.ShortUrlGenerator;
import com.esoft.core.util.ThreeDES;
import com.esoft.ipspay.trusteeship.IpsPayConstans;
import com.esoft.ipspay.trusteeship.IpsPayConstans.Config;
import com.esoft.ipspay.trusteeship.service.impl.IpsPayOperationServiceAbs;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;

import cryptix.jce.provider.MD5;

@Service
public class IpsPayCreateAccountOperation extends
		IpsPayOperationServiceAbs<User> {

	@Logger
	Log log;

	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	UserService userService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(User user,
			FacesContext facesContext) throws IOException {

		String markId = ShortUrlGenerator.shortUrl(IdGenerator.randomUUID())
				+ ShortUrlGenerator.shortUrl(IdGenerator.randomUUID())
				+ ShortUrlGenerator.shortUrl(IdGenerator.randomUUID());

		StringBuffer content = new StringBuffer();
		content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		content.append("<pReq>");
		// 商户开户流水号，商户系统唯一不重复
		content.append("<pMerBillNo>" + markId + "</pMerBillNo>");
		// 证件类型,1#身份证，默认：1
		content.append("<pIdentType>1</pIdentType>");
		// 真实身份证
		content.append("<pIdentNo>" + user.getIdCard() + "</pIdentNo>");
		// 真实姓名
		content.append("<pRealName><![CDATA[" + user.getRealname()
				+ "]]></pRealName>");
		// 用户发送短信
		content.append("<pMobileNo>" + user.getMobileNumber() + "</pMobileNo>");
		// 用于登录账号,激活邮件
		content.append("<pEmail>" + user.getEmail() + "</pEmail>");
		// 提交日期
		content.append("<pSmDate>"
				+ DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD)
				+ "</pSmDate>");
		content.append("<pWebUrl><![CDATA["
				+ IpsPayConstans.ResponseWebUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.CREATE_ACCOUNT
				+ "]]></pWebUrl>");
		content.append("<pS2SUrl><![CDATA["
				+ IpsPayConstans.ResponseS2SUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.CREATE_ACCOUNT
				+ "]]></pS2SUrl>");
		content.append("<pMemo1><![CDATA[" + user.getSex() + "]]></pMemo1>");
		content.append("<pMemo2><![CDATA["
				+ DateUtil.DateToString(user.getBirthday(),
						DateStyle.YYYY_MM_DD_CN) + "]]></pMemo2>");
		content.append("<pMemo3><![CDATA[]]></pMemo3>");
		content.append("</pReq>");

		String arg3DesXmlPara = ThreeDES.encrypt(content.toString(),
				Config.THREE_DES_BASE64_KEY, Config.THREE_DES_IV,
				Config.THREE_DES_ALGORITHM);

		if (log.isDebugEnabled()) {
			log.debug("发送的content------------>" + content.toString());
		}
		String argSign = new MD5().toMD5(
				IpsPayConstans.Config.MER_CODE + arg3DesXmlPara
						+ IpsPayConstans.Config.CERT).toLowerCase();
		// 包装参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("argMerCode", IpsPayConstans.Config.MER_CODE);
		params.put("arg3DesXmlPara", arg3DesXmlPara);
		params.put("argSign", argSign);

		TrusteeshipOperation to = createTrusteeshipOperation(markId,
				IpsPayConstans.RequestUrl.CREATE_IPS_ACCT, user.getId(),
				TrusteeshipConstants.OperationType.CREATE_ACCOUNT, params);

		try {
			sendOperation(to, facesContext);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return to;
	}

	@Override
	@Transactional
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		// 商户号
		String pMerCode = request.getParameter("pMerCode");
		// 开户状态
		String pErrCode = request.getParameter("pErrCode");
		// 返回信息
		String pErrMsg = request.getParameter("pErrMsg");
		// 获取返回的3DES报文体加密
		String p3DesXmlPara = request.getParameter("p3DesXmlPara");
		// 获取返回的加密摘要
		String pSign = request.getParameter("pSign");
		// 解密
		String pXmlPara = ThreeDES.decrypt(p3DesXmlPara,
				Config.THREE_DES_BASE64_KEY, Config.THREE_DES_IV,
				Config.THREE_DES_ALGORITHM);
		// 处理账户开通成功
		Map<String, String> resultMap = Dom4jUtil.xmltoMap(pXmlPara);
		String markId = resultMap.get("pMerBillNo");
		String idCard = resultMap.get("pIdentNo");
		String realName = resultMap.get("pRealName");
		String mobileNumber = resultMap.get("pMobileNo");
		String sex = resultMap.get("pMemo1");
		String birthdayStr = resultMap.get("pMemo2");
		if (log.isDebugEnabled()) {
			log.debug("开户返回的信息：");
			log.debug("pErrCode:" + pErrCode);
			log.debug("pErrMsg:" + pErrMsg);
		}
		TrusteeshipOperation to = trusteeshipOperationBO.get(
				TrusteeshipConstants.OperationType.CREATE_ACCOUNT, markId,
				"ips");
		to.setResponseTime(new Date());
		// 记录返回信息
		to.setResponseData(GsonUtil.fromMap2Json(resultMap));

		if ("MG00000F".equals(pErrCode)) {
			User user = ht.get(User.class, to.getOperator());
			if (user != null) {
				Date pIpsAcctDate = DateUtil.StringToDate(
						resultMap.get("pIpsAcctDate"), DateStyle.YYYYMMDD);
				// user.setArea(ht.get(Area.class, areaId));
				user.setIdCard(idCard);
				user.setRealname(realName);
				user.setMobileNumber(mobileNumber);
				user.setSex(sex);
				user.setBirthday(DateUtil.StringToDate(birthdayStr));

				// 往用户托管账户表里面插入信息
				TrusteeshipAccount ta = ht.get(TrusteeshipAccount.class,
						user.getId());
				if (ta == null) {
					ta = new TrusteeshipAccount();
					ta.setId(user.getId());
					ta.setUser(user);
				}
				ta.setAccountId(resultMap.get("pIpsAcctNo"));
				ta.setCreateTime(pIpsAcctDate);
				ta.setStatus(TrusteeshipConstants.Status.PASSED);
				ta.setTrusteeship("ips");
				ht.saveOrUpdate(ta);
				userService.realNameCertification(user);
				to.setStatus(TrusteeshipConstants.Status.PASSED);
				ht.update(to);
			}
		} else {
			to.setStatus(TrusteeshipConstants.Status.REFUSED);
			ht.update(to);
			if ("MG01011F".equals(pErrCode)) {
				throw new TrusteeshipReturnException("该邮箱在环迅平台下已被使用，请更换绑定邮箱。");
			}
			throw new TrusteeshipReturnException(pErrMsg);
		}

	}

	@Override
	@Transactional
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
		try {
			receiveOperationPostCallback(request);
		} catch (TrusteeshipReturnException e) {
			e.printStackTrace();
		}
	}

}
