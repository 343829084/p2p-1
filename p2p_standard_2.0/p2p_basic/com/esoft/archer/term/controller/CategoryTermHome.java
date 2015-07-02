package com.esoft.archer.term.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.annotations.OnDelete;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.menu.controller.MenuHome;
import com.esoft.archer.menu.model.Menu;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.term.TermConstants;
import com.esoft.archer.term.model.CategoryTerm;
import com.esoft.archer.term.model.CategoryTermType;
import com.esoft.archer.theme.controller.TplVars;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.VIEW)
public class CategoryTermHome extends EntityHome<CategoryTerm> {

	@Logger
	static Log log;
	@Resource
	HibernateTemplate ht;
	private static final StringManager sm = StringManager
			.getManager(TermConstants.Package);

	public CategoryTermHome() {
		setUpdateView(FacesUtil.redirect(TermConstants.View.TERM_LIST));
	}

	public CategoryTerm getTermById(String id) {
		CategoryTerm instance = getBaseService().get(getEntityClass(), id);
		return instance;
	}

	@Override
	@Transactional(readOnly = false)
	public String delete() {

		// if (log.isInfoEnabled()) {
		// log.info(sm.getString("log.info.deleteTerm",
		// (FacesUtil
		// .getExpressionValue("#{loginUserInfo.loginUserId}").toString()), new
		// Date(), getId()));
		// }
		Set<Node> nodeSets = getInstance().getNodes();
		List<CategoryTerm> ct = getInstance().getChildren();
		if ((nodeSets != null && nodeSets.size() > 0) || ct.size() > 0) {
			// log.info(sm.getString("log.info.deleteTermUnccessful",
			// (FacesUtil
			// .getExpressionValue("#{loginUserInfo.loginUserId}").toString()),
			// new Date(), getId()));
			FacesUtil.addErrorMessage("删除失败，请先删除该分类下的所有分类或文章。");
			return null;
		} else {
			return super.delete();
		}
	}

	@Transactional(readOnly = false)
	public String save() {

		// 判断父菜单是否是自己本身
		boolean parentIsOneself = false;
		CategoryTerm term = getInstance();
		while (term.getParent() != null) {

			if (StringUtils.equals(getInstance().getId(), term.getParent()
					.getId())) {
				parentIsOneself = true;
				break;
			}

			term = term.getParent();
		}

		if (parentIsOneself) {
			FacesUtil.addWarnMessage(sm.getString("parentCanNotBeItself"));
			return null;
		}

		return super.save();
	}
	
	
	private String id;
	private String Name1;
	private String keywords;
	private String description1;
	private String title;
	
	public String getDescription1() {
		return description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName1() {
		return Name1;
	}

	public void setName1(String name1) {
		Name1 = name1;
	}

	
	

	public String getParentTile() {

		String parentTile = "";
		CategoryTerm cat = getInstance();
		if (null != cat) {
			if (null != cat.getParent()) {

				parentTile = cat.getParent().getName();
			} else {

				parentTile = cat.getName();
				
					
				
				
			}

		}
		//System.out.println("parentTile:" + parentTile);
		return parentTile;
	}
	
	

	public void setTitle(String title) {
		this.title = title;
	}
	
	public StringBuffer navStringAll = new StringBuffer();
	@Resource
	private MenuHome menuHome;

	@Resource
	private TplVars tplVars;
	private String navTitle = "";
	private String navId = "";
	private Menu menu = null;

	public String getNowTitle(CategoryTerm cat) {

		if (null != cat) {

			if (null != cat.getParent()) {

				fillHref(navStringAll, cat);
				getNowTitle(cat.getParent());
			} else {
				fillHref(navStringAll, cat);
			}

		}
//System.out.println(navStringAll);
		return navStringAll.toString();
	}

	private void fillHref(StringBuffer navStringAll, CategoryTerm cat) {
		String url = "";
		if (null != cat && null != navStringAll) {

			Menu menu = menuHome.getMenuById(cat.getId());
			if (null != menu) {
				url = menu.getUrl();

				if (!StringUtils.isBlank(url)) {

					navStringAll.insert(0, "<a href='" + getComponentsPath()
							+ url + "'>" + cat.getName() + "</a>>>");

				}
			}
		}

	}
	
	public String getMenuParentId() {
		
		String menuParent = "";
		CategoryTerm cat = getInstance();
		if (null != cat) {

			Menu menu = menuHome.getMenuById(cat.getId());
			if (null != menu) {
				Menu parent = menu.getParent();
				if (null != parent) {
					menuParent = parent.getId();
				} else {
					menuParent = menu.getId();
				
				}
			}
		}
		System.out.println("menuParent:" + menuParent);
	
		
		return menuParent;
	}
	
	
	
	

	private String getComponentsPath() {

		return tplVars.getContextPath();
	}

	public String getRootTile() {

		String rootTile = "";
		CategoryTerm cat = getInstance();
		if (null != cat) {
			rootTile = getNowTitle(cat);
			//System.out.println(rootTile);
		}
		return rootTile;
	}
	
	public StringBuffer SEOStringAll = new StringBuffer();
	public String getSEOTitleString(CategoryTerm cat) {
		
		if (null != cat) {
			if(null!=cat.getParent()){
				plusSEOString(SEOStringAll, cat,TermConstants.View.CONNECTOR_TITLE);
				
				getSEOTitleString(cat.getParent());
				//System.out.println(cat.getDescription());
			}else{
				plusSEOString(SEOStringAll, cat,TermConstants.View.CONNECTOR_TITLE);
			}
		} 
		return SEOStringAll.toString();
	}
	
	
	public StringBuffer SEOKeywordsAll = new StringBuffer();
	public String getSEOkeywordsString(CategoryTerm cat) {
		if (null != cat) {
			if(null!=cat.getParent()){
				plusSEOString(SEOKeywordsAll, cat,TermConstants.View.CONNECTOR_KEYWORDS);
				
				getSEOkeywordsString(cat.getParent());
			}else{
				plusSEOString(SEOKeywordsAll, cat,TermConstants.View.CONNECTOR_KEYWORDS);
			}
		} 
		return SEOKeywordsAll.toString();
	}
	
	public StringBuffer SEODescpritionAll = new StringBuffer();
	public String getSEOdescriptionString(CategoryTerm cat) {
		if (null != cat) {
			if(null!=cat.getParent()){
				plusSEOString(SEODescpritionAll, cat,TermConstants.View.CONNECTOR_DESCRIPTION);
				getSEOdescriptionString(cat.getParent());
			}else{
				plusSEOString(SEODescpritionAll, cat,TermConstants.View.CONNECTOR_DESCRIPTION);
			}
		} 
		return SEODescpritionAll.toString();
	}
	
	private void plusSEOString(StringBuffer navStringAll, CategoryTerm cat,String connector) {
		if (null != cat && null != navStringAll) {
					navStringAll.append(
							cat.getName()+connector);
		}
	}

}
