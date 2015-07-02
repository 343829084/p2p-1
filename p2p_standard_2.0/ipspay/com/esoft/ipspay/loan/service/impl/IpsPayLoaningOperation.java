package com.esoft.ipspay.loan.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew;
import com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.Transfer;
import com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.TransferResponse;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.exception.BorrowedMoneyTooLittle;
import com.esoft.jdp2p.loan.exception.ExistWaitAffirmInvests;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import cryptix.jce.provider.MD5;

@Service
public class IpsPayLoaningOperation extends IpsPayOperationServiceAbs<Loan> {

	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	LoanService loanService;

	@Logger
	Log log;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(Loan loan,
			FacesContext facesContext) throws IOException {
		loan = ht.get(Loan.class, loan.getId());
		DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");

		StringBuffer content = new StringBuffer();
		content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		content.append("<pReq>");
		// 商户放款订单号
		content.append("<pMerBillNo>" + loan.getId() + "</pMerBillNo>");
		// 标号
		content.append("<pBidNo>" + loan.getId() + "</pBidNo>");
		// 商户日期
		content.append("<pDate>"
				+ DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD)
				+ "</pDate>");
		// 转账类型
		content.append("<pTransferType>" + 1 + "</pTransferType>");
		// 转账方式
		content.append("<pTransferMode>" + 1 + "</pTransferMode>");
		content.append("<pS2SUrl><![CDATA["
				+ IpsPayConstans.ResponseS2SUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.GIVE_MOENY_TO_BORROWER
				+ "]]></pS2SUrl>");

		content.append("<pDetails>");

		List<Invest> invests = Lists.newArrayList(Collections2.filter(
				loan.getInvests(), new Predicate<Invest>() {
					public boolean apply(Invest invest) {
						return invest.getStatus().equals(
								InvestConstants.InvestStatus.BID_SUCCESS);
					}
				}));
		double fee = loan.getLoanGuranteeFee();
		for (Invest invest : invests) {

			content.append("<pRow>");
			// 登记债权人时提交的订单号
			content.append("<pOriMerBillNo>" + invest.getId()
					+ "</pOriMerBillNo>");
			// 放款金额
			String money = currentNumberFormat.format(invest.getInvestMoney());

			content.append("<pTrdAmt>" + money + "</pTrdAmt>");
			// 转出方账户类型
			content.append("<pFAcctType>" + 1 + "</pFAcctType>");
			// IPS 个人托管账户号
			content.append("<pFIpsAcctNo>"
					+ ht.get(TrusteeshipAccount.class, invest.getUser().getId())
							.getAccountId() + "</pFIpsAcctNo>");
			// 转出方明细手续费
			content.append("<pFTrdFee>" + currentNumberFormat.format(0.00)
					+ "</pFTrdFee>");
			// 转入方账户类型:0#机构；1#个人
			content.append("<pTAcctType>" + 1 + "</pTAcctType>");
			// 转入方 IPS 托管账户号
			content.append("<pTIpsAcctNo>"
					+ ht.get(TrusteeshipAccount.class, loan.getUser().getId())
							.getAccountId() + "</pTIpsAcctNo>");
			double feeT = ArithUtil.sub(fee, invest.getInvestMoney());
			// 转入方明细手续费
			if (feeT < 0) {
				content.append("<pTTrdFee>" + currentNumberFormat.format(fee)
						+ "</pTTrdFee>");
				fee = 0;
			} else {
				fee = feeT;
				content.append("<pTTrdFee>"
						+ currentNumberFormat.format(invest.getInvestMoney())
						+ "</pTTrdFee>");
			}
			content.append("</pRow>");
		}
		content.append("</pDetails>");

		// 备注
		content.append("<pMemo1><![CDATA[]]></pMemo1>");
		content.append("<pMemo2><![CDATA[]]></pMemo2>");
		content.append("<pMemo3><![CDATA[]]></pMemo3>");
		content.append("</pReq>");
		if (log.isDebugEnabled()) {
			log.debug("放款发送的信息：" + content.toString());
		}
		String arg3DesXmlPara = ThreeDES.encrypt(content.toString(),
				Config.THREE_DES_BASE64_KEY, Config.THREE_DES_IV,
				Config.THREE_DES_ALGORITHM);
		String argSign = new MD5().toMD5(
				IpsPayConstans.Config.MER_CODE + arg3DesXmlPara
						+ IpsPayConstans.Config.CERT).toLowerCase();

		ServiceStubNew stubNew;
		try {
			stubNew = new ServiceStubNew();
			Transfer transfer = new Transfer();
			transfer.setPMerCode(IpsPayConstans.Config.MER_CODE);
			transfer.setP3DesXmlPara(arg3DesXmlPara);
			transfer.setPSign(argSign);

			TransferResponse response = stubNew.transfer(transfer);
			String argXmlPara = response.getTransferResult();
			Document resultDom = DocumentHelper.parseText(Dom4jUtil
					.removeBom(argXmlPara));
			Map<String, String> resultMap = Dom4jUtil.xmltoMap(argXmlPara);
			String pErrCode = resultMap.get("pErrCode");
			String pErrMsg = resultMap.get("pErrMsg");
			String p3DesXmlPara = resultMap.get("p3DesXmlPara");
			String pXmlPara = ThreeDES.decrypt(p3DesXmlPara,
					Config.THREE_DES_BASE64_KEY, Config.THREE_DES_IV,
					Config.THREE_DES_ALGORITHM);
			Map<String, String> paraMap = Dom4jUtil.xmltoMap(pXmlPara);
			if (log.isDebugEnabled()) {
				log.debug("******放款同步返回的信息*******");
				log.debug("返回的参数：" + GsonUtil.fromMap2Json(resultMap));
			}

			if (pErrCode.equals("MG00008F")) {
				loan.setStatus(LoanConstants.LoanStatus.WAITING_RECHECK_VERIFY);
				ht.update(loan);
			} else {
				// 真实错误原因
				throw new IpsPayOperationException(pErrCode + ":" + pErrMsg);
			}
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {
		throw new RuntimeException("unexpected invocation");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
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
		String loanId = resultMap.get("pMerBillNo");
		if (log.isDebugEnabled()) {
			log.debug("pErrCode:" + pErrCode + "*****放款异步返回信息*****");
		}

		Loan loan = ht.get(Loan.class, loanId);
		try {
			if ("MG00000F".equals(pErrCode)) {
				if (loan.getStatus().equals(
						LoanConstants.LoanStatus.WAITING_RECHECK_VERIFY)) {
					// FIXME:解冻保证金
					loanService.giveMoneyToBorrower(loanId);
				}
			} else {
				if (log.isInfoEnabled()) {
					log.info(pErrCode + pErrMsg);
				}
			}
		} catch (ExistWaitAffirmInvests e) {
			throw new RuntimeException(e);
		} catch (BorrowedMoneyTooLittle e) {
			throw new RuntimeException(e);
		}
	}
}
