package com.esoft.upgrade.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Component
@Scope(ScopeType.VIEW)
public class UpgradeHome {

	@Resource
	JdbcTemplate jt;
	
	@Logger
	Log log;

	/** 需要清除的表名 */
	private Set<String> clearTables = Sets.newHashSet("config", "config_type",
			"loan_type", "url_mapping", "user_message_node",
			"user_message_template");

	/**
	 * 清除和新增时候，排除的数据 key:tableName, value:id
	 */
	private static Map<String, String> excludes = Maps.newHashMap();
	static {
		excludes.put(
				"config",
				"'password_fail_max_times','schedule.enable_auto_repayment','schedule.enable_refresh_trusteeship','site_dns','site_name','site_phone','site_slogan'");
	}

	@Transactional
	public void execute() {
		jt.execute("SET FOREIGN_KEY_CHECKS=0");
		initTableStructure();
		clearOldData();
		initData();
		replaceData();
		generateNewData();
	}
	
	/**
	 * 清理旧数据
	 */
	private void clearOldData() {
		log.debug("clearData start--------------------------");
		for (String tn : clearTables) {
			String sql = "delete from " + tn + " where id not in("
					+ StringUtils.defaultString(excludes.get(tn), "''") + ")";
			log.debug(sql);
			jt.execute(sql);
		}
		// 清理管理菜单
		jt.execute("delete from menu where type='Management'");
		log.debug("clearData end----------------------------");
	}

	/**
	 * 初始化表结构
	 */
	private void initTableStructure() {
		log.debug("initTableStructure start--------------------------");
		try {
			List<String> sqls = FileUtils.readLines(new File(UpgradeHome.class
					.getClassLoader().getResource("initTableStructure.sql").getFile()),
					"utf-8");
			for (String sql : sqls) {
				log.debug(sql);
				if (sql.startsWith("--") || StringUtils.isEmpty(sql)) {
					continue;
				}
				jt.execute(sql);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.debug("initTableStructure end----------------------------");
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		log.debug("initData start--------------------------");
		try {
			List<String> sqls = FileUtils.readLines(new File(UpgradeHome.class
					.getClassLoader().getResource("initData.sql").getFile()),
					"utf-8");
			for (String sql : sqls) {
				log.debug(sql);
				if (sql.startsWith("--") || StringUtils.isEmpty(sql)) {
					continue;
				}
				try {
					jt.execute(sql);
				} catch (DuplicateKeyException dke) {
					log.debug("duplicateKey: "+sql);
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.debug("initData end----------------------------");
	}

	/**
	 * 替换已有数据
	 */
	private void replaceData() {
		log.debug("replaceData start--------------------------");
		try {
			List<String> sqls = FileUtils.readLines(new File(UpgradeHome.class
					.getClassLoader().getResource("replaceData.sql").getFile()),
					"utf-8");
			for (String sql : sqls) {
				log.debug(sql);
				if (sql.startsWith("--") || StringUtils.isEmpty(sql)) {
					continue;
				}
				jt.execute(sql);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.debug("replaceData end----------------------------");
	}

	/**
	 * 生成新数据
	 */
	private void generateNewData() {

	}

}
