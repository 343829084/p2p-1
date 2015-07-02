package com.esoft.weixinapp.service;

import com.esoft.weixinapp.menu.Button;
import com.esoft.weixinapp.menu.ClickButton;
import com.esoft.weixinapp.menu.ComplexButton;
import com.esoft.weixinapp.menu.Menu;
import com.esoft.weixinapp.menu.ViewButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esoft.weixinapp.commons.CommonUtil;
import com.esoft.weixinapp.commons.MenuUtil;
import com.esoft.weixinapp.domain.AccessToken;



public class MenuManager 
{
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);
	private static Menu  getMenu(){
		ViewButton viewButton = new ViewButton();
		viewButton.setName("项目列表");
		viewButton.setType("view");
		viewButton.setUrl("http://www.haoyoulaitou.com/weixin/projectlist");
		
	/*    ViewButton btn12 = new ViewButton();
	    btn12.setName("好友计划");
	    btn12.setType("view");
	    btn12.setUrl("http://www.haoyoulaitou.com/weixin/article");*/
		ClickButton btn12 = new ClickButton();  
		btn12.setName("好友计划");  
		btn12.setType("click");  
		btn12.setKey("1");  
	    
	    ViewButton register = new ViewButton();
	    register.setName("注册");
	    register.setType("view");
	    register.setUrl("http://www.haoyoulaitou.com/weixin/register");

	    ViewButton login = new ViewButton();
	    login.setName("登陆");
	    login.setType("view");
	    login.setUrl("http://www.haoyoulaitou.com/weixinlogin");
	      
	    ViewButton bind = new ViewButton();
	    bind.setName("微信绑定");
	    bind.setType("view");
	    bind.setUrl("http://www.haoyoulaitou.com/weixin/bind");
	   
//
//	    ViewButton active = new ViewButton();
//	    active.setName("活动");
//	    active.setType("view");
//	    active.setUrl("http://www.haoyoulaitou.com/weixin/activity");
	    ClickButton active = new ClickButton();  
	    active.setName("活动");  
	    active.setType("click");  
	    active.setKey("3");  
	    
	    
	    ViewButton btn1 = new ViewButton();
	    btn1.setName("理财小工具");
	    btn1.setType("view");
	    btn1.setUrl("http://www.haoyoulaitou.com/weixin/calculator");
	    
	    ClickButton btn23 = new ClickButton();  
	    btn23.setName("关于好友");  
	    btn23.setType("click");  
	    btn23.setKey("2");  
	   /* ViewButton btn23 = new ViewButton();
	    btn23.setName("关于好友");
	    btn23.setType("view");
	    btn23.setUrl("http://www.haoyoulaitou.com/weixin/article");*/
	    ComplexButton mainBtn1 = new ComplexButton();  
	    mainBtn1.setName("理财");  
	    mainBtn1.setSub_button(new Button[] { viewButton, btn12});  
	    ComplexButton mainBtn2 = new ComplexButton();  
	    mainBtn2.setName("账户");  
	    mainBtn2.setSub_button(new Button[] { register, login,bind });  

	    ComplexButton mainBtn3 = new ComplexButton();  
	    mainBtn3.setName("更多");  
	    mainBtn3.setSub_button(new Button[] { active, btn1, btn23});  
	    
	    Menu menu = new Menu();  
	    menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });  

	    return menu;
	}
	public static void main(String[] args) {
		
		AccessToken token = CommonUtil.IsGetAccessToken("服务号");
				
		System.out.println(token.getToken());
		if(null!=token){
			boolean result=MenuUtil.createMenu(getMenu(),token.getToken());
			System.out.println(result);

			if (result) {
				log.info("菜单创建成功!!!!!!!!!!!");
			}else {
				log.info("菜单创建失败!!!!!!!!!!!");
			}
		}
		
				
	}
}
