package com.esoft.weixinapp.web.servlet;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esoft.weixinapp.commons.AdvancedUtil;
import com.esoft.weixinapp.domain.SNSUserInfo;
import com.esoft.weixinapp.domain.WeixinOauth2Token;


public class OAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("gb2312");
		response.setCharacterEncoding("gb2312");

		String code = request.getParameter("code");

		if (!"authdeny".equals(code)) {
			WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken("APPID", "APPSECRET", code);
			String accessToken = weixinOauth2Token.getAccessToken();
			String openId = weixinOauth2Token.getOpenId();
			SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);

			request.setAttribute("snsUserInfo", snsUserInfo);
		}
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
