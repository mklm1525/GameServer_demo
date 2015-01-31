package mina.player;

public class PlayerSnapshot {
	
	private long id;
	private long userId;
	private String name;
	private int level;
	private long exp;
	private int action;
	private long money;
	private long gold;
	
	public PlayerSnapshot(long id, long userId, String nickname, int level, long exp, int action, long money, long gold) {
		this.id = id;
		this.userId = userId;
		this.name = nickname;
		this.level = level;
		this.exp = exp;
		this.action = action;
		this.money = money;
		this.gold = gold;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public long getGold() {
		return gold;
	}

	public void setGold(long gold) {
		this.gold = gold;
	}

	public void update(long id, long userId, String nickname, int level, long exp, int action, long money, long gold) {
		this.id = id;
		this.userId = userId;
		this.name = nickname;
		this.level = level;
		this.exp = exp;
		this.action = action;
		this.money = money;
		this.gold = gold;
	}

}
