package com.esoft.jdp2p.schedule.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.esoft.archer.node.controller.NodeList;
import com.esoft.archer.node.model.Node;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Guid;
import com.sun.syndication.feed.rss.Item;

/**
 * 
 * 
 * @author Administrator
 * 
 */
@Component
public class PushInfo implements Job {

	@Resource
	public NodeList nodeList;
	Log log = LogFactory.getLog(PushInfo.class);
	private final String BAIDU_RPC = "http://ping.baidu.com/ping/RPC2";
	private final String BAIDU_METHOD = "weblogUpdates.extendedPing";
	private final String HOST_URL = "www.haoyoulaitou.com";
	private final String BLOG_NAME = "好友来投";
	private final String RSS_URL = "http://www.haoyoulaitou.com/rssxml.xml";

	public void process() {
		try {
//			createXML(nodeList.getPingInfo());
			log.info("the pushjob is start. messages:");
			for (Node item : nodeList.getPingInfo()) {
				String shareURL = "www.haoyoulaitou.com/node/"
						+ item.getCategoryTerms().get(0).getId() + "/"
						+ item.getId();
				String resultStr = pingBaidu("好友来投", HOST_URL, shareURL,	BAIDU_METHOD,
						RSS_URL);
				System.out.println("........result: " + resultStr);
				log.info("博客名称:" + BLOG_NAME);
				log.info("博客首页地址:" + HOST_URL);
				log.info("新发文章地址:" + shareURL);
				log.info("博客rss地址:" + RSS_URL);
				log.info("the ping result : " + resultStr);
			}
			log.info("the pushjob is end.");

		} catch (Exception e) {
			e.printStackTrace();
			log.error("the pushjob is error : " + e.getMessage());
			log.info("the pushjob is exit for exception.");
		}

	}

	public String pingBaidu(String title, String url, String shareURL,String methodName,
			String rssURL) throws Exception {
		PostMethod post = new PostMethod(BAIDU_RPC);
		post.addRequestHeader("User-Agent", "request");
		post.addRequestHeader("Content-Type", "text/xml");
		String methodCall = buildMethodCall(title, url, shareURL, methodName, rssURL);
		RequestEntity entity = new StringRequestEntity(methodCall, "text/xml",
				"utf-8");
		post.setRequestEntity(entity);
		HttpClient httpclient = new HttpClient();
		httpclient.executeMethod(post);
		String ret = post.getResponseBodyAsString();
		post.releaseConnection();
		return ret;
	}

	private String buildMethodCall(String title, String url, String shareURL,String methodName,
			String rssURL) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		buffer.append("<methodCall>");
		buffer.append("<methodName>"+methodName+"</methodName>");
		buffer.append("<params>");
		buffer.append("<param><value><string>" + url
				+ "</string></value></param>");
		buffer.append("<param><value><string>" + title
				+ "</string></value></param>");
		buffer.append("<param><value><string>" + shareURL
				+ "</string></value></param>");
		buffer.append("<param><value><string>" + rssURL
				+ "</string></value></param>");
		buffer.append("</params>");
		buffer.append("</methodCall>");
		return buffer.toString();
	}

	public Channel createXML(List<Node> nodes) {
		Channel channel = new Channel("rss_2.0");
		channel.setTitle("好友来投资讯");// 网站标题
		channel.setDescription("好友来投给个人与企业提供网上贷款、投资理财、小额贷款、P2P网贷、网上理财、P2P理财、无抵押个人贷款、信用贷款等服务，是一个安全、可靠、透明、高效的互联网金融P2P网贷平台，门槛低、收益高");// 网站描述
		channel.setLink("www.haoyoulaitou.com");// 网站主页链接
		channel.setEncoding("utf-8");// RSS文件编码
		channel.setLanguage("zh-cn");// RSS使用的语言
		channel.setCopyright("好友来投信息科技（北京）有限公司");// 版权声明
		channel.setPubDate(new Date());// RSS发布时间
		channel.setUri("www.haoyoulaitou.com");
		List<Item> items = new ArrayList<Item>();// 这个list对应rss中的item列表
		for (Node node : nodes) {
			Item item = new Item();// 新建Item对象，对应rss中的<item></item>
			item.setAuthor("friend");// 对应<item>中的<author></author>
			item.setTitle(node.getTitle());// 对应<item>中的<title></title>
			item.setGuid(new Guid());// GUID=Globally Unique Identifier
										// 为当前新闻指定一个全球唯一标示，这个不是必须的

			item.setLink("node/"
					+ node.getCategoryTerms().get(0).getId() + "/"
					+ node.getId());
			item.setUri("node/"
					+ node.getCategoryTerms().get(0).getId() + "/"
					+ node.getId());
			item.setPubDate(node.getCreateTime());// 这个<item>对应的发布时间
			item.setComments("注释");// 代表<item>节点中的<comments></comments>
			// 新建一个Description，它是Item的描述部分
			Description description = new Description();
			description.setValue(node.getDescription());// <description>中的内容
			item.setDescription(description);// 添加到item节点中
			items.add(item);// 代表一个段落<item></item>，
		}
		channel.setItems(items);
		// 用WireFeedOutput对象输出rss文本
//		WireFeedOutput out = new WireFeedOutput();
		return channel;
	}


	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		process();
	}
}
