package com.leyikao.onlinelearn.serviceapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class EmojiUtils {
	
	private static Pattern emoji = Pattern.compile (
			"[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]" ,
			Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
  
	/**
	 * 检测是否有emoji字符
	 * @param source
	 * @return
	 */
    public static boolean containsEmoji(String source) {
        if (StringUtils.isEmpty(source)) {
            return false;
        }
        return source.chars().filter(EmojiUtils::isEmojiCharacter).limit(1).count() > 0;
    }
 
	private static boolean isEmojiCharacter(int codePoint) {
		Matcher emojiMatcher = emoji.matcher((char)codePoint + "");
		return emojiMatcher.find();
	}
 
    /**
     * 过滤emoji 或者 其他非文字类型的字符
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
 
        if (!containsEmoji(source)) {
            return source;
        }
        
        StringBuffer sb = new StringBuffer(source);
        source.chars().forEach(p -> {
        	if (isEmojiCharacter(p)){
        		int index = sb.indexOf((char)p + "");
        		if (index > 0){
        			sb.deleteCharAt(index);
        		}
        	}
        });
        
        return sb.toString();
    }
    
    public static void main(String[] args){
    	String str = "\\xF0\\x9F\\x98\\x8F\\xF0\\x9F是不是啊☺.,!?`':;{}[]|\\\"-_+=*&^%$#@!~1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    	System.out.println(str);
    	System.out.println(filterEmoji(str));
    }

}
