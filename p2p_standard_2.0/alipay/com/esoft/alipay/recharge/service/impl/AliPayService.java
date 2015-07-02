package com.esoft.alipay.recharge.service.impl;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.esoft.archer.pay.service.PayService;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;

@Service
public class AliPayService implements PayService {

	@Resource
	private AliPayRechargeOperation operation;
	
	@Override
	public void recharge(FacesContext fc, Recharge recharge, String bankCardNo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveReturnWeb(HttpServletRequest request) {
		try {
			operation.receiveOperationPostCallback(request);
		} catch (TrusteeshipReturnException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void receiveReturnS2S(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

}
