package com.esoft.jdp2p.message.service.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import com.esoft.jdp2p.message.service.FwSmsService;
@Service("FwSmsService")
public class FwSmsServiceImpl implements FwSmsService {
	private String wsAdress ="http://61.145.229.28:7902/MWGate/wmgw.asmx/MongateSendSubmit?userId=CPID&password=CPPWD&pszMobis=PHONE&pszMsg=MSGCONT&iMobiCount=1&pszSubPort=*&MsgId=0";
	private static String cpid="";
	private static String cppwd="";
	
	
	private static Properties props = new Properties(); 
	
	static{
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("mw_sms_config.properties"));
			cpid = props.getProperty("fwid");
			cppwd = props.getProperty("fwpwd");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void send(String content, String mobileNumber) {
			if (cpid == null || cppwd == null) {
				System.out.println("");
			}
			String content1;
			try {
				content1 = URLEncoder.encode(content, "UTF-8");
				String requestUrl = wsAdress.replace("CPID", cpid).replace("CPPWD", cppwd).replace("PHONE", mobileNumber).replace("MSGCONT", content1);  
				String resultString = httpsRequest(requestUrl,null);
				System.out.println(resultString);
				Map<String, String> map;
				try {
					map = this.xmlElements(resultString);
					String result = map.get("string");
					if(null!=result){
						Long code= Long.parseLong(result);
						System.out.println(code);
					}else{
						System.out.println("string 结果为空");
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			
			
		
	}
	/*转码并且发送http请求*/
	public String httpsRequest(String requestUrl, String outputStr){
		StringBuffer buffer = new StringBuffer();
		try {
			
			URL url = new URL(requestUrl);
			URLConnection connection = url.openConnection();
			HttpURLConnection conn =  (HttpURLConnection) connection;
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type","text/xml; charset=UTF-8");
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		} 
		
			return buffer.toString();
		
	}
	/* 解析XML 得到它的父节点名称 */
	  public Map<String,String> xmlElements(String xmlDoc) {
		  Map<String, String> map = new HashMap<String, String>();

	        //创建一个新的字符串
	        StringReader read = new StringReader(xmlDoc);
	        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
	        InputSource source = new InputSource(read);
	        //创建一个新的SAXBuilder
	        SAXBuilder sb = new SAXBuilder();
	        try {
	            //通过输入源构造一个Document
	            Document doc = sb.build(source);
	            //取的根元素
	            Element root = doc.getRootElement();
	            map.put(root.getName(), root.getText());
	            //得到根元素所有子元素的集合
	           /* List<Element> elementList = root.getChildren();
	            	map.put(e.getName(), e.getText());
	            	System.out.println(e.getName());
	            }*/
	        } catch (JDOMException e) {
	            // TODO 自动生成 catch 块
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO 自动生成 catch 块
	            e.printStackTrace();
	        }
	        return map;
	    }
	  /* 测试*/
public static void main(String[] args) {
	System.out.println(new Date());
	new FwSmsServiceImpl().send("您的账户提现操作已成功，金额:1000元，已转到您绑定的银行账户。如非本人操作，请致电客服：4008-716-567", "15510492893");
}

}
