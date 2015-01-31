package mina.server;


public abstract class MessageParser{
	
//	private static final int HEADINFO_LENGTH = 10;
//	private static final int USER_DATA       = 0x40;
//	private static final int SERVER_CMD_PRE  = 0x80;
	
	public static final int TEST_PARSER = 0x0001;//测试解析器
	public static final int LOGIN_PARSER = 0x0002;//登录
			
	abstract public int getParseId();
	abstract public boolean parse(ReceivedMessage message) throws Exception;
	
	abstract public void catchException(ReceivedMessage message, Exception e);
	
}
