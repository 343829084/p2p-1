package com.esoft.jdp2p.invest.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 公司代投工具类
 * @author Administrator
 *
 */
public class CompanyUtil {
	
	/**
	 * 初始化公司ID
	 * @return
	 */
	public static List<String> getCompanyList(){
		List<String> list = new ArrayList<String>();
		list.add("admin");
		list.add("cherry10046");
		list.add("cherry1004");
		return list;
	}
	
	/**
	 * 获得公司随机ID
	 * @return
	 */
	public static String getRandomStr(){
		List<String> strList = getCompanyList();
		int i = new Random().nextInt(strList.size()-1);
		return strList.get(i);
	}

}
