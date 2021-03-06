package com.esoft.jdp2p.message.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.message.model.InBox;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.MailUtil;
import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
import com.esoft.jdp2p.message.model.UserMessageWay;

import com.esoft.jdp2p.message.service.FwSmsService;
import com.esoft.jdp2p.message.service.SmsService;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-13 下午6:12:23
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-13 wangzhi 1.0
 */
@Service("messageFw")
public class MessageFw {

	@Logger
	Log log;

	@Resource
	private HibernateTemplate ht;

	
	@Resource
	private FwSmsService smsFwService;
	

	/**
	 * 发送消息，会根据接收用户的设定，自动判断是否发站内信、邮件、短信。
	 * 
	 * @param userId
	 *            接收用户id
	 * @param userMessageNodeId
	 *            发送消息节点id
	 * @param params
	 *            发送消息中的参数
	 */
	public void sendMsg(User user, String userMessageNodeId,
			Map<String, String> params) {
		// 找到node与userid所有的template，外加必须发送的template，然后一一发送。
		String hql = "select um.userMessageTemplate from UserMessage um right join um.userMessageTemplate umt where "
				+ "(um.user.id=:userId or umt.status='"
				+ MessageConstants.UserMessageTemplateStatus.REQURIED
				+ "') "
				+ " and "
				+ "umt.status != '"
				+ MessageConstants.UserMessageTemplateStatus.CLOSED
				+ "'"
				+ " and "
				+ "umt.userMessageNode.status = '"
				+ MessageConstants.UserMessageNodeStatus.OPEN
				+ "'"
				+ " and "
				+ "umt.userMessageWay.status = '"
				+ MessageConstants.UserMessageWayStatus.OPEN
				+ "'"
				+ " and "
				+ "umt.userMessageNode.id=:userMessageNodeId";
		if (log.isDebugEnabled()) {
			log.debug(hql +"  userId:"+ user.getId()+"  messageNodeId:" + userMessageNodeId);
		}
		List<UserMessageTemplate> umts = ht.findByNamedParam(hql, new String[] {
				"userId", "userMessageNodeId" }, new String[] { user.getId(),
				userMessageNodeId });
		for (UserMessageTemplate umt : umts) {
			String msg = replaceParams(umt, params);
			if (umt.getUserMessageWay().getId()
					.equals(MessageConstants.UserMessageWayId.EMAIL)) {
				// 邮件
				MailUtil.sendMail(user.getEmail(), umt.getName(), msg);
			} else if (umt.getUserMessageWay().getId()
					.equals(MessageConstants.UserMessageWayId.SMS)) {
				// 短信
				smsFwService.send(msg, user.getMobileNumber());
			} else if (umt.getUserMessageWay().getId()
					.equals(MessageConstants.UserMessageWayId.LETTER)) {
				// 站内信
				sendStationMsg(user, new User("admin"), umt.getName(), msg);
			}
		}
	}

	public void sendEmail(UserMessageTemplate umt, Map<String, String> params,
			String email) {
		String msg = replaceParams(umt, params);
		MailUtil.sendMail(email, umt.getName(), msg);
	}

	
		
		public void sendFWSMS(UserMessageTemplate umt, Map<String, String> params,
				String mobileNumber) {
			String msg = replaceParams(umt, params);
			System.out.println(msg);
			smsFwService.send (msg, mobileNumber);
	}

	public void sendStationMsg(UserMessageTemplate umt,
			Map<String, String> params, User sender, User reveiver) {
		String msg = replaceParams(umt, params);
		sendStationMsg(reveiver, sender, umt.getName(), msg);
	}

	/**
	 * 替换模板里面的参数
	 * 
	 * @param umt
	 * @param params
	 * @return 替换完成的内容
	 */
	private String replaceParams(UserMessageTemplate umt,
			Map<String, String> params) {
		String returnStr = umt.getTemplate();
		for (String key : params.keySet()) {
			returnStr = returnStr.replace("#{" + key + "}", params.get(key));
		}
		return returnStr;
	}

	public boolean isMessageWayenable(String userMessageWayId) {
		UserMessageWay umw = ht.get(UserMessageWay.class, userMessageWayId);
		if (umw != null
				&& umw.getStatus().equals(
						MessageConstants.UserMessageWayStatus.OPEN)) {
			return true;
		}
		return false;
	}

	public void sendStationMsg(User receiver, User sender, String title,
			String msg) {
		InBox inbox = new InBox();
		inbox.setId(IdGenerator.randomUUID());
		inbox.setRecevier(receiver);
		inbox.setSender(sender);
		inbox.setReceiveTime(new Date());
		inbox.setContent(msg);
		inbox.setStatus(MessageConstants.InBoxConstants.NOREAD);
		inbox.setTitle(title);
		ht.save(inbox);
	}

}
