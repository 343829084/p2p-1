package com.esoft.archer.node.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.lucene.LuceneConstants;
import com.esoft.archer.lucene.model.Indexing;
import com.esoft.archer.lucene.service.LuceneService;
import com.esoft.archer.menu.controller.MenuHome;
import com.esoft.archer.menu.model.Menu;
import com.esoft.archer.node.NodeConstants;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.node.model.NodeBody;
import com.esoft.archer.node.model.NodeType;
import com.esoft.archer.node.service.NodeService;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.term.model.CategoryTerm;
import com.esoft.archer.theme.controller.TplVars;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.ImageUploadUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.VIEW)
public class NodeHome extends EntityHome<Node> implements Serializable {
	private static final long serialVersionUID = 1736551447868451250L;
	@Logger
	static Log log;
	@Resource
	private LuceneService luceneService;
	@Resource
	private LoginUserInfo loginUserInfo;
	@Resource
	private HibernateTemplate ht;

	@Resource
	private MenuHome menuHome;
	
	@Resource
	private TplVars tplVars;
	
	
	
	@Resource
	private NodeService nodeService;
	private static final StringManager sm = StringManager
			.getManager(NodeConstants.Package);
	private static final StringManager smCommon = StringManager
			.getManager(CommonConstants.Package);

	private Integer weight;

	public NodeHome() {
		setUpdateView(FacesUtil.redirect(NodeConstants.View.NODE_LIST));
		setDeleteView(FacesUtil.redirect(NodeConstants.View.NODE_LIST));
	}

	public LuceneService getLuceneService() {
		return luceneService;
	}

	public void setLuceneService(LuceneService luceneService) {
		this.luceneService = luceneService;
	}

	public Integer getWeight() {
		if (weight == null) {
			weight = 1;
		}
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getParentTile() {

		String parentTile = "";
		CategoryTerm cat = getInstance().getCategoryTerms().get(0);
		if (null != cat) {
			if (null != cat.getParent()) {

				parentTile = cat.getParent().getName();
			} else {

				parentTile = cat.getName();
			}

		}
		System.out.println("parentTile:" + parentTile);
		return parentTile;
	}

	public StringBuffer navStringAll = new StringBuffer();

	private String navTitle = "";
	private String navId = "";
	private Menu menu = null;

	public String getNowTitle(CategoryTerm cat) {

		if (null != cat) {

		
			if(null!=cat.getParent()){
				
				fillHref(navStringAll, cat);
				getNowTitle(cat.getParent());
			}else{
				fillHref(navStringAll, cat);
			}
			
		} 

		return navStringAll.toString();
	}

	public String getMenuParentId(){
		
	    String menuParent="";
		CategoryTerm cat = getInstance().getCategoryTerms().get(0);
		if(null!=cat){
		
			Menu menu = menuHome.getMenuById(cat.getId());
			if(null!=menu){
				Menu  parent=menu.getParent();
				if(null!=parent){
					menuParent=parent.getId();
				}else{
					menuParent=menu.getId();
				}
			}
		}
		System.out.println("menuParent:"+menuParent);
		return menuParent;
	}
	private void fillHref(StringBuffer navStringAll, CategoryTerm cat) {
		String url = "";
		if (null != cat && null != navStringAll) {

			Menu menu = menuHome.getMenuById(cat.getId());
			if (null != menu) {
		
				url = menu.getUrl();
				if (!StringUtils.isBlank(url)) {
          
					navStringAll.insert(0,
							"<a class='color57 fontsize14' href='"+getComponentsPath()+url+"'>"+cat.getName()+"</a>>>");

				}
			}
		}

	}

	public String getRootTile() {

		String rootTile = "";
		CategoryTerm cat = getInstance().getCategoryTerms().get(0);
		if (null != cat) {
			rootTile=getNowTitle(cat);
			System.out.println(rootTile);
		}
		getMenuParentId();
		return rootTile;
	}

	@Override
	protected Node createInstance() {
		Node node = new Node();
		node.setNodeType(new NodeType());
		node.setNodeBody(new NodeBody());
		node.setSeqNum(0);
		// node.setCategoryTerms(new HashSet<CategoryTerm>());
		return node;
	}

	// FIXME: 这是一个死循环
	@Override
	protected void initInstance() {
		super.initInstance();
		/*
		 * if (isIdDefined()) {
		 * //setInstance(getBaseService().get(getEntityClass(), getId()));
		 * //getInstance().getNodeBodyHistories(); } else {
		 * setInstance(createInstance()); }
		 */
	}

	@Override
	@Transactional(readOnly = false)
	public String save() {
		Node node = getInstance();
		String loginUserId = loginUserInfo.getLoginUserId();
		User user = getBaseService().get(User.class, loginUserId);
		if (user == null) {
			log.warn("save node: " + smCommon.getString("loginUserIsNull"));
			FacesUtil.addErrorMessage(smCommon.getString("loginUserIsNull"));
			return null;
		}
		node.setUserByLastModifyUser(user);
		node.setUpdateTime(new Date());

		// addIndex(node);
		// 判断id重复
		if (!isIdDefined()) {// create instance
			if (StringUtils.isEmpty(getId())) {
				setId(getInstance().getId());
			}
			node.setUserByCreator(user);
			node.setCreateTime(new Date());
			if (null != getBaseService().get(getEntityClass(), getId())) {
				FacesUtil.addErrorMessage(commonSM.getString(
						"entityIdHasExist", getId()));
				setId(null);
				getInstance().setId(null);
				return null;
			}
		}
		nodeService.save(getInstance());
		FacesUtil.addInfoMessage(getSaveSuccessMessage());
		return getSaveView();
	}

	public void handleFileUpload(FileUploadEvent event) {
		UploadedFile file = event.getFile();
		InputStream is = null;
		try {
			is = file.getInputstream();
			this.getInstance().setThumb(
					ImageUploadUtil.upload(is, file.getFileName()));
		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage(sm.getString("imageUploadFail"));
			return;
		}

	}

	/**
	 * 获取相同类型的上一个内容。按照序号、时间排序
	 * 
	 * @param nodeId
	 * @param termId
	 * @return
	 */
	public Node getPreviousNode(final String nodeId, final String termId) {
		Node nodeOri = getBaseService().get(Node.class, nodeId);
		if (nodeOri == null) {
			return null;
		}
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(Node.class);
		detachedCriteria.add(Restrictions.eq("nodeType.id", getNodeTypeId()));
		detachedCriteria.createAlias("categoryTerms", "term").add(
				Restrictions.eq("term.id", termId));
		if (nodeOri.getSeqNum() != null) {
			detachedCriteria.add(Restrictions.or(Restrictions.gt("seqNum",
					nodeOri.getSeqNum()), Restrictions.and(
					Restrictions.eq("seqNum", nodeOri.getSeqNum()),
					Restrictions.gt("updateTime", nodeOri.getUpdateTime()))));
		} else {
			detachedCriteria.add(Restrictions.gt("updateTime",
					nodeOri.getUpdateTime()));
		}
		detachedCriteria.addOrder(Order.asc("seqNum")).addOrder(
				Order.asc("updateTime"));
		List<Node> nodes = ht.findByCriteria(detachedCriteria, 0, 1);
		if (nodes.size() > 0) {
			return nodes.get(0);
		}
		return null;
	}

	private String nodeTypeId = NodeConstants.DEFAULT_TYPE;

	public String getNodeTypeId() {
		return nodeTypeId;
	}

	/**
	 * 获取相同类型的下一个内容。按照序号、时间排序
	 * 
	 * @param nodeId
	 * @param termId
	 * @return
	 * 
	 */
	public Node getNextNode(final String nodeId, final String termId) {
		Node nodeOri = getBaseService().get(Node.class, nodeId);
		if (nodeOri == null) {
			return null;
		}
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(Node.class);
		detachedCriteria.add(Restrictions.eq("nodeType.id", getNodeTypeId()));
		detachedCriteria.createAlias("categoryTerms", "term").add(
				Restrictions.eq("term.id", termId));
		if (nodeOri.getSeqNum() != null) {

			detachedCriteria.add(Restrictions.or(Restrictions.lt("seqNum",
					nodeOri.getSeqNum()), Restrictions.and(
					Restrictions.eq("seqNum", nodeOri.getSeqNum()),
					Restrictions.lt("updateTime", nodeOri.getUpdateTime()))));
		} else {
			detachedCriteria.add(Restrictions.lt("updateTime",
					nodeOri.getUpdateTime()));
		}
		detachedCriteria.addOrder(Order.desc("seqNum")).addOrder(
				Order.desc("updateTime"));
		List<Node> nodes = ht.findByCriteria(detachedCriteria, 0, 1);
		if (nodes.size() > 0) {
			return nodes.get(0);
		}
		return null;
	}

	private String getComponentsPath(){
		
		
		return tplVars.getContextPath();
	}
	private void addIndex(Node node) {
		// FIXME:建立一个关于索引的service,搞个索引的模块。
		// 封装一个索引
		Indexing indexing = new Indexing();
		indexing.setId(node.getId());
		indexing.setTitle(node.getTitle());
		indexing.setAuthor(node.getUserByCreator().getUsername());
		indexing.setCreateTime(node.getCreateTime());
		indexing.setContent(node.getNodeBody().getBody());
		// FIXME:getWeight() 用途？？？
		// 添加索引
		luceneService.createNewIndex(indexing,
				LuceneConstants.LUCENE_INDEX_PATH, getWeight());

		// FacesUtil.addInfoMessage(commonSM.getString("saveSuccessMessage",
		// this
		// .getInstance().getId()));
	}

	@Override
	@Transactional(readOnly = false)
	public String delete() {
		if (log.isInfoEnabled()) {
			log.info(sm.getString("log.info.deleteNode", (FacesUtil
					.getExpressionValue("#{loginUserInfo.loginUserId}")),
					new Date(), getId()));
		}

		// 删除文章的索引, 先删除文章
		return super.delete();
		// luceneService.deleteIndex(getId(),
		// LuceneConstants.LUCENE_INDEX_PATH);
		// return null;
	}

	/**
	 * 给当前instance添加term
	 * 
	 * @param termId
	 */
	public void addTerm(String termId) {
		if (StringUtils.isNotEmpty(termId)) {
			CategoryTerm term = getBaseService()
					.get(CategoryTerm.class, termId);
			if (term != null) {
				this.getInstance().getCategoryTerms().add(term);
			}
		}
	}

}
