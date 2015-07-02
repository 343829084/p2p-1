package com.esoft.ipspay.repay.service.impl;

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

import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.service.impl.UserBillBO;
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
import com.esoft.ipspay.trusteeship.service.impl.IpsPayOperationServiceAbs;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.LoanConstants.RepayStatus;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.repay.exception.NormalRepayException;
import com.esoft.jdp2p.repay.model.InvestRepay;
import com.esoft.jdp2p.repay.model.LoanRepay;
import com.esoft.jdp2p.repay.service.RepayService;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;

import cryptix.jce.provider.MD5;

@Service
public class IpsPayOverdueRepayOperation extends
		IpsPayOperationServiceAbs<LoanRepay> {
	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	UserBillBO userBillBO;

	@Resource
	RepayService repayService;

	@Logger
	Log log;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(LoanRepay lr,
			FacesContext facesContext) throws IOException {
		lr.setStatus(RepayStatus.WAIT_REPAY_VERIFY);
		ht.update(lr);
		Double allRepayMoney = ArithUtil.add(lr.getCorpus(),
				lr.getDefaultInterest(), lr.getFee(), lr.getInterest());
		// 冻结还款金额
		try {
			userBillBO.freezeMoney(lr.getLoan().getUser().getId(),
					allRepayMoney, OperatorInfo.NORMAL_REPAY, "冻结还款金额，还款编号："
							+ lr.getId());
		} catch (InsufficientBalance e) {
			throw new RuntimeException(e);
		}

		DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
		StringBuffer content = new StringBuffer();
		content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		content.append("<pReq>");

		// 标号
		content.append("<pBidNo>" + lr.getLoan().getId() + "</pBidNo>");
		// 还款日期
		content.append("<pRepaymentDate>"
				+ DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD)
				+ "</pRepaymentDate>");
		// 商户还款订单号
		content.append("<pMerBillNo>" + lr.getId() + "</pMerBillNo>");
		// 还款类型：1#手劢还款，2#自劢还款
		content.append("<pRepayType>" + 1 + "</pRepayType>");
		// 授权号：当还款类型为自劢还款时不为空，为手劢还款时为空
		content.append("<pIpsAuthNo></pIpsAuthNo>");
		// 转出方 IPS 账号
		content.append("<pOutAcctNo>"
				+ ht.get(TrusteeshipAccount.class,
						lr.getLoan().getUser().getId()).getAccountId()
				+ "</pOutAcctNo>");
		// 还款总金额
		String Amount = currentNumberFormat.format(allRepayMoney); // 支付金额
		content.append("<pOutAmt>" + Amount + "</pOutAmt>");
		// 转出方总手续费
		content.append("<pOutFee>" + currentNumberFormat.format(lr.getFee())
				+ "</pOutFee>");

		content.append("<pWebUrl><![CDATA["
				+ IpsPayConstans.ResponseWebUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.REPAY + "]]></pWebUrl>");
		content.append("<pS2SUrl><![CDATA["
				+ IpsPayConstans.ResponseS2SUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.REPAY + "]]></pS2SUrl>");

		content.append("<pDetails>");

		List<InvestRepay> irs = ht
				.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?",
						new Object[] { lr.getLoan().getId(), lr.getPeriod() });

		for (int i = 0; i < irs.size(); i++) {
			InvestRepay ir = irs.get(i);
			content.append("<pRow>");
			// 登记债权人时提交的订单号
			content.append("<pCreMerBillNo>" + ir.getInvest().getId()
					+ "</pCreMerBillNo>");
			// 转入方 IPS 托管账户号
			content.append("<pInAcctNo>"
					+ ht.get(TrusteeshipAccount.class,
							ir.getInvest().getUser().getId()).getAccountId()
					+ "</pInAcctNo>");
			// 转入方手续费
			content.append("<pInFee>" + currentNumberFormat.format(ir.getFee())
					+ "</pInFee>");
			if (i == 0) {
				// 借款人还款手续费
				content.append("<pOutInfoFee>"
						+ currentNumberFormat.format(lr.getFee())
						+ "</pOutInfoFee>");
			} else {
				content.append("<pOutInfoFee>" + currentNumberFormat.format(0)
						+ "</pOutInfoFee>");
			}
			// 还款金额
			content.append("<pInAmt>"
					+ currentNumberFormat.format(ArithUtil.add(ir.getCorpus(),
							ir.getInterest())) + "</pInAmt>");
			content.append("</pRow>");
		}
		content.append("</pDetails>");

		// 备注
		content.append("<pMemo1><![CDATA[]]></pMemo1>");
		content.append("<pMemo2><![CDATA[]]></pMemo2>");
		content.append("<pMemo3><![CDATA[]]></pMemo3>");
		content.append("</pReq>");
		if (log.isDebugEnabled()) {
			log.debug("还款发送的信息：" + content.toString());
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

		TrusteeshipOperation to = createTrusteeshipOperation(lr.getId(),
				IpsPayConstans.RequestUrl.REPAY, lr.getId(),
				TrusteeshipConstants.OperationType.REPAY, params);

		try {
			sendOperation(to, facesContext);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return to;
	}

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
		String repayId = resultMap.get("pMerBillNo");
		if (log.isDebugEnabled()) {
			log.debug("还款返回的信息：");
			log.debug("pErrCode：" + pErrCode);
			log.debug("pErrMsg：" + pErrMsg);
		}
		TrusteeshipOperation to = trusteeshipOperationBO.get(
				TrusteeshipConstants.OperationType.REPAY, repayId, "ips");

		to.setResponseTime(new Date());
		to.setResponseData(GsonUtil.fromMap2Json(resultMap));
		LoanRepay lr = ht.get(LoanRepay.class, repayId, LockMode.UPGRADE);
		if (log.isInfoEnabled()) {
			log.info(pErrCode + ":" + pErrMsg);
		}
		try {
			if ("MG00008F".equals(pErrCode)) {
				// 还款受理中!
				lr.setStatus(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY);
				to.setStatus(TrusteeshipConstants.Status.PASSED);
			} else if ("MG00000F".equals(pErrCode)) {
				userBillBO.unfreezeMoney(lr.getLoan().getUser().getId(),
						ArithUtil.add(lr.getCorpus(), lr.getDefaultInterest(),
								lr.getFee(), lr.getInterest()),
						OperatorInfo.NORMAL_REPAY,
						"资金托管方还款，解冻还款金额，还款编号：" + lr.getId());
				// 正常还款
				LoanRepay repay = ht.get(LoanRepay.class, repayId,
						LockMode.UPGRADE);
				repay.setStatus(RepayStatus.REPAYING);
				repayService.normalRepay(repay);
			} else {
				fail(to);
				// 真实错误原因
				throw new IpsPayOperationException(pErrCode + ":" + pErrMsg);
			}
		} catch (InsufficientBalance e) {
			throw new RuntimeException(e);
		} catch (NormalRepayException e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void fail(TrusteeshipOperation to) {
		LoanRepay lr = ht.get(LoanRepay.class, to.getMarkId(), LockMode.UPGRADE);
		// 解冻金额
		lr.setStatus(RepayStatus.REPAYING);
		try {
			userBillBO.unfreezeMoney(
					lr.getLoan().getUser().getId(),
					ArithUtil.add(lr.getCorpus(), lr.getDefaultInterest(),
							lr.getFee(), lr.getInterest()),
					OperatorInfo.NORMAL_REPAY,
					"资金托管方还款失败，解冻还款金额，还款编号：" + lr.getId());
		} catch (InsufficientBalance e) {
			throw new RuntimeException(e);
		}
		to.setStatus(TrusteeshipConstants.Status.REFUSED);
		ht.update(lr);
		ht.update(to);
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

}
