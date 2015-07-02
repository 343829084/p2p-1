package com.esoft.ipspay.user.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import com.esoft.archer.user.controller.UserHome;
import com.esoft.archer.user.model.User;
import com.esoft.ipspay.user.service.impl.IpsPayCreateAccountOperation;

public class IpsPayUserHome extends UserHome {

	@Resource
	IpsPayCreateAccountOperation createAccountOperation;

	@Override
	public String getInvestorPermission() {
		try {
			createAccountOperation.createOperation(this.getInstance(),
					FacesContext.getCurrentInstance());
		} catch (IOException e) {
			throw new RuntimeException("unexpected invocation", e);
		}
		return null;
	}
	
	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}
}
