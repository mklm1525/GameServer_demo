package mina.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import mina.world.World;

import org.apache.log4j.Logger;

public class DateUtil {

	private static Logger logger = Logger.getLogger(DateUtil.class);

	public static final long TIME_ZONE = TimeZone.getDefault().getRawOffset();

	public static final long SECOND_TIME = 1000;
	public static final long MINUTE_TIME = 60 * SECOND_TIME;
	public static final long HOUR_TIME = 60 * MINUTE_TIME;
	public static final long DAY_TIME = 24 * HOUR_TIME;
	public static final long WEEK_TIME = 7 * DAY_TIME;
	
	/**
	 * 获取天数
	 * @param date
	 * @return
	 */
	public static long getDayCount(Date date){
		return getDayCount(date.getTime());
	}
	
	/**
	 * 获取天数
	 * @param time
	 * @return
	 */
	public static long getDayCount(long time){
		return (time + TIME_ZONE) / DAY_TIME;
	}
	
	/**
	 * 获取周数
	 * @param date
	 * @return
	 */
	public static long getWeekCount(Date date){
		return getWeekCount(date.getTime());
	}
	
	/**
	 * 获取周数
	 * @param time
	 * @return
	 */
	public static long getWeekCount(long time){
		
		//周一凌晨0点(2014-05-05 00:00:00)
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, Calendar.MAY);
		cal.set(Calendar.DAY_OF_MONTH, 5);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		long t1 = cal.getTimeInMillis();
		cal.setTimeInMillis(time);
		long t2 = cal.getTimeInMillis();
		
		return (t2 - t1) / WEEK_TIME;
	}
	
	/**
	 * 获取当前日期
	 * @return
	 */
	public static String getCurrentAllTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		StringBuffer now = new StringBuffer();
		now.append(c.get(Calendar.YEAR));
		now.append('-');
		now.append(c.get(Calendar.MONTH) + 1);
		now.append('-');
		now.append(c.get(Calendar.DAY_OF_MONTH));
		now.append('_');
		now.append(c.get(Calendar.HOUR_OF_DAY)<10?"0"+c.get(Calendar.HOUR_OF_DAY):c.get(Calendar.HOUR_OF_DAY));
		now.append(':');
		now.append(c.get(Calendar.MINUTE)<10?"0"+c.get(Calendar.MINUTE):c.get(Calendar.MINUTE));
		now.append(':');
		now.append(c.get(Calendar.SECOND)<10?"0"+c.get(Calendar.SECOND):c.get(Calendar.SECOND));
		return now.toString();
	}

//	/**
//	 * 初始化时间，将一段时间显示成（xx天xx小时xx分钟）
//	 * @param time
//	 * @return
//	 */
//	public static String formatTime(long time){
//		StringBuffer sb = new StringBuffer();
//		sb.append(time / DAY_TIME == 0 ? "" : time / DAY_TIME + ResManager.getStr("天"));
//		time %= DAY_TIME;
//		sb.append(time / HOUR_TIME == 0 ? "" : time / HOUR_TIME + ResManager.getStr("小时"));
//		time %= HOUR_TIME;
//		sb.append(time / MINUTE_TIME == 0 ? ResManager.getStr("1分钟") : time / MINUTE_TIME + ResManager.getStr("分钟"));
//		return sb.toString();
//	}
	
//	/**
//	 * 获取当前日期
//	 * @return
//	 */
//	public static String getCurrentTime() {
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date(WorldBase.getCurrentTime()));
//		StringBuffer now = new StringBuffer();
//		now.append(c.get(Calendar.MONTH) + 1);
//		now.append(ResManager.getStr("月"));
//		now.append(c.get(Calendar.DAY_OF_MONTH));
//		now.append(ResManager.getStr("日"));
//		now.append(c.get(Calendar.HOUR_OF_DAY));
//		now.append(ResManager.getStr("时"));
//		now.append(c.get(Calendar.MINUTE));
//		now.append(ResManager.getStr("分"));
//		return now.toString();		
//	}

	/**
	 * 获得当前日期数
	 * @return
	 */
	public static int getCurrentDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		return c.get(Calendar.DAY_OF_MONTH);		
	}
	
	/**
	 * 获取当前小时数
	 * @return
	 */
	public static int getCurrentHour() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
//	public static void main(String[] args){
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date(World.getCurrentTime()));
//		long l1 = c.getTime().getTime();
//		
//		Calendar c2 = Calendar.getInstance();
//		c2.setTime(new Date(World.getCurrentTime()));
//		c2.add(Calendar.MINUTE, 60);
//		long l2 = c2.getTime().getTime();
//		
//		System.out.println((l2 - l1) / HOUR_TIME);
//	}
	
	/**
	 * 获得当前分钟数
	 * @return
	 */
	public static int getCurrentMinute() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		return c.get(Calendar.MINUTE);
	}
	
	/***
	 * 时间
	 * @param d
	 * @param format
	 * @return
	 */
	public static String format(Date d, String format) {
        if (d == null)
            return "";
        SimpleDateFormat myFormatter = new SimpleDateFormat(format);
        return myFormatter.format(d);
    }

    public static long toLong(Date d) {
        if (d==null)
            return 0;
        else
            return d.getTime();
    }

    public static String toLongString(Date d) {
        return "" + toLong(d);
    }
  
    public static Date parse(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = null;
        try {
        	d = sdf.parse(time);
        } catch (Exception e) {
        	logger.error("DateUtil.parse异常", e);
        }
        return d;
    }

    public static String format(Calendar cal, String format) {
        if (cal == null)
            return "";
        SimpleDateFormat myFormatter = new SimpleDateFormat(format);
        return myFormatter.format(cal.getTime());
    }

    
    public static Calendar add(java.util.Date d, int day) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, day);
        return cal;
    }

    public static Date addDate(java.util.Date d, int day) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    public static Date addHourDate(java.util.Date d, int h) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.HOUR, h);
        return cal.getTime();
    }

    public static Calendar addHour(java.util.Date d, int h) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.HOUR, h);
        return cal;
    }

    public static Date addMinuteDate(java.util.Date d, int m) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, m);
        return cal.getTime();
    }

    public static Calendar addMinute(java.util.Date d, int m) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, m);
        return cal;
    }

    public static int compare(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null)
            return -1;
        long r = c1.getTimeInMillis() - c2.getTimeInMillis();
        if (r > 0)
            return 1;
        else if (r == 0)
            return 0;
        else
            return 2;
    }

    public static int compare(Date c1, Date c2) {
        if (c1 == null || c2 == null)
            return -1;
        long r = c1.getTime() - c2.getTime();
        
        if (r > 0)
            return 1;
        else if (r == 0)
            return 0;
        else
            return 2;
    }
    
    public static int datediff(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null)
            return -1;
        long r = c1.getTimeInMillis() - c2.getTimeInMillis();
        r = r / (24 * 60 * 60 * 1000);
        return (int) r;
    }
   
    public static int datediff(Date c1, Date c2) {
        if (c1 == null || c2 == null)
            return -1;
        long r = c1.getTime() - c2.getTime();
        r = r / (24 * 60 * 60 * 1000);
        return (int) r;
    }
   
    public static int datediffMinute(Date c1, Date c2) {
        if (c1 == null || c2 == null)
            return 0;
        long r = c1.getTime() - c2.getTime();
        r = r / (60 * 1000);
        return (int) r;
    }

    public static int datediffMinute(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null)
            return 0;
        long r = c1.getTimeInMillis() - c2.getTimeInMillis();
        r = r / (60 * 1000);
        return (int) r;
    }
    
    public static int getDayCount(int year, int month) {
        int daysInMonth[] = {
                            31, 28, 31, 30, 31, 30, 31, 31,
                            30, 31, 30, 31};
        
        if (1 == month)
            return ((0 == year % 4) && (0 != (year % 100))) ||
                    (0 == year % 400) ? 29 : 28;
        else
            return daysInMonth[month];
    }
//    public static String getYMDDate(Date d)
//    {
//        Calendar c=Calendar.getInstance();
//        c.setTime(d);
//        return getChinessNumber(c.get(Calendar.YEAR))+ResManager.getStr("年")+getChinessNumber((c.get(Calendar.MONTH)+1))+ResManager.getStr("月") + getChinessNumber(c.get(Calendar.DAY_OF_MONTH))+ResManager.getStr("日");
//    }
//    public static String getChinessNumber(int number) {
//        StringBuffer chiness = new StringBuffer();
//        String temp = String.valueOf(number);
//        for (int index = 0; index < temp.length(); index++) {
//            String str = "";
//            switch (temp.charAt(index)) {
//            case '0':
//                str = ResManager.getStr("零");
//                break;
//            case '1':
//                str = ResManager.getStr("一");
//                break;
//            case '2':
//                str = ResManager.getStr("二");
//                break;
//            case '3':
//                str = ResManager.getStr("三");
//                break;
//            case '4':
//                str = ResManager.getStr("四");
//                break;
//            case '5':
//                str = ResManager.getStr("五");
//                break;
//            case '6':
//                str = ResManager.getStr("六");
//                break;
//            case '7':
//                str = ResManager.getStr("七");
//                break;
//            case '8':
//                str = ResManager.getStr("八");
//                break;
//            case '9':
//                str = ResManager.getStr("九");
//                break;
//            }
//            chiness.append(str);
//        }
//        if (number <= 31) {
//            if (number % 10 == 0 && number!=10) {
//                chiness.delete(1, 2);
//                chiness.replace(1, 1, ResManager.getStr("十"));
//            } else if (number > 20) {     
//                chiness.insert(1, ResManager.getStr("十"));
//            } else if (number > 10) {
//                chiness.replace(0, 1, ResManager.getStr("十"));
//            } else if (number == 10) {
//                chiness.delete(0, 2);
//                chiness.insert(0, ResManager.getStr("十"));
//            }
//        }
//        return chiness.toString();
//
//    }
//    public static String getFormatDate(Date d)
//    {
//        Calendar c=Calendar.getInstance();
//        c.setTime(d);
//        return c.get(Calendar.YEAR)+ResManager.getStr("年")+(c.get(Calendar.MONTH)+1)+ResManager.getStr("月") + c.get(Calendar.DAY_OF_MONTH)+ResManager.getStr("日")+"  " +c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+":"+c.get(Calendar.MILLISECOND);
//    }
//    public static String getFormatDataByShort(Date d)
//    {
//        Calendar c=Calendar.getInstance();
//        c.setTime(d);
////        return c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-" + c.get(Calendar.DAY_OF_MONTH)+"  " +(c.get(Calendar.HOUR_OF_DAY)>=10?c.get(Calendar.HOUR_OF_DAY):"0"+c.get(Calendar.HOUR_OF_DAY))+":"+(c.get(Calendar.MINUTE)>=10?c.get(Calendar.MINUTE):"0"+c.get(Calendar.MINUTE));
//        return c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-" + c.get(Calendar.DAY_OF_MONTH);
//    }
//    public static String getFormatDate(Calendar date)
//    {
//        Calendar c=date;
//        return c.get(Calendar.YEAR)+ResManager.getStr("年")+(c.get(Calendar.MONTH)+1)+ResManager.getStr("月") + c.get(Calendar.DAY_OF_MONTH)+ResManager.getStr("日")+"  " +c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+":"+c.get(Calendar.MILLISECOND);
//    }
//    public static String getFormatDate(long l)
//    {
//        Calendar c=Calendar.getInstance();
//        c.setTime(new Date(l));
//        return c.get(Calendar.YEAR)+ResManager.getStr("年")+(c.get(Calendar.MONTH)+1)+ResManager.getStr("月") + c.get(Calendar.DAY_OF_MONTH)+ResManager.getStr("日")+"  " +c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+":"+c.get(Calendar.MILLISECOND);
//    }
//    public static String getFormatDateWithoutMillesecond(long l)
//    {
//        Calendar c=Calendar.getInstance();
//        c.setTime(new Date(l));
//        return c.get(Calendar.YEAR)+ResManager.getStr("年")+(c.get(Calendar.MONTH)+1)+ResManager.getStr("月") + c.get(Calendar.DAY_OF_MONTH)+ResManager.getStr("日")+ "  " +c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
//    }
	public static String getNextDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1); 
		StringBuffer now = new StringBuffer();
		now.append(c.get(Calendar.YEAR));
		now.append('_');
		now.append(c.get(Calendar.MONTH) + 1);
		now.append('_');
		now.append(c.get(Calendar.DAY_OF_MONTH));
		return now.toString();		
	}
	
	public static boolean checkSunday() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		return (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);		 
	}
	
	/**
	 * 获得周几 周日1，星期一2
	 * @return
	 */
	public static int getDayOfWeek(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		return c.get(Calendar.DAY_OF_WEEK);	
	}
	
//	/**
//	 * 取得一个long型时间的时长
//	 * 1000  ->  一秒
//	 * @param time
//	 * @return
//	 */
//	public static String getChineseTime(long time){
//		StringBuffer sb = new StringBuffer();
//		if(time / DAY_TIME > 0){
//			sb.append((int) (time / DAY_TIME)).append(ResManager.getStr("天"));
//			time -= time / DAY_TIME * DAY_TIME;
//		}
//		if(time / HOUR_TIME > 0){
//			sb.append((int) (time / HOUR_TIME)).append(ResManager.getStr("小时"));
//			time -= time / HOUR_TIME * HOUR_TIME;
//		}
//		if(time / MINUTE_TIME > 0){
//			sb.append((int) (time / MINUTE_TIME)).append(ResManager.getStr("分钟"));
//		}
//		if(time / MINUTE_TIME <= 0 && time > 0){
//			sb.append(ResManager.getStr("1分钟"));
//		}
//		return sb.toString();
//	}
	
	/**
	 * 计算已经过的天数
	 * @return
	 */
	public static int calculatePassDay(){
		return (int)((System.currentTimeMillis() + TIME_ZONE) / DAY_TIME); 
	}
	
	/**
	 * 是否是同一天
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isOneDay(long time1, long time2){
		long d1 = (time1 + TIME_ZONE) / DAY_TIME;
		long d2 = (time2 + TIME_ZONE) / DAY_TIME;
		return d1 == d2;
	}
	
	public static long getDayNumber(long time){
		return (time + TIME_ZONE) / DAY_TIME;
	}
	
	/**
	 * 判断每日整点整个游戏更新CD  fish  2012-12-9
	 * @param curTime  当前时间
	 * @param lastTime 上一次时间
	 * @param CDTIME 每日更新在几点进行如22点
	 * @return  0-代表两次时间在同一天内，不用进行更新操作
	 * 			1-代表两次时间在隔日，需进行更新操作
	 * 			2-代表两次时间在隔日及以上，需要进行更新操作
	 */
    public static int isDoingEveryDayCD(long curTime,long lastTime){
    	long currDay = getDayCount(curTime + (24 - World.EVERYDAY_CD_TIME) * HOUR_TIME);
    	long lastDay = getDayCount(lastTime + (24 - World.EVERYDAY_CD_TIME) * HOUR_TIME);
    	if(currDay < lastDay){
    		//最后登录时间比现在靠后 而且不在同一天
    		logger.debug("这个应该不会跑到！内网QA测试除外！");
    		return 1;//这个应该不会跑到
    	}
    	else{
    		return (int)(currDay - lastDay);
    	}
    }
    
    /**
	 * 获得当前指定时间  24小时制
	 * @param hour
	 * @return
	 */
	public static Date getCurHourDate(int hour){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(new Date(System.currentTimeMillis()));
		c2.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH), hour, 0, 0);
		c2.set(Calendar.MILLISECOND, 0);
		return c2.getTime();
	}
	
	public static void main(String[] args){
//		long aaa = DAY_TIME * 25 + HOUR_TIME * 18 + MINUTE_TIME * 45;
//		System.out.println(getChineseTime(aaa));
		if(15118 == (System.currentTimeMillis() + HOUR_TIME * 5) / DAY_TIME){
			System.out.println(false);
		}else{
			System.out.println(true);
		}
		System.out.println((System.currentTimeMillis() + HOUR_TIME * 5) / DAY_TIME);
	}
}
