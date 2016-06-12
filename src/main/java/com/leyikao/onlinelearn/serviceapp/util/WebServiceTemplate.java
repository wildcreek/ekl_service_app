package com.leyikao.onlinelearn.serviceapp.util;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 取出webservice中的公共操作
 * @author Administrator
 *
 */
public abstract class WebServiceTemplate {
	protected final Logger logger = LoggerFactory.getLogger(WebServiceTemplate.class);
	
	private String requestJson;
	private String templateJson;
	protected Map<?, ?> requestJsonMap;
	private Map<?, ?> templateJsonMap;
	
	private Map<String, Object> responseMap = new HashMap<>();
	protected Map<String, Object> data = new HashMap<>();
	protected String notice = "";
	protected boolean isSuccess = false;
	
	private ObjectMapper objectMapper;
	private MessageSourceHelper messageSourceHelper;
	
	private String requestURI;
	public WebServiceTemplate(String requestJson, String templateJson, String requestURI){
		logger.info("request jsonInfo: " + requestJson);
		this.requestJson = requestJson;
		this.templateJson = templateJson;
		this.requestURI = requestURI;
		this.objectMapper = SpringUtil.getBean("objectMapper", ObjectMapper.class);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		this.messageSourceHelper = SpringUtil.getBean("messageSourceHelper", MessageSourceHelper.class);
	}
	
	public Map<String, Object> execute() throws JsonParseException, JsonMappingException, IOException{
		Instant start = Instant.now();
		init();
		boolean isParameterRight = validation(requestJsonMap, templateJsonMap);
		if (isParameterRight){
			boolean isException = false;
			try {
				business();
			} catch (Exception e) {
				isException = true;
				logger.error(e.getMessage(), e);
			}
			String successNotice = messageSourceHelper.getMessage("common.success");
			if (isException){
				notice = messageSourceHelper.getMessage("common.network.anomaly");
			} else if (StringUtils.isEmpty(notice) || successNotice.equals(notice)){
				isSuccess = true;
			}
		} else {
			notice = messageSourceHelper.getMessage("common.parameter.not.right");
		}
		buildResponseInfo();
		Instant end = Instant.now();
		long responseTime = end.toEpochMilli() - start.toEpochMilli();
		if (responseTime > 1000){
			logger.error(requestURI + ": 共耗时：" + responseTime + "(毫秒)");
		} else {
			logger.info(requestURI + ": 共耗时：" + responseTime + "(毫秒)");
		}
		logger.debug(responseMap.toString());
		return responseMap;
	}
	
	private void init() throws JsonParseException, JsonMappingException, IOException{
		this.requestJsonMap = objectMapper.readValue(requestJson, Map.class);
		this.templateJsonMap = objectMapper.readValue(templateJson, Map.class);
	}
	
	/**
	 * 验证json模板中的key在请求的json中是否存在
	 * @param requestMap
	 * @param templateMap
	 * @return
	 */
	private boolean validation(Map<?, ?> requestMap, Map<?, ?> templateMap){
		boolean isRight = false;
		for (Map.Entry<?, ?> entry : templateMap.entrySet()){
			isRight = false;
			String templateKey = entry.getKey().toString();
			Object templateValue = entry.getValue();
			if (templateValue == null){
				throw new RuntimeException(templateKey + " of value can't null");
			}
			
			Object requestValue = requestMap.get(templateKey);
			if (requestValue == null) {
				break;
			}
			
			if (templateValue.getClass().getName().equals(requestValue.getClass().getName())){
				if (requestValue instanceof Map){
					isRight = validation((Map<?, ?>)requestValue, (Map<?, ?>)templateValue);
				} else if (requestValue instanceof List){
					List<?> templateList = (List<?>)templateValue;
					if (templateList.size() == 0){
						throw new RuntimeException(templateKey + " of arry size can't is zero");
					}
					
					List<?> requestList = (List<?>)requestValue;
					if (requestList.size() == 0){ 
						break;
					}
					
					for (Object obj : requestList){
						if (obj instanceof Map){
							isRight = validation((Map<?, ?>)obj, (Map<?, ?>)templateList.get(0));
						} else {
							break;
						}
					}
				} else {
					isRight = true;
				}
			} else {
				break;
			}
			
			// 只要有一个key不存在于templateJson，就能确定验证不通过
			if (isRight == false){
				break;
			}
		}
		
		// 不需要请求参数
		if (templateMap.size() == 0){
			isRight = true;
		}
		
		return isRight;
	}
	
	protected abstract void business();
	
	private void buildResponseInfo(){
		responseMap.put("result", isSuccess ? "1" : "0");
		responseMap.put("message", notice);
		responseMap.put("data", data);
		logger.info("result: " + responseMap);
	}
}
