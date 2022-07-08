package com.blog.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
	/**
	 * 得到当前到秒的时间字符串
	 */
	public static String getCurrentDateStr() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		return sdf.format(date);
	}
	
	public static String formatDate(Date date,String format) {
		String result="";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if(date!=null) {
			result=sdf.format(date);
		}
		return result;
	}
}
