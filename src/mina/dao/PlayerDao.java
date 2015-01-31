package mina.dao;

import java.util.List;

import mina.player.Player;
import mina.player.PlayerSnapshot;

public interface PlayerDao {
	
	public boolean save(Player player);
	
	public Player getPlayer(long userId);

	public List<PlayerSnapshot> getAllPlayerSnapshots();

}
