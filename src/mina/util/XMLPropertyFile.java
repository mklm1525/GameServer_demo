package mina.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLPropertyFile {
	
	private static final Logger logger = Logger.getLogger(XMLPropertyFile.class);
	
	private String filePath;
	
	private HashMap<String, String> properties = new HashMap<String, String>();

	public XMLPropertyFile(String filePath) {
		this.filePath = filePath;
	}
	
	public String getFileName(){
		return filePath;
	}
	
	public HashMap<String, String> getProperties(){
		return properties;
	}
	
	public void setProperty(String key, String value){
		if(key == null || value == null){
    		throw new NullPointerException("key or value is null");
    	}
    	
    	key = key.trim();
    	value = value.trim();
    	
    	getProperties().put(key.trim(), value.trim());
    	logger.debug("set new property key[" + key + "] value[" + value + "]");
	}
	
    public void load(){
    	try{
	    	SAXReader reader = new SAXReader();
	    	File xmlFile = new File(getFileName());
	    	
	    	if(!xmlFile.exists() || !xmlFile.isFile()){
	    		logger.error("配置文件" + getFileName() + "不存在。");
	    		return;
	    	}
	    	
	    	Document document = reader.read(xmlFile);
	         
	        Element root = document.getRootElement();        
	        @SuppressWarnings("unchecked")
			List<Element> elements = root.elements();
	        
	        for(Element e : elements){
	        	
	        	@SuppressWarnings("unchecked")
				List<Element> innerElements = e.elements();
	        	
	        	if(innerElements.size() == 0){
		        	String key = e.getName();
		        	String value = e.getText();
		        	setProperty(key, value);
	        	}
	        	else{
	        		for(Element ee : innerElements){
	        			String key = e.getName() + "." + ee.getName();
			        	String value = ee.getText();
			        	setProperty(key, value);
	        		}
	        	}
	        	
	        }
    	}
    	catch(Exception e){
    		logger.error("load properties file error", e);
    	}
    }
    
    private long parseNumStr(String s){
		long value = Long.parseLong(s);
		return value;
	}
    
    public int getInteger(String key){
    	return (int)parseNumStr(properties.get(key));
    }

}
