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
import com.esoft.jdp2p.invest.InvestConstants.TransferStatus;
import com.esoft.jdp2p.invest.model.TransferApply;
import com.esoft.jdp2p.invest.service.TransferService;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.LoanConstants.RepayStatus;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.exception.AdvancedRepayException;
import com.esoft.jdp2p.repay.exception.NormalRepayException;
import com.esoft.jdp2p.repay.model.InvestRepay;
import com.esoft.jdp2p.repay.model.LoanRepay;
import com.esoft.jdp2p.repay.service.RepayService;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeePoint;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeeType;
import com.esoft.jdp2p.risk.service.impl.FeeConfigBO;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;

import cryptix.jce.provider.MD5;

@Service
public class IpsPayAdvanceRepayOperation extends
		IpsPayOperationServiceAbs<Loan> {

	@Resource
	HibernateTemplate ht;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;

	@Resource
	UserBillBO userBillBO;

	@Resource
	RepayService repayService;

	@Resource
	FeeConfigBO feeConfigBO;

	@Resource
	TransferService transferService;

	@Logger
	Log log;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public TrusteeshipOperation createOperation(Loan loan,
			FacesContext facesContext) throws IOException {

		ht.evict(loan);
		loan = ht.get(Loan.class, loan.getId(), LockMode.UPGRADE);
		// 查询当期还款是否已还清
		List<LoanRepay> repays = loan.getLoanRepays();
		// 剩余所有本金
		double sumCorpus = 0D;
		// 手续费总额
		double feeAll = 0D;
		for (LoanRepay repay : repays) {
			if (repay.getStatus().equals(LoanConstants.RepayStatus.REPAYING)) {
				// 在还款期，而且没还款
				if (repayService.isInRepayPeriod(repay.getRepayDay())) {
					// 有未完成的当期还款。
					throw new AdvancedRepayException("当期还款未完成");
				} else {
					sumCorpus = ArithUtil.add(sumCorpus, repay.getCorpus());
					feeAll = ArithUtil.add(feeAll, repay.getFee());
				}
			} else if (repay.getStatus().equals(
					LoanConstants.RepayStatus.BAD_DEBT)
					|| repay.getStatus().equals(
							LoanConstants.RepayStatus.OVERDUE)) {
				// 还款中存在逾期或者坏账
				throw new AdvancedRepayException("还款中存在逾期或者坏账");
			}
		}

		// 给投资人的罚金
		double feeToInvestor = feeConfigBO.getFee(
				FeePoint.ADVANCE_REPAY_INVESTOR, FeeType.PENALTY, null, null,
				sumCorpus);
		// 给系统的罚金
		double feeToSystem = feeConfigBO.getFee(FeePoint.ADVANCE_REPAY_SYSTEM,
				FeeType.PENALTY, null, null, sumCorpus);

		double sumPay = ArithUtil.add(feeToInvestor, feeToSystem, sumCorpus,
				feeAll);
		double sumToInvestor = ArithUtil.add(feeToInvestor, sumCorpus);
		double sumToSystem = ArithUtil.add(feeToSystem, feeAll);
		// 扣除还款金额+罚金
		// 冻结还款金额
		try {
			userBillBO.freezeMoney(loan.getUser().getId(), sumPay,
					OperatorInfo.ADVANCE_REPAY, "冻结还款金额，借款编号：" + loan.getId());
		} catch (InsufficientBalance e) {
			throw new AdvancedRepayException("余额不足");
		}

		List<TransferApply> tas = ht
				.find("from TransferApply ta where ta.invest.loan.id=? and ta.status=?",
						new Object[] { loan.getId(), TransferStatus.TRANSFERING });

		for (TransferApply ta : tas) {
			// 该投资下面的债权转让，取消。
			transferService.cancel(ta.getId());
		}

		// 改项目状态。
		loan.setStatus(RepayStatus.WAIT_REPAY_VERIFY);
		ht.merge(loan);

		DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
		StringBuffer content = new StringBuffer();
		content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		content.append("<pReq>");

		// 标号
		content.append("<pBidNo>" + loan.getId() + "</pBidNo>");
		// 还款日期
		content.append("<pRepaymentDate>"
				+ DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD)
				+ "</pRepaymentDate>");
		// 商户还款订单号
		content.append("<pMerBillNo>" + loan.getId() + "advance_repay"
				+ "</pMerBillNo>");
		// 还款类型：1#手劢还款，2#自劢还款
		content.append("<pRepayType>" + 1 + "</pRepayType>");
		// 授权号：当还款类型为自劢还款时不为空，为手劢还款时为空
		content.append("<pIpsAuthNo></pIpsAuthNo>");
		// 转出方 IPS 账号
		content.append("<pOutAcctNo>"
				+ ht.get(TrusteeshipAccount.class, loan.getUser().getId())
						.getAccountId() + "</pOutAcctNo>");
		// 还款总金额
		String Amount = currentNumberFormat.format(sumToInvestor); // 支付金额
		content.append("<pOutAmt>" + Amount + "</pOutAmt>");
		// 转出方总手续费
		content.append("<pOutFee>" + currentNumberFormat.format(feeToSystem)
				+ "</pOutFee>");

		content.append("<pWebUrl><![CDATA["
				+ IpsPayConstans.ResponseWebUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.ADVANCE_REPAY
				+ "]]></pWebUrl>");
		content.append("<pS2SUrl><![CDATA["
				+ IpsPayConstans.ResponseS2SUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.ADVANCE_REPAY
				+ "]]></pS2SUrl>");

		content.append("<pDetails>");

		List<InvestRepay> irs = ht
				.find("from InvestRepay ir where ir.invest.loan.id=? and ir.status=?",
						new Object[] { loan.getId(), RepayStatus.REPAYING });

		double feeToInvestorTemp = feeToInvestor;
		for (int i = 0; i < irs.size(); i++) {

			InvestRepay ir = irs.get(i);

			// 罚金
			double cashFine;
			if (i == irs.size() - 1) {
				cashFine = feeToInvestorTemp;
			} else {
				cashFine = ArithUtil.round(ir.getCorpus() / sumCorpus
						* feeToInvestor, 2);
				feeToInvestorTemp = ArithUtil.sub(feeToInvestorTemp, cashFine);
			}

			userBillBO.transferIntoBalance(ir.getInvest().getUser().getId(),
					ArithUtil.add(ir.getCorpus(), cashFine),
					OperatorInfo.ADVANCE_REPAY, "投资：" + ir.getInvest().getId()
							+ "收到还款" + "  本金：" + ir.getCorpus() + "  罚息："
							+ cashFine);

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
			content.append("<pInFee>" + currentNumberFormat.format(0)
					+ "</pInFee>");
			if (i == 0) {
				// 借款人还款手续费
				content.append("<pOutInfoFee>"
						+ currentNumberFormat.format(loan.getFeeOnRepay())
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

		TrusteeshipOperation to = createTrusteeshipOperation(loan.getId(),
				IpsPayConstans.RequestUrl.REPAY, loan.getId(),
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
		LoanRepay lr = ht
				.get(LoanRepay.class, to.getMarkId(), LockMode.UPGRADE);
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
