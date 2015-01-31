package mina.main;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import mina.player.Player;
import mina.util.DateUtil;
import mina.world.World;

import org.apache.log4j.Logger;

public class SaveService {
	private static final Logger logger = Logger.getLogger(SaveService.class);

	private static final long SLEEP = 30 * DateUtil.MINUTE_TIME;

	private static final SaveService instance = new SaveService();

	public static SaveService getService() {
		return instance;
	}

	private Map<Long, Player> needSaveMap = new ConcurrentHashMap<Long, Player>();
	private Map<Long, Player> errorMap = new ConcurrentHashMap<Long, Player>();
	private boolean shouldRun = true;
	private Worker worker;
	private Thread thread;

	private SaveService() {
		worker = new Worker();
		thread = new Thread(worker);
		thread.setName("saveWorker");
		thread.start();
	}

	public void stop() {
		shouldRun = false;
	}

	public void addToError(Player player) {
		if (this.errorMap.containsKey(player.getId())) {
			return;
		}
		this.errorMap.put(player.getId(), player);
	}

	class Worker implements Runnable {

		public void run() {
			logger.info("保存服务开启");

			while (shouldRun) {
				try {
					Thread.sleep(SLEEP);

					Map<Long, Player> players = World.getInstance().getPlayers();

					int worldPlayerCount = players.size();

					for (Player p : players.values()) {
						if (p == null) {
							continue;
						}

						Player player = (Player) p;
						try {
							// GameSaver.getInstance().save(player);
							// player.savePlayer(true);
//							player.save();
						} catch (Throwable inner) {
							logger.error("save player " + p.getName() + " error!", inner);
							try {
								if (!errorMap.containsKey(player.getId())) {
									errorMap.put(player.getId(), player);
								}
							} catch (Throwable t) {
								logger.error("add to error error" + p.getName(), t);
							}
						}
					}

					int errorCount = errorMap.size();

					Iterator<Player> it = errorMap.values().iterator();
					while (it.hasNext()) {
						Player player = (Player) it.next();
						try {
//							GameSaver.getInstance().save(player);
							// player.savePlayer(true);
							it.remove();
						} catch (Throwable t) {
							logger.error("add to error error", t);
						}
					}

					int remainCount = errorMap.size();

					logger.info("保存世界玩家和错误玩家[W:" + worldPlayerCount + "][E:" + errorCount + "][R:" + remainCount + "]");
				} catch (Throwable outter) {
					logger.error("SaveService error", outter);
				}
			}

			logger.info("退出保存线程, 未保存成功玩家数量[" + errorMap.size() + "]");
			if (errorMap.size() > 0) {
				logger.info("尝试保存玩家信息到文件...");

				// TODO
			}

		}
	}
}
