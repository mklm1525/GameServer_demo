package mina.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ZipUtil {
	
	private static final String CHARSET = "utf-8";
		
	public static byte[] zip(byte[] datas) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
		DeflaterOutputStream dos = new DeflaterOutputStream(out);
		dos.write(datas);
		dos.close();
        return out.toByteArray();
	}
	
	private static byte[] unzip(byte[] datas) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
        ByteArrayInputStream in = new ByteArrayInputStream(datas);  
        InflaterInputStream iin = new InflaterInputStream(in);
        byte[] buffer = new byte[256];  
        int n;  
        while ((n = iin.read(buffer)) >= 0) {
            out.write(buffer, 0, n);  
        }  
        return out.toByteArray();  
	}
}