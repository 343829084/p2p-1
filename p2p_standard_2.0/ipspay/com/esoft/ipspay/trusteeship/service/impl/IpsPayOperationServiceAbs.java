package com.esoft.ipspay.trusteeship.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.core.util.GsonUtil;
import com.esoft.core.util.HtmlElementUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationServiceAbs;

public abstract class IpsPayOperationServiceAbs<T> extends
		TrusteeshipOperationServiceAbs<T> {

	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createTrusteeshipOperation(String markId,
			String requestUrl, String operator, String type,
			Map<String, String> content) {
		TrusteeshipOperation to = new TrusteeshipOperation();
		to.setId(IdGenerator.randomUUID());
		to.setMarkId(markId);
		to.setOperator(operator);
		to.setRequestUrl(requestUrl);
		to.setCharset("utf-8");
		to.setRequestData(GsonUtil.fromMap2Json(content));
		to.setType(type);
		to.setTrusteeship("ips");
		to.setRequestTime(new Date());
		to.setStatus(TrusteeshipConstants.Status.SENDED);
		trusteeshipOperationBO.save(to);
		return to;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createTrusteeshipOperationWealth(String markId,
			String requestUrl, String operator, String type,
			Map<String, String> content) {
		TrusteeshipOperation to = new TrusteeshipOperation();
		to.setId(IdGenerator.randomUUID());
		to.setMarkId(markId);
		to.setOperator(operator);
		to.setRequestUrl(requestUrl);
		to.setCharset("utf-8");
		to.setRequestData(GsonUtil.fromMap2Json(content));
		to.setType(type);
		to.setTrusteeship("uw");
		to.setRequestTime(new Date());
		to.setStatus(TrusteeshipConstants.Status.SENDED);
		trusteeshipOperationBO.save(to);
		return to;
	}

	public void sendOperation(TrusteeshipOperation to, FacesContext facesContext)
			throws IOException {
		String form = HtmlElementUtil.createAutoSubmitForm(
				GsonUtil.fromJson2Map(to.getRequestData()), to.getRequestUrl(),
				to.getCharset());
		super.sendOperation(form, to.getCharset(), facesContext);
	}
}
