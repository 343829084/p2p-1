package com.esoft.archer.theme.controller;

import org.apache.commons.logging.Log;
import org.primefaces.expression.impl.ThisExpressionResolver;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.config.ConfigConstants;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.borrower.BorrowerConstant.Url;

/**
 * SiteVars，通过JSF EL 表达式调用方法#{siteVars.siteName}，
 * 该类主要是包含了一些网站的信息，比如网站标题、名称、口号等信息。
 * 这些信息是通过数据库存储的详细请参见 config模块。
 * @author wanghm
 *
 */
@Component
@Scope(ScopeType.SESSION)
public class SiteVars implements java.io.Serializable{
	private static final long serialVersionUID = -7089517277500127277L;
	
	@Logger static Log log ;
	
	private final static String SITE_NAME_EL = 
		"#{configHome.getConfigValue('"+ConfigConstants.Website.SITE_NAME+"')}";
	private final static String SITE_SLOGAN_EL = 
		"#{configHome.getConfigValue('"+ConfigConstants.Website.SITE_SLOGAN+"')}";
	private final static String SITE_DNS_EL = 
		"#{configHome.getConfigValue('"+ConfigConstants.Website.SITE_DNS+"')}";
	//index.htm
	private String siteDns,siteName ,siteSlogan;
	private  String keywords1;
	private  String description;
	private  String tittle3;
	static final  String FRONT_PATTERN1 = "(.*?)(p2p/loanList.htm)(.*?)";
	static final  String FRONT_PATTERN2 = "(.*?)(/loanList.htm)(.*?)";
	static  final  String FRONT_PATTERN3 = "(.*?)(/loan_transfer.htm)(.*?)";
	static  final  String FRONT_PATTERN4 = "(.*?)(/applyLoan.htm)(.*?)";
	static final  String FRONT_PATTERN5 = "(.*?)(/applyLoan_enterprise.htm)(.*?)";
	static final  String FRONT_PATTERN6 = "(.*?)(/index.htm)(.*?)";
	static final  String FRONT_PATTERN7 = "(.*?)(/term.htm)(.*?)";
	static final  String FRONT_PATTERN8 = "(.*?)(/p2p/loanerPersonInfo.htm)(.*?)";
	static final  String FRONT_PATTERN9 = "(.*?)(/p2p/loanerAdditionInfo.htm)(.*?)";
	static final  String FRONT_PATTERN10 = "(.*?)(/p2p/loanerAuthentication.htm)(.*?)";
	static final  String FRONT_PATTERN11 = "(.*?)(/user/myCashFlow.htm)(.*?)";
	static final  String FRONT_PATTERN12 = "(.*?)(/user/recharge.htm)(.*?)";
	static final  String FRONT_PATTERN13 = "(.*?)(/user/withdraw.htm)(.*?)";
	static final  String FRONT_PATTERN14 = "(.*?)(/user/invest/repaying.htm)(.*?)";
	static final  String FRONT_PATTERN15 = "(.*?)(/user/transfer/purchased.htm)(.*?)";
	static final  String FRONT_PATTERN16 = "(.*?)(/user/autoInvest.htm)(.*?)";
	static final  String FRONT_PATTERN17 = "(.*?)(/user/loan/repaying.htm)(.*?)";
	static final  String FRONT_PATTERN18 = "(.*?)(/user/loan/applying-p2p.htm)(.*?)";
	static final  String FRONT_PATTERN19 = "(.*?)(/user/get_investor_permission.htm)(.*?)";
	static final  String FRONT_PATTERN20 = "(.*?)(/user/accountSafe.htm)(.*?)";
	static final  String FRONT_PATTERN21 = "(.*?)(/user/messages.htm)(.*?)";
	static final  String FRONT_PATTERN22 = "(.*?)(/user/messageTypeSet.htm)(.*?)";
	static final  String FRONT_PATTERN23 = "(.*?)(/user/my_point_history.htm)(.*?)";
	static final  String FRONT_PATTERN24 = "(.*?)(/user/my_presentee.htm)(.*?)";
	static final  String FRONT_PATTERN25 = "(.*?)(/nav.htm)(.*?)";
	static final  String FRONT_PATTERN26 = "(.*?)(/tzlcList.htm)(.*?)";
	static final  String FRONT_PATTERN27 = "(.*?)(/encyclopedia.htm)(.*?)";
	static final  String FRONT_PATTERN28 = "(.*?)(/wdbklist.htm)(.*?)";
	static final  String FRONT_PATTERN29 = "(.*?)(/financial.htm)(.*?)";
	static final  String FRONT_PATTERN30 = "(.*?)(/nav_jrjc.htm)(.*?)";
	static final  String FRONT_PATTERN31 = "(.*?)(/jrjcList.htm)(.*?)";
	static final  String FRONT_PATTERN32 = "(.*?)(/center.htm)(.*?)";
	
	 public String getKeywords1() {
		 setHeaderMeta();
		return keywords1;
	}

	public void setKeywords1(String keywords1) {
		
		setHeaderMeta();
		this.keywords1 = keywords1;
	}

	public String getDescription() {
		setHeaderMeta();
		return description;
	}

	public void setDescription(String description) {
		setHeaderMeta();
		this.description = description;
	}

	public String getTittle3() {
		setHeaderMeta();
		return tittle3;
	}

	public void setTittle3(String tittle3) {
		setHeaderMeta();
		this.tittle3 = tittle3;
	}

	private  String setHeaderMeta() {
		
		String url="";
	    String uri = FacesUtil.getHttpServletRequest().getRequestURI();
		System.out.println(FacesUtil.getHttpServletRequest().getRequestURL());
		System.out.println(uri);

		if(uri.matches(FRONT_PATTERN1)){
			//个人项目
			keywords1="个人理财 个人投资 个人投资理财 个人理财产品";
			description="个人项目栏目为大家提供个人投资理财产品、项目";
			tittle3="个人理财_个人投资_个人投资理财_个人理财产品_好友来投";
			
		}else if(uri.matches(FRONT_PATTERN2)){
			//企业项目
			keywords1="投资理财 企业投资  公司投资";
			description="好友来投企业投资频道为企业提供企业投资、公司投资的投资理财项目";
			tittle3="投资理财_企业投资_公司投资_好友来投";
		}else if(uri.matches(FRONT_PATTERN3)){
			//我要融资
			keywords1="贷款  借款  小额贷款  无抵押贷款";
			description="好友来投我要融资栏目给大家提供贷款、借款、小额贷款、无抵押贷款等融资服务";
			tittle3="贷款_借款_小额贷款_无抵押贷款_好友来投 ";
		}else if(uri.matches(FRONT_PATTERN4)){
			//个人融资
			keywords1="个人贷款 个人借款 无抵押个人贷款 信用贷款";	
			description="好友来投个人融资栏目给个人提供个人贷款、个人借款、无抵押个人贷款、信用贷款等服务";	
			tittle3="个人贷款_个人借款_无抵押个人贷款_信用贷款_好友来投 ";
		}else if(uri.matches(FRONT_PATTERN5)){
			//企业融资
			keywords1="企业贷款 公司贷款 信用贷款 小微企业贷款";	
			description="好友来投企业融资栏目给企业提供企业贷款、公司贷款、信用贷款、小微企业贷款等融资项目";	
			tittle3="企业贷款_公司贷款_信用贷款_小微企业贷款_好友来投 ";
		}else if(uri.matches(FRONT_PATTERN6)){
			//首页
			keywords1="网络贷款 投资理财 P2P网贷 互联网金融 小额贷款 个人贷款  P2P理财  个人理财 ";	
			description="好友来投给个人与企业提供网上贷款、投资理财、小额贷款、P2P网贷、网上理财、P2P理财、无抵押个人贷款、信用贷款等服务，是一个安全、可靠、透明、高效的互联网金融P2P网贷平台，门槛低、收益高";	
			tittle3="好友来投官网_网上贷款_投资理财_可以信赖的互联网金融P2P网贷平台 ";
		}else if(uri.matches(FRONT_PATTERN7)){
			
			keywords1="";
			description="";
			tittle3="";
			
		}else if(uri.matches(FRONT_PATTERN8)){
					
			keywords1="";
			description="";
			tittle3="个人信息_好友来投";
		}else if(uri.matches(FRONT_PATTERN9)){
			
			keywords1="";
			description="";
			tittle3="工作财务信息_好友来投";
		}else if(uri.matches(FRONT_PATTERN10)){
			
			keywords1="";
			description="";
			tittle3="认证资料_好友来投";	
		}else if(uri.matches(FRONT_PATTERN11)){
			
			keywords1="";
			description="";
			tittle3="交易记录_好友来投";	
		}else if(uri.matches(FRONT_PATTERN12)){
			
			keywords1="";
			description="";
			tittle3="账户充值_好友来投";	
		}else if(uri.matches(FRONT_PATTERN13)){
			
			keywords1="";
			description="";
			tittle3="账户提现";		
		}else if(uri.matches(FRONT_PATTERN14)){
			
			keywords1="";
			description="";
			tittle3="我的投资_好友来投";		
		}else if(uri.matches(FRONT_PATTERN15)){
			
			keywords1="";
			description="";
			tittle3="债权转让_好友来投";		
		}else if(uri.matches(FRONT_PATTERN16)){
			
			keywords1="";
			description="";
			tittle3="自动投标_好友来投";		
		}else if(uri.matches(FRONT_PATTERN17)){
			
			keywords1="";
			description="";
			tittle3="我的借款_好友来投";		
		}else if(uri.matches(FRONT_PATTERN18)){
			
			keywords1="";
			description="";
			tittle3="借款申请查询_好友来投";	
		}else if(uri.matches(FRONT_PATTERN19)){
			
			keywords1="";
			description="";
			tittle3="个人信息_好友来投";	
		}else if(uri.matches(FRONT_PATTERN20)){
			
			keywords1="";
			description="";
			tittle3="安全信息_好友来投";		
		}else if(uri.matches(FRONT_PATTERN21)){
			
			keywords1="";
			description="";
			tittle3="站内信_好友来投";		
		}else if(uri.matches(FRONT_PATTERN22)){
			
			keywords1="";
			description="";
			tittle3="通知设置_好友来投";	
		}else if(uri.matches(FRONT_PATTERN23)){
			
			keywords1="";
			description="";
			tittle3="积分_好友来投";	
			
			
		}else if(uri.matches(FRONT_PATTERN24)){
			
			keywords1="";
			description="";
			tittle3="推荐管理_好友来投";	
			//投资理财
		}else if(uri.matches(FRONT_PATTERN25)){
			
			keywords1="投资理财_小额投资理财_投资理财知识";
			description=" 投资理财栏目为大家提供投资理财、小额投资理财、投资理财知识等方面的信息";
			tittle3="投资理财_小额投资理财_投资理财知识_好友来投";		
		}else if(uri.matches(FRONT_PATTERN26)){
			
			keywords1="";
			description="";
			tittle3="";
			
			//网贷百科
		}else if(uri.matches(FRONT_PATTERN27)){
			
			keywords1="网贷名词 热门词条 网贷事件 网贷名人";
			description="网贷百科为大家提供网贷名词、热门词条、网贷事件、网贷名人方面的信息。";
			tittle3="网贷名词_热门词条_网贷事件_网贷名人_好友来投";	
		}else if(uri.matches(FRONT_PATTERN28)){	
			keywords1="";
			description="";
			tittle3="";		
			//金融基础
		}else if(uri.matches(FRONT_PATTERN29)){	
			keywords1="理财产品 贷款 外汇 信托 股票 债券 期货 基金";
			description="金融基础栏目为大家提供理财产品、贷款、外汇、信托、股票、债券、期货、基金等相关金融知识";
			tittle3="理财产品_贷款_外汇_信托_股票_债券_期货_基金_好友来投";
		}else if(uri.matches(FRONT_PATTERN30)){	
			keywords1="";
			description="";
			tittle3="";		
		}else if(uri.matches(FRONT_PATTERN31)){	
			keywords1="";
			description="";
			tittle3="";	
		}else if(uri.matches(FRONT_PATTERN32)){	
			keywords1="";
			description="";
			tittle3="账户中心_好友来投";	
		}
			
		return url;
	}
	

	




	/**
	 * 站点域名
	 * @return
	 */
	public String getSiteDns(){
		if(this.siteDns == null){
			this.siteDns = (String)FacesUtil.getExpressionValue(SITE_DNS_EL);
		}
		return this.siteDns;
	}
	
	/**
	 * 网站名称
	 * 
	 * public String getSiteName(){
		if(siteName == null){
			siteName = (String) FacesUtil.getExpressionValue(SITE_NAME_EL);
		}
		return siteName ;
	}

	 * 
	 */
	public String getSiteName() {
		if(siteName == null){
			siteName = (String) FacesUtil.getExpressionValue(SITE_NAME_EL);
		}
		return siteName ;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	/**
	 * 网站标语
	 * @return
	 */
	public String getSiteSlogan(){
		if(siteSlogan == null){
			this.siteSlogan = (String) FacesUtil.getExpressionValue(SITE_SLOGAN_EL);
		}
		return this.siteSlogan;
	}

	

	
	

	
}
