package mina.player;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import mina.dao.PlayerDao;
import mina.dao.imp.PlayerDaoImp;

import org.apache.log4j.Logger;
import org.apache.mina.util.ConcurrentHashSet;

public class SnapshotManager {
	
	private static final Logger logger = Logger.getLogger(SnapshotManager.class);
	//所有玩家的名字集合，用来在创建用户的时候去重
	private Set<String> nameSnap = new ConcurrentHashSet<String>();
	//所有玩家基础信息集合，用来判断是否有这个玩家
	private Map<Long, PlayerSnapshot> playerSnapshots = new ConcurrentHashMap<Long, PlayerSnapshot>();
	//userId做key的集合
	private Map<Long, PlayerSnapshot> userIdSnapshots = new ConcurrentHashMap<Long, PlayerSnapshot>();
	
	private PlayerDao playerDao = new PlayerDaoImp();
	
	private static SnapshotManager instance;
	
	public static SnapshotManager getInstance(){
		if(instance == null){
			instance = new SnapshotManager();
		}
		return instance;
	}
	
	public void load(){
		playerSnapshots.clear();
		nameSnap.clear();
		userIdSnapshots.clear();
		try{
			List<PlayerSnapshot> snapshots = playerDao.getAllPlayerSnapshots();
			for(PlayerSnapshot s : snapshots){
				addPlayerSnapshot(s);
			}
		}catch (Exception e){
			e.printStackTrace();
			logger.error("读取Player Snapshot error");
		}
		
	}

	public void addPlayerSnapshot(PlayerSnapshot s) {
		playerSnapshots.put(s.getId(), s);
		userIdSnapshots.put(s.getUserId(), s);
		nameSnap.add(s.getName());
	}

	public PlayerSnapshot getPlayerSnapshotByUserId(long userId) {
		return userIdSnapshots.get(userId);
	}

	public boolean isAlreadyExsistName(String nickName) {
		if(nameSnap.contains(nickName)){
			return false;
		}
		return true;
	}

	public PlayerSnapshot getPlayerSnapshot(long id) {
		return playerSnapshots.get(id);
	}

}
