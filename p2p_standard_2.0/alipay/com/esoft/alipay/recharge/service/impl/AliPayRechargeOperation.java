package com.esoft.alipay.recharge.service.impl;

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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.ObjectNotFoundException;
import org.jsoup.nodes.Document.QuirksMode;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.alipay.test.callback.TestBackMethod;
import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.UserConstants.RechargeStatus;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserBill;
import com.esoft.archer.user.service.UserBillService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.Dom4jUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.ThreeDES;
import com.esoft.ipspay.trusteeship.IpsPayConstans;
import com.esoft.ipspay.trusteeship.IpsPayConstans.Config;
import com.esoft.ipspay.trusteeship.service.impl.IpsPayOperationServiceAbs;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.jdp2p.message.service.FwSmsTemplate;
import com.esoft.jdp2p.statistics.controller.WealthStatistics;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.jdp2p.user.service.RechargeService;

import cryptix.jce.provider.MD5;

@Service
public class AliPayRechargeOperation extends
		IpsPayOperationServiceAbs<Recharge> {

	@Logger
	Log log;

	@Resource
	HibernateTemplate ht;

	@Resource
	RechargeService rechargeService;
	
	@Resource
	ConfigService configService;

	@Resource
	TrusteeshipOperationBO trusteeshipOperationBO;
	
	@Resource
	FwSmsTemplate fwSmsTemplate;

	@Override
	@Transactional
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {
		/*try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		// 商户号
		String pMerCode = request.getParameter("pMerCode");
		// 充值状态
		String pErrCode = request.getParameter("pErrCode");
		// 返回信息
		String pErrMsg = request.getParameter("pErrMsg");
		// 获取返回的3DES报文体加密
		String p3DesXmlPara = request.getParameter("p3DesXmlPara");
		// 获取返回的加密摘要
		String pSign = request.getParameter("pSign");
		// 验证签名是否正确
		// FIXME:缺少md5withrsa验证，详情参见文档。
		// String argSign = new MD5WithRSA().verifysignature(arg0, arg1, arg2)(
		// TrusteeshipConstants.Config.MER_CODE +pErrCode+ pErrMsg +p3DesXmlPara
		// + TrusteeshipConstants.Config.CERT).toLowerCase();
		// if (!argSign.equals(pSign)) {
		// // 如果不正确，抛异常
		// throw new TrusteeshipReturnException("sign is inlegal");
		// }
		// 解密
		String pXmlPara = ThreeDES.decrypt(p3DesXmlPara, Config.THREE_DES_BASE64_KEY, Config.THREE_DES_IV, Config.THREE_DES_ALGORITHM);
		// 处理账户开通成功
		Map<String, String> resultMap = Dom4jUtil.xmltoMap(pXmlPara);
		String rechargeId = resultMap.get("pMerBillNo");
		String pAcctType = resultMap.get("pAcctType");
		String pIdentNo = resultMap.get("pIdentNo");
		String pRealName = resultMap.get("pRealName");
		String pIpsAcctNo = resultMap.get("pIpsAcctNo");
		String pTrdDate = resultMap.get("pTrdDate");
		String pTrdAmt = resultMap.get("pTrdAmt");
		String pTrdBnkCode = resultMap.get("pTrdBnkCode");
		String pIpsBillNo = resultMap.get("pIpsBillNo");
*/
		
		//param1  rechargeId
		String rechargeId = request.getParameter("param1");
		System.out.println(rechargeId);
		System.out.println(TrusteeshipConstants.OperationType.RECHARGE);
		//TODO:关于TrusteeshipOperation表中的数据：好有钱的标识暂定为：uw  The meaning of the word is :user_wealth
		TrusteeshipOperation to = trusteeshipOperationBO.get(
				TrusteeshipConstants.OperationType.RECHARGE,
				rechargeId, "uw");
		

		to.setResponseTime(new Date());
		 
		//XXX:默认充值好友钱成功
		
		Recharge recharge = ht.get(Recharge.class,rechargeId);
		
		rechargeService.rechargePaySuccessWealth(rechargeId);
		to.setStatus(TrusteeshipConstants.Status.PASSED);
		ht.update(to);
		//可用金额
		//#{wealthStatistics.getFrozenMoneyByUserId(loginUserInfo.loginUserId)}
		fwSmsTemplate.sendRechargeSuccess(recharge.getUser().getMobileNumber(),recharge.getActualMoney());
		
		//TODO:关于好友钱的第三方返回处理：因平台未定，则无法解析
		/*to.setResponseData(GsonUtil.fromMap2Json(resultMap))
		if(log.isDebugEnabled()){
			log.debug("充值返回的信息：");
			log.debug("pErrCode:"+pErrCode);
			log.debug("pErrMsg:"+pErrMsg);
		}
		if ("MG00000F".equals(pErrCode)) {
			rechargeService.rechargePaySuccess(rechargeId);
			to.setStatus(TrusteeshipConstants.Status.PASSED);
			ht.update(to);
		} else {
			// 标记充值失败。
			Recharge recharge = ht.get(Recharge.class, rechargeId);
			recharge.setStatus(RechargeStatus.FAIL);
			ht.update(recharge);
			to.setStatus(TrusteeshipConstants.Status.REFUSED);
			ht.update(to);

			if ("MG00001F".equals(pErrCode)) {
				throw new TrusteeshipReturnException(pErrMsg);
			}
			// 真实错误原因
			throw new RuntimeException(new TrusteeshipReturnException(pErrCode
					+ ":" + pErrMsg));
		}*/
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
		try {
			receiveOperationPostCallback(request);
		} catch (TrusteeshipReturnException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public TrusteeshipOperation createOperation(Recharge recharge,
			FacesContext facesContext) {
		rechargeService.createRechargeOrder(recharge);
		recharge = ht.get(Recharge.class, recharge.getId());
		recharge.setUser(ht.get(User.class, recharge.getUser().getId()));
		System.out.println(recharge.getId());
		/*StringBuffer content = new StringBuffer();
		content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		content.append("<pReq>");
		// 充值订单号
		content.append("<pMerBillNo>" + recharge.getId() + "</pMerBillNo>");
		// 账户类型 0#非托管（目前不开放） 1#托管
		content.append("<pAcctType>1</pAcctType>");
		// 证件号码 个人身份证
		content.append("<pIdentNo>" + recharge.getUser().getIdCard()
				+ "</pIdentNo>");
		// 用户真实姓名
		content.append("<pRealName>" + recharge.getUser().getRealname()
				+ "</pRealName>");
		// IPS 账户号
		content.append("<pIpsAcctNo>"
				+ ht.get(TrusteeshipAccount.class, recharge.getUser().getId())
						.getAccountId() + "</pIpsAcctNo>");
		// 充值日期
		content.append("<pTrdDate>"
				+ DateUtil.DateToString(recharge.getTime(), DateStyle.YYYYMMDD)
				+ "</pTrdDate>");
		// 充值金额
		DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
		String Amount = currentNumberFormat.format(recharge.getActualMoney()
				+ recharge.getFee()); // 支付金额
		content.append("<pTrdAmt>" + Amount + "</pTrdAmt>");

		String bankCardNo = recharge.getRechargeWay();

		// 充值渠道种类 1#网银充值；2#代扣充值
		String pChannelType = "2";
		if (StringUtils.isNotEmpty(bankCardNo)) {
			pChannelType = "1";
		}
		content.append("<pChannelType>" + pChannelType + "</pChannelType>");
		// 充值银行 代扣充值这里传空
		content.append("<pTrdBnkCode>" + bankCardNo + "</pTrdBnkCode>");
		// 平台手续费
		content.append("<pMerFee>0.00</pMerFee>");
		// content.append("<pMerFee>"
		// + currentNumberFormat.format(recharge.getFee()) + "</pMerFee>");
		// 谁付IPS 手续费 1：平台支付 2：用户支付
		String feeType = "1";
		try {
			feeType = configService.getConfigValue("ipspay.recharge_fee_type");
		} catch (ObjectNotFoundException e) {
			if (log.isDebugEnabled()) {
				log.debug(e);
			}
		}
		content.append("<pIpsFeeType>"+feeType+"</pIpsFeeType>");

		content.append("<pWebUrl>"
				+ IpsPayConstans.ResponseWebUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.RECHARGE + "</pWebUrl>");
		content.append("<pS2SUrl>"
				+ IpsPayConstans.ResponseS2SUrl.PRE_RESPONSE_URL
				+ TrusteeshipConstants.OperationType.RECHARGE + "</pS2SUrl>");

		// 备注
		content.append("<pMemo1></pMemo1>");
		content.append("<pMemo2></pMemo2>");
		content.append("<pMemo3></pMemo3>");
		content.append("</pReq>");
		if(log.isDebugEnabled()){
			log.debug("充值发送的信息："+content.toString());
		}
		String arg3DesXmlPara = ThreeDES.encrypt(content.toString(), Config.THREE_DES_BASE64_KEY, Config.THREE_DES_IV, Config.THREE_DES_ALGORITHM);
		String argSign = new MD5().toMD5(
				IpsPayConstans.Config.MER_CODE + arg3DesXmlPara
						+ IpsPayConstans.Config.CERT).toLowerCase();
		// 包装参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("pMerCode", IpsPayConstans.Config.MER_CODE);
		params.put("p3DesXmlPara", arg3DesXmlPara);
		params.put("pSign", argSign);*/
		
		
		Map<String, String> params = new HashMap<String, String>();
		ht.setFlushMode(HibernateTemplate.FLUSH_EAGER);
		TrusteeshipOperation to = createTrusteeshipOperationWealth(recharge.getId(),
				"", recharge.getId(),
				TrusteeshipConstants.OperationType.RECHARGE, params);
	
	
		try {
			//sendOperation(to, facesContext);
			ht.merge(recharge);
			ht.merge(to);
			TestBackMethod.post("http://localhost:8080/archer/testRechargeCallBack/ali", to.getMarkId());
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return to;

	}

}
