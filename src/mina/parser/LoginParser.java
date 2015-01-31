package mina.parser;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONObject;

import mina.player.Player;
import mina.player.PlayerSnapshot;
import mina.player.SnapshotManager;
import mina.pool.LoginThreadPool;
import mina.server.MessageParser;
import mina.server.ReceivedMessage;
import mina.util.ByteArray;
import mina.util.RandomUtil;
import mina.world.WorldCache;

public class LoginParser extends MessageParser {
	
	private static final Logger logger = Logger.getLogger(LoginParser.class);
	
	public static final int ALREADY_EXISTS_PLAYER_LOGIN = 0x0001;//已存在用户登录
	public static final int CREAT_PLAYER_LOGIN = 0x0002;//不存在用户创建角色登录

	@Override
	public int getParseId() {
		return MessageParser.LOGIN_PARSER;
	}

	@Override
	public boolean parse(ReceivedMessage message) throws Exception {
		IoSession session = message.getSession();
		JSONObject jsonObject = message.getJsonObject();
		switch(jsonObject.getInteger("command")){
		case ALREADY_EXISTS_PLAYER_LOGIN:{
			String str = jsonObject.getString("data");
			JSONObject reObject = new JSONObject();
			reObject.put("data", str);
			reObject.put("parseId", 0x0801);
			reObject.put("command", 0x0002);
			ByteArray ba = new ByteArray();
			ba.writeUTF(reObject.toJSONString());
			session.write(ba.toArray());
//			playerLogin(session, jsonObject);
			break;
		}
		case CREAT_PLAYER_LOGIN:{
			creatPlayerLogin(session, jsonObject);
			break;
		}
		}
		return false;
	}

	private void playerLogin(IoSession session, JSONObject jsonObject) {
		Runnable login = new Runnable(){
			
			@Override
			public void run(){
				try{				
				
					String uidStr = jsonObject.getString("uidStr");
					String uuid = jsonObject.getString("uuid");
//					String token = jsonObject.getString("token");
//					String md5  = jsonObject.getString("md5");
					String serverId = "0";
					
					try{
						serverId = jsonObject.getString("serverId");
					}catch(Exception e){
//						UISystemCP.getInstance().sendFail(session, ResManager.getStr("创建角色失败，请更新客户端"));
						return;
					}
					
					long userId = 0;
					try{
						userId = Long.parseLong(uidStr);
					}catch(Exception e){
//						UISystemCP.getInstance().sendFail(session, ResManager.getStr("连接服务器失败"));
						return;
					}
					
					if(userId == 0){
//						UISystemCP.getInstance().sendFail(session, ResManager.getStr("连接服务器失败"));
						return;
					}
					
//					if(SecretCreator.instance().checkMd5(md5, token, userId + "")){
//						
//					}else{
//						UISystemCP.getInstance().sendFail(session, ResManager.getStr("用户登录失败！"));
//						logger.error("MD5验证失败,token=" + token + " userId=" + userId);
//						return;
//					}
					
//					int maxNum = GlobalVariables.getInstance().get("MAX_ONLINE_NUM").getIntValue();
//					if(WorldBase.getInstance().getPlayers().size() >= maxNum){
//						UISystemCP.getInstance().sendFail(session, ResManager.getStr("服务器已满，请稍后登录"));
//						return;
//					}
					
					PlayerSnapshot snap = SnapshotManager.getInstance().getPlayerSnapshotByUserId(userId);
					Player player = null;
					//没有玩家
					if(snap == null){
//						//发送创建角色命令
//						session.send(CommandPacker.pack(getParseId(), S_CMD_SELECT_PRO, null));
//						LoginoutLogger.info("没有玩家信息[userID:" + userId + "],发送创建角色命令");
//						return;
						player = Player.createPlayer(userId, "aaa" + RandomUtil.random(1, 1000));
						
					}else{
						//并发读取
						player = WorldCache.getInstance().get(snap.getId());
					}
					
					//有snapshot但是没有玩家信息
					if(player == null){
//						UISystemCP.getInstance().sendFail(session, "登录错误，无此角色");
						return;
					}
										
//					if(player.getBlockTime() != null &&
//							player.getBlockTime().getTime() > System.currentTimeMillis()){
//							UISystemCP.getInstance().sendFail(session, ResManager.getStr("角色已封，请使用其他账号登录"));
//							return;
//					}
//					
//					if(player.getLevel() >= 200){
//						UISystemCP.getInstance().sendFail(session, "error:level >= 200");
//						return;
//					}
					
					playerLoginWorld(player, session, userId, uuid, false);
				}
				catch(Throwable e){
					logger.error("玩家登录异常", e);
//					UISystemCP.getInstance().sendFail(session, "玩家登录异常");
				}
			}
		};
		
		LoginThreadPool.getInstance().execute(login);
		
	}
	
	public void creatPlayerLogin(IoSession session, JSONObject jsonObject){
		
		Runnable creat = new Runnable(){
			
			@Override
			public void run(){
				try {
						
					String uidStr = jsonObject.getString("uidStr");
					String uuid = jsonObject.getString("uuid");
					String nickName = jsonObject.getString("name");
					
					long userId = 0;
					try{
						userId = Long.parseLong(uidStr);
					}catch(Exception e){
	//					UISystemCP.getInstance().sendFail(session, ResManager.getStr("连接服务器失败"));
						return;
					}
	
	//				int maxNum = GlobalVariables.getInstance().get("MAX_ONLINE_NUM").getIntValue();
	//				if(WorldBase.getInstance().getPlayers().size() >= maxNum){
	//					UISystemCP.getInstance().sendFail(session, ResManager.getStr("服务器已满，请稍后登录"));
	//					return;
	//				}
					
					if(nickName == null || nickName.length() == 0){
	//					UISystemCP.getInstance().sendFail(session, ResManager.getStr("先给人家 起个名字吧~"));
//						sendCreatePlayerFail(session);
						return;
					}
					
//					if(StringUtil.getLength(nickName) > 12){
//	//					UISystemCP.getInstance().sendFail(session, "用户昵称不可大于12位");
////						sendCreatePlayerFail(session);
//						return;
//					}
//					
//					if(!StringUtil.validateNickName(nickName) ||
//							!ForbidName.checkName(nickName)){
//	//					UISystemCP.getInstance().sendFail(session, "用户昵称含有非法字符");
//						sendCreatePlayerFail(session);
//						return;
//					}
//					
//					if(!ForbidName.checkName(nickName)){
//	//					UISystemCP.getInstance().sendFail(session, "用户昵称含有非法文字");
//						sendCreatePlayerFail(session);
//						return;
//					}
					
					try {
						if(SnapshotManager.getInstance().isAlreadyExsistName(nickName)){
	//						UISystemCP.getInstance().sendFail(session, "昵称已存在");
//							sendCreatePlayerFail(session);
							return;
						}
					} catch (Exception e1) {
						logger.error("用昵称取得玩家对象错误", e1);
					}
					//错误的时候会返回null -> 创建失败
					String serverId = "0";
					
					try{
//						serverId = ba.readUTF();
					}catch(Exception e){
	//					UISystemCP.getInstance().sendFail(session, ResManager.getStr("创建角色失败，请更新客户端"));
						return;
					}
					
					Player player = Player.createPlayer(userId, nickName);
					
					//创建失败
					if(player == null){
//	//					UISystemCP.getInstance().sendFail(session, "创建角色失败");
//						sendCreatePlayerFail(session);
						return;
					}
									
					playerLoginWorld(player, session, userId, uuid, true);
				}catch(Throwable e){
					logger.error("玩家登录异常", e);
				}
			}
		};
		
		LoginThreadPool.getInstance().execute(creat);
	}

	private void playerLoginWorld(Player player, IoSession session, long userId, String uuid, boolean b) {
		if(!player.loginWorld(session, uuid)){
//			UISystemCP.getInstance().sendFail(session, "角色登录失败");
			return;
		}
	}

	@Override
	public void catchException(ReceivedMessage message, Exception e) {
		IoSession session = message.getSession();
//		session.write("操作失败");
	}

}
