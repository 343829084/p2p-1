package com.esoft.weixinapp.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import antlr.StringUtils;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.common.controller.StringHome;
import com.esoft.archer.node.NodeConstants;
import com.esoft.archer.node.controller.NodeBodyHome;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.node.model.NodeBody;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.util.SpringBeanUtil;
import com.esoft.core.util.StringManager;
import com.esoft.weixinapp.commons.MessageUtil;
import com.esoft.weixinapp.message.respone.Article;
import com.esoft.weixinapp.message.respone.NewsMessage;
import com.sun.org.apache.bcel.internal.generic.NEW;

@Component
@Scope(ScopeType.VIEW)
public class NewNOde  extends EntityHome<NodeBody>{
	
	private StringHome stringHome=new StringHome() ;
//	@Autowired
//	private HibernateTemplate	ht;

	

	public NodeBody getBodyById(){
		
		NodeBody body=getBaseService().get(NodeBody.class, "7d806664cd904a2c97e9d133e0b7b96f");
		return body;
	}
	
	private String getBodySplit(){
		
		String strBodyString="";
		NodeBody nodeBody=getNodeBody();
		return 	splitNodeBody(nodeBody);
		
	}
	private String splitNodeBody(NodeBody nodeBody){
		String resultString="";
		if(null!=nodeBody){
			
			String bodyString=nodeBody.getBody();
			resultString=splitBody(bodyString);
		}else{
			
			System.out.print(" splitNodeBody error: NodeBody is null");
			throw new RuntimeException(" splitNodeBody error: NodeBody is null");
			
		}
		
		return resultString;
	}
	private String splitBody(String body){
		
		String result="";
		if(null!=body&&!body.isEmpty()){
			
			
			result=body.length()>100?stringHome.ellipsis(body, 100):body;
			
		}else{
			
			System.out.print(" splitBody error: body is null");
		}
		return result;
	}
	private NodeBody getNodeBody(){
		
		return getBodyById();
		
		
	}
	public String getNewMessage(String picURL, String URL, String title,String fromUserName, String toUserName) {
		String respMessage = null;
		Article article = new Article();
		String string1=	getBodySplit();
	//	ht = (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");
		if( string1 !=null){
			//NodeBody body=ht.get(NodeBody.class, "7d806664cd904a2c97e9d133e0b7b96f");
			//NodeBody body =getBaseService().get(NodeBody.class, "7d806664cd904a2c97e9d133e0b7b96f");
		
			/*if (body!=null) {
				String string = stringHome.ellipsis(body.getBody(), 100);
				article.setDescription("123456"+string); 	
			}else {
						  article.setDescription("好友来投（haoyoulaitou.com），是目前中国互联网金融中P2P信贷行业的新兴企业。" +
						 "是一个以个人对个人小额借贷为主要产品，为借贷两端搭建的公平、透明、安全、高效的互联网金融服务平台。" +
						 "借款用户可以在好友来投上获得信用评级、发布借款请求来实现个人快捷的融资需要。" +
						 "理财用户可以把自己的部分闲余资金通过好友来投平台出借给信用良好有资金需求的个人，在获得有保障，高收益的理财回报的同时帮助了优质的借款人。"); 
			}
				*/
			
	
			article.setDescription("123456"+string1); 	
		}
		else{
				  article.setDescription("hibernate is null"); 
		}		
		article.setTitle(title);
		article.setPicUrl(picURL);
		article.setUrl(URL);
		List<Article> articleList2 = new ArrayList<Article>();
		articleList2.add(article);
		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		newsMessage.setArticleCount(articleList2.size());
		newsMessage.setArticles(articleList2);
		respMessage = MessageUtil.messageToXml(newsMessage);
//		
//		 Article article = new Article(); article.setTitle(title);
//		  article.setDescription
//		  ("好友来投（haoyoulaitou.com），是目前中国互联网金融中P2P信贷行业的新兴企业。" +
//		  "是一个以个人对个人小额借贷为主要产品，为借贷两端搭建的公平、透明、安全、高效的互联网金融服务平台。" +
//		  "借款用户可以在好友来投上获得信用评级、发布借款请求来实现个人快捷的融资需要。" +
//		  "理财用户可以把自己的部分闲余资金通过好友来投平台出借给信用良好有资金需求的个人，在获得有保障，高收益的理财回报的同时帮助了优质的借款人。"
//		 ); 
//		  article.setPicUrl(
//		  "http://115.28.191.60/site/themes/default/images/wxHT.png");
//		  article.setUrl("http://115.28.191.60/node/aboutUs/2");
//		  article.setPicUrl(picURL); article.setUrl( URL); List<Article>
//		 articleList2 = new ArrayList<Article>(); articleList2.add(article);
//		 NewsMessage newsMessage = new NewsMessage();
//		  newsMessage.setToUserName(fromUserName);
//		  newsMessage.setFromUserName(toUserName);
//		  newsMessage.setCreateTime(new Date().getTime());
//		 newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
//		  newsMessage.setArticleCount(articleList2.size());
//		  newsMessage.setArticles(articleList2); 
//		  respMessage=MessageUtil.newsMessageToXml(newsMessage);
		return respMessage;
			}
}
