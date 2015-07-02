package com.esoft.ipspay.trusteeship;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class IpsPayConstans {
	
	private static Properties props;
	static {
		props = new Properties();
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("ipspay.properties"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("找不到ipspay.properties文件", e);
		} catch (IOException e) {
			throw new RuntimeException("读取ipspay.properties文件出错", e);
		}
	}
	
	public static final class OperationType {
		/**标的登记*/
		public static final String LOAN = "loan";
	}

	public static final class Config {
		/**
		 * 商户号
		 */
		 public static final String MER_CODE =  props.getProperty("mer_code");

		/**
		 * MD5证书
		 */
		 public static final String CERT =props.getProperty("cert");

		/**
		 * 3des base64 key
		 */
		 public static final String THREE_DES_BASE64_KEY =props.getProperty("three_des_base64_key");

		/**
		 * 3des iv(向量)
		 */
		 public static final String THREE_DES_IV = props.getProperty("three_des_iv");

		 /**
		 * 3des 加密方法／运算模式／填充模式
		 */
		public static final String THREE_DES_ALGORITHM = props.getProperty("three_des_algorithm");
		
		public static final String WEBSERVICE_URL = props.getProperty("webservice.url");

	}

	/**
	 * 请求地址
	 * 
	 * @author Administrator
	 * 
	 */
	public static final class RequestUrl {
		/**
		 * 开户
		 */
		 public static final String CREATE_IPS_ACCT =props.getProperty("url.create_ips_acct");
		/**
		 * 充值
		 */
		 public static final String RECHARGE =props.getProperty("url.recharge");

		/**
		 * 借款
		 */
		 public static final String LOAN =props.getProperty("url.loan");

		/**
		 * 投标
		 */
		 public static final String INVEST =props.getProperty("url.invest");
		
		/**
		 * 还款
		 */
		 public static final String REPAY =props.getProperty("url.repay");

		/**
		 * 提现
		 */
		 public static final String WITHDRAW_CASH =props.getProperty("url.withdraw_cash");

		 /**
		  * 债权转让
		  */
		 public static final String INVEST_TRANSFER =props.getProperty("url.invest_transfer");
		 
		 /**
		  * 自动投标签约
		  */
		 public static final String AUTO_INVEST_SIGN =props.getProperty("url.auto_invest_sign");
		 
		 
	}

	/**
	 * 返回地址（web）
	 */
	public static final class ResponseWebUrl {
		/**
		 * 地址前缀
		 */
		public static final String PRE_RESPONSE_URL = props.getProperty("pre_response_web_url");
	}

	/**
	 * 返回地址（server to server）
	 */
	public static final class ResponseS2SUrl {
		/**
		 * 地址前缀
		 */
		public static final String PRE_RESPONSE_URL = props.getProperty("pre_response_s2s_url");
	}
}
