package com.esoft.zcxy.user.controller;

import org.apache.commons.logging.Log;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import com.esoft.archer.user.controller.UserHome;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.zcxy.QueryValidatorServicesImplServiceStub;
import com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingle;
import com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleE;
import com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleResponseE;
import com.esoft.zcxy.ZcxyConstants.Config;
import com.esoft.zcxy.util.DesUtil;

public class ZcxyUserHome extends UserHome {
	
	@Logger
	Log log;

	@Override
	public String getInvestorPermission() {
		try {
			if (zcxy(getInstance())) {
				return super.getInvestorPermission();
			} else {
				FacesUtil.addInfoMessage("身份信息不真实，实名认证失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean zcxy(User user) throws Exception {
		QueryValidatorServicesImplServiceStub stub = new QueryValidatorServicesImplServiceStub();
		stub._getServiceClient()
				.getOptions()
				.setProperty(
						org.apache.axis2.Constants.Configuration.DISABLE_SOAP_ACTION,
						true);
		QuerySingleE qe = new QuerySingleE();
		QuerySingle qs = new QuerySingle();
		// 用户名
		qs.setArg0(DesUtil.encrypt(Config.USERNAME, Config.KEY));
		// 密码
		qs.setArg1(DesUtil.encrypt(Config.PASSWORD, Config.KEY));
		// 数据类型
		qs.setArg2(DesUtil.encrypt(Config.DATA_TYPE, Config.KEY));
		qs.setArg3(DesUtil.encrypt(user.getRealname().trim() + ","
				+ user.getIdCard().trim(), Config.KEY));
		qe.setQuerySingle(qs);
		QuerySingleResponseE qre = stub.querySingle(qe);
		String result = DesUtil.decrypt(qre.getQuerySingleResponse()
				.get_return(), Config.KEY);
		if (log.isDebugEnabled()) {
			log.debug(result);
		}
		Node pNode = DocumentHelper.parseText(result)
				.selectSingleNode("/data/policeCheckInfos/policeCheckInfo/compStatus");
		return pNode!=null && "3".equals(pNode.getText().trim());
	}

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}
}
