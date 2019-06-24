package com.haier.rrs.htms.sdk.common;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class DateUtil implements Serializable {

	public static final String DATEFORMAT = "yyyy-MM-dd";
	public static final String DATEFORMAT2 = "yyyy.MM.dd";
	public static final String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIMEFORMAT2 = "HH:mm:ss";
	public static final String TIMEFORMAT3 = "yyyyMMddHHmmss";
	public static final String DATEFORMATCl = "yyyyMMdd";
	public static final String TIMEFORMATCl = "HHmmss";
	public static final Integer CUTLENTH = 3;
    /** S：秒 M：分 H：小时 D:天**/
	public static final String S = "S";
    public static final String H = "H";
    public static final String M = "M";
    public static final String Y = "Y";


	/**
	 *  
	 */
	private static final long serialVersionUID = -8018614666414610187L;

	/**
	 * 获取date类型格式化日期，formt格式化类型，dateStr字符型日期
	 * 
	 * @param formt
	 *            时间格式
	 * @param dateStr
	 *            时间字符串
	 * @return Date 时间date
	 */
	public static Date getDate(String formt, String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formt);
		try {
			if (StringUtils.isEmpty(dateStr)) {
				return sdf.parse(sdf.format(new Date()));
			} else {
				return strToDate(dateStr, formt);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param dateStr
	 *            日期
	 * @param timeStr
	 *            时间
	 * @param formt
	 *            格式化类型
	 * @return Date
	 */
	public static Date getDate(String dateStr, String timeStr, String formt) {
		if ("0000-00-00".equals(dateStr) || "0000.00.00".equals(dateStr) || StringUtils.isEmpty(dateStr)) {
			return null;
		}
		return strToDate(dateStr + " " + (StringUtils.isEmpty(timeStr) ? "00:00:00" : timeStr), formt);
	}

	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	public static String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		return sdf.format(new Date());
	}

	public static String getExpiryDate() {
		String today = getDate();
		return (Integer.parseInt(today.split("-")[0]) + 50) + "-" + today.split("-")[1] + "-" + today.split("-")[2];
	}

	public static Date convertToDate(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			return sdf.parse(s);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 字符串转换为Date
	 * 
	 * @param dateStr
	 *            - 时间字符串
	 * @param formatType
	 *            - 该字符串格式
	 **/
	public static Date strToDate(String dateStr, String formatType) {
		try {
			if (StringUtils.isBlank(dateStr))
				return null;
			if (StringUtils.isBlank(formatType))
				formatType = DATEFORMAT;
			DateFormat sdf = new SimpleDateFormat(formatType);
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Date转换为字符串
	 * 
	 * @param date
	 *            - 时间
	 * @param formatType
	 *            - 该字符串格式
	 **/
	public static String dateToStr(Date date, String formatType) {
		try {
			if (date == null)
				return "";
			if (StringUtils.isBlank(formatType))
				formatType = DATEFORMAT;
			DateFormat sdf = new SimpleDateFormat(formatType);
			return sdf.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 时间字符串增加天数
	 * 
	 * @param dateStr
	 *            - 时间字符串
	 * @param addDateNum
	 *            - 增加天数
	 * @param formatType
	 *            - 该字符串格式
	 **/
	public static String addDateStr(String dateStr, Integer addDateNum, String formatType) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(strToDate(dateStr, null));
		if (addDateNum == null)
			addDateNum = 1;
		calendar.add(Calendar.DATE, addDateNum);
		return dateToStr(calendar.getTime(), formatType);
	}

	/**
	 * 时间类型增加天数
	 * 
	 * @param dateStr
	 *            - 时间字符串
	 * @param addDateNum
	 *            - 增加天数
	 * @param formatType
	 *            - 该字符串格式
	 **/
	public static Date addDate(Date date, Integer addDateNum) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		if (addDateNum == null)
			addDateNum = 1;
		calendar.add(Calendar.DATE, addDateNum);
		return calendar.getTime();
	}

    /**
     * 时间类型增加天数
     * @param date
     * @param addDateNum
     * @return
     */
	public static Date addHour(Date date, Integer addDateNum) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		if (addDateNum == null){
			addDateNum = 1;
		}
		calendar.add(Calendar.HOUR, addDateNum);
		return calendar.getTime();
	}

	/**
	 * 判断时间是否在时间段内 * @param date 当前时间 yyyy-MM-dd HH:mm:ss @param strDateBegin
	 * 开始时间 00:00:00 @param strDateEnd 结束时间 00:05:00
	 * 
	 */
	public static boolean isInDate(Date date, String strDateBegin, String strDateEnd) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String strDate = sdf.format(date);
		// 截取当前时间时分秒
		int strDateH = Integer.parseInt(strDate.substring(0, 2));
		int strDateM = Integer.parseInt(strDate.substring(3, 5));
		int strDateS = Integer.parseInt(strDate.substring(6, 8));
		// 截取开始时间时分秒
		int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
		int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
		int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));
		// 截取结束时间时分秒
		int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
		int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
		int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));
		if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
			// 当前时间小时数在开始时间和结束时间小时数之间
			if (strDateH > strDateBeginH && strDateH < strDateEndH) {
				return true;
				// 当前时间小时数等于开始时间小时数，分钟数在开始和结束之间
			} else if (strDateH == strDateBeginH && strDateM >= strDateBeginM && strDateM <= strDateEndM) {
				return true;
				// 当前时间小时数等于开始时间小时数，分钟数等于开始时间分钟数，秒数在开始和结束之间
			} else if (strDateH == strDateBeginH && strDateM == strDateBeginM && strDateS >= strDateBeginS && strDateS <= strDateEndS) {
				return true;
				// 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数
			} else if (strDateH >= strDateBeginH && strDateH == strDateEndH && strDateM <= strDateEndM) {
				return true;
				// 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数等于结束时间分钟数，秒数小等于结束时间秒数
			} else if (strDateH >= strDateBeginH && strDateH == strDateEndH && strDateM == strDateEndM && strDateS <= strDateEndS) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;

		}

	}

	public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);

		Calendar begin = Calendar.getInstance();
		begin.setTime(beginTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断les月结时间 用户干线记账
	 * 
	 * @return true:les月结|false:les不月结
	 * @param beginTime
	 *            执行开始时间
	 * @param hour
	 *            执行时间（小时）0-如果是0不执行
	 * 
	 */
	public static boolean accountCalendars(String beginTime, String hour) throws ParseException {
		if ("0".equals(hour)) {// 判断执行时间是否为0
			return false;
		}
		Date ds = new Date();
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (DateUtil.isLastDayOfMonth(ds)) {// 判断是月末
			SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
			String ds1 = sdfs.format(ds);
			String dq = ds1.concat(" ").concat(beginTime);
			// String d=sdf.format(System.currentTimeMillis());
			long timeNow = sdf.parse(sdf.format(ds.getTime())).getTime();// 当前时间
			long timeLose = sdf.parse(dq).getTime() + Integer.valueOf(hour).intValue() * 60 * 60 * 1000;// 结束时间
			long begindate = sdf.parse(dq).getTime();// 开始时间
			if (timeNow >= begindate && timeNow <= timeLose) {
				return true;
			}
		} else if (c.get(Calendar.DATE) == 1) {
			SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
			String ds1 = sdfs.format(ds);
			String dq = ds1.concat(" ").concat(beginTime);
			c.setTime(sdfs.parse(dq));
			int day = c.get(Calendar.DATE);
			c.set(Calendar.DATE, day - 1);
			String dayBefore1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
			String dayBefore = dayBefore1.substring(0, 10).concat(" ").concat(beginTime);
			long timeNow = sdf.parse(sdf.format(ds.getTime())).getTime();// 当前时间
			long timeLose = sdf.parse(dayBefore).getTime() + Integer.valueOf(hour).intValue() * 60 * 60 * 1000;// 结束时间
			long begindate = sdf.parse(dayBefore).getTime();// 开始时间
			if (timeNow >= begindate && timeNow <= timeLose) {
				return true;
			}
		}

		return false;

	}

	/**
	 * 判断给定日期是否为月末的一天
	 * 
	 * @param date
	 * @return true:是|false:不是
	 */
	public static boolean isLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
		if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * 获取系统当前周
	 * 
	 * @return
	 */

	public static Integer getCalendarWeek() {
		Calendar calendar = Calendar.getInstance();
		int i = calendar.get(Calendar.WEEK_OF_YEAR);
		return Integer.valueOf(i);
	}

	/**
	 * 获取两个时间间隔，默认返回毫秒 startDate 开始时间 endDate 结束时间 type 时间间隔类型：S：秒 M：分 H：小时 D:天
	 */
	public static Long getTimeCell(Date startDate, Date endDate, String type) {
		Long times = endDate.getTime() - startDate.getTime();
		if ("S".equals(type)) {
			return times / 1000;
		} else if ("M".equals(type)) {
			return times / (1000 * 60);
		} else if ("H".equals(type)) {
			return times / (1000 * 60 * 60);
		} else if ("D".equals(type)) {
			return times / (1000 * 60 * 60 * 24);
		}
		return times;
	}

	/**
	 * 时间类型增加分钟 ,默认加一分钟
	 * 
	 * @param date
	 *            - 时间
	 * @param addMinNum
	 *            - 增加分钟数
	 **/
	public static Date addMinute(Date date, Integer addMinNum) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		if (addMinNum == null)
			addMinNum = 1;
		calendar.add(Calendar.MINUTE, addMinNum);
		return calendar.getTime();
	}

	/**
	 * 时间类型增加秒数 ,默认加一秒
	 * 
	 * @param date
	 *            - 时间
	 * @param addSecNum
	 *            - 增加秒数
	 **/
	public static Date addSec(Date date, Integer addSecNum) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		if (addSecNum == null)
			addSecNum = 1;
		calendar.add(Calendar.SECOND, addSecNum);
		return calendar.getTime();
	}

	/**
	 * 获取系统当前年
	 * 
	 * @return
	 */
	public static Integer getYearOfWeek() {
		Calendar calendar = Calendar.getInstance();
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		int year = calendar.get(Calendar.YEAR);
		// 处理跨年 EG 2018-12-31 2019年第一个周 对应年份按周走=2019
		if (week < calendar.get(Calendar.WEEK_OF_YEAR)) {
			year += 1;
		}
		return year;
	}

	/**
	 * 使用日历获取下一周周序
	 * 
	 * @return
	 */
	public static Integer getNextWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_MONTH, 1);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 使用日历获取下一周对应的年份
	 * 
	 * @return
	 */
	public static Integer getYearOfNextWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_MONTH, 1);
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		// 处理跨年 EG 2018-12-31 2019年第一个周 对应年份按周走=2019
		if (month == 11 && week < calendar.get(Calendar.WEEK_OF_YEAR)) {
			year += 1;
		}
		return year;
	}

	/**
	 * 根据传入日期（yyMMdd）获取日期对应周
	 * 
	 * @param paramDate
	 * @return
	 * @throws ParseException
	 */
	public static Integer getWeekByParamDate(String paramDate) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		calendar.setTime(simpleDateFormat.parse(paramDate));
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 根据传入日期（yyMMdd）获取日期对应年
	 * 
	 * @param paramDate
	 * @return
	 * @throws ParseException
	 */
	public static Integer getYearByParamDate(String paramDate) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		calendar.setTime(simpleDateFormat.parse(paramDate));
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		// 处理跨年 EG 2018-12-31 2019年第一个周 对应年份按周=2019
		if (month == 11 && week < calendar.get(Calendar.WEEK_OF_YEAR)) {
			year += 1;
		}
		return year;
	}

	/**
	 * 判断输入是否为有效的日期格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isValidDate(String str) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 设置lenient为false.
			// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * 切割時間段
	 *
	 * @param dateType
	 *            交易類型 M/D/H/N -->每月/每天/每小時/每分鐘
	 * @param start
	 *            yyyy-MM-dd HH:mm:ss
	 * @param end
	 *            yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static List<String> cutDate(String dateType, String start, String end) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
			Date dBegin = sdf.parse(start);
			Date dEnd = sdf.parse(end);
			return findDates(dateType, dBegin, dEnd);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static List<String> findDates(String dateType, Date dBegin, Date dEnd) throws Exception {
		List<String> listDate = new ArrayList<>();
		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(dEnd);
		while (calEnd.after(calBegin)) {
			switch (dateType) {
			case "M":
				calBegin.add(Calendar.MONTH, CUTLENTH);
				break;
			case "D":
				calBegin.add(Calendar.DAY_OF_YEAR, CUTLENTH);
				break;
			case "H":
				calBegin.add(Calendar.HOUR, CUTLENTH);
				break;
			case "N":
				calBegin.add(Calendar.SECOND, CUTLENTH);
				break;
			}
			if (calEnd.after(calBegin))
				listDate.add(new SimpleDateFormat(TIMEFORMAT).format(calBegin.getTime()));
			else
				listDate.add(new SimpleDateFormat(TIMEFORMAT).format(calEnd.getTime()));
		}
		return listDate;
	}
	
	public static void findDates2(Date start, Date end) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(start);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(end);
		calBegin.add(Calendar.DAY_OF_YEAR, CUTLENTH);
		if (calEnd.after(calBegin)) {
			String start1 =  sdf.format(start);
			String end1 = sdf.format(calBegin.getTime());
  			System.out.println("开始：" + start1 + " 结束：" + end1 );
			findDates2(calBegin.getTime(),end);
		}else{
  			System.out.println("开始：" + sdf.format(start) + " 结束：" + sdf.format(end) );
		}
	}

	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(TIMEFORMAT);
		String start = "2016-02-01 00:00:00";
		String end = "2016-02-08 23:59:59";
		findDates2(sdf.parse(start),sdf.parse(end));

	}

}
