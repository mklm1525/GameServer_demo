package mina.dao.imp;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import mina.dao.PlayerDao;
import mina.db.HibernateSessionFactory;
import mina.player.Player;
import mina.player.PlayerSnapshot;
import mina.player.SnapshotManager;

public class PlayerDaoImp implements PlayerDao {

	@Override
	public boolean save(Player player) {
		boolean re = HibernateSessionFactory.saveOrUpdateObject(player);
		if(re){
			PlayerSnapshot shot = SnapshotManager.getInstance().getPlayerSnapshot(player.getId());
			if(shot == null){//新建的人物，快照里边没有此对象
				shot = new PlayerSnapshot(player.getId(), player.getUserId(), player.getName(),
						player.getLevel(), player.getExp(), player.getAction(), player.getMoney(), player.getGold());
			}else{
				shot.update(player.getId(), player.getUserId(), player.getName(),
						player.getLevel(), player.getExp(), player.getAction(), player.getMoney(), player.getGold());
			}
			//都更新
			SnapshotManager.getInstance().addPlayerSnapshot(shot);
		}
		return re;
	}

	@Override
	public Player getPlayer(long userId) {
		if(userId < 0){
			return null;
		}
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from Player as p where p.userId=?");
			query.setLong(0, userId);
			
			Player player = (Player) query.setMaxResults(1).uniqueResult();
			if(player == null){
				return null;
			}
			
			tx.commit();
			return player;
		}finally {
			session.close();
		}
	}

	@Override
	public List<PlayerSnapshot> getAllPlayerSnapshots() {
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("select new mina.player.PlayerSnapshot(id,userId,name,level,exp,action,money,gold) from Player");
			
			List<PlayerSnapshot> snapshots = (List<PlayerSnapshot>) query.list();
//			for (Player p : players){
//				PlayerSnapshot snapshot = new PlayerSnapshot(p);
//				snapshots.add(snapshot);
//			}
			tx.commit();
			return snapshots;
		}finally {
			session.close();
		}
	}

}
