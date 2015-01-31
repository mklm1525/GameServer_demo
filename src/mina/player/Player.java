package mina.player;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import mina.dao.PlayerDao;
import mina.dao.entity.PlayerEntity;
import mina.dao.imp.PlayerDaoImp;

public class Player extends PlayerEntity{
	
	private static final Logger logger = Logger.getLogger(Player.class);
	protected Lock lockPlayer = new ReentrantLock();
	
	private IoSession session;
	
	public static Player createPlayer(long userId, String nickname) {
		Player player = new Player();
		player.setUserId(userId);
		player.setName(nickname);
		player.setAction(100);
		player.setLevel(1);
		player.setExp(0);
		player.setGold(0);
		player.setMoney(0);
		
		PlayerDao dao = new PlayerDaoImp();
		//创建角色错误 直接返回 
		if(!dao.save(player)){
			return null;
		}
		logger.info("玩家" + nickname + "创建player成功,plauerId=" + player.getId() + ",开始创建其他信息");
		return null;
	}
	
	
	public void tick() {
		// TODO Auto-generated method stub
		
	}


	public boolean loginWorld(IoSession session, String uuid) {
		try {
			this.lockPlayer.lock();
			IoSession old = this.getSession();
			if (old != null) {
				// old.send(CommandPacker.pack(CPCommand.PLAYER_CP,
				// PlayerCP.S_KICK_OUT, null));
				this.unbindSession();
				old.close(true);
				// 先退出
				this.logoutWorld(true);
			}

			// 绑定到现在session上
			this.bindSession(session);
			logger.info("玩家" + this.getName() + "登录成功!");
		} catch (Exception e) {
			logger.error("player.login错误:" + e);
			return false;
		} finally {
			this.lockPlayer.unlock();
		}
		return true;
	}


	private void logoutWorld(boolean b) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * session绑定
	 * @param session
	 */
	private void bindSession(IoSession session){
		this.setSession(session);
		this.getSession().setAttribute("player", this);
	}


	/**
	 * 解除session绑定
	 */
	private IoSession unbindSession(){
		IoSession temp = this.getSession();
		if(temp != null){
			temp.setAttribute("player", null);
		}		
		return temp;
	}


	public IoSession getSession() {
		return session;
	}


	public void setSession(IoSession session) {
		this.session = session;
	}
	
}