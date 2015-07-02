package com.esoft.alipay.test.callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.stereotype.Service;

import com.esoft.jdp2p.borrower.BorrowerConstant.Url;

@Service
public class TestBackMethod {
	public void test() throws Exception {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>TestBackMethod.test()");
		Map<String, String> request=FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String urlString=request.get("url");
		String paramString=request.get("param1");
		System.out.println("url======>>"+urlString);
		System.out.println("paramString=======>>>>"+paramString);
		TestBackMethod.post(urlString, paramString);
	}
	
	 
	 public static void post(final String url,final String paramString ) throws HttpException, IOException{
		 Runnable nable= new Runnable() {
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				HttpClient client = new HttpClient();
				 PostMethod method = new PostMethod(url);   
				 method.addParameter("url", url); 
				 method.addParameter("param1", paramString);  
		         HttpMethodParams param = method.getParams();  
		         param.setContentCharset("UTF-8");  
				 try {
					client.executeMethod(method);
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 System.out.println(method.getStatusLine());  
				 
				InputStream stream = null;
				try {
					stream = method.getResponseBodyAsStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				 BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			     StringBuffer buf = new StringBuffer();  
			     String line;  
			     try {
					while (null != (line = br.readLine())) {  
					     buf.append(line).append("\n");  
					 }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
//			     System.out.println(buf.toString());  
			       //释放连接  
			     method.releaseConnection();  
			}
		};
		Thread t1=new Thread(nable);
		t1.start();
	 }
//	 
	 public static void main(String[] args) throws HttpException, IOException {
		 post("http://localhost:8080/archer/testBackMethod", "2");
	}
	 
	 
}
