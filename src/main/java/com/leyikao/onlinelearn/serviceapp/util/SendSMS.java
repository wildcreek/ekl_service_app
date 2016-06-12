package com.leyikao.onlinelearn.serviceapp.util;

import com.cloopen.rest.sdk.CCPRestSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;



public class SendSMS {
	private final Logger logger = LoggerFactory.getLogger(SendSMS.class);
	public static final String RIGHT_CODE = "000000";
	
	private SendSMS(){
	}
	
	public static SendSMS create(){
		return new SendSMS();
	}
	
	public String send(String mobilePhone, String verificationCode){
		CCPRestSDK restAPI = new CCPRestSDK();
		
		String ipaddr = ResourceHelper.getProperty("ccp.sms.ipaddr");
		String port = ResourceHelper.getProperty("ccp.sms.port");
		restAPI.init(ipaddr, port);
		
		String accountSid = ResourceHelper.getProperty("ccp.sms.account.sid");
		String authToken = ResourceHelper.getProperty("ccp.sms.auth.token");
		restAPI.setAccount(accountSid, authToken);
		
		
		restAPI.setAppId(ResourceHelper.getProperty("ccp.sms.app.id"));
		
		String timeOut = ResourceHelper.getProperty("ccp.sms.verification.code.timeout");
		String templateId = ResourceHelper.getProperty("ccp.sms.template.id");
		
		HashMap<String, Object> result = null;
		result = restAPI.sendTemplateSMS(mobilePhone, templateId ,new String[]{verificationCode, timeOut});
		String statusCode = result.get("statusCode") != null ? result.get("statusCode").toString() : "";
		if (!RIGHT_CODE.equals(result.get("statusCode"))) {
			logger.error("手机号=" + mobilePhone + " 验证码："
					+ verificationCode + " 错误码=" + result.get("statusCode")
					+ " 错误信息= " + result.get("statusMsg"));
		}
		return statusCode;
	}
}
