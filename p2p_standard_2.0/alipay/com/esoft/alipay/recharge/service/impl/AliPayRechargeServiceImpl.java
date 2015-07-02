package com.esoft.alipay.recharge.service.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;

import com.esoft.archer.user.model.RechargeBankCard;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.Dom4jUtil;
import com.esoft.ipspay.recharge.bean.RechargeBankCardImpl;
import com.esoft.ipspay.trusteeship.IpsPayConstans;
import com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew;
import com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.GetBankList;
import com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.GetBankListResponse;
import com.esoft.jdp2p.user.service.impl.RechargeServiceImpl;

public class AliPayRechargeServiceImpl extends RechargeServiceImpl {
	
	@Logger
	Log log;

	@Override
	public List<RechargeBankCard> getBankCardsList() {
		List<RechargeBankCard> rechargeBankCards = new ArrayList<RechargeBankCard>();

		cryptix.jce.provider.MD5 b = new cryptix.jce.provider.MD5();
		String signMD5 = b.toMD5(
				IpsPayConstans.Config.MER_CODE + IpsPayConstans.Config.CERT)
				.toLowerCase();

		ServiceStubNew stubNew;
		try {
			stubNew = new ServiceStubNew();

			GetBankList getBankList = new GetBankList();
			getBankList
					.setArgMerCode(IpsPayConstans.Config.MER_CODE);
			getBankList.setArgSign(signMD5);
			GetBankListResponse reponse = stubNew.getBankList(getBankList);
			String argXmlPara = reponse.getGetBankListResult();
			Map<String, String> resultMap = Dom4jUtil.xmltoMap(argXmlPara);
			String pBankList = resultMap.get("pBankList");
			if (log.isDebugEnabled()) {
				log.debug(pBankList);				
			}
			String[] bankCardStrs = pBankList.split("#");
			for (String str : bankCardStrs) {
				String[] strs = str.split("[|]");
				RechargeBankCard rBC = new RechargeBankCardImpl();
				rBC.setBankName(strs[0]);
				rBC.setCardAlias(strs[1]);
				rBC.setNo(strs[2]);
				rechargeBankCards.add(rBC);
			}

			Collections.reverse(rechargeBankCards);
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return rechargeBankCards;
	}
}
