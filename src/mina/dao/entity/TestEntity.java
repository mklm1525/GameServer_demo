package mina.dao.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestEntity {
	
	private long id;
	private long userId;
	private JSONObject jsonData;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public JSONObject getJsonData() {
		return jsonData;
	}
	public void setJsonData(JSONObject jsonData) {
		this.jsonData = jsonData;
	}
	
	public void setData(String data){
		this.jsonData = JSON.parseObject(data);
	}
	
	public String getData(){
		return jsonData.toString();
	}

}
