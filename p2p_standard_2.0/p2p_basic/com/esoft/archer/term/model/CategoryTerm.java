package com.esoft.archer.term.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.esoft.archer.node.model.Node;

/**
 * CategoryTerm entity. 分类术语，node的分类
 * 
 */
@Entity
@Table(name = "category_term")
@NamedQueries({
		@NamedQuery(name = "CategoryTerm.findByType", query = "select c from CategoryTerm c where c.categoryTermType.id = ?"),
		@NamedQuery(name = "CategoryTerm.findByParentId", query = "select c from CategoryTerm c where c.parent.id = ?"),
		@NamedQuery(name = "CategoryTerm.getTermCountByParentId", query = "select count(c) from CategoryTerm c where c.parent.id =:pId"),
		@NamedQuery(name = "CategoryTerm.findByParentIdOrderBySeqNum", query = "select c from CategoryTerm c where c.parent.id =:pId order by c.seqNum asc") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class CategoryTerm implements java.io.Serializable {

	private static final long serialVersionUID = -4573430309029721341L;
	// Fields
	private String id;
	private CategoryTermType categoryTermType;
	private String name;
	private String thumb;
	private CategoryTerm parent;
	private String description;
	private Integer seqNum = 0;
	private List<CategoryTerm> children = new ArrayList<CategoryTerm>(0);
	private Set<Node> nodes = new HashSet<Node>(0);

	// Constructors

	/** default constructor */
	public CategoryTerm() {
	}

	public CategoryTerm(String id) {
		super();
		this.id = id;
	}

	public CategoryTerm(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/** minimal constructor */
	public CategoryTerm(String id, CategoryTermType categoryTermType) {
		this.id = id;
		this.categoryTermType = categoryTermType;
	}

	/** full constructor */
	public CategoryTerm(String id, CategoryTermType categoryTermType,
			String name, String description, Integer seqNum, Set<Node> nodes) {
		this.id = id;
		this.categoryTermType = categoryTermType;
		this.name = name;
		this.description = description;
		this.seqNum = seqNum;
		this.nodes = nodes;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		
		
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type", nullable = false)
	public CategoryTermType getCategoryTermType() {
		return this.categoryTermType;
	}

	public void setCategoryTermType(CategoryTermType categoryTermType) {
		this.categoryTermType = categoryTermType;
	}

	@Column(name = "name", length = 100)
	public String getName() {

		return this.name;
	}

	public String getTitle() {
		System.out.println(id);
		//String type = getId();
		//平台介绍
		if ("ptjs".equals(id)) {
			this.setTitle("平台介绍_新手指引");
			this.setName1("平台介绍");
			this.setDescription1("平台介绍可以让大家了解平台原理、平台操作流程等相关信息");
		//平台原理
		} else if ("pingtaiyuanli".equals(id)) {
			this.setTitle("平台原理_新手指引");
			this.setName1("平台原理");
			this.setDescription1("为大家介绍平台的基本原理");
		//我要投资
		} else if ("wytz".equals(id)) {
			this.setTitle("我要投资_新手指引");
			this.setName1("我要投资");
			this.setDescription1("为大家提供平台上我要投资的注意事项等相关方面信息");
		//好友账户
		} else if ("hyzh".equals(id)) {
			this.setTitle("好友账户_新手指引");
			this.setName1("好友账户");
			this.setDescription1("为大家介绍平台账户充值、提现等相关信息");
		//我要融资
		} else if ("faqijiekuan".equals(id)) {
			this.setTitle("我要融资_新手指引");
			this.setName1("我要融资");
			this.setDescription1("我要融资为大家介绍平台融资的相关详细信息。");
		//费率说明
		} else if ("feilvshuoming".equals(id)) {
			this.setTitle("资费说明_新手指引");
			this.setName1("资费说明");
			this.setDescription1("为大家介绍在平台上进行一系列交易时需要的费用。");
		//用户协议
		} else if ("yhxy".equals(id)) {
			this.setTitle(" 服务协议_新手指引");
			this.setName1("服务协议");
			this.setDescription1("为大家介绍好友来投网站的服务协议规定。");
		//好友保障
		} else if ("hybzjh".equals(id)) {
			this.setTitle("好友保障计划_安全保障");
			this.setName1("好友保障计划");
			this.setDescription1("好友保障计划为大家提供平台关于资金保障的相关计划。");
		//贷款审核流程
		} else if ("dkshlc".equals(id)) {
			this.setTitle("贷款审核流程_安全保障");
			this.setName1("贷款审核流程");
			this.setDescription1("对网站贷款审核流程进行详细的介绍");
		//用户安全意识
		} else if ("yhaqys".equals(id)) {
			this.setTitle("用户安全意识_安全保障");
			this.setName1(" 用户安全意识");
			this.setDescription1("介绍平台用户在安全方面应该注意的地方");
		//政策法规保障
		} else if ("zcfgbz".equals(id)) {
			this.setTitle("政策法规保障_安全保障");
			this.setName1("政策法规保障");
			this.setDescription1("对平台政策法规保障的相关介绍。");
		//资金安全保障
		} else if ("zjaqbz".equals(id)) {
			this.setTitle("资金安全保障_安全保障");
			this.setName1("资金安全保障");
			this.setDescription1("介绍网站投资资金安全保障的措施");
		//公司简介
		} else if ("gsjj".equals(id)) {
			this.setTitle("关于好友来投_关于我们");
			this.setName1("关于好友来投");
			this.setDescription1("好友来投网站平台的相关信息介绍");
		//联系我们
		} else if ("lianxiwomen".equals(id)) {
			this.setTitle("联系我们_关于我们");
			this.setName1("联系我们");
			this.setDescription1("介绍好友来投的各种联系方式。");
		//加入我们
		} else if ("jiaRuWoMen".equals(id)) {
			this.setTitle("加入我们_关于我们");
			this.setName1("加入我们");
			this.setDescription1("为大家介绍一下加入好友来投的渠道。");
		//公司证照
		} else if ("case".equals(id)) {
			this.setTitle("公司证照_关于我们");
			this.setName1("公司证照");
			this.setDescription1("给大家介绍好友来投公司证照的情况。");
		//合作伙伴
		} else if ("heZuoHuoBan1".equals(id)) {
			this.setTitle("合作伙伴_关于我们");
			this.setName1("合作伙伴");
			this.setDescription1("合作伙伴为大家与好友来投公司合作的盟友");
		//公司动态
		} else if ("dongtai".equals(id)) {
			this.setTitle("公司新闻_公司动态_好友来投");
			this.setName1("公司新闻 公司动态");
			this.setDescription1("公司动态栏目给大家提供公司新闻、公司动态等信息");
		//媒体报道
		} else if ("meitibaodao".equals(id)) {
			
			this.setTitle("媒体报道_好友来投");
			this.setName1("媒体报道");
			this.setDescription1("媒体报道栏目给大家提供新闻媒体对好友来投的相关报道");
		//发标预告
		} else if ("fabiaoyugao".equals(id)) {
		
			this.setTitle("发标公告_好友来投");
			this.setName1("发标公告");
			this.setDescription1("发标公告栏目给大家提供大量发标信息");
		//平台活动
		} else if ("wangzhangonggao".equals(id)) {

			this.setTitle("平台活动_好友来投");
			this.setName1("平台活动");
			this.setDescription1("平台活动栏目给大家提供平台的一些最新活动信息");
			//互联网金融	
		} else if ("hlwjr".equals(id)) {

			this.setTitle("互联网金融_互联网金融概念_互联网金融产品_互联网金融新闻_好友来投");
			this.setName1("互联网金融 互联网金融概念 互联网金融产品 互联网金融新闻");
			this.setDescription1("互联网金融栏目为大家提供互联网金融概念、互联网金融产品、互联网金融新闻等相关信息，让大家了解更多的互联网金融知识");
					
			//新闻政策
		} else if ("xwzc".equals(id)) {

			this.setTitle("网贷新闻_网贷政策_好友来投");
			this.setName1("网贷新闻 网贷政策");
			this.setDescription1("新闻政策栏目给大家提供网贷新闻、网贷政策等方面的内容，让大家能够及时的了解网贷方面的新闻政策");
			//行业动态
		} else if ("hydt".equals(id)) {

			this.setTitle(" 网贷动态_好友来投");
			this.setName1("网贷动态");
			this.setDescription1("行业动态栏目给大家提供网贷动态方面的信息，让大家能够及时的了解网贷行业的最新变动");
		//理财故事
		} else if ("lcgs".equals(id)) {
			this.setTitle("理财故事_投资理财的故事_p2p投资理财故事_好友来投");
			this.setName1("理财故事 投资理财的故事 p2p投资理财故事");
			this.setDescription1("理财故事频道为大家提供p2p投资理财的故事，大家可以从中吸取一些经验");
			
		//理财攻略
		} else if ("lcgl".equals(id)) {

			this.setTitle("理财攻略_p2p理财_理财技巧_好友来投");
			this.setName1("理财攻略 p2p理财 理财技巧 理财知识");
			this.setDescription1("理财攻略栏目为大家提供丰富的p2p理财攻略、理财技巧等知识，让大家在理财的路上走的更正确");
		
		//热门词条
		} else if ("rmct".equals(id)) {
			
			this.setTitle("网贷热门词条_好友来投");
			this.setName1("网贷热门词条");
			this.setDescription1("热门词条栏目为大家提供最新网贷热门词条，使大家对最新的网贷词汇不再陌生");
		//网络术语
		} else if ("wlsy".equals(id)) {
			
			this.setTitle(" 网络术语_网贷名词");
			this.setName1("网络术语 网贷术语");
			this.setDescription1("网络术语栏目对网贷术语进行详细介绍");	
		//标的类型
		} else if ("bdlx".equals(id)) {
			
			this.setTitle("标的类型_网贷名词");
			this.setName1("标的类型");
			this.setDescription1("标的类型栏目对网贷标的类型进行详细介绍");	
		//还款方式
		} else if ("hkfs".equals(id)) {
			
			this.setTitle("还款方式_网贷名词");
			this.setName1("还款方式");
			this.setDescription1("还款方式栏目对网贷还款方式进行详细介绍");
		//保障机制
		} else if ("bzjz".equals(id)) {
			
			this.setTitle("保障机制_网贷名词");
			this.setName1("保障机制");
			this.setDescription1("保障机制栏目对网贷保障机制进行详细介绍");	
		//平台高层
		} else if ("ptgc".equals(id)) {
			
			this.setTitle("平台高层_名人介绍");
			this.setName1(" 平台高层");
			this.setDescription1(" 平台高层栏目对平台高层人物进行详细的介绍");
		//网贷名人
		} else if ("wdmr".equals(id)) {
			
			this.setTitle("网贷名人_名人介绍");
			this.setName1("网贷名人");
			this.setDescription1("网贷名人网贷界的一些名人进行详细的介绍。");
		//行业专家
		} else if ("hyzj".equals(id)) {
			
			this.setTitle("行业专家_名人介绍");
			this.setName1("行业专家");
			this.setDescription1("行业专家栏目对网贷行业专家进行详细的介绍");
		//2015年
		} else if ("2015n".equals(id)) {
	
			this.setTitle("网贷事件_好友来投");
			this.setName1("网贷事件");
			this.setDescription1("网贷事件栏目给大家提供一些网贷方面发生的事情");
		//股票
		} else if (id.startsWith("gp")) {
			
			this.setTitle("股票_股票入门基础知识_股票行情_好友来投");
			this.setName1("股票 股票入门基础知识 股票行情");
			this.setDescription1("股票栏目为大家提供股票、股票入门基础知识、股票行情等相关股票知识");
		//理财产品	
		} else if (id.startsWith("lccp")) {
			
			this.setTitle(" 理财产品_理财宝_拉卡拉_余额宝_贝宝_好友来投");
			this.setName1("理财产品 理财宝 拉卡拉 余额宝 贝宝");
			this.setDescription1("理财产品栏目为大家介绍理财宝、拉卡拉、余额宝、贝宝等相关理财产品知识");
		//基金产品
		} else if (id.startsWith("jjcp")) {
		this.setTitle("基金产品_基金理财产品_基金投资_基金定投产品_好友来投");
		this.setName1("基金产品 基金理财产品 基金投资 基金定投产品");
		this.setDescription1("基金产品栏目为大家介绍基金产品、基金理财产品、基金定投产品等基金产品方面的知识。");	
		//基金
		} else if (id.startsWith("jj")) {
		this.setTitle("基金知识_基金知识入门_基金基础知识_好友来投");
		this.setName1("基金知识 基金知识入门 基金基础知识");
		this.setDescription1("基金知识栏目为大家提供基金知识入门、基金基础知识方面的知识");	
		//基金公司
		} else if (id.startsWith("jjgs")) {
		this.setTitle("基金公司_华夏基金管理有限公司_南方基金_哪个基金公司好_好友来投");
		this.setName1("基金公司 华夏基金管理有限公司 南方基金 哪个基金公司好");
		this.setDescription1("基金公司栏目为大家介绍华夏基金管理有限公司、南方基金、哪个基金公司好等有关基金公司方面的知识");	
		//外汇
		} else if (id.startsWith("wh")) {
		this.setTitle("外汇_炒外汇_外汇新手入门_外汇投资_好友来投");
		this.setName1("外汇 炒外汇 外汇新手入门 外汇投资");
		this.setDescription1("外汇知识栏目为大家介绍外汇、炒外汇、外汇新手入门、外汇投资等相关外汇知识。");
		//财经名人
		} else if (id.startsWith("cjmr")) {
		this.setTitle("财经名人_财经名人汇_财经界名人_好友来投");
		this.setName1("财经名人 财经名人汇 财经界名人");
		this.setDescription1("财经名人栏目为大家提供财经名人、财经名人汇、财经界名人等相关信息。");	
		//银行网点
		} else if (id.startsWith("yhwd")) {
		this.setTitle("银行网点_银行营业网点_好友来投");
		this.setName1("银行网点 银行营业网点");
		this.setDescription1("银行网点为大家提供银行营业网点等相关的信息");
		//信用卡
		} else if (id.startsWith("xyk")) {
		this.setTitle("信用卡_银行信用卡_办理信用卡_好友来投");
		this.setName1("信用卡 银行信用卡 办理信用卡");
		this.setDescription1("信用卡栏目为大家提供各大银行信用卡方面的相关信息");	
		//各国货币
		} else if (id.startsWith("gghb")) {
		this.setTitle("各国货币_世界各国货币_好友来投");
		this.setName1("各国货币 世界各国货币");
		this.setDescription1("各国货币为大家提供世界各国货币的信息。");	
		//融资租赁
		} else if (id.startsWith("rzzl")) {
		this.setTitle(" 融资租赁_金融租赁_股权融资_信托融资_好友来投");
		this.setName1("融资租赁 金融租赁 股权融资 信托融资");
		this.setDescription1("融资租赁栏目为大家提供融资租赁、金融租赁、股权融资、信托融资等相关知识。");
		//贵金属
		} else if (id.startsWith("gjs")) {
		this.setTitle("贵金属_贵金属投资_贵金属行情_工行贵金属_好友来投");
		this.setName1(" 贵金属 贵金属投资 贵金属行情 工行贵金属");
		this.setDescription1("贵金属栏目为大家提供贵金属、贵金属投资、贵金属行情等方面的知识与信息。");
		//证券公司
		} else if (id.startsWith("zqgs")) {
		this.setTitle("证券公司_好友来投");
		this.setName1("证券公司");
		this.setDescription1("证券公司栏目为大家提供证券公司方面的信息");	
		//期权交易
		} else if (id.startsWith("qqjy")) {
		this.setTitle("期权交易_期权交易_平台期权交易策略_好友来投");
		this.setName1("期权交易 期权交易 平台期权交易策略");
		this.setDescription1("期权交易栏目为大家提供期权交易、期权交易、平台期权交易策略等相关知识。");	
		//贷款
		} else if (id.startsWith("dk")) {
		this.setTitle("贷款_小额贷款_个人贷款_银行贷款_好友来投");
		this.setName1("贷款 小额贷款 个人贷款 银行贷款");
		this.setDescription1("贷款知识为大家提供贷款、小额贷款、个人贷款、银行贷款等的方面的知识。");
		//期货公司
		} else if (id.startsWith("qhgs")) {
		this.setTitle("期货公司_期货经纪公司_金属期货_期货行情_好友来投");
		this.setName1("期货公司 期货经纪公司");
		this.setDescription1("期货公司栏目为大家提供期货经纪公司方面的信息。");
		//金融
		} else if (id.startsWith("jr")) {
		this.setTitle("金融_金融界_金融投资_好友来投");
		this.setName1("金融 金融界 金融投资");
		this.setDescription1("金融知识为大家提供金融界、金融投资、什么是金融等有关金融方面的知识");	
		//银行理财
		} else if (id.startsWith("yhlc")) {
		this.setTitle(" 银行理财_银行理财产品_银行投资理财产品_银行理财产品排行榜_好友来投");
		this.setName1(" 银行理财_银行理财产品_银行投资理财产品_银行理财产品排行榜_好友来投");
		this.setDescription1("银行理财栏目为大家提供银行理财、银行理财产品、银行投资理财产品、银行理财产品排行榜等知识。");	
		//信托
		} else if (id.startsWith("xt")) {
		this.setTitle("信托_信托产品_信托是什么_好友来投");
		this.setName1("信托 信托产品 信托是什么");
		this.setDescription1("信托栏目为大家提供信托、信托产品、信托是什么等方面的知识");	
		//债券品种
		} else if (id.startsWith("zqpz")) {
		this.setTitle("债券_国债_企债_央债_短期融资券_好友来投");
		this.setName1("债券 国债 企债 央债 短期融资券");
		this.setDescription1("债券品种为大家提供债券、国债、企债、央债、短期融资券等相关知识。");	
		
		
		}
		return title;
	}
	

	public void setTitle(String title) {
		this.title = title;
	}

	private String name1;

	public void setName1(String name1) {
		this.name1 = name1;
	}

	private String title;

	public String getName1() {
		
		return this.name1;
	}

	private String description1;

	public String getDescription1() {
		
		return description1;
	}

	public void setDescription1(String description1) {

		this.description1 = description1;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	@Column(name = "thumb", length = 80)
	public String getThumb() {
		return thumb;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	public CategoryTerm getParent() {
		return parent;
	}

	public void setParent(CategoryTerm parent) {
		this.parent = parent;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
	public List<CategoryTerm> getChildren() {
		return children;
	}

	public void setChildren(List<CategoryTerm> children) {
		this.children = children;
	}

	@Lob
	@Column(name = "description", columnDefinition = "CLOB")
	public String getDescription() {

		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "seq_num")
	public Integer getSeqNum() {
		return this.seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "categoryTerms")
	public Set<Node> getNodes() {
		return this.nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

}