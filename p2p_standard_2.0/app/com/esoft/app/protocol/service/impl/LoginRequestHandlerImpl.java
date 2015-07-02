package com.esoft.app.protocol.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.app.AppConstants;
import com.esoft.app.protocol.key.ResponseMsgKey;
import com.esoft.app.protocol.model.In;
import com.esoft.app.protocol.model.Out;
import com.esoft.app.protocol.service.RequestHandler;
import com.esoft.app.protocol.util.exclusionStrategy.UserExclusionStrategy;
import com.esoft.archer.user.model.User;
import com.esoft.core.util.HashCrypt;
import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * 
 * 登陆
 *
 */
@Service("loginRequestHandler")
public class LoginRequestHandlerImpl implements RequestHandler{

	@Resource
	HibernateTemplate ht;
	
	@Override
	public Out handle(In in,Out out) {
		// TODO Auto-generated method stub
		try {
			JSONObject json=new JSONObject(in.getValue());
			String username=json.getString("username");
			String password=json.getString("password");
			if(username!=null&&password!=null&&username.length()>0&&password.length()>0){
				List<User> list=ht.find("from User where username=? and password=?",new Object[]{username,HashCrypt.getDigestHash(password)});
				if(list!=null&&list.size()>0){
					User user=list.get(0);
					ExclusionStrategy exclusionStrategy=new UserExclusionStrategy();
					Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setExclusionStrategies(exclusionStrategy).create();
					String str=gson.toJson(user);
					out.encryptResult(str);
					out.sign();
					out.setResultCode(ResponseMsgKey.SUCCESS);
					out.setResultMsg(String.valueOf(AppConstants.rMsgProps.get(ResponseMsgKey.SUCCESS)));
				}else{
					out.setResultCode(ResponseMsgKey.LOGIN_NOT_FIND);
					out.setResultMsg(String.valueOf(AppConstants.rMsgProps.get(ResponseMsgKey.LOGIN_NOT_FIND)));
				}
			}else{
				out.setResultCode(ResponseMsgKey.PARAMETER_INVALID);
				out.setResultMsg(String.valueOf(AppConstants.rMsgProps.get(ResponseMsgKey.PARAMETER_INVALID)));
			}
		}catch(JSONException e){
			out.setResultCode(ResponseMsgKey.ERROR);
			out.setResultMsg(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.setResultCode(ResponseMsgKey.ERROR);
			out.setResultMsg(String.valueOf(ResponseMsgKey.ERROR));
			e.printStackTrace();
		}
		return out;
	}

}
