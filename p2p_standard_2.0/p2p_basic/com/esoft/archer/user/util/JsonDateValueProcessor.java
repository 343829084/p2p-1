package com.esoft.archer.user.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * json日期转换类
 * @author Administrator
 *
 */
public class JsonDateValueProcessor implements JsonValueProcessor {  
	  
	//设置日期格式
    private String format ="yyyy-MM-dd";  
      
    public Object processArrayValue(Object value, JsonConfig config) {  
        return process(value);  
    }  
  
    public Object processObjectValue(String key, Object value, JsonConfig config) {  
        return process(value);  
    }  
      
    private Object process(Object value){  
          
        if(value instanceof Date){  
            SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.UK);  
            return sdf.format(value);  
        }  
        return value == null ? "" : value.toString();  
    }  
} 