package com.esoft.weixinapp.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.esoft.weixinapp.service.impl.CoreServiceImpl;
@Component
public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 确认请求来自微信服务器
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		PrintWriter out = response.getWriter();
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
	
		out.close();
		out = null;
	}

	/**
	 * 处理微信服务器发来的消息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		String respXml = "";
		PrintWriter out = null;
		try {
		request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			String signature = request.getParameter("signature");
			// 时间戳
			String timestamp = request.getParameter("timestamp");
			// 随机数
			String nonce = request.getParameter("nonce");
			out = response.getWriter();
			// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {

					CoreServiceImpl coreServiceImpl = new CoreServiceImpl();
					respXml = coreServiceImpl.processRequest(request);
		}

		} catch (Exception ex) {
			respXml = "系统维护";
		}
		out.print(respXml);
		out.close();
		out = null;
	}
/*
	private Map convert(HttpServletRequest request) throws Exception {
		Map<String, String> requestMap = new HashMap<String, String>();

		if (null != request) {
			requestMap = MessageUtil.parseXml(request);

		}
		return requestMap;
	}*/
}