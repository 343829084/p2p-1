package com.esoft.weixinapp.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esoft.weixinapp.commons.MessageUtil;
import com.esoft.weixinapp.message.respone.Article;
import com.esoft.weixinapp.message.respone.NewsMessage;
import com.esoft.weixinapp.message.respone.TextMessage;


public class CoreServiceImpl {
	
	private static Logger log = LoggerFactory.getLogger(CoreServiceImpl.class);
	
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
			String respMessage = null;
			try {
				// 默认返回的文本消息内容
				String respContent = "请求处理异常，请稍候尝试！";

				// xml请求解析
				Map<String, String> requestMap = MessageUtil.parseXml(request);

				// 发送方帐号（open_id）
				String fromUserName = requestMap.get("FromUserName");
				// 公众帐号
				String toUserName = requestMap.get("ToUserName");
				// 消息类型
				String msgType = requestMap.get("MsgType");

				// 回复文本消息
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				textMessage.setFuncFlag(0);
				List<Article> articleList = new ArrayList<Article>();
				// 文本消息
				if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
					respContent = "您发送的是文本消息！";
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				// 图片消息
				else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
					respContent = "您发送的是图片消息！";
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				// 地理位置消息
				else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
					respContent = "您发送的是地理位置消息！";
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				// 链接消息
				else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
					respContent = "您发送的是链接消息！";
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				// 音频消息
				else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
					respContent = "您发送的是音频消息！";
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				// 事件推送
				else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
					// 事件类型
					String eventType = requestMap.get("Event");
					// 订阅
					if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
						respContent = "您好!欢迎关注好友来投！我们致力于打造方便快捷的P2P平台,体验生活,从这里开始!";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					}
					// 取消订阅
					else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
						// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
					}
					// 自定义菜单点击事件
					else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
						// 事件KEY值，与创建自定义菜单时指定的KEY值对应
						String eventKey = requestMap.get("EventKey");
						if (eventKey.equals("1")) {
							 Article article = new Article();
							 article.setTitle("好友计划");
							  article.setDescription("好友来投（haoyoulaitou.com）是一个以个人对个人小额借贷为主要产品,为借贷两端搭建的公平、透明、安全、高效的互联网金融服务平台,用户可以实现个人快捷的融资需要。"); 
							  article.setPicUrl("http://www.haoyoulaitou.com/site/themes/default/images/wxHT.png");
							  article.setUrl("http://www.haoyoulaitou.com/weixin/article");
							  articleList.add(article);
							  NewsMessage newsMessage = new NewsMessage();
							  	newsMessage.setToUserName(fromUserName);
								newsMessage.setFromUserName(toUserName);
								newsMessage.setCreateTime(new Date().getTime());
								newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
								newsMessage.setFuncFlag(0);
							  newsMessage.setArticleCount(articleList.size());
							  newsMessage.setArticles(articleList); 
							  respMessage = MessageUtil.messageToXml(newsMessage);
						} else if (eventKey.equals("2")) {
							 Article article = new Article();
							 article.setTitle("关于好友");
							  article.setDescription("好友来投（haoyoulaitou.com）是一个以个人对个人小额借贷为主要产品,为借贷两端搭建的公平、透明、安全、高效的互联网金融服务平台,用户可以实现个人快捷的融资需要。"); 
							  article.setPicUrl("http://www.haoyoulaitou.com/site/themes/default/images/wxHT.png");
							  article.setUrl("http://www.haoyoulaitou.com/weixin/article");
							  articleList.add(article);
							  NewsMessage newsMessage = new NewsMessage();
							  	newsMessage.setToUserName(fromUserName);
								newsMessage.setFromUserName(toUserName);
								newsMessage.setCreateTime(new Date().getTime());
								newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
								newsMessage.setFuncFlag(0);
							  newsMessage.setArticleCount(articleList.size());
							  newsMessage.setArticles(articleList); 
							  respMessage = MessageUtil.messageToXml(newsMessage);
						} else if (eventKey.equals("3")) {
							
							 Article article = new Article();
							 article.setTitle("活动");
							  article.setDescription("好友家庭已为你敞开大门! 小伙伴么还在等什么?"); 
							  article.setPicUrl("http://www.haoyoulaitou.com/site/themes/default/images/weixinarticle/weixin_activity.jpg");
							  article.setUrl("http://www.haoyoulaitou.com/weixin/activity");
							  articleList.add(article);
							  NewsMessage newsMessage = new NewsMessage();
							  	newsMessage.setToUserName(fromUserName);
								newsMessage.setFromUserName(toUserName);
								newsMessage.setCreateTime(new Date().getTime());
								newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
								newsMessage.setFuncFlag(0);
							  newsMessage.setArticleCount(articleList.size());
							  newsMessage.setArticles(articleList); 
							  respMessage = MessageUtil.messageToXml(newsMessage);
						} else if (eventKey.equals("14")) {
							respContent = "历史上的今天菜单项被点击！";
						} else if (eventKey.equals("21")) {
							respContent = "歌曲点播菜单项被点击！";
						} else if (eventKey.equals("22")) {
							respContent = "经典游戏菜单项被点击！";
						} else if (eventKey.equals("23")) {
							respContent = "美女电台菜单项被点击！";
						} else if (eventKey.equals("24")) {
							respContent = "人脸识别菜单项被点击！";
						} else if (eventKey.equals("25")) {
							respContent = "聊天唠嗑菜单项被点击！";
						} else if (eventKey.equals("31")) {
							respContent = "Q友圈菜单项被点击！";
						} else if (eventKey.equals("32")) {
							respContent = "电影排行榜菜单项被点击！";
						} else if (eventKey.equals("33")) {
							respContent = "幽默笑话菜单项被点击！";
						}
					}else{
						
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					}
				}
		
			} catch (Exception e) {
				e.printStackTrace();
			}

			return respMessage;
	}

	/**
	 * emoji表情转换(hex -> utf-16)
	 * 
	 * @param hexEmoji
	 * @return
	 */
	public static String emoji(int hexEmoji) {
		return String.valueOf(Character.toChars(hexEmoji));
	}
}