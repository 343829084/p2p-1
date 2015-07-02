package com.esoft.weixinapp.message.respone;


import java.util.List;


public class NewsMessage extends BaseMessage {
	private int ArticleCount;
	private List<Article> Articles;
	private BaseMessage baseMessage;
	
	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}
	public BaseMessage getBaseMessage() {
		return baseMessage;
	}

	public void setBaseMessage(BaseMessage baseMessage) {
		this.baseMessage = baseMessage;
	}
}
