package com.esoft.archer.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.model.AuthInfo;
import com.esoft.archer.user.model.UserBill;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-13 下午4:33:32
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-13 wangzhi 1.0
 */
@Service("authInfoBO")
public class AuthInfoBO {
	@Resource
	private HibernateTemplate ht;

	// 存储一个来源，查询时候，来源、目标、类型，三个作为联合主键。
	public AuthInfo get(String source, String target, String authType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AuthInfo.class);
		if (source == null) {
			criteria.add(Restrictions.isNull("authSource"));			
		} else {
			criteria.add(Restrictions.eq("authSource", source));						
		}
		if (target == null) {
			criteria.add(Restrictions.isNull("authTarget"));			
		} else {
			criteria.add(Restrictions.eq("authTarget", target));						
		}
		if (authType == null) {
			criteria.add(Restrictions.isNull("authType"));			
		} else {
			criteria.add(Restrictions.eq("authType", authType));						
		}
		List<AuthInfo> ais = ht.findByCriteria(criteria);
		
		if (ais.size() > 1) {
			// 找到多个，抛异常。
			throw new DuplicateKeyException("authSource:" + source
					+ " authTarget:" + target + " autyType:" + authType
					+ "  duplication!");
		}
		if (ais.size() == 0) {
			return null;
		}
		AuthInfo ai = ais.get(0);
		return ai;
	}

	public AuthInfo get(String source, String target, String authType,
			String authCode) {
		AuthInfo ai = get(source, target, authType);
		if (ai.getAuthCode().equals(authCode)) {
			return ai;
		}
		return null;
	}

	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void save(AuthInfo authInfo) {
		if (StringUtils.isEmpty(authInfo.getAuthCode())) {
			throw new IllegalArgumentException(
					"authInfo.authCode can not be empty!");
		}
		if (StringUtils.isEmpty(authInfo.getAuthTarget())) {
			throw new IllegalArgumentException(
					"authInfo.authTarget can not be empty!");
		}
		if (StringUtils.isEmpty(authInfo.getAuthType())) {
			throw new IllegalArgumentException(
					"authInfo.authType can not be empty!");
		}

		ht.bulkUpdate(
				"delete AuthInfo ai where ai.authTarget=? and ai.authType=?",
				new String[] { authInfo.getAuthTarget(), authInfo.getAuthType() });
		ht.save(authInfo);
	}
	
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void activate(AuthInfo authInfo) {
		authInfo.setStatus(CommonConstants.AuthInfoStatus.ACTIVATED);
		ht.update(authInfo);
	}

	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void activate(String source, String target, String authType) {
		AuthInfo authInfo = get(source, target, authType);
		authInfo.setStatus(CommonConstants.AuthInfoStatus.ACTIVATED);
		ht.update(authInfo);
	}
}
