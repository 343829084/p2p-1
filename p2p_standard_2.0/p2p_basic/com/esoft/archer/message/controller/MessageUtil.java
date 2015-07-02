package com.esoft.archer.message.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.service.BaseService;
import com.esoft.archer.message.MessageConstants;
import com.esoft.archer.message.MessageConstants.MessageGroup;
import com.esoft.archer.message.model.InBox;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.MailUtil;
import com.esoft.core.util.SpringBeanUtil;

/**
 * 发送站内短信、手机短信通知、邮件短信通知 工具类
 * @author wanghm
 *
 */
@Component("messageUtil")
@Scope(ScopeType.VIEW)
public class MessageUtil implements java.io.Serializable{
	
	private static final long serialVersionUID = 7384660339093697122L;

	@Resource
	private HibernateTemplate ht ;

	@Resource
	private LoginUserInfo loginUser ;
	
	@Logger 
	private static Log log ;
	
	private BaseService<InBox> inBoxBaseService; 
	
	/**
	 * 发送消息，会根据接收用户的设定，自动判断是否发站内信、邮件、短信。
	 * @param userId
	 * @param msgTypeCode
	 * @param title
	 * @param msg
	 */
	public void sendMsg(String userId ,String msgTypeCode ,String title ,String msg){
		sendPrivateMsg(userId, msgTypeCode, title, msg);
		sendEmailMsg(userId, msgTypeCode, title, msg);
	}
	
	/**
	 * 发送站内信
	 * @param userId
	 * @param msgTypeCode
	 * @param title
	 * @param msg
	 */
	public void sendPrivateMsg(String userId,String msgTypeCode,String title ,String msg){
		if(log.isDebugEnabled())
			log.debug("Send private message to User ," +
					"id = " + userId + ",msgTypeCode="+msgTypeCode+ ",title=" + title +",msg = " + msg);
		if(enable(MessageGroup.PRIVATE_MESSAGE,msgTypeCode)){
			//FIXME 
			savePrivateMsg(userId, title, msg);
		}
	}
	
	public void sendEmailMsg(String userId ,String msgTypeCode,String title ,String msg){
		if(log.isDebugEnabled())
			log.debug("Send email message to User ," +
					"id = " + userId +",msgTypeCode="+msgTypeCode+ ",title=" + title +",msg = " + msg);
		if(enable(MessageGroup.EMAIL,msgTypeCode)){
			//FIXME 
			MailUtil.sendMail(getUserById(userId).getEmail(), title, msg);
		}
	}
	
	private boolean enable(String msgGroup , String msgTypeCode){
		String hql = "Select code from UserMessageType where userMessageTypeGroup.id = ? and id in (Select messageTypeId from UserMessage where user.id = ?)";
		List<String> results = ht.find(hql,msgGroup,loginUser.getLoginUserId());
		if(results.contains(msgTypeCode) && containsMsgTypeCode(msgTypeCode)){
			return true ;
		}
		return false ;
	}
	
	public boolean containsMsgTypeCode(String TypeCode){
		List<String> msgTypecodes = new ArrayList<String>();
		msgTypecodes.add(MessageConstants.MessageType.INVESTED_OPNE);
		msgTypecodes.add(MessageConstants.MessageType.RECHARGE_SUCCESS);
		msgTypecodes.add(MessageConstants.MessageType.INVESTED__SUCCESS);
		if(msgTypecodes.contains(TypeCode)){
			return true;
		}
		return false;
	}
	
	public User getUserById(String userId){
		return ht.get(User.class, userId);
	}
	
	@SuppressWarnings("unchecked")
	public void savePrivateMsg(String userId,String title ,String msg){
		InBox inbox = new InBox();
		inbox.setId(IdGenerator.randomUUID());
		inbox.setRecevier(getUserById(userId));
		inbox.setSender(getUserById("admin"));
		inbox.setReceiveTime(new Date());
		inbox.setContent(msg);
		inbox.setStatus(MessageConstants.InBoxConstants.NOREAD);
		inbox.setTitle(title);
		inBoxBaseService = (BaseService<InBox>) SpringBeanUtil.getBeanByName("baseService");
		inBoxBaseService.save(inbox);
	}
	
}
