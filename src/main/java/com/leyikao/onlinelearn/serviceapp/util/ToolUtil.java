package com.leyikao.onlinelearn.serviceapp.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 工具类
 * 
 */
public class ToolUtil {

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Double StrToDouble(String str) {
		Double res = 0.0;
		try {
			res = Double.valueOf(str);
		} catch (Exception e) {
			res = -0.000987;
		}
		return res;
	}

	public static String FormatNumber(String str) {
		str = new StringBuffer(str).reverse().toString();// 先将字符串颠倒顺序
		String str2 = "";
		int size = (str.length() % 3 == 0) ? (str.length() / 3) : (str.length() / 3 + 1);// 每三位取一长度
		if (str.length() < 3) { // 判断字符串的长度是否大于3
			str2 = str;
		} else {
			for (int i = 0; i < size - 1; i++) {// 前n-1段
				str2 += str.substring(i * 3, i * 3 + 3) + ",";
			}
			for (int i = size - 1; i < size; i++) {// 第n段
				str2 += str.substring(i * 3, str.length());
			}
		}
		return new StringBuffer(str2).reverse().toString();// 再将字符串颠倒顺序
	}

	public static boolean checkNumber(String value) {
		String regex = "^(\\d)+$";
		return value != null && value.matches(regex);
	}

	/**
	 * String数组转int数组
	 * 
	 * @param strs
	 * @author LoadChange
	 */
	public static Integer[] toIntArray(String[] strs) {
		Integer[] result = null;
		if (strs != null && strs.length > 0) {
			result = new Integer[strs.length];
			for (int i = 0; i < strs.length; i++) {
				if (checkNumber(strs[i])) {
					result[i] = Integer.valueOf(strs[i]);
				}
			}
		}
		return result;
	}

	/**
	 * 得到两日期相差几个月
	 *
	 * @param String
	 * @author LoadChange
	 */
	public static long getMonthDiff(Date startDate, Date endDate) {
		long monthday;

		Calendar starCal = Calendar.getInstance();
		starCal.setTime(startDate);
		starCal.set(Calendar.DAY_OF_MONTH, 1);

		int sYear = starCal.get(Calendar.YEAR);
		int sMonth = starCal.get(Calendar.MONTH);
		int sDay = starCal.get(Calendar.DAY_OF_MONTH);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		endCal.set(Calendar.DAY_OF_MONTH, 2);

		int eYear = endCal.get(Calendar.YEAR);
		int eMonth = endCal.get(Calendar.MONTH);
		int eDay = endCal.get(Calendar.DAY_OF_MONTH);

		monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));

		// 这里计算零头的情况，根据实际确定是否要加1 还是要减1
		if (sDay < eDay) {
			monthday = monthday + 1;
		}
		return monthday;
	}

	/**
	 * 检查字符串，如不null或者空串 返回true
	 * 
	 * @param str
	 * @return
	 */
	public static boolean strNotNull(String str) {
		return str != null && !"".equals(str.trim());
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String DateFormat(Date date, String formatStr) {
		if (date != null && strNotNull(formatStr)) {
			SimpleDateFormat format = new SimpleDateFormat(formatStr);
			return format.format(date);
		} else {
			return "";
		}
	}

	public static String DateFormat(Timestamp date, String formatStr) {
		Date d1 = new Date();
		d1 = date;
		return DateFormat(d1, formatStr);
	}

	public static Date strToDate(String timeStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date DateAdd(Date date, int addNum) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, addNum);
		return c.getTime();
	}



	public static String GetConfigForKey(String key) {
		Properties prop = new Properties();
		try {
			InputStream in = ToolUtil.class.getClassLoader().getResourceAsStream("/com/zcb/config/config.properties");
			prop.load(in);
			return prop.getProperty(key).trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

