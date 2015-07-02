package com.esoft.app.protocol.controller;

import java.io.IOException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.app.AppConstants;
import com.esoft.app.AppConstants.Config;
import com.esoft.app.protocol.key.ResponseMsgKey;
import com.esoft.app.protocol.model.In;
import com.esoft.app.protocol.model.Out;
import com.esoft.app.protocol.service.RequestHandler;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.SpringBeanUtil;
import com.esoft.core.util.ThreeDES;
import com.google.gson.Gson;

/**
 * 处理app发来的请求
 * 
 * @author Administrator
 * 
 */
@Component
@Scope(ScopeType.REQUEST)
public class TestRequestHome {
	private static final Gson gson = new Gson();
	
//	@Resource(name="bannerRequestHandler")
//	private RequestHandler handler;

	/** 处理请求 */
	public void handleRequest() {
		In in = new In(FacesUtil.getHttpServletRequest());
		//TODO:验签
		//in.verify();
		Out out = new Out(null,null, in.getDeviceId(),
				in.getRequestId(), in.getMethod(), null,
				null);
		//TODO:根据操作，取到对应service，如果找不到，报错
		if(!in.verify()){
			//解密
			if(in.getValue()!=null&&in.getValue().length()>0){
				in.setValue(ThreeDES.decrypt(in.getValue(), Config.THREE_DES_BASE64_KEY,Config.THREE_DES_IV, Config.THREE_DES_ALGORITHM));
			}
			//System.out.println("______"+in.getValue());
			//测试数据start
			//in.setValue("{'userId':'admin'}");
			//in.setValue("{'termId':'wangzhangonggao'}");
			//in.setValue("{'isRecommend':'0','status':'','curPage':'1','size':'0'}");
			//in.setValue("{'userId':'test501','curPage':2,'size':10,'type':'','typeInfo':''}");
			//in.setMethod("loanRequestHandler");
			//in.setMethod("loginRequestHandler");
			//in.setValue("{'loanId':'20140922000023','objName':''}");
			//in.setMethod("loanIdFindRequestHandler");
			//in.setMethod("centerHandler");
			//in.setMethod("bannerRequestHandler");
			//in.setMethod("cashFlowRequestHandler");
			//in.setMethod("nodeRequestHandler");
			//in.setValue("{'phone':'15201526179','op':'zhaohui','userId':''}");
			//in.setMethod("smsRequestHandler");
			//in.setValue("{'username':'admin','password':'','email':'','mobileNumber':'','authCode':'','referrer':''}");
			//in.setMethod("registRequestHandler");
			//in.setValue("{'userId':'test501','loanId':'20141027000001','investMoney':49}");
			//in.setMethod("loanInvestRequestHandler");
			//in.setValue("{'userId':'test501','oldPwd':'123456','newPwd':'t123456','newPwd1':'t123456'}");
			//in.setMethod("pwdUpdateHandler");
/*			in.setValue("{'userId':'test501','phone':'15201526179','code':'271713'}");
			in.setMethod("pwdFindHandler");*/
			//in.setValue("{'userId':'test501','newPwd':'t123456','newPwd1':'t123456'}");
			//in.setMethod("pwdFind2Handler");
			//in.setValue("{'phone':'15201526179','op':'update_phone1','userId':'test501'}");
			//in.setMethod("smsRequestHandler");
			//in.setValue("{'phone':'15201526179','op':'2','userId':'test501','code':'085715'}");
			//in.setMethod("phoneUpdateHandler");
			//in.setValue("{'phone':'15201526179','op':'update_phone2','userId':'test501'}");
			//in.setMethod("smsRequestHandler");
			//in.setValue("{'userId':'admin','curPage':1,'size':0,'op':'wcg'}");
			//in.setMethod("myInvestRequestHandler");
			in.setValue("{'userId':'admin','curPage':1,'size':0,'op':'hk'}");
			in.setMethod("myLoanRequestHandler");
			//测试数据end
			RequestHandler handler = null;
			try{
				handler=(RequestHandler) SpringBeanUtil.getBeanByName(in.getMethod());
				if(handler!=null){
					out= handler.handle(in,out);
				}else{
					out.setResultCode(ResponseMsgKey.METHOD_NOT_FOUND);
					out.setResultMsg(String.valueOf(AppConstants.rMsgProps.get(ResponseMsgKey.METHOD_NOT_FOUND)));
				}
			}catch(org.springframework.beans.factory.NoSuchBeanDefinitionException e){
				out.setResultCode(ResponseMsgKey.METHOD_NOT_FOUND);
				out.setResultMsg(String.valueOf(AppConstants.rMsgProps.get(ResponseMsgKey.METHOD_NOT_FOUND)));
			}
	
		}else{
			out.setResultCode(ResponseMsgKey.ILLEGAL_SIGN);
			out.setResultMsg(String.valueOf(AppConstants.rMsgProps.get(ResponseMsgKey.PARAMETER_INVALID)));
		}
		try {
			FacesUtil.getHttpServletResponse().getWriter()
					.print(gson.toJson(out));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				FacesUtil.getHttpServletResponse().getWriter().flush();
				FacesUtil.getHttpServletResponse().getWriter().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FacesUtil.getCurrentInstance().responseComplete();
	}
	public static void main(String[] args){
		Out out=new Out();
		out.setDeviceId("1111");
		out.setMethod("111");
		System.out.println(gson.toJson(out));
	}
}
