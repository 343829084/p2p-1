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
import com.esoft.app.protocol.util.NumberUtil;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.util.ArithUtil;
/**
 * 
 * 获取个人中心一些信息
 *
 */
@Service("centerHandler")
public class CenterHandlerImpl implements RequestHandler{

	@Resource
	HibernateTemplate ht;
	@Resource
	private UserBillBO userBillBO;
	
	@Override
	public Out handle(In in, Out out) {
		// TODO Auto-generated method stub
		try{
			JSONObject json=new JSONObject(in.getValue());
			String userId=json.getString("userId");
			//可用余额
			double balcance=userBillBO.getBalance(userId);
			//冻结金额
			double frozen=userBillBO.getFrozenMoney(userId);
			//待收金额=待收本金+待收利息
			double dsje=ArithUtil.add(getReceivableCorpus(userId), getReceivableInterest(userId));
			//资产总额=账户可用金额+冻结金额+待收本息
			double mySum=ArithUtil.add(ArithUtil.add(balcance,frozen),dsje);
			//累计收益
			double myInvestsInterest=getInvestsInterest(userId);
/*			StringBuilder str=new StringBuilder("{");
			str.append("'mySum':'").append(NumberUtil.getNumber(mySum)).append("',");
			str.append("'myInvestsInterest':'").append(NumberUtil.getNumber(myInvestsInterest)).append("',");
			str.append("'balcance':'").append(NumberUtil.getNumber(balcance)).append("',");
			str.append("'frozen':'").append(NumberUtil.getNumber(frozen)).append("',");
			str.append("'dsje':'").append(NumberUtil.getNumber(dsje)).append("'");
			str.append("}");*/
			StringBuilder str=new StringBuilder("{");
			str.append("\"mySum\":\"").append(NumberUtil.getNumber(mySum)).append("\",");
			str.append("\"myInvestsInterest\":\"").append(NumberUtil.getNumber(myInvestsInterest)).append("\",");
			str.append("\"balcance\":\"").append(NumberUtil.getNumber(balcance)).append("\",");
			str.append("\"frozen\":\"").append(NumberUtil.getNumber(frozen)).append("\",");
			str.append("\"dsje\":\"").append(NumberUtil.getNumber(dsje)).append("\"");
			str.append("}");
			//System.out.println(str.toString());
			out.encryptResult(str.toString());
			out.sign();
			out.setResultCode(ResponseMsgKey.SUCCESS);
			out.setResultMsg(String.valueOf(AppConstants.rMsgProps.get(ResponseMsgKey.SUCCESS)));
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
	/**
	 * 应收（待收）本金
	 * 
	 * @return
	 */
	private double getReceivableCorpus(String userId) {
		String hql = "Select sum(corpus) from InvestRepay where time is null and invest.user.id = ? ";
		List<Object> result = ht.find(hql, userId);
		double money = 0;
		if (result != null && result.get(0) != null) {
			money = (Double) result.get(0);
		}
		return money;
	}
	/**
	 * 应收（待收）利息
	 * 
	 * @return
	 */
	public double getReceivableInterest(String userId) {
		String hql = "Select sum(interest+defaultInterest-fee) from InvestRepay where time is null and invest.user.id = ?";

		List<Object> result = ht.find(hql, userId);
		double money = 0;
		if (result != null && result.get(0) != null) {
			money = (Double) result.get(0);
		}
		return money;
	}
	/**
	 * 
	 * @param userId
	 * @return 获取个人已经投资成功的总的收益
	 */
	public double getInvestsInterest(String userId){
		StringBuilder hql =new StringBuilder("select sum(interest+defaultInterest-fee)");
		hql.append(" from InvestRepay where time is not null and invest.id in(select id");
		hql.append(" from Invest where user.id=?)");
		double money=0;
		List<Object> result=ht.find(hql.toString(),userId);
		if(result!=null&&result.get(0)!=null){
			money=(Double)result.get(0);
		}
		return money;
	}
}
