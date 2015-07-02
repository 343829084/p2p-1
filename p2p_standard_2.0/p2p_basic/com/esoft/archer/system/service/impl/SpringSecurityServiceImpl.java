package com.esoft.archer.system.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import com.esoft.archer.system.service.SpringSecurityService;
import com.esoft.archer.user.service.impl.MyJdbcUserDetailsManager;

@Service(value = "springSecurityService")
public class SpringSecurityServiceImpl implements SpringSecurityService {

	@Autowired
	SessionRegistry sessionRegistry;

	/**
	 * 需要刷新权限的用户名HashSet
	 */
	private static HashSet<String> unrgas = new HashSet<String>();

	@Resource
	HibernateTemplate ht;

	@Resource
	MyJdbcUserDetailsManager myJdbcUserDetailsManager;

	private void addRefreshAuthoritiesByLoginedUsername(String username,
			Collection<? extends GrantedAuthority> authorities) {
		List<Object> loggedUsers = sessionRegistry.getAllPrincipals();
		for (Object principal : loggedUsers) {
			if (principal instanceof org.springframework.security.core.userdetails.User) {
				org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) principal;
				if (username.equals(u.getUsername())) {
					// List<GrantedAuthority> authorities = new
					// ArrayList<GrantedAuthority>();
					// authorities.addAll(u.getAuthorities());
					// authorities.add(new GrantedAuthorityImpl("INVESTOR"));
					org.springframework.security.core.userdetails.User userN = new org.springframework.security.core.userdetails.User(
							u.getUsername(), u.getPassword(), u.isEnabled(),
							u.isAccountNonExpired(),
							u.isCredentialsNonExpired(),
							u.isAccountNonLocked(), authorities);
					if (!unrgas.contains(username)) {
						unrgas.add(username);
					}
					List<SessionInformation> sessionsInfo = sessionRegistry
							.getAllSessions(principal, false);
					if (null != sessionsInfo && sessionsInfo.size() > 0) {
						for (SessionInformation sessionInformation : sessionsInfo) {
							sessionRegistry
									.removeSessionInformation(sessionInformation
											.getSessionId());
							sessionRegistry.registerNewSession(
									sessionInformation.getSessionId(), userN);
						}
					}
				}
			}
		}
	}

	@Override
	public void refreshLoginUserAuthorities(String userId) {
		List<GrantedAuthority> authorities = myJdbcUserDetailsManager
				.getUserAuthorities(userId);
		// 刷新登录用户权限
		addRefreshAuthoritiesByLoginedUsername(userId, authorities);

	}

	@Override
	public HashSet<String> getUsersNeedRefreshGrantedAuthorities() {
		return unrgas;
	}
}
