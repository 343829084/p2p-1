package com.esoft.zcxy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ZcxyConstants {
	private static Properties props;
	static {
		props = new Properties();
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("zcxy.properties"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("找不到zcxy.properties文件", e);
		} catch (IOException e) {
			throw new RuntimeException("读取zcxy.properties文件出错", e);
		}
	}

	public static final class Config {
		public static String KEY = props.getProperty("key");
		public static String USERNAME = props.getProperty("username");
		public static String PASSWORD = props.getProperty("password");
		public static String DATA_TYPE = props.getProperty("data_type");
		public static String URL = props.getProperty("url");
	}
}
