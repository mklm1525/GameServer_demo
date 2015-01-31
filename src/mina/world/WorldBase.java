package mina.world;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import mina.player.Player;
import mina.pool.TestExecutor;
import mina.pool.TestThreadExecutor;

import org.apache.log4j.Logger;

public class WorldBase{
	private static final Logger logger = Logger.getLogger(WorldBase.class);
	
	private static TestExecutor executor = null;
	
	private Map<Long, Player> players;//所有在线的玩家
	
	private Map<Long, Player> needSavePlayers;//需要保存的玩家
	
	protected WorldBase(){
		innerInit();
	}
	
	private void innerInit(){
		if(executor == null){
			executor = new TestThreadExecutor();
			executor.start();
		}
		players = new ConcurrentHashMap<Long, Player>();
	}
	
	public void shutDown(){
		try{
			if(executor != null){
				executor.stop();
			}
			logger.info("游戏世界线程池关闭 OK");			
		}
		catch(Exception e){
			logger.error("游戏世界线程池关闭出现错误", e);
		}
	}
	
	public void execute(Runnable r){
		executor.execute(r);
	}
	
	public Map<Long, Player> getPlayers(){
		return players;
	}
	
}