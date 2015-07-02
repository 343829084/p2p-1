package com.esoft.ipspay.invest.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.Dom4jUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.core.util.ThreeDES;
import com.esoft.ipspay.trusteeship.IpsPayConstans;
import com.esoft.ipspay.trusteeship.IpsPayConstans.Config;
import com.esoft.ipspay.trusteeship.exception.IpsPayOperationException;
import com.esoft.ipspay.trusteeship.service.impl.IpsPayOperationServiceAbs;
import com.esoft.jdp2p.coupon.exception.ExceedDeadlineException;
import com.esoft.jdp2p.coupon.exception.UnreachedMoneyLimitException;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.exception.ExceedMaxAcceptableRate;
import com.esoft.jdp2p.invest.exception.ExceedMoneyNeedRaised;
import com.esoft.jdp2p.invest.exception.IllegalLoanStatusException;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.service.InvestService;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;

import cryptix.jce.provider.MD5;

@Service
public class IpsPayInvestOperation extends IpsPayOperationServiceAbs<Invest> {

	@Logger
	Log log;

	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	UserBillBO userBillBO;

	@Resource
	InvestService investService;

	@Override
	@Transactional(noRollbackFor = IpsPayOperationException.class)
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
		String investId = resultMap.get("pMerBillNo");
		if (log.isDebugEnabled()) {
			log.debug("pErrCode:" + pErrCode);
			log.debug("pErrMsg:" + pErrMsg);
		}
		TrusteeshipOperation to = trusteeshipOperationBO.get(
				TrusteeshipConstants.OperationType.INVEST, investId, "ips");

		to.setResponseTime(new Date());
		to.setResponseData(GsonUtil.fromMap2Json(resultMap));
		if ("MG00000F".equals(pErrCode)) {
			Invest invest = ht.get(Invest.class, investId, LockMode.UPGRADE);
			if (invest.getStatus().equals(
					InvestConstants.InvestStatus.WAIT_AFFIRM)) {
				invest.setStatus(InvestConstants.InvestStatus.BID_SUCCESS);
				ht.update(invest);
				to.setStatus(TrusteeshipConstants.Status.PASSED);
				ht.update(to);
				if (invest.getInvestId() != null) {
					Invest no = ht.get(Invest.class, invest.getInvestId());
					no.setStatus(InvestConstants.InvestStatus.BID_SUCCESS);
					ht.saveOrUpdate(no);
				}
			}
		} else {
			fail(to);
			// 真实错误原因
			throw new IpsPayOperationException(pErrCode + ":" + pErrMsg);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void fail(TrusteeshipOperation to) {
		Invest invest = ht.get(Invest.class, to.getMarkId(), LockMode.UPGRADE);

		if (invest.getStatus().equals(InvestConstants.InvestStatus.WAIT_AFFIRM)) {
			invest.setStatus(InvestConstants.InvestStatus.CANCEL);
			ht.update(invest);
			// 改项目状态，项目如果是等待复核的状态，改为募集中
			if (invest.getLoan().getStatus()
					.equals(LoanConstants.LoanStatus.RECHECK)) {
				invest.getLoan().setStatus(LoanConstants.LoanStatus.RAISING);
				ht.update(invest.getLoan());
			}
			// 解冻投资金额
			try {
				userBillBO.unfreezeMoney(invest.getUser().getId(),
						invest.getMoney(), OperatorInfo.CANCEL_INVEST, "投资："
								+ invest.getId() + "失败，解冻投资金额, 借款ID："
								+ invest.getLoan().getId());
			} catch (InsufficientBalance e) {
				throw new RuntimeException(e);
			}
			to.setStatus(TrusteeshipConstants.Status.REFUSED);
			ht.update(to);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
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
	public TrusteeshipOperation createOperation(Invest invest,
			FacesContext facesContext) throws IOException {
		try {
			investService.create(invest);
		} catch (InsufficientBalance e) {
			throw new IpsPayOperationException("账户余额不足，请充值！");
		} catch (ExceedMoneyNeedRaised e) {
			throw new IpsPayOperationException("投资金额不能大于尚未募集的金额！");
		} catch (ExceedMaxAcceptableRate e) {
			throw new IpsPayOperationException("竞标利率不能大于借款者可接受的最高利率！");
		} catch (ExceedDeadlineException e) {
			throw new IpsPayOperationException("优惠券已过期");
		} catch (UnreachedMoneyLimitException e) {
			throw new IpsPayOperationException("投资金额未到达优惠券使用条件");
		} catch (IllegalLoanStatusException e) {
			throw new IpsPayOperationException("当前借款不可投资");
		}
		invest.setStatus(InvestConstants.InvestStatus.WAIT_AFFIRM);
		ht.saveOrUpdate(invest);

		if (invest.getInvestId() != null) {
			Invest no = ht.get(Invest.class, invest.getInvestId());
			no.setStatus(InvestConstants.InvestStatus.WAIT_AFFIRM);
			ht.saveOrUpdate(no);
		}

		// FIXME:好恶心。。。
		invest.setLoan(ht.get(Loan.class, invest.getLoan().getId()));
		invest.setUser(ht.get(User.class, invest.getUser().getId()));

		DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
		StringBuffer content = new StringBuffer();
		content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		content.append("<pReq>");

		// 商户订单号
		content.append("<pMerBillNo>" + invest.getId() + "</pMerBillNo>");
		// 商户日期
		content.append("<pMerDate>"
				+ DateUtil.DateToString(invest.getTime(), DateStyle.YYYYMMDD)
				+ "</pMerDate>");
		// 标号
		content.append("<pBidNo>" + invest.getLoan().getId() + "</pBidNo>");
		// 合同号
		content.append("<pContractNo>" + invest.getLoan().getId()
				+ "</pContractNo>");
		// 登记方式 ：1：手动投标 2：自动投标
		content.append("<pRegType>" + 1 + "</pRegType>");
		// 授权号
		content.append("<pAuthNo>" + "" + "</pAuthNo>");
		// 债权面额
		String Amount = currentNumberFormat.format(invest.getMoney()); // 支付金额
		content.append("<pAuthAmt>" + Amount + "</pAuthAmt>");
		// 交易金额
		content.append("<pTrdAmt>" + Amount + "</pTrdAmt>");
		// 投资人手续费
		content.append("<pFee>" + 0 + "</pFee>");
		// 账户类型：0#机构（暂未开放） ；1#个人
		content.append("<pAcctType>" + 1 + "</pAcctType>");
		// 证件号码
		content.append("<pIdentNo>" + invest.getUser().getIdCard()
				+ "</pIdentNo>");
		// 真实姓名
		content.append("<pRealName>" + invest.getUser().getRealname()
				+ "</pRealName>");
		// 投资人账户
		content.append("<pAccount>"
				+ ht.get(TrusteeshipAccount.class, invest.getUser().getId())
						.getAccountId() + "</pAccount>");
		// 借款用途
		content.append("<pUse>"
				+ (StringUtils.isEmpty(invest.getLoan().getLoanPurpose()) ? "系统借款"
						: invest.getLoan().getLoanPurpose()) + "</pUse>");
		content.append("<pWebUrl>"
				+ IpsPayConstans.ResponseWebUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.INVEST + "</pWebUrl>");
		content.append("<pS2SUrl>"
				+ IpsPayConstans.ResponseS2SUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.INVEST + "</pS2SUrl>");

		// 备注
		content.append("<pMemo1></pMemo1>");
		content.append("<pMemo2></pMemo2>");
		content.append("<pMemo3></pMemo3>");
		content.append("</pReq>");

		if (log.isDebugEnabled()) {
			log.debug("invest send to ips:" + content.toString());
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

		TrusteeshipOperation to = createTrusteeshipOperation(invest.getId(),
				IpsPayConstans.RequestUrl.INVEST, invest.getId(),
				TrusteeshipConstants.OperationType.INVEST, params);

		try {
			sendOperation(to, facesContext);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return to;
	}

}
