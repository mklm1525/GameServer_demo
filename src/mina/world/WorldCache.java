package mina.world;

import mina.player.Player;

public class WorldCache {
	
	private static WorldCache instance;
	

	public static WorldCache getInstance() {
		if(instance == null){
			instance = new WorldCache();
		}
		return instance;
	}

	public Player get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
