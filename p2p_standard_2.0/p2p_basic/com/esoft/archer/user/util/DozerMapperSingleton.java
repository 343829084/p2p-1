package com.esoft.archer.user.util;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * yong_jiang
 * 
 * dozer单例的wrapper.
 * 
 * dozer在同一jvm里使用单例即可,无需重复创建.
 * 但Dozer4.0自带的DozerBeanMapperSingletonWrapper必须使用dozerBeanMapping
 * .xml作参数初始化,因此重新实现.
 * 
 * 实现PO到VO的深层次复制
 */
public class DozerMapperSingleton {

	private static Mapper instance;

	private DozerMapperSingleton() {
		// shoudn't invoke.
	}

	public static synchronized Mapper getInstance() {
		if (instance == null) {
			instance = new DozerBeanMapper();
		}
		return instance;
	}

	public static void main(String args[]) {

	}

	public static void listCopy(List<Object> sourc, List dest, String className)
			throws Exception {

		if (null != sourc && null != dest) {
			dest.clear();
			for (Object obj : sourc) {
				Object obdest = Class.forName(className).newInstance();
				DozerMapperSingleton.getInstance().map(obj, obdest);
				dest.add(obdest);
			}
		}
	}

	
	
}
