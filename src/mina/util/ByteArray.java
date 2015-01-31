package mina.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.apache.log4j.Logger;

/**
 * Byte数组
 * @author Administrator
 * 
 */
public class ByteArray {

	private static Logger logger = Logger.getLogger(ByteArray.class);
	
	//===================
	//长度常量
	//===================
	
	//默认长度
    private static final int DEFAULT_SIZE = 256;
    
    //各种类型的长度    
    static final public byte BOOLEAN_SIZE = 1;
    static final public byte BYTE_SIZE    = 1;
    static final public byte CHAR_SIZE    = 2;
    static final public byte SHORT_SIZE   = 2;
    static final public byte INT_SIZE     = 4;
    static final public byte LONG_SIZE    = 8;
    
    //对象默认大小 用于控制
    private int thisSize = DEFAULT_SIZE;
    
    //===================
    //数组指针类似于ByteBuffer
    //position <= limit <= capacity;
    //===================
    
    //当前读位置
    private int position = 0;
    //当前写位置
    private int limit = 0;
    //数组长度
    private int capacity = 0;
    
    //内部数组
    private byte[] data;

    public ByteArray() {
        this(DEFAULT_SIZE);
    }

    public ByteArray(int size) {
    	initWithLength(size);
    	thisSize = size;
    }
    
    private void initWithLength(int size){
    	data = new byte[size];
        position = 0;
        limit = 0;
        capacity = size;
    }

    public ByteArray(byte[] src) {
    	if(src == null){
    		throw new NullPointerException("cannot init with null");
    	}
        data = src;
        position = 0; 
        limit = src.length;
        capacity = src.length;
        thisSize = src.length;
    }
    
    //====================================
    //====================================
    //write方法 在当前limit位置追加
    //limit变化 capacity在长度不够的时候会变化
    //====================================
    //====================================

    /**
     * 检测data数组是否足够长
     * @param length int
     */
    private void ensureCapacity(int length) {
        if (limit + length >= capacity) {
            byte[] tmp = new byte[data.length + (length << 1)];
            System.arraycopy(data, 0, tmp, 0, data.length);
            data = tmp;
            capacity = data.length;
        }
    }

    public void writeBoolean(boolean val) {
        ensureCapacity(BOOLEAN_SIZE);
        data[limit++] = (byte) (val ? 1 : 0);
    }

    public void writeByte(byte val) {
        ensureCapacity(BYTE_SIZE);
        data[limit++] = val;
    }
    
    public void writeByte(int val) {
        writeByte((byte) val);
    }

    public void writeChar(char c) {
        ensureCapacity(CHAR_SIZE);
        data[limit + 1] = (byte) (c >>> 0);
        data[limit + 0] = (byte) (c >>> 8);
        limit += 2;
    }

    public void writeShort(short val) {
        ensureCapacity(SHORT_SIZE);
        data[limit + 1] = (byte) (val >>> 0);
        data[limit + 0] = (byte) (val >>> 8);
        limit += 2;
    }

    public void writeShort(int val) {
        writeShort((short) val);
    }

    public void writeInt(int val) {
        ensureCapacity(INT_SIZE);
        data[limit + 3] = (byte) (val >>> 0);
        data[limit + 2] = (byte) (val >>> 8);
        data[limit + 1] = (byte) (val >>> 16);
        data[limit + 0] = (byte) (val >>> 24);
        limit += INT_SIZE;
    }

    public void writeLong(long val) {
        ensureCapacity(LONG_SIZE);
        data[limit + 7] = (byte) (val >>> 0);
        data[limit + 6] = (byte) (val >>> 8);
        data[limit + 5] = (byte) (val >>> 16);
        data[limit + 4] = (byte) (val >>> 24);
        data[limit + 3] = (byte) (val >>> 32);
        data[limit + 2] = (byte) (val >>> 40);
        data[limit + 1] = (byte) (val >>> 48);
        data[limit + 0] = (byte) (val >>> 56);
        limit += LONG_SIZE;
    }

    public void writeByteArray(byte[] src) {
        if (src == null || src.length == 0) {
            return;
        }
        ensureCapacity(src.length);
        System.arraycopy(src, 0, data, limit, src.length);
        limit += src.length;        
    }

    public void writeUTF(String str) {
    	if(str == null){
    		writeUTF("");
    		return;
    	}
        writeByteArray(getByteArrFromUTF(str));
    }

    public void append(byte[] data){
    	writeByteArray(data);
    }
    
    //====================================
    //====================================
    //read方法 从当前position位置读取
    //position变化
    //====================================
    //====================================

    public boolean readBoolean() {
        return data[position++] != 0;
    }

    public int readUnsignedByte() {
        return data[position++] & 0x00FF;
    }

    public byte readByte() {
        return data[position++];
    }

    public char readChar() {
        char c = (char) (((data[position + 1] & 0xFF) << 0) |
                         ((data[position + 0] & 0xFF) << 8));
        position += CHAR_SIZE;
        return c;
    }

    public short readShort() {
        short s = (short) (((data[position + 1] & 0xFF) << 0) |
                           ((data[position + 0] & 0xFF) << 8));
        position += SHORT_SIZE;
        return s;
    }

    public int readInt() {
        int i = ((data[position + 3] & 0xFF) << 0) |
                ((data[position + 2] & 0xFF) << 8)  |
                ((data[position + 1] & 0xFF) << 16) |
                ((data[position + 0] & 0xFF) << 24);
        position += INT_SIZE;
        return i;
    }

    public long readLong() {
        long l = ((data[position + 7] & 0xFFL) << 0) |
                 ((data[position + 6] & 0xFFL) << 8) |
                 ((data[position + 5] & 0xFFL) << 16) |
                 ((data[position + 4] & 0xFFL) << 24) |
                 ((data[position + 3] & 0xFFL) << 32) |
                 ((data[position + 2] & 0xFFL) << 40) |
                 ((data[position + 1] & 0xFFL) << 48) |
                 ((data[position + 0] & 0xFFL) << 56);
        position += LONG_SIZE;
        return l;
    }

    public byte[] readByteArray(int length) {
        if (length == -1 || position + length > limit) {
            length = limit - position;
        }
        byte[] temp = new byte[length];
        System.arraycopy(data, position, temp, 0, length);
        position += length;
        return temp;
    }

    public String readUTF() {
    	try{
	        int utflen = this.getUnsignedShort(this.position);
	        if (utflen < 0) {
	        	logger.info("bytearray readUTF错误:utflen < 0  utflen=" + utflen);
	            return null;
	        }
	        
	    	int length = utflen + 2;
	    	if(this.remain() < length){
	    		logger.info("bytearray readUTF错误:this.remain() < length this.remain()=" + this.remain() + " length=" + length);
	    		return null;
	    	}
	    	
	        byte[] bytearr = new byte[length];
	        System.arraycopy(this.data, this.position(), bytearr, 0, length);
	//        char[] chararr = null;
	        
	        ByteArrayInputStream bais = new ByteArrayInputStream(bytearr);
	        DataInputStream dis = new DataInputStream(bais);
        
        	String str = dis.readUTF();
        	
        	dis.close();
        	bais.close();
        	
        	//跳过utf
        	this.skip(length);
        	
        	return str;
        }
        catch(Exception e){
        	logger.info("bytearray readUTF错误", e);
        	return null;
        }
        

//        bytearr = readByteArray(utflen);
//                
//        if(utflen > bytearr.length){
//        	return null;
//        }
//        chararr = new char[utflen];
//
//        int c, char2, char3;
//        int count = 0;
//        int chararr_count = 0;
//
//        while (count < utflen) {
//            c = (int) bytearr[count] & 0xff;
//            if (c > 127) {
//                break;
//            }
//            count++;
//            chararr[chararr_count++] = (char) c;
//        }
//
//        while (count < utflen) {
//            c = (int) bytearr[count] & 0xff;
//            switch (c >> 4) {
//            case 0:
//            case 1:
//            case 2:
//            case 3:
//            case 4:
//            case 5:
//            case 6:
//            case 7: /* 0xxxxxxx*/
//                count++;
//                chararr[chararr_count++] = (char) c;
//                break;
//            case 12:
//            case 13: /* 110x xxxx   10xx xxxx*/
//                count += 2;
//                char2 = (int) bytearr[count - 1];
//                chararr[chararr_count++] = (char) (((c & 0x1F) << 6) |
//                        (char2 & 0x3F));
//                break;
//            case 14: /* 1110 xxxx  10xx xxxx  10xx xxxx */
//                count += 3;
//                char2 = (int) bytearr[count - 2];
//                char3 = (int) bytearr[count - 1];
//                chararr[chararr_count++] = (char) (((c & 0x0F) << 12) |
//                        ((char2 & 0x3F) << 6) |
//                        ((char3 & 0x3F) << 0));
//                break;
//            default:
//                break;
//            }
//        }
//        return new String(chararr, 0, chararr_count);
    }
    
    public String readUTFWithException() throws Exception{
        int utflen = this.getUnsignedShort(this.position);
        if (utflen < 0) {
        	logger.info("bytearray readUTF错误:utflen < 0  utflen=" + utflen);
            return null;
        }
        
    	int length = utflen + 2;
    	if(this.remain() < length){
    		logger.info("bytearray readUTF错误:this.remain() < length this.remain()=" + this.remain() + " length=" + length);
    		return null;
    	}
    	
        byte[] bytearr = new byte[length];
        System.arraycopy(this.data, this.position(), bytearr, 0, length);
//        char[] chararr = null;
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytearr);
        DataInputStream dis = new DataInputStream(bais);
    
    	String str = dis.readUTF();
    	
    	dis.close();
    	bais.close();
    	
    	//跳过utf
    	this.skip(length);
    	
    	return str;
    
    }

    //====================================
    //====================================
    //get方法 所有指针均不动
    //====================================
    //====================================

    private int readUnsignedShort() {
        int ch1 = readUnsignedByte();
        int ch2 = readUnsignedByte();
        if ((ch1 | ch2) < 0) {
            return -1;
        }
        return (ch1 << 8) + (ch2 << 0);
    }
    
    public int getUnsignedShort(int offset){
    	int ch1 = this.data[offset++] & 0x00FF;
        int ch2 = this.data[offset++] & 0x00FF;
        if ((ch1 | ch2) < 0) {
            return -1;
        }
        return (ch1 << 8) + (ch2 << 0);
    }
    
    public byte[] subArray(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("start > end");
        }
        byte[] tmp = new byte[end - start];
        System.arraycopy(data, start, tmp, 0, tmp.length);
        return tmp;
    }
    
    public byte getByte(int offset){
    	return data[offset];
    }
    
    public int getInt(int offset){
    	return ((data[offset + 3] & 0xFF) << 0) + ((data[offset + 2] & 0xFF) << 8)
		+ ((data[offset + 1] & 0xFF) << 16) + ((data[offset + 0] & 0xFF) << 24);
    }
    
    public short getShort(int offset){
    	return (short) (((data[offset + 1] & 0xFF) << 0) + ((data[offset + 0] & 0xFF) << 8));
    }
    
    public long getLong(int offset){
    	return ((data[offset + 7] & 0xFFL) << 0) + ((data[offset + 6] & 0xFFL) << 8)
		+ ((data[offset + 5] & 0xFFL) << 16) + ((data[offset + 4] & 0xFFL) << 24)
		+ ((data[offset + 3] & 0xFFL) << 32) + ((data[offset + 2] & 0xFFL) << 40)
		+ ((data[offset + 1] & 0xFFL) << 48) + ((data[offset + 0] & 0xFFL) << 56);
    }
    
    //====================================
    //====================================
    //属性方法
    //====================================
    //====================================
    
    public int remain(){
    	return limit - position;
    }
    
    public int position(){
        return this.position;
    }
    
    public int limit(){
    	return this.limit;
    }
    
    public int size(){
    	return this.limit;
    }
    
    public int capacity(){
    	return this.capacity;
    }

    public void resetPosition(){
        position = 0;
    }
    
    public void close() {
        data = null;
    }
    
    public void compact(){
    	//无需compact
    	if(position == 0){
    		return;
    	}
    	
    	//无数据重置buff
    	if(position >= limit){
    		initWithLength(thisSize);
    		return;
    	}
    	
    	//compact内容
    	int length = capacity - position;
    	byte[] temp = new byte[length];
    	System.arraycopy(data, position, temp, 0, length);    	
    	data = temp;
    	
    	//设置指针
    	limit -= position;
    	capacity -= position;
    	position = 0;    	
    }
    
    public void skip(int offset){
    	position += offset;
    }

    public byte[] toArray() {
    	byte[] temp = new byte[limit];
    	System.arraycopy(data, 0, temp, 0, limit);
        return temp;
    }
    
    //===============================
    //===============================
    //===============================
    
    public void setByte(int offset,byte val){
    	data[offset] = val;
	}
    
	public void setBoolean(int offset, boolean val) {
		data[offset] = (byte) (val ? 1 : 0);
	}

	public void setChar(int offset, char val) {
		data[offset + 1] = (byte) (val >>> 0);
		data[offset + 0] = (byte) (val >>> 8);
	}

	public void setShort(int offset, short val) {
		data[offset + 1] = (byte) (val >>> 0);
		data[offset + 0] = (byte) (val >>> 8);
	}

	public void setInt(int offset, int val) {
		data[offset + 3] = (byte) (val >>> 0);
		data[offset + 2] = (byte) (val >>> 8);
		data[offset + 1] = (byte) (val >>> 16);
		data[offset + 0] = (byte) (val >>> 24);
	}

	public void setLong(int offset, long val) {
		data[offset + 7] = (byte) (val >>> 0);
		data[offset + 6] = (byte) (val >>> 8);
		data[offset + 5] = (byte) (val >>> 16);
		data[offset + 4] = (byte) (val >>> 24);
		data[offset + 3] = (byte) (val >>> 32);
		data[offset + 2] = (byte) (val >>> 40);
		data[offset + 1] = (byte) (val >>> 48);
		data[offset + 0] = (byte) (val >>> 56);
	}
    
    //===============================
    //===============================
    //===============================
    
    public String toString(){
    	return "ByteArray" + position;
    }
    
    public static int[] bytesToInts(byte[] bytes){
        if(bytes == null || bytes.length < 4){
            return null;
        }
        int[] ints = new int[bytes.length >> 2];
        ByteArray ba = new ByteArray(bytes);
        for(int i=0,kk=ints.length; i<kk; i++){
            ints[i] = ba.readInt();
        }
        return ints;
    }
    
    public static byte[] intsToBytes(int[] ints){
        if(ints == null || ints.length <= 0){
            return null;
        }
        byte[] bytes = new byte[ints.length << 2];
        ByteArray ba = new ByteArray(bytes);
        for(int i=0,kk=ints.length; i<kk; i++){
            ba.writeInt(ints[i]);
        }
        return ba.toArray();
    }
    
    public static byte[] getByteArrFromUTF(String str) {
        int strlen = str.length();
        int utflen = 0;
        int c, count = 0;

        for (int i = 0; i < strlen; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                utflen++;
            } else if (c > 0x07FF) {
                utflen += 3;
            } else {
                utflen += 2;
            }
        }

        byte[] bytearr = new byte[utflen + 2];

        bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
        bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);

        int i = 0;
        for (i = 0; i < strlen; i++) {
            c = str.charAt(i);
            if (!((c >= 0x0001) && (c <= 0x007F))) {
                break;
            }
            bytearr[count++] = (byte) c;
        }

        for (; i < strlen; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                bytearr[count++] = (byte) c;

            } else if (c > 0x07FF) {
                bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                bytearr[count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
                bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            } else {
                bytearr[count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
                bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            }
        }
        return bytearr;
    }
    
}