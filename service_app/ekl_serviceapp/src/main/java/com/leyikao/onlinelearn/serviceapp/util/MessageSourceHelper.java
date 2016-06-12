package com.leyikao.onlinelearn.serviceapp.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class MessageSourceHelper {
	@Autowired
	private MessageSource messageSource;
	
	public String getMessage(String code){
		return messageSource.getMessage(code, null, null);
	}
	
	public String getMessage(String code, Object[] args, Locale locale){
		return messageSource.getMessage(code, args, locale);
	}
	
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale){
		return messageSource.getMessage(code, args, defaultMessage, locale);
	}
	
	
}
