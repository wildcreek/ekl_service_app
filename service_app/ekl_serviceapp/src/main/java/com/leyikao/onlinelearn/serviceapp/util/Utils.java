package com.leyikao.onlinelearn.serviceapp.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Utils {
	
	public static class DateParttern{
		public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	}
	
	public static class Regular{
		public final static String PASSWORD = "^(?!\\D+$)(?!\\d+$)[a-zA-Z0-9]{6,12}$";
	}
	
	/**
	 * 题目操作类型
	 * 1-真题, 2-题库, 3-错题, 4-收藏
	 *
	 */
	public static class QuestionOperateType{
		/**
		 * 真题
		 */
		public static final String REAL = "1";
		
		/**
		 * 题库
		 */
		public static final String TEST_DATABASE = "2";
		
		/**
		 * 错题
		 */
		public static final String ERROR = "3";
		
		/**
		 * 收藏
		 */
		public static final String COLLECTION = "4";
	}
	
	public static class AnswerResult{
		public static final String NOT_ANSWERED = "2";
		public static final String RIGHT = "1";
		public static final String ERROR = "0";
	}
	
	
	
	/**
	 * 解析类型
	 *
	 */
	public static class AnalyticalType {
		/**
		 * 所有错题
		 */
		public static final String ALL_ERROR = "1";
		
		/**
		 * 所有题
		 */
		public static final String ALL_QUESTION = "2";
		
		/**
		 * 已答题
		 */
		public static final String ANSWERED = "3";
		
		/**
		 * 未答题
		 */
		public static final String NOT_ANSWERED = "4";
		
		/**
		 * 收藏
		 */
		public static final String COLLECTION = "5";
	}
	
	/**
	 * 根据用户传入的长度，生在一个随机字符串
	 * @param length字符串长度
	 * @throws
	 * @return 返回字符串
	 **/
	public static String getRandomInteger(int length) {
		String str = "1234567890";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(10);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}
	
	public static List<String> subList(List<String> questionIds, int questionCount, int pageNumber){
		List<String> subList = new ArrayList<>();
		
		int size = questionIds.size();
		int totalPage = size / questionCount + ( (size % questionCount) > 0 ? 1 : 0);
		
		if (totalPage >= pageNumber){
			int fromIndex = (pageNumber - 1) * questionCount;
			int toIndex = pageNumber * questionCount > size ? size : pageNumber * questionCount;
			subList.addAll(questionIds.subList(fromIndex, toIndex));
		}
		
		return subList;
	}
	
	public static synchronized String instantToString(Instant instant, String pattern){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return formatter.format(localDateTime);
	}
	
	public static synchronized List<Integer> daysOfWeek(){
		LocalDate localDate = LocalDate.now();
		LocalDate startDate = localDate.minusDays(localDate.getDayOfWeek().getValue());
		LocalDate endDate = startDate.plusDays(startDate.getDayOfWeek().getValue() - 1);
		
		List<Integer> daysOfWeek = new ArrayList<>();
		daysOfWeek.add(endDate.getDayOfMonth());
		while (startDate.isBefore(endDate)){
			daysOfWeek.add(startDate.getDayOfMonth());
			startDate = startDate.plusDays(1);
		}
		
		
		System.out.println(Arrays.toString(daysOfWeek.toArray()));
		
		return daysOfWeek;
	}
	
	public static synchronized Map<Integer, String> weekDisplayNames(MessageSourceHelper messageHelper){
		Map<Integer, String> names = new HashMap<>();
		names.put(1, messageHelper.getMessage("td.calendar.sunday"));
		names.put(2, messageHelper.getMessage("td.calendar.monday"));
		names.put(3, messageHelper.getMessage("td.calendar.tuesday"));
		names.put(4, messageHelper.getMessage("td.calendar.wednesday"));
		names.put(5, messageHelper.getMessage("td.calendar.thursday"));
		names.put(6, messageHelper.getMessage("td.calendar.friday"));
		names.put(7, messageHelper.getMessage("td.calendar.saturday"));
		return names;
	}
	
	private static Map<String, String> nameImagesOfCity = new HashMap<>();
	static {
			nameImagesOfCity.put("北京", "app/province/city_beijing.png");
			nameImagesOfCity.put("上海", "app/province/city_shanghai.png");
			nameImagesOfCity.put("天津", "app/province/city_tianjin.png");
			nameImagesOfCity.put("重庆", "app/province/city_chongqing.png");
			nameImagesOfCity.put("安徽", "app/province/city_anhui.png");
			nameImagesOfCity.put("澳门", "app/province/city_aomen.png");
			nameImagesOfCity.put("福建", "app/province/city_fujian.png");
			nameImagesOfCity.put("甘肃", "app/province/city_gansu.png");
			nameImagesOfCity.put("广东", "app/province/city_guangdong.png");
			nameImagesOfCity.put("广西", "app/province/city_guangxi.png");
			nameImagesOfCity.put("海南", "app/province/city_hainan.png");
			nameImagesOfCity.put("贵州", "app/province/city_guizhou.png");
			nameImagesOfCity.put("河北", "app/province/city_hebei.png");
			nameImagesOfCity.put("河南", "app/province/city_henan.png");
			nameImagesOfCity.put("黑龙江", "app/province/city_heilongjiang.png");
			nameImagesOfCity.put("吉林", "app/province/city_jilin.png");
			nameImagesOfCity.put("辽宁", "app/province/city_liaoning.png");
			nameImagesOfCity.put("湖北", "app/province/city_hubei.png");
			nameImagesOfCity.put("湖南", "app/province/city_hunan.png");
			nameImagesOfCity.put("江苏", "app/province/city_jiangsu.png");
			nameImagesOfCity.put("浙江", "app/province/city_zhejiang.png");
			nameImagesOfCity.put("江西", "app/province/city_jiangxi.png");
			nameImagesOfCity.put("山东", "app/province/city_shandong.png");
			nameImagesOfCity.put("山西", "app/province/city_shanxi.png");
			nameImagesOfCity.put("陕西", "app/province/city_shanxi2.png");
			nameImagesOfCity.put("四川", "app/province/city_sichuan.png");
			nameImagesOfCity.put("香港", "app/province/city_xianggang.png");
			nameImagesOfCity.put("台湾", "app/province/city_taiwan.png");
			nameImagesOfCity.put("内蒙", "app/province/city_neimeng.png");
			nameImagesOfCity.put("云南", "app/province/city_yunnan.png");
			nameImagesOfCity.put("宁夏", "app/province/city_ningxia.png");
			nameImagesOfCity.put("青海", "app/province/city_qinghai.png");
			nameImagesOfCity.put("西藏", "app/province/city_xizang.png");
			nameImagesOfCity.put("新疆", "app/province/city_xinjiang.png");
	}
	
	public static synchronized Map<String, String> cityNameImages(){
		return Collections.unmodifiableMap(nameImagesOfCity);
	}

	public  static  String saveImg(){
		return null;
	}

	public  static  String getUserWebImgUrl(Object picurl){
		if(picurl==null||picurl.toString().trim().length()<=0) return "";
		String bastpath= ResourceHelper.getProperty("sc.image.service.web.path") + "/lykimg";
		return bastpath+"/"+picurl;
	}
	
	public  static  String getWebImgUrl(Object picurl){
		if(picurl==null||picurl.toString().trim().length()<=0) return "";
	
		String bastpath= ResourceHelper.getProperty("sc.image.service.web.path");
		return bastpath+"/"+picurl;
	}
	private static String min="分钟前";
	private static String hos="小时前";
	public static String getSubDateString(Timestamp tim){
		Long ss=(long) (60*1000);
		Long mm=(long)60*60*1000;
		Long hh=(long) (24*60*60*1000);
		Long currenttim=new Date().getTime();
		long subtim=currenttim-tim.getTime();
	if(subtim<ss){
		return "不到1分钟";
	}
	if(subtim>=ss&&subtim<mm){
		return (subtim/(1000*60)+1)+min;
	}
	if(subtim>=mm&&subtim<hh){
		return (subtim/(1000*60*60)+1)+hos;
	}
		return new SimpleDateFormat("yyyy-MM-dd").format(tim);
	}
}
