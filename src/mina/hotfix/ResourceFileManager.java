package mina.hotfix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import mina.data.TestData;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class ResourceFileManager {
	private static final Logger logger = Logger.getLogger(ResourceFileManager.class);
	
	private static final ResourceFileManager manager = new ResourceFileManager();
	public static ResourceFileManager getManager(){
		return manager;
	}
	
	private Queue<Autoloadable> resFiles = new ConcurrentLinkedQueue<Autoloadable>();
	
	private String inter = "chs";
	private Worker worker = null;
	
	private ResourceFileManager(){
		worker = new Worker();
		Thread thread = new Thread(worker);
		thread.setDaemon(true);
		thread.start();
	}
	
	public void setInter(String inter){
		this.inter = inter;
	}
	
	public boolean parseReadableFile(String fileName, Class<? extends Readable> clazz){
		Autoloadable file = new ReadableFile(this.inter, fileName, clazz);
		resFiles.add(file);
		return true;
	}
	
	public boolean parseHotfixableFile(String fileName, Class<? extends Hotfixable> clazz){
		Autoloadable file = new HotfixableFile(this.inter, fileName, clazz);
		resFiles.add(file);
		return true;
	}
	
	public boolean addFile(Autoloadable autoLoadableFile){
		resFiles.add(autoLoadableFile);
		return true;
	}
	
	private class Worker implements Runnable{
		public void run(){
			while(true){
				try{
					Thread.sleep(2000);
					for(Autoloadable f : resFiles){
						try{
							f.checkModifyAndLoad();
						}
						catch(Throwable t){
							logger.error("File Check Modify Error", t);
						}
					}
				}
				catch(Exception e){
					logger.error("ResFile Manager Error",e);
				}
			}
		}
	}

	public void parseJsonArray(String string, Class<? extends Hotfixable> clazz) throws Exception {
		Object o = clazz.newInstance();
		File file = new File(string);
		BufferedReader in = new BufferedReader(new FileReader(file));
		StringBuffer sb = new StringBuffer();
		String str = null;
		while((str = in.readLine()) != null){
			sb.append(str);
		}
		JSON json = JSON.parseArray(sb.toString());
		if(o instanceof Hotfixable){
			if(((Hotfixable)o).containsKey()){
				((Hotfixable)o).replace();
				return;
			}
		}
		((Readable)o).addData(json);
		TestData.getJson();
		
	}
	
}