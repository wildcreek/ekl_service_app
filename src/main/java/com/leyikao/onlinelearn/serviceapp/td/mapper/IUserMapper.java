package com.leyikao.onlinelearn.serviceapp.td.mapper;

import java.util.List;
import java.util.Map;

public interface IUserMapper {
	
	/**
	 * app 用户注册
	 * @param map key: mobilePhone, password
	 * @return
	 */
	public int registerMobileClient(Map<String, String> map);
	
	/**
	 * 第三方注册
	 * @param map
	 * @return
	 */
	public int thirdPartyRegister(Map<String, String> map);
	
	/**
	 * 删除手机用户
	 * @param mobilePhone
	 * @return
	 */
	public int deleteMobilePhoneUser(String mobilePhone);
	
	/**
	 * 删除第三方用户
	 * @param map
	 * @return
	 */
	public int deleteThirdPartyUser(Map<String, String> map);

	/**
	 * 查看手机用户信息
	 * 
	 * @param mobilePhone
	 * @return
	 */
	public List<Map<String, String>> queryMobilePhoneUser(String mobilePhone);
	
	/**
	 * 查看第三方信息
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> queryThirdPartyUser(Map<String, String> map);

	/**
	 * app 用户登录
	 * 
	 * @param map key: username, password
	 * @return
	 */
	public List<Map<String, String>> loginMobileClient(Map<String, String> map);

	/**
	 * 第三方登录
	 * 
	 * @param map key: thirdPartyLoginId, thirdPartyLoginType
	 * @return
	 */
	public List<Map<String, String>> thirdPartyLogin(Map<String, String> map);
	
	/**
	 * 修改密码
	 * 
	 * @param map key: username, password
	 * @return
	 */
	public int revisePassword(Map<String, String> map);
	
	/**
	 * 保存考试类型
	 * @param map
	 * @return
	 */
	public int saveExamType(Map<String, String> map);
	
	/**
	 * 获取终端用户
	 * @param userId
	 * @return
	 */
	public List<Map<String, String>> queryUser(String userId);
	
	public List<String> userCount();
	
	/**
	 * 修改昵称
	 * @param map
	 * @return
	 */
	public int updateReviseNickName(Map<String, String> map);
	
	public List<Map<String, String>> queryUserByName(String nickname);
	
	public int saveUserImage(Map<String, String> map);
	
	public List<Map<String, String>> teacherInfoList();
	
	public List<String> teacherImageList();
	
	public List<Map<String, String>> teacherDetail(String userId);
}
