package com.esoft.ipspay.query.service.impl;

import java.rmi.RemoteException;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.axis2.AxisFault;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.core.util.Dom4jUtil;
import com.esoft.ipspay.query.bean.CheckBalanceResult;
import com.esoft.ipspay.trusteeship.IpsPayConstans.Config;
import com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew;
import com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.QueryForAccBalance;
import com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.QueryForAccBalanceResponse;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;

import cryptix.jce.provider.MD5;

@Service("ipsCheckBalanceServiceImpl")
public class IpsCheckBalanceServiceImpl {
	
	@Resource HibernateTemplate ht;
	
	
	public CheckBalanceResult CheckBalanceByUserId(String userId) throws UserNotFoundException{
		CheckBalanceResult checkBalanceResult = null;
		try {
			ServiceStubNew stubNew = new ServiceStubNew();
			QueryForAccBalance queryForAccBalance0 =new QueryForAccBalance();
			//商户号
			queryForAccBalance0.setArgMerCode(Config.MER_CODE);
			//IPS 托管账号
			if(ht.get(TrusteeshipAccount.class, userId)==null){
				throw new UserNotFoundException("该用户未在环迅开户！");
			}
			String argIpsAccount = ht.get(TrusteeshipAccount.class, userId).getAccountId();
			queryForAccBalance0.setArgIpsAccount(argIpsAccount);
			String argSign = new MD5().toMD5(
					Config.MER_CODE + argIpsAccount
							+ Config.CERT).toLowerCase();
			queryForAccBalance0.setArgSign(argSign);
			QueryForAccBalanceResponse response= stubNew.queryForAccBalance(queryForAccBalance0);
			String result = response.getQueryForAccBalanceResult();
			Map<String, String> resultMap = Dom4jUtil.xmltoMap(result);
			String pErrCode = resultMap.get("pErrCode");
			String pErrMsg = resultMap.get("pErrMsg");
			String pBalance = resultMap.get("pBalance");//可用余额
			String pLock =resultMap.get("pLock");//冻结余额
			String pNeedstl =resultMap.get("pNeedstl");//未结算余额
			
			checkBalanceResult =new CheckBalanceResult();
			if(pErrCode.equals("0000")){
				checkBalanceResult.setpBalance(pBalance);
				checkBalanceResult.setpLock(pLock);
				checkBalanceResult.setpNeedstl(pNeedstl);
				checkBalanceResult.setpErrMsg(pErrMsg);
			}else{
				checkBalanceResult.setpErrMsg("查询失败");
			}
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return checkBalanceResult;
		
	}
}
