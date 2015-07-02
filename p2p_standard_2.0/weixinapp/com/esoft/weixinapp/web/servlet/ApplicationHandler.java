package com.esoft.weixinapp.web.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;



import com.esoft.weixinapp.commons.MessageUtil;
import com.esoft.weixinapp.service.impl.CoreServiceImpl;
import com.esoft.weixinapp.test.SignUtil;

public class ApplicationHandler extends ViewHandler {
	private final ViewHandler parent;
    @Resource
	private CoreServiceImpl coreServiceImpl;
    
    private static Logger log = Logger.getLogger(ApplicationHandler.class);
    public ApplicationHandler(ViewHandler parent) {
       this.parent = parent;
    }
 
    @Override
    public Locale calculateLocale(FacesContext context) {
       return parent.calculateLocale(context);
    }
 
    @Override
    public String calculateRenderKitId(FacesContext context) {
    	String resultString="success";
    	try{
    		
    		checkToken(context);
        	 responseHandle(context);	
        	
    	}catch (Exception e) {
    		log.error("token认证失败:1233"+e.getMessage());
    		System.out.println("token认证失败:1233"+e.getMessage());
    		resultString="error:"+e.getMessage();
    	}
    
    	return this.parent.calculateRenderKitId(context);
    	
    }
   
    
    private ExternalContext  getExternalContext(FacesContext context){
    	ExternalContext ecoContext=null;
    	if(null!=context){
			
    		ecoContext =context.getExternalContext();
			
		}
    	return ecoContext;
    	
    }
    private  HttpServletRequest getRequestFromFaces(FacesContext context) {
    	
    	HttpServletRequest request=null;
    	if(null!=context){
    		ExternalContext ecoContext=getExternalContext(context);
    		if(null!=ecoContext){
    			
    			request =(HttpServletRequest)ecoContext.getRequest();
    			
    		}else{
    			
    			throw new RuntimeException("ExternalContext is null");
    		}
    		
		}
    	return request;
    	
	}
    private Map convert(HttpServletRequest request) throws Exception {
		Map<String, String> requestMap = new HashMap<String, String>();

		if (null != request) {
			requestMap = MessageUtil.parseXml(request);

		}
		return requestMap;
	}
    
  private String  getRespXml(FacesContext context){
	  
	
	  HttpServletRequest request=getRequestFromFaces(context);
	  Map map=null;
	  String respXml="";
	  if(null!=request){
	   	
		  try{
   			  map = convert(request);
   			  respXml = coreServiceImpl.processRequest(map);
   		  }catch(Exception ex){

   			  respXml="token 认证失败:"+ex.getMessage();
   			  log.error("token 认证失败--转换失败--coreService.processRequest :可能是解析参数map is null 或者参数解析错误:"+ex.getMessage());
   		  }
   		
	  }else{
		  
		  throw new RuntimeException("token 认证失败,request is null");
		  
	  }
	return  respXml;
  }
  private void responseHandle(FacesContext context){
	  
	  HttpServletResponse response= getResponseFromFaces(context);
	  PrintWriter out = null;
	  String respXml="";
	  if(null!=response){
		  
		 
		  try {
		    respXml=getRespXml(context);
			out = response.getWriter();
			out.print(respXml);
		} catch (IOException e) {
			
			log.error("token 认证失败:outprint错误:"+e.getMessage());
			
		}finally{
			
			
		      out.close();
		      out = null;
		}
		  
	  }else{
		  throw new RuntimeException("token 认证失败,获取response is null");
	  }
	 
  }
  private  HttpServletResponse getResponseFromFaces(FacesContext context) {
    	
	  HttpServletResponse response=null;
    	if(null!=context){
    		ExternalContext ecoContext=getExternalContext(context);
    		if(null!=ecoContext){
    			
    			response =(HttpServletResponse)ecoContext.getResponse();//Request();
    			
    		}else{
    			
    			throw new RuntimeException("ExternalContext is null");
    		}
    		
		}
    	return response;
    	
	}
    
    private  void checkToken(FacesContext context){
    	
        Boolean isRightBoolean=false;
        HttpServletRequest request= getRequestFromFaces(context);
    	if(null!=request){
    		
    		// 微信加密签名
    		String signature = request.getParameter("signature");
    		System.out.println("signature++++++++++++++++++++++++++++++++++++++++++++++"+signature);
    		if(StringUtils.isBlank(signature)){
    			throw new RuntimeException("Token 验证失败 ,可能signature为空或者值不对");
    		}
    		// 时间戳
    		String timestamp = request.getParameter("timestamp");
    		if(StringUtils.isBlank(timestamp)){
    			throw new RuntimeException("Token 验证失败 ,可能timestamp为空或者值不对");
    		}
    		// 随机数
    		String nonce = request.getParameter("nonce");
    		if(StringUtils.isBlank(nonce)){
    			throw new RuntimeException("Token 验证失败 ,可能nonce为空或者值不对");
    		}
    		// 随机字符串
    		String echostr = request.getParameter("echostr");
    		if(StringUtils.isBlank(echostr)){
    			throw new RuntimeException("Token 验证失败 ,可能echostr为空或者值不对");
    		}
    		try{
    			isRightBoolean=SignUtil.checkSignature(signature, timestamp, nonce);
    		}catch (Exception ex) {
    			isRightBoolean=false;
    			throw new RuntimeException("Token 验证失败 ,SignUtil.checkSignature throw exception:"+ex.getMessage());
			}
    		
    	}else{
    		
    		throw new RuntimeException("Token 验证失败 ,可能Request为空或者值不对");

    	}
    	if(!isRightBoolean){
    		
    		throw new RuntimeException("Token 验证失败 ,可能Request为空或者值不对");
    	}
    	
    }
    
    
    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {
       return parent.createView(context, viewId);
    }
 
    @Override
    public String getActionURL(FacesContext context, String viewId) {
       return parent.getActionURL(context, viewId);
    }
 
    @Override
    public String getResourceURL(FacesContext context, String path) {
       return parent.getResourceURL(context, path);
    }
 
    @Override
    public void renderView(FacesContext context, UIViewRoot viewToRender)
           throws IOException, FacesException {
       parent.renderView(context, viewToRender);    
    }
 
    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
       return parent.restoreView(context, viewId);
    }
 
    @Override
    public void writeState(FacesContext context) throws IOException {
       parent.writeState(context);
    }
}

