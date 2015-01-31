/**
 * 
 */
/**
 * @author Tuya
 *
 */
package mina.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import mina.hotfix.Hotfixable;

public class TestData implements Hotfixable{
	
	private static JSON json = new JSONArray();

	@Override
	public boolean addData(JSON json) {
		this.json = json;
		return false;
	}

	@Override
	public boolean containsKey() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean replace() {
		// TODO Auto-generated method stub
		return false;
	}

	public static JSON getJson() {
		return json;
	}

	public static void setJson(JSON json) {
		TestData.json = json;
	}
	
}