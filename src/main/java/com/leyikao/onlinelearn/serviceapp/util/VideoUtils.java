package com.leyikao.onlinelearn.serviceapp.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import static sun.plugin.javascript.navig.JSType.Document;


public class VideoUtils {

	//获取视频MP4播放地址
	public static String getMp4XmlUrl(String ccUserId ,String ccVideoId,String key){
		String url= getMp4Url(ccUserId ,ccVideoId,key);
		String mp4XmlStr = APIServiceFunction.HttpRetrieve(url);    
		Document mp4Doc=null;
		try {
			mp4Doc = DocumentHelper.parseText(mp4XmlStr);
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
		Element mp4Root = mp4Doc.getRootElement();
		List mp4Nodes = mp4Root.elements("copy");
		
		String mp4Url= "";
		for (Iterator it = mp4Nodes.iterator(); it.hasNext();) {
		     Element elm = (Element) it.next();
		     Attribute attribute=elm.attribute("quality");
		     String text=attribute.getText();
		     if("10".equals(text)){
		    	 mp4Url= elm.getText();
		     }
		}
		mp4Url = mp4Url.replace("&amp;", "&");
		return mp4Url;
	}
	//获取视频图片地址
	public static String getImgXmlUrl(String ccUserId ,String ccVideoId,String key){
		String url =getImgUrl(ccUserId ,ccVideoId,key);
		String imgXmlStr = APIServiceFunction.HttpRetrieve(url);
		Document imgDoc=null;
		try {
			imgDoc = DocumentHelper.parseText(imgXmlStr);
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
		Element imgElement = imgDoc.getRootElement().element("image");
		String imgUrl= "";
		imgUrl = imgElement.getText();
		return imgUrl;
	}
	/**
	 * @param args
	 */
	private static String getMp4Url(String ccUserId ,String ccVideoId,String key) {		

		Map<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("userid", ccUserId);
		treeMap.put("videoid", ccVideoId);			
		String qs = createQueryString(treeMap);		
		//生成时间片
		long time = new Date().getTime() / 1000;			
		//生成HASH码值	
		String hash =md5(String.format("%s&time=%s&salt=%s", qs, time, key));	
		String url = "http://union.bokecc.com/api/mobile"+"?"+qs+"&time="+time+"&hash="+hash;
		return url;		
	}
	
	private static String getImgUrl(String ccUserId ,String ccVideoId,String key) {		
		
		//举例说明为视频搜索
		Map<String, String> treeMap = new TreeMap<String, String>();
		//查询参数输入
		treeMap.put("userid", ccUserId);
		treeMap.put("videoid", ccVideoId);		

		String qs = createQueryString(treeMap);		
		//生成时间片
		long time = new Date().getTime() / 1000;	
		String hash =md5(String.format("%s&time=%s&salt=%s", qs, time, key));
		/** 批量获取视频 */
		String url = "http://spark.bokecc.com/api/video"+"?"+qs+"&time="+time+"&hash="+hash;

		return url;
	}
	
	/** 以下为THQS算法的相关函数  */	
	
	/**
	 * 功能：用一个Map生成一个QueryString，参数的顺序不可预知。
	 * 
	 * @param queryString
	 * @return
	 */
	public static String createQueryString(Map<String, String> queryMap) {

		if (queryMap == null) {
			return null;
		}

		try {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() == null) {
					continue;
				}
				String key = entry.getKey().trim();
				String value = URLEncoder.encode(entry.getValue().trim(),
						"utf-8");
				sb.append(String.format("%s=%s&", key, value));
			}
			return sb.substring(0, sb.length() - 1);
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 功能：计算字符串的md5值
	 * 
	 * @param src
	 * @return
	 */
	public static String md5(String src) {			
		return digest(src, "MD5");			
	}
	
	/**
	 * 功能：根据指定的散列算法名，得到字符串的散列结果。
	 * 
	 * @param src
	 * @param name
	 * @return
	 */
	private static String digest(String src, String name){
		try {
			MessageDigest alg = MessageDigest.getInstance(name);
			byte[] result = alg.digest(src.getBytes());
			return byte2hex(result);
		} catch (NoSuchAlgorithmException ex) {
			return null;
		}	
	}
	
	/**
	 * 功能：将byte数组转换成十六进制可读字符串。
	 * @param b 需要转换的byte数组
	 * @return 如果输入的数组为null，则返回null；否则返回转换后的字符串。
	 */
	public static String byte2hex(byte[] b) {
		
		if (b == null){
			return null;
		}		
		StringBuilder hs = new StringBuilder();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0" + stmp);				
			else
				hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}	

}
