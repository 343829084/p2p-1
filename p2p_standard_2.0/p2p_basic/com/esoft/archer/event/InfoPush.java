package com.esoft.archer.event;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.esoft.archer.node.controller.NodeList;
import com.esoft.archer.node.model.Node;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Guid;
import com.sun.syndication.feed.rss.Item;

@Component
public class InfoPush {
	@Resource
	public NodeList nodeList;
	

	public Channel createXML(List<Node> nodes){
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
			
			item.setLink("node/"+node.getCategoryTerms().get(0).getId()+"/"+node.getId());
				item.setUri("node/"+node.getCategoryTerms().get(0).getId()+"/"+node.getId());
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
		//WireFeedOutput out = new WireFeedOutput();
		/*try {
			System.out.println(out.outputString(channel));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		}	*/
		return channel;
	}


	
	public NodeList getNodeList() {
		return nodeList;
	}

	public void setNodeList(NodeList nodeList) {
		this.nodeList = nodeList;
	}
}
