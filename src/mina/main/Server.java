package mina.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.TimeZone;

import mina.codec.MessageCodecFactory;
import mina.data.TestData;
import mina.db.HibernateSessionFactory;
import mina.hotfix.Hotfixable;
import mina.hotfix.Readable;
import mina.hotfix.ResourceFileManager;
import mina.hotfix.ResourceFileReader;
import mina.player.SnapshotManager;
import mina.pool.TestExecutor;
import mina.pool.TestThreadExecutor;
import mina.server.MessageDispatcher;
import mina.util.ServerConfig;
import mina.world.World;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Server {
	
	private static final Logger logger = Logger.getLogger(Server.class);
	
	private IoAcceptor ioAcceptor = null;
	private TestExecutor executor;//线程池
	private MessageDispatcher messageDispatcher;//消息处理器
	protected static Server server;
	
	public void startServer(){
		startThreadPool();//启动线程池
		startMessageDispatcher();//启动消息处理器
		startNetServer();//启动网络服务
		logger.info("服务器启动成功。");
	}
	
	public void addParses() {
		this.getMessageDispatcher().addParse(new mina.parser.TestParser());
	}
	
	private static void loadRecourceFile() {
		loadFile("gear.json", TestData.class);
		logger.info("读取远程资源文件成功。");
	}
	
	public static void main(String[] args) throws IOException {
		
		if(args != null && args.length >= 1){
			
			// 时区
			String[] timeZones = args[0].split("=");
			String timeZone = timeZones[1];
			if(timeZone == null){
				logger.error("timeZone error");
				System.exit(0);
			}
			TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
			
			// log路径
			String[] logPaths = args[1].split("=");
			String logPath = logPaths[1];
			if(logPath == null){
				logger.error("logPath error");
				System.exit(0);
			}
			System.setProperty("minaTest.logs", logPath);
			
		}
		else{
			logger.error("args error");
			System.exit(0);
		}
		
		//加载配置
		loadConfig();
		//读取资源文件
		loadRecourceFile();
		//数据库连接初始化
		HibernateSessionFactory.loadHibernate();
		//load snapshot
		SnapshotManager.getInstance().load();
		//初始化世界
		World.getInstance();
		//启动服务器
		server = new Server();
		server.startServer();
		server.addParses();
		//启动关闭服务
		new ShutDownServer();
		//启动保存服务
		SaveService.getService();
	}
	
	/**
	 * 读取文件
	 */
	private static void loadFile(String path, Class<? extends Hotfixable> clazz){
		int remoteFile = 0/*GlobalVariables.getInstance().get("REMOTE_FILE").getIntValue()*/;
		String inter = "chs"/*GlobalVariables.getInstance().get("LANGUAGE").getValue()*/;
		ResourceFileManager.getManager().setInter(inter);
		if(remoteFile == 0){
			try {
				ResourceFileManager.getManager().parseJsonArray("res/data/" + path, clazz);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			ResourceFileManager.getManager().parseHotfixableFile("res/data/" + path, clazz);
		}else if(remoteFile == 1){
			getRemoteFileDatas(inter, path, clazz);
		}
	}
	
	private static void getRemoteFileDatas(String inter, String path, Class<? extends Readable> clazz){
		String url = "http://125.39.224.57:10000/x3res/serverall20140819/ExcelData/" + path;
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		HttpClient client = httpClientBuilder.build();
		HttpGet httpGet = new HttpGet(url);
		System.out.println(httpGet.getRequestLine());
		try {
		    //执行get请求
		    HttpResponse httpResponse = client.execute(httpGet);
		    //获取响应消息实体
		    HttpEntity entity = httpResponse.getEntity();
		    //响应状态
		    System.out.println("status:" + httpResponse.getStatusLine());
		    //判断响应实体是否为空
		    if (entity != null) {
		        System.out.println("contentEncoding:" + entity.getContentEncoding());
		        InputStream is = entity.getContent();
		        ResourceFileReader.getReader().parseXlsFile(inter, is, clazz);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		}
		
	}

	private static void loadConfig() {
		PropertyConfigurator.configure("config/log4j.properties");
		logger.info("设置配置文件成功。");
	}
	
	public static Server getServer(){
		return server;
	}
	
	/**
	 * 关服流程
	 */
	protected void shutDownServer(){
		//关闭游戏世界
		World.getInstance().shutDown();
		//关闭线程池
		executor.stop();
		logger.info("线程池关闭。");
		//关闭网络服务
		if(ioAcceptor!= null){
			ioAcceptor.dispose();
		}
		logger.info("网络服务关闭");
	}
	
	private void startThreadPool(){
		executor = new TestThreadExecutor();
		executor.start();
		logger.info("主线程池启动。");
	}
	
	private void startMessageDispatcher(){
		messageDispatcher = new MessageDispatcher(executor);
		logger.info("消息处理器启动");
	}
	
	private void startNetServer(){
		ioAcceptor = new NioSocketAcceptor();
		ioAcceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter( new MessageCodecFactory()));//设置解码器
		ioAcceptor.setHandler(new ServerHandler(messageDispatcher));
		ioAcceptor.getSessionConfig().setReadBufferSize(ServerConfig.getProperties().getInteger("ReadBufferSize"));
		ioAcceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		try {
			ioAcceptor.bind(new InetSocketAddress(ServerConfig.getProperties().getInteger("Port")));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		logger.info("网络服务启动。");
	}
	
	public MessageDispatcher getMessageDispatcher(){
		return messageDispatcher;
	}

	public IoAcceptor getIoAcceptor(){
		return ioAcceptor;
	}

}
