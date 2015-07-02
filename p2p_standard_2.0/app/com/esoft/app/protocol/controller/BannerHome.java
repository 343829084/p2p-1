package com.esoft.archer.banner.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.banner.BannerConstants;
import com.esoft.archer.banner.model.Banner;
import com.esoft.archer.banner.model.BannerPicture;
import com.esoft.archer.common.controller.EntityHome;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.StringManager;

@Controller  
@RequestMapping("/blog") 
@Scope(ScopeType.REQUEST)
public class Btest extends EntityHome<Banner> {
	private static final long serialVersionUID = 2373410201504708160L;
	@Logger
	static Log log;
	private static final StringManager sm = StringManager
			.getManager(BannerConstants.Package);

	public BannerHome() {
		this.setUpdateView(FacesUtil.redirect(BannerConstants.View.BANNER_LIST));
	}

	@Override
	public Banner createInstance() {
		Banner banner = new Banner();
		banner.setPictures(new ArrayList<BannerPicture>());
		return banner;
	}

	@RequestMapping("save") 
	public String save() {
	  System.out.println("dddddddddddddddd");
	}
}
