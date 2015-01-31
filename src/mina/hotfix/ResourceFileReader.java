package mina.hotfix;

import mina.util.ByteArray;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import jxl.Sheet;
import jxl.Workbook;



import org.apache.log4j.Logger;

//import antlr.collections.List;

public class ResourceFileReader {
	private static final Logger logger = Logger.getLogger(ResourceFileReader.class);
	
	public static final byte CHAR_VARS_START = ';';
	public static final byte CHAR_COMMENT_START = '/';
	
//	private static final String C = "    ";
	
	private static final ResourceFileReader reader = new ResourceFileReader();
	public static ResourceFileReader getReader(){
		return reader;
	}
	
	private ResourceFileReader(){
		
	}
	
	public void parseBinFile(String inter, String fileName,Class<? extends Readable> clazz) {
		try{
			FileInputStream fis = new FileInputStream(new File(fileName));
			DataInputStream dis = new DataInputStream(fis);
			byte[] b = new byte[fis.available()];
			dis.readFully(b);
			ByteArray ba = new ByteArray(b);
			int rows = ba.readInt();
			int cols = ba.readInt();
			Field[] fields = null;
			for(int i=0;i<rows;i++){
				String[] strs = new String[cols];
				for(int j=0;j<cols;j++){
					strs[j] = ba.readUTF().trim();
				}
				if(strs[0].length() == 0){
					continue;
				}
				if(strs[0].charAt(0) == CHAR_COMMENT_START){
					continue;
				} else if(strs[0].charAt(0) == CHAR_VARS_START){
					fields = getFields(inter, clazz,strs);
				} else {
					parseLine(strs,fields,clazz);
				}
			}
		} catch(Exception e){
			logger.info("读取文件：" + fileName + "出错", e);
		}
	}
	
	public void parseXlsFile(String inter, String fileName,Class<? extends Readable> clazz) {
		try{
			File xlsFile = new File(fileName);
						
			Workbook ework = Workbook.getWorkbook(xlsFile);
			Sheet esheet = ework.getSheet(0);
			
			int cols = esheet.getColumns();
			int rows = esheet.getRows();
			
			Field[] fields = null;
			for(int i=0;i<rows;i++){
				String[] strs = new String[cols];
				for(int j=0;j<cols;j++){
					strs[j] = esheet.getCell(j, i).getContents();
				}
				if(strs[0].length() == 0){
					continue;
				}
				if(strs[0].charAt(0) == CHAR_COMMENT_START){
					continue;
				} else if(strs[0].charAt(0) == CHAR_VARS_START){
					fields = getFields(inter, clazz, strs);
				} else {
					parseLine(strs,fields,clazz);
				}
			}
		} catch(Exception e){
			logger.info("读取文件：" + fileName + "出错", e);
		}
	}
	
	public void parseXlsFile(String inter, InputStream is, Class<? extends Readable> clazz) {
		try{
			Workbook ework = Workbook.getWorkbook(is);
			Sheet esheet = ework.getSheet(0);
			
			int cols = esheet.getColumns();
			int rows = esheet.getRows();
			
			Field[] fields = null;
			for(int i=0;i<rows;i++){
				String[] strs = new String[cols];
				for(int j=0;j<cols;j++){
					strs[j] = esheet.getCell(j, i).getContents();
				}
				if(strs[0].length() == 0){
					continue;
				}
				if(strs[0].charAt(0) == CHAR_COMMENT_START){
					continue;
				} else if(strs[0].charAt(0) == CHAR_VARS_START){
					fields = getFields(inter, clazz,strs);
				} else {
					parseLine(strs,fields,clazz);
				}
			}
			
			logger.debug("读取远程文件成功! ClassName[" + clazz.getName() + "]");
			
		} catch(Exception e){
			logger.info("读取文件：" + clazz.getName() + "出错", e);
		}
	}
	
//	public void parseXlsFileAdv(String fileName,Class<? extends Readable> clazz){
//		try{
//			File xlsFile = new File(fileName);
//						
//			Workbook ework = Workbook.getWorkbook(xlsFile);
//			Sheet esheet = ework.getSheet(0);
//			
//			int cols = esheet.getColumns();
//			int rows = esheet.getRows();
//				
//			String[] fieldStrs = null;
//			StringBuffer buffer = new StringBuffer();
//			
//			buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
//			buffer.append("<Data>\n");
//			
//			for(int i=0;i<rows;i++){
//				
//				String[] strs = new String[cols];
//				for(int j=0;j<cols;j++){
//					strs[j] = esheet.getCell(j, i).getContents();
//				}
//				if(strs[0].length() == 0){
//					continue;
//				}
//				if(strs[0].charAt(0) == CHAR_COMMENT_START){
//					continue;
//				} else if(strs[0].charAt(0) == CHAR_VARS_START){
//					fieldStrs = strs;
//					fieldStrs[0] = fieldStrs[0].substring(1,fieldStrs[0].length()); 
//				} else {
//					buffer.append(C).append("<").append(clazz.getSimpleName()).append(">\n");
//					toXml(fieldStrs, strs, buffer);
//					buffer.append(C).append("</").append(clazz.getSimpleName()).append(">\n");
//				}
//				
//			}
//			buffer.append("</Data>\n");
//			System.err.println(buffer.toString());
//		} catch(Exception e){
//			logger.info("读取文件：" + fileName + "出错", e);
//		}
//	}
//	
//	private void toXml(String[] fieldStrs, String[] values, StringBuffer buffer){
//		if(fieldStrs.length != values.length){
//			return;
//		}
//		String temp = null;
//		for(int i=0; i<values.length; i++){
//			if(fieldStrs[i].contains(".")){
//				int index = fieldStrs[i].indexOf(".");
//				String sub = fieldStrs[i].substring(0, index);
//				if(temp == null){
//					temp = sub;
//					buffer.append(C + C).append("<").append(temp).append(">\n");
//				}
//				
//				String f = fieldStrs[i].substring(index + 1);
//				buffer.append(C + C + C).append("<").append(f).append(">").append(values[i]);
//				buffer.append("</").append(f).append(">\n");
//			}
//			else{
//				if(temp != null){
//					buffer.append(C).append(C);
//					buffer.append("</").append(temp).append(">\n");
//					temp = null;
//				}
//				buffer.append(C).append(C);
//				buffer.append("<").append(fieldStrs[i]).append(">").append(values[i]);
//				buffer.append("</").append(fieldStrs[i]).append(">\n");
//			}
//		}
//		if(temp != null){
//			buffer.append(C).append(C);
//			buffer.append("</").append(temp).append(">\n");
//		}
//	}
	
	protected Field[] getFields(String inter, Class<? extends Readable> clazz,String[] strs) throws Exception{
		Field[] fields = new Field[strs.length];
		for(int i=0;i<strs.length;i++){
			String s = strs[i];
			if(s.length() > 0){
				if(s.charAt(0) == CHAR_VARS_START){
					s = s.substring(1,s.length());
				}
				s = s.trim();
				if(s.endsWith("[]")){
					String tempS = s.substring(0, s.length() - 2);
					Field listField = getFieldAd(clazz, tempS);					
					if(listField.getType() == java.util.List.class){
						s = tempS;
					}
				}
				if(s.contains("[") && s.contains("]")){
					if(inter == null || inter.trim().length() == 0){
						fields[i] = getFieldAd(clazz, s.trim());
						fields[i].setAccessible(true);
					}
					else{
						if(s.contains("[" + inter + "]")){
							int index = s.indexOf("[");
							fields[i] = getFieldAd(clazz, s.trim().substring(0, index));
							fields[i].setAccessible(true);
						}
						else{
							fields[i] = null;
						}
					}
				}
				else{
					fields[i] = getFieldAd(clazz, s.trim());
					fields[i].setAccessible(true);
				}
			}
		}
		return fields;
	}
	
	private Field getFieldAd(Class<?> clazz, String fieldName) throws Exception{
		Field field = null;
		try{
			field = clazz.getDeclaredField(fieldName);
		}
		catch(Exception e){}
		if(field == null){
			field = clazz.getSuperclass().getDeclaredField(fieldName);
		}
		return field;
	}
	
	protected void parseLine(String[] value,Field[] fields,Class<? extends Readable> clazz) throws Exception{
		Object o = clazz.newInstance();
		for(int i=0;i<fields.length;i++){
			Field f = fields[i];
			if(f == null){
				continue;
			} 
			if(f.getType().equals(int.class)){
				f.setInt(o,decodeInt(value[i]));
			} else if(f.getType().equals(double.class)) {
				f.setDouble(o,decodeDouble(value[i]));
			} else if(f.getType().equals(long.class)){
				f.setLong(o, decodeLong(value[i]));
			} else if(f.getType().equals(int[].class)){
				f.set(o,parseIntArray(value,i));
			} else if(f.getType().equals(double[].class)){
				double[] r = new double[value.length-i];
				for(int j=0;j<r.length;j++){
					r[j] = decodeDouble(value[i+j]);
				}
				f.set(o,r);
			} else if(f.getType().equals(String[].class)){
				f.set(o,parseStringArray(value,i));
			} else if(f.getType() == java.util.List.class){
				Method m = clazz.getDeclaredMethod("add" + f.getName(), String.class);
				m.setAccessible(true);
				m.invoke(o, value[i]);
			} 
		    else{
				f.set(o,value[i].replace("\\n", "\n"));
			}
		}
		if(o instanceof Hotfixable){
			if(((Hotfixable)o).containsKey()){
				((Hotfixable)o).replace();
				return;
			}
		}
//		((Readable)o).addData();
	}
	
	protected int decodeInt(String str){
		try{
			return Integer.decode(str);
		} catch(Exception e){
			return 0;
		}
	}
	
	protected double decodeDouble(String str){
		try{
			return Double.parseDouble(str);
		} catch(Exception e){
			return 0;
		}
	}
	
	protected long decodeLong(String str){
		try{
			return Long.parseLong(str);
		}
		catch(Exception e){
			return 0;
		}
	}
	
	protected int[] parseIntArray(String[] value,int startIndex){
		int[] r = new int[value.length-startIndex];
		int length = 0;
		for(int j=0;j<r.length;j++){
			r[j] = decodeInt(value[startIndex+j]);
			if(r[j] != 0){
				length++;
			}
		}
		if(length == r.length){
			return r;
		} else {
			int[] d = new int[length];
			for(int i=0,j=0;i<r.length;i++){
				if(r[i] != 0){
					d[j] = r[i];
					j++;
				}
			}
			return d;
		}
	}
	
	protected String[] parseStringArray(String[] value,int startIndex){
		String[] r = new String[value.length-startIndex];
		System.arraycopy(value, startIndex, r, 0, r.length);
		
		int len = 0;
		for(int i=0;i<r.length;i++){
			if(r[i].length() > 0){
				len ++;
			}
		}
		if(len == r.length){
			return r;
		} else {
			String[] d = new String[len];
			for(int i=0,j=0;i<r.length;i++){
				if(r[i].length() > 0){
					d[j] = r[i];
					j++;
				}
			}
			return d;
		}
	}

}
