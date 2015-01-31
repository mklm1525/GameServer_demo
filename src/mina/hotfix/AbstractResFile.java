package mina.hotfix;

import java.io.File;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

public abstract class AbstractResFile implements Autoloadable{
	private static final Logger logger = Logger.getLogger(AbstractResFile.class);
	
	private File handleFile = null;
	
	private String inter = null;
	private String fileName = null;
	
	private long lastModifDTime = 0;
		
	public AbstractResFile(String inter, String fileName){
		this.fileName = fileName;
		this.inter = inter;
	}
	
	public AbstractResFile(String inter, File file){
		this.fileName = file.getPath();
		this.inter = inter;
	}
	
	protected void reload(){
		if(handleFile == null){
			handleFile = new File(fileName);
			if(!handleFile.exists()){
				throw new NullPointerException("文件不存在。 " + fileName);
			}
			updateLastModifDTime();
		}
		else{
			logger.info("重新读取文件 " + fileName + " " + new Timestamp(lastModifDTime));
		}
		if(load(this.inter)){
			logger.debug("读取文件成功! " + this);
			updateLastModifDTime();
		}
		else{
			logger.info("读取文件错误! " + this);
		}
	}
	
	abstract public boolean load(String inter);
	
	public boolean checkModifyAndLoad(){
		if(handleFile.lastModified() != lastModifDTime){
			reload();
			return true;
		}
		return false;
	}
	
	protected void updateLastModifDTime(){
		if(handleFile != null && handleFile.exists()){
			this.lastModifDTime = handleFile.lastModified();
		}
	}
	
	protected String getFileName(){
		return this.fileName;
	}
	
	protected String getSimpleName(){
		return this.handleFile.getName();
	}
	
	public int hashCode(){
		if(fileName == null){
			return 0;
		}
		else{
			return fileName.hashCode();
		}
	}
			
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("FileName[").append(this.fileName).append("]");
		buffer.append("LastModifyTime[").append(new Timestamp(lastModifDTime)).append("]");
		
		return buffer.toString();
	}
}