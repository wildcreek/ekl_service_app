package com.leyikao.onlinelearn.serviceapp.td.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.leyikao.onlinelearn.serviceapp.td.dao.IUserDao;
import com.leyikao.onlinelearn.serviceapp.td.mapper.IUserMapper;

public class TerminalUserDao implements IUserDao{
	
	@Resource
	private IUserMapper iUserMapper;
	
	/**
	 * 手机用户注册
	 * @param mobilePhone
	 * @param password
	 * @return
	 */
	public int registerMobileClient(String mobilePhone, String password, String nickname){
		Map<String, String> map = new ConcurrentHashMap<>();
		map.put("mobilePhone", mobilePhone);
		map.put("password", password);
		map.put("nickname", nickname);
		return iUserMapper.registerMobileClient(map);
	}
	
	/**
	 * app 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	public List<Map<String, String>> loginMobileClient(String username, String password){
		Map<String, String> map = new HashMap<>();
		map.put("username", username);
		map.put("password", password);
		return iUserMapper.loginMobileClient(map);
	}
	
	/**
	 * 删除手机用户
	 * @param mobilePhone
	 * @return
	 */
	public int deleteMobilePhoneUser(String mobilePhone){
		return iUserMapper.deleteMobilePhoneUser(mobilePhone);
	}
	
	/**
	 * 查看手机用户信息
	 * 
	 * @param mobilePhone
	 * @return
	 */
	public List<Map<String, String>> queryUser(String mobilePhone){
		return iUserMapper.queryMobilePhoneUser(mobilePhone);
	}
	
	/**
	 * 第三方注册
	 * @param thirdPartyLoginId
	 * @param thirdPartyLoginType
	 * @return
	 */
	public int thirdPartyRegister(String thirdPartyLoginId, String thirdPartyLoginType){
		Map<String, String> map = new HashMap<>();
		map.put("thirdPartyLoginId", thirdPartyLoginId);
		map.put("thirdPartyLoginType", thirdPartyLoginType);
		return iUserMapper.thirdPartyRegister(map);
	}
	
	/**
	 * 第三方登录
	 * @param thirdPartyLoginId
	 * @param thirdPartyLoginType
	 * @return
	 */
	public List<Map<String, String>> thirdPartyLogin(String thirdPartyLoginId, String thirdPartyLoginType){
		Map<String, String> map = new HashMap<>();
		map.put("thirdPartyLoginId", thirdPartyLoginId);
		map.put("thirdPartyLoginType", thirdPartyLoginType);
		return iUserMapper.thirdPartyLogin(map);
	}
	
	/**
	 * 删除第三方用户
	 * @param thirdPartyLoginId
	 * @param thirdPartyLoginType
	 * @return
	 */
	public int deleteThirdPartyUser(String thirdPartyLoginId, String thirdPartyLoginType){
		Map<String, String> map = new HashMap<>();
		map.put("thirdPartyLoginId", thirdPartyLoginId);
		map.put("thirdPartyLoginType", thirdPartyLoginType);
		return iUserMapper.deleteThirdPartyUser(map);
	}

	
	/**
	 * 查看第三方信息
	 * @param thirdPartyLoginId
	 * @param thirdPartyLoginType
	 * @return
	 */
	public List<Map<String, String>> queryUser(String thirdPartyLoginId, String thirdPartyLoginType){
		Map<String, String> map = new HashMap<>();
		map.put("thirdPartyLoginId", thirdPartyLoginId);
		map.put("thirdPartyLoginType", thirdPartyLoginType);
		return iUserMapper.queryThirdPartyUser(map);
	}
	
	
	/**
	 * 修改密码
	 * @param username
	 * @param password
	 * @return
	 */
	public int revisePassword(String username, String password){
		Map<String, String> map = new HashMap<>();
		map.put("username", username);
		map.put("password", password);
		return iUserMapper.revisePassword(map);
	}
	
	public int saveExamType(String userId, String examType){
		Map<String, String> map = new HashMap<>();
		map.put("userId", userId);
		map.put("examType", examType);
		return iUserMapper.saveExamType(map);
	}

	@Override
	public List<Map<String, String>> queryUserById(String userId) {
		return iUserMapper.queryUser(userId);
	}
	
	@Override
	public Integer userCount() {
		Integer userCount = 0;
		List<String> userStat = iUserMapper.userCount();
		if (userStat.size() > 0){
			userCount = Integer.parseInt(userStat.get(0));
		}
		return userCount;
	}
	
	@Override
	public int reviseNickName(String userId, String nickname) {
		Map<String, String> map = new HashMap<>();
		map.put("userId", userId);
		map.put("nickname", nickname);
		return iUserMapper.updateReviseNickName(map);

	}

	@Override
	public int saveUserImage(String userId, String picUrl) {
		Map<String, String> map = new HashMap<>();
		map.put("userId", userId);
		map.put("picUrl", picUrl);
		return iUserMapper.saveUserImage(map);
	}

	@Override
	public List<Map<String, String>> queryUserByName(String nickname) {
		return iUserMapper.queryUserByName(nickname);
	}

	@Override
	public List<Map<String, String>> teacherInfoList() {
		return iUserMapper.teacherInfoList();
	}

	@Override
	public List<String> teacherImageList() {
		return iUserMapper.teacherImageList();
	}

	@Override
	public List<Map<String, String>> teacherDetail(String userId) {
		return iUserMapper.teacherDetail(userId);
	}
	
}
