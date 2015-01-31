package mina.util;

/**
 * ServerProperties
 * @author Administrator
 *
 */
public class ServerConfig {	
	private static String filePath = "config/server.config.xml";
	private static XMLPropertyFile properties;
	
	public static void setFilePath(String filePath){
		ServerConfig.filePath= filePath; 
	}
	
	public static XMLPropertyFile getProperties(){
		if(properties == null){
			properties = new XMLPropertyFile(filePath);
			properties.load();
		}
		return properties;
	}
	
}
