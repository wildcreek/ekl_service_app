package com.leyikao.onlinelearn.serviceapp.td.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import com.leyikao.onlinelearn.serviceapp.td.dao.IFeedbackDao;
import com.leyikao.onlinelearn.serviceapp.td.dao.IUserDao;
import com.leyikao.onlinelearn.serviceapp.util.MessageSourceHelper;
import com.leyikao.onlinelearn.serviceapp.util.ResourceHelper;
import com.leyikao.onlinelearn.serviceapp.util.SendSMS;
import com.leyikao.onlinelearn.serviceapp.util.Utils;

public class TerminalUserBusiness {
	private final Logger logger = LoggerFactory.getLogger(TerminalUserBusiness.class);
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IFeedbackDao feedbackDao;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private MessageSourceHelper messageHelper;
	
	private static List<String> famousSayings = new ArrayList<>();
	
	/**
	 * app 用户注册
	 * @param mobilePhone
	 * @param password
	 * @return
	 */
	public String registerMobileClient(String mobilePhone, String verificationCode, String password, String nickname) {
		String notice = "";
		
		// 验证码有效期检查 
		boolean isVCodeValid = checkVerificationCode(mobilePhone, verificationCode);
		if (!isVCodeValid){
			notice = messageHelper.getMessage("tu.verification.code.expire");
		}
		
		// 重复注册检查
		List<Map<String, String>> userInfoList = userDao.queryUser(mobilePhone);
		boolean isNotRepeat = userInfoList.size() > 0 ? false : true;
		if (!isNotRepeat){
			String existNotice = messageHelper.getMessage("tu.registered");
			notice = (StringUtils.isEmpty(notice) ? existNotice	: notice + "; " + existNotice);
		}
		
		// 昵称唯一性检查
		userInfoList = userDao.queryUserByName(nickname);
		boolean isNicknameNotRepeat = userInfoList.size() > 0 ? false : true;
		if (!isNicknameNotRepeat){
			String existNotice = messageHelper.getMessage("tu.nickname.used");
			notice = (StringUtils.isEmpty(notice) ? existNotice	: notice + "; " + existNotice);
		}
		
		// 手机号长度验证：11位数字检查
		final String regularOfMobilePhone = "\\d{11}";
		boolean isLengthValid = mobilePhone.matches(regularOfMobilePhone);
		if (!isLengthValid){
			String invalidNotice = messageHelper.getMessage("tu.register.mobilephone.invalid");
			notice = (StringUtils.isEmpty(notice) ? invalidNotice : notice + "; " + invalidNotice);
		}
		
		// 呢称验证：2个字符以上且不能多于8个字符
		boolean isNicknameValid = (nickname.length() >= 2 && nickname.length() <= 8);
		if (!isNicknameValid) {
			String inValidNotice = messageHelper.getMessage("tu.register.nickname.failure");
			notice = StringUtils.isEmpty(notice) ? inValidNotice:notice+";"+inValidNotice;
		}
		
		// 注册
		if (isVCodeValid && isNotRepeat && isLengthValid && isNicknameValid){
			int effectRows = userDao.registerMobileClient(mobilePhone, password, nickname);
			if (effectRows == 0){
				notice = messageHelper.getMessage("tu.register.failure");
			}
		}
		
		return notice;
	}
	
	/**
	 * app 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	public Map<String, String> loginMobileClient(String username, String password) {
		Map<String, String> userInfo = new HashMap<>();
		String notice = "";
		List<Map<String, String>> userList = userDao.loginMobileClient(username, password);
		if (userList != null && userList .size() > 0){
			userInfo = userList .get(0);
			if (!StringUtils.isEmpty(userInfo.get("picUrl"))){
				String basePath = ResourceHelper.getProperty("sc.image.service.web.path") + "/lykimg/";
				String picUrl =  (basePath + userInfo.get("picUrl"));
				userInfo.put("picUrl", picUrl);
			}
			
		} else {
			// 检查用户是否已注册
			List<Map<String, String>> userInfoList = userDao.queryUser(username);
			boolean isValid = userInfoList.size() > 0 ? true : false;
			if (isValid){
				notice = messageHelper.getMessage("tu.login.password.error");
			} else {
				notice = messageHelper.getMessage("tu.login.unregister");
			}
			userInfo.put("notice", notice);
		}
		return userInfo;
	}
	
	/**
	 * 查看手机用户信息
	 * @param mobilePhone
	 * @return
	 */
	public List<Map<String, String>> queryUser(String mobilePhone) {
		return userDao.queryUser(mobilePhone);
	}
	
	/**
	 * 删除手机用户
	 * @param mobilePhone
	 * @return
	 */
	public int deleteMobilePhoneUser(String mobilePhone) {
		return userDao.deleteMobilePhoneUser(mobilePhone);
	}
	
	/**
	 * 第三方注册
	 * @param thirdPartyLoginId
	 * @param thirdPartyLoginType
	 * @return
	 */
	public int thirdPartyRegister(String thirdPartyLoginId, String thirdPartyLoginType) {
		return userDao.thirdPartyRegister(thirdPartyLoginId, thirdPartyLoginType);
	}
	
	/**
	 * 第三方便登录
	 * @param thirdPartyLoginId
	 * @param thirdPartyLoginType
	 * @return
	 */
	public Map<String, String> thirdPartyLogin(String thirdPartyLoginId, String thirdPartyLoginType) {
		Map<String, String> userInfo = new HashMap<>();
		List<Map<String, String>> userList = userDao.thirdPartyLogin(thirdPartyLoginId, thirdPartyLoginType);
		boolean isExist = userList  != null && userList .size() > 0;
		int effectRows = 0;
		if (isExist){
			userInfo = userList .get(0);
			if (!StringUtils.isEmpty(userInfo.get("picUrl"))){
				String basePath = ResourceHelper.getProperty("sc.image.service.web.path") + "/lykimg/";
				String picUrl =  (basePath + userInfo.get("picUrl"));
				userInfo.put("picUrl", picUrl);
			}
		} else {
			effectRows = userDao.thirdPartyRegister(thirdPartyLoginId, thirdPartyLoginType);
		}
		
		userInfo.put("isFirstLogin", isExist ? "0" : "1");
		userInfo.put("isSuccess", (userInfo.size() > 0 || effectRows > 0) ? "1" : "0");
		
		return userInfo;
	}
	
	/**
	 * 第三方用户删除
	 * @param thirdPartyLoginId
	 * @param thirdPartyLoginType
	 * @return
	 */
	public int deleteThirdPartyUser(String thirdPartyLoginId, String thirdPartyLoginType) {
		return userDao.deleteThirdPartyUser(thirdPartyLoginId, thirdPartyLoginType);
	}
	
	/**
	 * 查看第三方信息
	 * @param thirdPartyLoginId
	 * @param thirdPartyLoginType
	 * @return
	 */
	public List<Map<String, String>> queryUser(String thirdPartyLoginId, String thirdPartyLoginType) {
		return userDao.queryUser(thirdPartyLoginId, thirdPartyLoginType);
	}
	
	public Map<String, String> queryUserById(String userId){
		Map<String, String> userInfo = new HashMap<>();
		List<Map<String, String>> userList =  userDao.queryUserById(userId);
		if (userList.size() > 0){
			userInfo.putAll(userList.get(0));
		}
		return userInfo;
	}
	
	/**
	 * 修改密码
	 * @param username
	 * @param password
	 * @return
	 */
	public String revisePassword(String mobilePhone, String verificationCode, String password) {
		String notice = "";
		
		// 验证码有效期检查
		boolean isVCode = checkVerificationCode(mobilePhone, verificationCode);
		if (!isVCode){
			notice = messageHelper.getMessage("tu.verification.code.expire");
		}
		
		// 检查用户是否已注册
		List<Map<String, String>> userInfoList = userDao.queryUser(mobilePhone);
		boolean IsUnRegister = userInfoList.size() > 0 ? false : true;
		if (IsUnRegister){
			String existNotice = messageHelper.getMessage("tu.unregister");
			notice = (StringUtils.isEmpty(notice) ? existNotice	: notice + "; " + existNotice);
		}
		
		// 修改密码
		if (isVCode && !IsUnRegister){
			int effectRows = userDao.revisePassword(mobilePhone, password);
			if (effectRows == 0){
				notice = messageHelper.getMessage("tu.modify.password.failure");
			}
		}
		
		return notice;
	}
	
	/**
	 * 发送短信验证码
	 * @param mobilePhone
	 * @return
	 */
	public Map<String, String> sendSMSVerificationCode(String mobilePhone, boolean isRegistered){
		Map<String, String> resultMap = new HashMap<>();
		
		String notice = "";
		
		List<Map<String, String>> userList = userDao.queryUser(mobilePhone);
		if (isRegistered && userList.size() == 0){
			notice = messageHelper.getMessage("tu.unregister");
			resultMap.put("notice", notice);
			return resultMap;
		} else if (!isRegistered && userList.size() > 0){
			notice = messageHelper.getMessage("tu.registered");
			resultMap.put("notice", notice);
			return resultMap;
		}
		
		// 11位数字验证
		final String regularOfMobilePhone = "\\d{11}";
		if (!mobilePhone.matches(regularOfMobilePhone)) {
			notice = messageHelper.getMessage("tu.register.mobilephone.invalid");
			resultMap.put("notice", notice);
			return resultMap;
		}
		
		// 有效号段验证
		String numberSections = ResourceHelper.getProperty("sc.mobile.phone.number.section");
		List<String> list = Arrays.asList(numberSections.split(","));
		List<String> startsWithList = list.stream()
				.filter(item -> mobilePhone.startsWith(item))
				.collect(Collectors.toList());
		if (startsWithList.size() == 0){
			notice = messageHelper.getMessage("tu.send.sms.invalid.number.section");
			logger.error(mobilePhone + " 请输入有效的号段");
			resultMap.put("notice", notice);
			return resultMap;
		}
		
		// 同一手机号一天中只能发送过10次不相同的验证
		String cacheVCode = stringRedisTemplate.opsForValue().get(mobilePhone + "vCodeList");
		if (!StringUtils.isEmpty(cacheVCode) && cacheVCode.split(",").length > 9){
			notice = messageHelper.getMessage("tu.send.sms.too.much.operation");
			logger.error(mobilePhone + " 同一手机号一天中只能发送过10次不相同的验证");
			resultMap.put("notice", notice);
			return resultMap;
		}
		
		// 要保存同一手机号在一天中发送的验证码不同
		String verificationCode = getDiffRandomPerDay(mobilePhone, 6);
		stringRedisTemplate.opsForValue().set(mobilePhone + "verificationCode", verificationCode);
		int timeout = Integer.parseInt(ResourceHelper.getProperty("ccp.sms.verification.code.timeout"));
		boolean executeResult = stringRedisTemplate.expire(mobilePhone + "verificationCode", timeout, TimeUnit.MINUTES);
		if (executeResult){
			String statusCode = SendSMS.create().send(mobilePhone, verificationCode);
			if (!SendSMS.RIGHT_CODE.equals(statusCode)){
				notice = messageHelper.getMessage("tu.send.sms.failure");
			} else {
				resultMap.put("verificationCode", verificationCode);
			}
		} else {
			notice = messageHelper.getMessage("tu.send.sms.generator.failure");
			logger.error("手机号：" + mobilePhone + " 验证码未发送，因为有效期设置失败");
		}
		
		resultMap.put("notice", notice);
		return resultMap;
	}
	
	public Map<String, String> generatorVerificationCode(String mobilePhone, boolean isRegistered){
		Map<String, String> resultMap = new HashMap<>();
		List<Map<String, String>> userList = userDao.queryUser(mobilePhone);
		String notice = "";
		if (isRegistered && userList.size() == 0){
			notice = messageHelper.getMessage("tu.unregister");
		} else if (!isRegistered && userList.size() > 0){
			notice = messageHelper.getMessage("tu.registered");
		}
		
		if (StringUtils.isEmpty(notice)){
			String verificationCode = Utils.getRandomInteger(6);
			
			stringRedisTemplate.opsForValue().set(mobilePhone + "verificationCode", verificationCode);
			int timeout = Integer.parseInt(ResourceHelper.getProperty("ccp.sms.verification.code.timeout"));
			boolean executeResult = stringRedisTemplate.expire(mobilePhone + "verificationCode", timeout, TimeUnit.MINUTES);
			logger.info(("expiration date of user " + mobilePhone + ": ") + stringRedisTemplate.getExpire(mobilePhone + "verificationCode"));
			if (executeResult){
				resultMap.put("verificationCode", verificationCode);
			} else {
				notice = messageHelper.getMessage("tu.send.sms.generator.failure");
			}
		}
		resultMap.put("notice", notice);
		
		return resultMap;
	}
	
	/**
	 * 查看验证码的正确性
	 * @param mobilePhone
	 * @param verificationCode
	 * @return
	 */
	public boolean checkVerificationCode(String mobilePhone, String verificationCode){
		boolean isValid = false;
		String cacheVerificationCode = stringRedisTemplate.opsForValue().get(mobilePhone + "verificationCode");
		if (!StringUtils.isEmpty(cacheVerificationCode) && cacheVerificationCode.equals(verificationCode)){
			isValid = true;
		}
		return isValid;
	}

	
	public Integer userCount(){
		return userDao.userCount();
	}


	public Map<String, String> updateNickname(String userId, String nickname) {
		Map<String, String> resultMap = new HashMap<String, String>();
		String notice = "";
		
		// 呢称验证：2个字符以上且不能多于8个字符
		boolean isNicknameValid = (nickname.length() >= 2 && nickname.length() <= 8);
		if (!isNicknameValid) {
			String inValidNotice = messageHelper.getMessage("tu.register.nickname.failure");
			notice = StringUtils.isEmpty(notice) ? inValidNotice : notice+";"+inValidNotice;
		}
		
		// 昵称唯一性检查
		List<Map<String, String>> userInfoList = userDao.queryUserByName(nickname);
		userInfoList = userInfoList.stream().filter(p -> !userId.equals(p.get("userId"))).collect(Collectors.toList());
		boolean isNicknameNotRepeat = userInfoList.size() > 0 ? false : true;
		if (!isNicknameNotRepeat){
			String existNotice = messageHelper.getMessage("tu.nickname.used");
			notice = (StringUtils.isEmpty(notice) ? existNotice	: notice + "; " + existNotice);
		}
		
		int effectRows = 0;
		if (isNicknameValid && isNicknameNotRepeat){
			effectRows = userDao.reviseNickName(userId, nickname);
		}
		
		resultMap.put("notice", notice);
		resultMap.put("effectRows", effectRows + "");
		
		return resultMap;
	}

	
	public int saveExamType(String userId, String examType){
		return userDao.saveExamType(userId, examType);
	}
	
	public int saveUserImage(String userId, String picUrl){
		return userDao.saveUserImage(userId, picUrl);
	}

	public List<Map<String, String>> teacherInfoList(){
		List<Map<String, String>> teacherInfoList = userDao.teacherInfoList();
		if (teacherInfoList  != null){
			teacherInfoList.forEach(userInfo -> {
				String basePath = ResourceHelper.getProperty("sc.image.service.web.path") + "/lykimg/";
				String picUrl = basePath + userInfo.get("picUrl");
				userInfo.put("picUrl", picUrl);
			});
		}
		return teacherInfoList;
	}
	
	public List<String> teacherImageList(){
		List<String> pictureWebPathList = new ArrayList<String>();
		List<String> picList = userDao.teacherImageList();
		if (picList  != null){
			picList.forEach(pic -> {
				String basePath = ResourceHelper.getProperty("sc.image.service.web.path") + "/lykimg/";
				pictureWebPathList.add(basePath + pic);
			});
		}
		return pictureWebPathList;
	}
	
	public Map<String, String> teacherDetail(String userId){
		Map<String, String> teacher = new HashMap<>();
		List<Map<String, String>> teacherList = userDao.teacherDetail(userId);
		if (teacherList  != null && teacherList.size() > 0){
			teacher = teacherList.get(0);
			String basePath = ResourceHelper.getProperty("sc.image.service.web.path") + "/lykimg/";
			String picUrl = basePath + teacher.get("picUrl");
			teacher.put("picUrl", picUrl);
		}
		return teacher;
	}
	
	public synchronized List<String> famousSayings(){
		if (famousSayings.size() == 0){
			try {
				File file = new File(System.getenv("LEARN_SERVICE_APP_ENV") + "/data/famous-sayings.properties" );
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = "";
				System.out.println(file.length());
				while ( (line = br.readLine()) != null ){
					famousSayings.add(line);
				}
				br.close();
				System.out.println(Arrays.toString(famousSayings.toArray()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return famousSayings;
	}
	
	
	/**
	 * 添加反馈
	 * @param userId
	 * @param score
	 * @param feedbackContent
	 * @return
	 */
	public int addFeedback(String userId, String score, String feedbackContent) {
		return feedbackDao.add(userId, score, feedbackContent);
	}
	
	
	/**
	 * 每个手机号每天都必须生产不同的验证码
	 * @param mobilePhone
	 * @param length
	 * @return
	 */
	private String getDiffRandomPerDay(String mobilePhone, int length) {
		String vCode = Utils.getRandomInteger(length);
		String cacheVCode = stringRedisTemplate.opsForValue().get(mobilePhone + "vCodeList");
		
		if ( !StringUtils.isEmpty(cacheVCode) ) {
			while ( cacheVCode.contains(vCode) ) {
				vCode = Utils.getRandomInteger(6);
			}
		}
		
		cacheVCode = (cacheVCode == null ? vCode : cacheVCode + "," + vCode);
		
		stringRedisTemplate.opsForValue().set(mobilePhone + "vCodeList", cacheVCode);
		
		long expireSeconds = LocalTime.of(23, 59, 59,999999999).minusNanos(LocalTime.now().toNanoOfDay()).toSecondOfDay();
		boolean executeResult = stringRedisTemplate.expire(mobilePhone + "vCodeList", expireSeconds, TimeUnit.SECONDS);
		if (!executeResult){
			logger.error(mobilePhone + " 的产生过的验证码列表有效期保存失败");
		}
		return vCode;
	}
}