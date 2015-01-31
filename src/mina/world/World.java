package mina.world;

import mina.logger.TestLogger;
import mina.main.Server;
import mina.player.Player;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;

public class World extends WorldBase {
	
	private static final Logger logger = Logger.getLogger(World.class);

	public static final int EVERYDAY_CD_TIME = 0;
	
	private static World instance;
	
	private boolean running = true;
	
	private LogicWorker worker;
	
	public static World getInstance(){
		if(instance == null){
			instance = new World();
		}
		return instance;
	}
	
	private World(){
		super();
		startWorkers();
	}
		
	public void startWorkers(){
		worker = new LogicWorker();
		Thread t = new Thread(worker);
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public void shutDown(){
		try{
			super.shutDown();
			this.worker.close();
		}
		catch(Throwable t){
			logger.error("World 关闭出现异常", t);
		}
	}
	
	class LogicWorker implements Runnable{
		int ticker = 0;
		
		@Override
		public void run(){
			logger.info("LogicWorker Running...");
			while(running){
				try{
					ticker ++;
					if(ticker % 150 == 0){
						try{
							printInfos();
						}
						catch(Throwable e){}
						ticker = 0;
					}
					
					for(Player player : getPlayers().values()){
						try{
							player.tick();
						}
						catch(Throwable t){
							
						}
					}
//					if(!isRefresh){
//						refreshTalismanIngeritTime();
//					}else{
//						resetRefreshState();
//					}

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						logger.error("World Error", e);
					}
				}
				catch(Throwable t){
					logger.error("Game World LogicWorker Error", t);
				}
			}
			logger.info("LogicWorker Endding...");
		}
		

		public void close(){
			running = false;
		}
	}

	private void printInfos() {
		if(Server.getServer() == null){
			return;
		}
		IoAcceptor ioAcceptor = Server.getServer().getIoAcceptor();
		int playerSize = this.getPlayers().size();
		TestLogger.info("服务器状态  players:[" + playerSize + "] " + "connect:[" + ioAcceptor.getManagedSessionCount() + "]");
		System.out.println(ioAcceptor.toString());
	}
}
