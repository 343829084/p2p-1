package com.esoft.archer.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.esoft.archer.node.model.Node;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;

public class XMLServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7969759709932200354L;
	public InfoPush infoPush;
	/**
	 * Constructor of the object.
	 */
	public XMLServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		WebApplicationContext wac= WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		infoPush=wac.getBean(InfoPush.class);
		List<Node> items=infoPush.nodeList.getPingInfo();
		Channel ch = null;
		try {
			ch=	infoPush.createXML(items);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		WireFeedOutput out1 = new WireFeedOutput(); 
		try {
			out.println(out1.outputString(ch));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}


}
