package mina.logger;

import org.apache.log4j.Logger;

public class TestLogger{
	
	private static final Logger logger = Logger.getLogger(TestLogger.class);
	
	public static void info(String msg){
		logger.info(msg);
	}
	
	public static void error(String msg,Exception e){
		logger.error(msg,e);
	}
	
	public static void error(String msg){
		logger.error(msg);
	}
	
}