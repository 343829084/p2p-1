package com.esoft.jdp2p.withdraw.service.impl;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.Dom4jUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.core.util.ThreeDES;
import com.esoft.ipspay.trusteeship.IpsPayConstans;
import com.esoft.ipspay.trusteeship.IpsPayConstans.Config;
import com.esoft.ipspay.trusteeship.exception.IpsPayOperationException;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.WithdrawCash;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.jdp2p.user.service.WithdrawCashService;
import com.esoft.jdp2p.withdraw.service.CapWithdrawCashService;

import cryptix.jce.provider.MD5;

@Service
public class CapitalPoolWithdrawOperation  extends	CapitalPoolOperationServiceAbs<WithdrawCash> {

	@Logger
	Log log;

	@Resource
	HibernateTemplate ht;

	@Resource
	CapWithdrawCashService capitalPoolwithdrawCashService;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	ConfigService configService;

	@Override
	@Transactional
	public void receiveOperationPostCallback(ServletRequest request) throws TrusteeshipReturnException {
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
		String withdrawCashId = resultMap.get("pMerBillNo");
		String pAcctType = resultMap.get("pAcctType");
		String pIdentNo = resultMap.get("pIdentNo");
		String pRealName = resultMap.get("pRealName");
		String pIpsAcctNo = resultMap.get("pIpsAcctNo");
		String pTrdDate = resultMap.get("pTrdDate");
		String pTrdAmt = resultMap.get("pTrdAmt");
		String pTrdBnkCode = resultMap.get("pTrdBnkCode");
		String pIpsBillNo = resultMap.get("pIpsBillNo");
		String trusteeship = resultMap.get("trusteeship");

		TrusteeshipOperation to = trusteeshipOperationBO.get(
				TrusteeshipConstants.OperationType.WITHDRAW_CASH,
				withdrawCashId, trusteeship);
		ht.evict(to);
		to = ht.get(TrusteeshipOperation.class, to.getId(), LockMode.UPGRADE);

		to.setResponseTime(new Date());
		to.setResponseData(GsonUtil.fromMap2Json(resultMap));
		if (log.isDebugEnabled()) {
			log.debug("提现返回的信息:");
			log.debug("pErrCode:" + pErrCode);
			log.debug("pErrMsg:" + pErrMsg);
		}
		if (to.getStatus().equals(TrusteeshipConstants.Status.SENDED)) {
			WithdrawCash wc = ht.get(WithdrawCash.class, withdrawCashId,
					LockMode.UPGRADE);
			if ("MG00001F".equals(pErrCode)) {
				capitalPoolwithdrawCashService.passWithdrawCashRecheck(wc);
				to.setStatus(TrusteeshipConstants.Status.PASSED);
				ht.merge(to);
			} else {
				fail(to);
//				if ("MG00001F".equals(pErrCode)) {
					throw new TrusteeshipReturnException(pErrMsg);
//				}
				// 真实错误原因
//				throw new RuntimeException(new TrusteeshipReturnException(
//						pErrCode + ":" + pErrMsg));
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void fail(TrusteeshipOperation to) {
		// FIXME：直接进行拒绝处理，不太合适。
		WithdrawCash wc = ht.get(WithdrawCash.class, to.getMarkId(),
				LockMode.UPGRADE);
		capitalPoolwithdrawCashService.refuseWithdrawCashApply(wc);
		to.setStatus(TrusteeshipConstants.Status.REFUSED);
		ht.merge(to);
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(WithdrawCash wc,FacesContext facesContext) {
		try {
			wc.setType(configService.getConfigValue("withdraw_cap"));
			capitalPoolwithdrawCashService.applyWithdrawCash(wc);
			wc.setUser(ht.get(User.class, wc.getUser().getId()));
		} catch (InsufficientBalance e1) {
			throw new IpsPayOperationException("余额不足", e1);
		}
		StringBuffer content = new StringBuffer();
		content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		content.append("<pReq>");
		// 商户提现订单号
		content.append("<pMerBillNo>" + wc.getId() + "</pMerBillNo>");
		// 账户类型 0#机构（暂未开放） ；1#个人
		content.append("<pAcctType>" + 1 + "</pAcctType>");
		// 提现模式:1#普通提现；2#定向提现<暂丌开放>
		content.append("<pOutType>" + 1 + "</pOutType>");
		// 标号
		content.append("<pBidNo>" + "" + "</pBidNo>");
		// 合同号
		content.append("<pContractNo>" + "" + "</pContractNo>");
		// 提现去向
		content.append("<pDwTo>" + "" + "</pDwTo>");
		// 证件号码
		content.append("<pIdentNo>" + wc.getUser().getIdCard() + "</pIdentNo>");
		// 姓名
		content.append("<pRealName>" + wc.getUser().getRealname()
				+ "</pRealName>");
		if (log.isDebugEnabled()) {
			log.debug("pRealName------------>" + wc.getUser().getRealname());
		}
		// IPS 账户号
		content.append("<pIpsAcctNo>"
				+ ht.get(TrusteeshipAccount.class, wc.getUser().getId())
						.getAccountId() + "</pIpsAcctNo>");
		// 提现日期
		content.append("<pDwDate>"
				+ DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD)
				+ "</pDwDate>");

		// 提现金额
		DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
		String Amount = currentNumberFormat.format(wc.getMoney());
		content.append("<pTrdAmt>" + Amount + "</pTrdAmt>");

		// IPS 手续费收取方:1：平台支付 2：提现方支付
		String feeType = "1";
		try {
			feeType = configService.getConfigValue("uw.withdraw_fee_type");
		} catch (ObjectNotFoundException e) {
			if (log.isDebugEnabled()) {
				log.debug(e);
			}
		}

		// 平台手续费
		if (feeType.equals("2")) {
			// 如果是用户自己承担手续费，假如提现100，手续费3块，则账户扣除103
			// 系统配置的手续费中，需要减去环迅手续费，才是系统最终收入系统的手续费。
			String ipsWithdrawFee = configService
					.getConfigValue("ipspay.withdraw_fee");
			String fee = currentNumberFormat.format(ArithUtil.sub(wc.getFee(),
					Integer.parseInt(ipsWithdrawFee)));
			content.append("<pMerFee>" + fee + "</pMerFee>");
		} else {
			content.append("<pMerFee>"
					+ currentNumberFormat.format(wc.getFee()) + "</pMerFee>");
		}

		content.append("<pIpsFeeType>" + feeType + "</pIpsFeeType>");

		content.append("<pWebUrl>"
				+ IpsPayConstans.ResponseWebUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.WITHDRAW_CASH
				+ "</pWebUrl>");
		content.append("<pS2SUrl>"
				+ IpsPayConstans.ResponseS2SUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.WITHDRAW_CASH
				+ "</pS2SUrl>");

		// 备注
		content.append("<pMemo1></pMemo1>");
		content.append("<pMemo2></pMemo2>");
		content.append("<pMemo3></pMemo3>");
		content.append("</pReq>");
		if (log.isDebugEnabled()) {
			log.debug("提现发送的信息：" + content.toString());
		}
		String arg3DesXmlPara = ThreeDES.encrypt(content.toString(),
				Config.THREE_DES_BASE64_KEY, Config.THREE_DES_IV,
				Config.THREE_DES_ALGORITHM);
		String argSign = new MD5().toMD5(
				IpsPayConstans.Config.MER_CODE + arg3DesXmlPara
						+ IpsPayConstans.Config.CERT).toLowerCase();
		// 包装参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("pMerCode", IpsPayConstans.Config.MER_CODE);
		params.put("p3DesXmlPara", arg3DesXmlPara);
		params.put("pSign", argSign);
		String str="支付宝";
		TrusteeshipOperation to = createTrusteeshipOperation(wc.getId(),
				IpsPayConstans.RequestUrl.WITHDRAW_CASH, wc.getId(),
				TrusteeshipConstants.OperationType.WITHDRAW_CASH, params,str);

		try {
			sendOperation(to, facesContext);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return to;
	}

}
