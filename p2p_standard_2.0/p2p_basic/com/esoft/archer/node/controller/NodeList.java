package com.esoft.archer.node.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.model.LazyDataModel;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.common.model.PageModel;
import com.esoft.archer.node.NodeConstants;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.node.model.NodeType;
import com.esoft.archer.term.model.CategoryTerm;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

/**
 * 文章查询
 * 
 * @author wanghm
 * 
 */
@Component(value="nodeList")
@Scope(ScopeType.APPLICATION)
public class NodeList extends EntityQuery<Node>  {

	//private static final long serialVersionUID = 3446001352023091099L;

	static StringManager sm = StringManager.getManager(NodeConstants.Package);
	@Logger
	static Log log;
	private static final String[] RESTRICTIONS = {
			"node.id like #{nodeList.example.id}",
			"node.title like #{nodeList.example.title}",
			"node.nodeType.id = #{nodeList.example.nodeType.id}",
			"node in elements(term.nodes) and term.id = #{nodeList.example.categoryTerms[0].id}" };
	
	private static final String lazyModelCountHql = "select count(distinct node) from Node node left join node.categoryTerms term ";
	private static final String lazyModelHql = "select distinct node from Node node left join node.categoryTerms term";
	
	public void init(){
		setCountHql(lazyModelCountHql);
		setHql(lazyModelHql);

		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
//		addOrder("node.updateTime", DIR_DESC);
	}
	
	public NodeList() {
		init();
	}

	@Override
	protected void initExample() {
		Node node = new Node();
		List<CategoryTerm> terms = new ArrayList<CategoryTerm>(0);
		terms.add(new CategoryTerm());
		node.setCategoryTerms(terms);
		setExample(node);
	}

	/**
	 * 通过分类术语编号查询前创建时间后15篇文章
	 * 
	 * @param termId
	 * @return
	 */
	public PageModel<Node> getNodes(String termId) {
		return getNodes(termId, NodeConstants.DEFAULT_RESULT_SIZE);
	}

	public PageModel<Node> getNodes(String termId, int maxResults) {
		return getNodes(termId, 0, maxResults);
	}

	private String nodeTypeId = NodeConstants.DEFAULT_TYPE;

	public String getNodeTypeId() {
		return nodeTypeId;
	}

	public void setNodeTypeId(String nodeTypeId) {
		this.nodeTypeId = nodeTypeId;
	}

	@Override
	public LazyDataModel<Node> getLazyModel() {
		this.getExample().setNodeType(new NodeType(getNodeTypeId()));
		return super.getLazyModel();
	}

	/**
	 * 通过分类id找到该分类底下的文章
	 * @param termId 分类id
	 * @param firstResult 文章数据库起始位置
	 * @param maxResults 要找出多少篇文章
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public PageModel<Node> getNodes(final String termId, final int firstResult,
			final int maxResults) {

		
		int page = 0;
		if (StringUtils.isNotEmpty(FacesUtil.getParameter("page"))) {
			page = Integer.valueOf(FacesUtil.getParameter("page"));
		}
		final PageModel<Node> pageModel = new PageModel<Node>();
		pageModel.setPageSize(maxResults-firstResult);
		pageModel.setPage(page);

		// data
		List<Node> data = getHt().executeFind(
				new HibernateCallback<List<Node>>() {
					public List<Node> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query2 = null;

						Query query = null;
						if (StringUtils.isNotEmpty(termId)) {
							// 如果分类编号不为空
							query = session
									.getNamedQuery("Node.findNodeByTermOrderBySeqNumAndUpdateTime");
							query.setParameter("nodeTypeId", getNodeTypeId());
							query.setParameter("termId", termId);

							query2 = session
									.getNamedQuery("Node.getNodeCountByTermId");
							query2.setParameter("nodeTypeId", getNodeTypeId());
							query2.setParameter("termId", termId);
						} else {
							query = session
									.getNamedQuery("Node.findNodeOrderBySeqNumAndUpdateTime");
							query.setParameter("nodeTypeId", getNodeTypeId());
							query2 = session.getNamedQuery("Node.getNodeCount");
							query2.setParameter("nodeTypeId", getNodeTypeId());
						}
						query.setCacheable(true);
						query.setFirstResult(firstResult);
						query.setMaxResults(maxResults);

						query2.setCacheable(true);
						pageModel.setCount(Integer.valueOf(query2
								.uniqueResult().toString()));
						return query.list();
					}
				});
		pageModel.setData(data);
		return pageModel;

	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<Node> getPingInfo() {
		// data
		List<Node> data = getHt().executeFind(
				new HibernateCallback<List<Node>>() {
					public List<Node> doInHibernate(Session session)
							throws HibernateException, SQLException {

						//Query query =session.createSQLQuery(" select distinct node0_.id as id15_, node0_.create_time as create2_15_, node0_.description as descript3_15_, node0_.keywords as keywords15_, node0_.language as language15_, node0_.body as body15_, node0_.node_type as node14_15_, node0_.seq_num as seq6_15_, node0_.status as status15_, node0_.subtitle as subtitle15_, node0_.thumb as thumb15_, node0_.title as title15_, node0_.update_time as update11_15_, node0_.creator as creator15_, node0_.last_modify_user as last16_15_, node0_.version as version15_, node0_1_.characteristics as characte1_26_, case when node0_1_.id is not null then 1 when node0_.id is not null then 0 end as clazz_ from node node0_ left outer join product node0_1_ on node0_.id=node0_1_.id, category_term categoryte1_ where date(create_time)=date_sub(curdate(),interval 1 day)");
						Query query=session.getNamedQuery("Node.pingInfo");
						//query.setCacheable(true);
						return (List<Node>)query.list();
					}
				});
		return data;
	}
	
	public Node getNodeById(String id){
		return getHt().get(Node.class, id);
	}

}
