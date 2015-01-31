package mina.util;

import java.util.Collections;
import java.util.List;

public class RandomUtil {

	public static final int RANDOM_SEED = 100000000;//随机数的种子

	private static java.util.Random ran = new java.util.Random();


	/**************************************************************/
	/*************        基本方法    		***************************/
	/**************************************************************/


	private static int getNumber(){
		return Math.abs(ran.nextInt());
	}
	
	private static long getNumberLong(){
		return Math.abs(ran.nextLong());
	}

	private static int getNumber(int seed){
		if(seed == 0){
			return 0;
		}
		return getNumber() % seed;
	}

	private static int getNumber(int start, int end){
		return start + getNumber(end - start);
	}

	private static int getNumberClose(int seed){
		if(seed == 0){
			return 0;
		}
		return getNumber() % (seed+1);
	}

	private static int getNumberClose(int start, int end){
		return start + getNumberClose(end  - start);
	}
	
	private static long getNumberClose(long seed){
		if(seed <= 0){
			return 0;
		}
		return getNumberLong() % (seed+1);
	}
	

	/**************************************************************/
	/*************        使用方法    		***************************/
	/**************************************************************/


	/**
	 * 获取一个[0,seed)之间随机数
	 * @param seed
	 * @return
	 */
	public static int random(int seed){
		return getNumber(seed);
	}
	
	/**
	 * 获取一个[0,seed]之间随机数
	 * @param seed
	 * @return
	 */
	public static int randomClose(int seed){
		return getNumberClose(seed);
	}
	
	/**
	 * 获取一个[0,seed]之间的随机数
	 * @param seed
	 * @return
	 */
	public static long randomClose(long seed){
		return getNumberClose(seed);
	}
	
	/**
	 * 获取[start,end)之间的随机数
	 * @param start
	 * @param end
	 * @return
	 */
	public static int random(int start,int end){
		if(start == end){
			return start;
		}
		if(end > start){
			return getNumber(start, end);
		} else {
			return getNumber(end,start);
		}
	}

	/**
	 * 获取[start,end]之间的随机数
	 * @param start
	 * @param end
	 * @return
	 */
	public static int randomClose(int start, int end){
		if(start == end){
			return start;
		}
		if(end > start){
			return getNumberClose(start, end);
		} else {
			return getNumberClose(end, start);
		}
	}

	/**
	 * 判定一个概率下是否成功
	 * @param rat(成功的概率,范围在0.01~100)
	 * @return true:成功  false:失败
	 */
	public static boolean canSuccess(double rat){
		if(rat < 0.01 || rat > 100){
			return false;
		}
		int ratRan = (int)(rat * 100);
		int result = randomClose(1, 10000);
		if(result <= ratRan){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 判定几个数据在一个圆桌下，哪个成功
	 * @param value 所有数据的int数组
	 * @return 返回成功的那个数据的数组索引
	 */
	public static int getPizzaRandom(int[] value){
		int sum = 0;
		for(int i = 0; i < value.length; i++){
			sum += value[i];
		}
		sum *= 100;
		int ran = randomClose(1, sum);
		int temp = 0;
		for(int i = 0; i < value.length; i++){
			if(i == value.length - 1){
				return i;
			}
			temp += value[i] * 100;
			if(ran <= temp){
				return i;
			}
		}
		return 0;
	}
	public static int getPizzaRandom(List<Integer> value){
		int sum = 0;
		for(int i = 0; i < value.size(); i++){
			sum += value.get(i);
		}
		sum *= 100;
		int ran = randomClose(1, sum);
		int temp = 0;
		for(int i = 0; i < value.size(); i++){
			if(i == value.size() - 1){
				return i;
			}
			temp += value.get(i) * 100;
			if(ran <= temp){
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * 在1~end之间取一个数，如果value在之间返回true
	 * @param end
	 * @param value
	 * @return
	 */
	public static boolean role(int end, int value){
		int seed2 = randomClose(1, end);
		if((seed2 >= 1) && (seed2 <= value)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 将一个list随机打乱
	 * @param lst
	 */
	public static void randomList(List<?> lst){
		Collections.shuffle(lst);
	}
}