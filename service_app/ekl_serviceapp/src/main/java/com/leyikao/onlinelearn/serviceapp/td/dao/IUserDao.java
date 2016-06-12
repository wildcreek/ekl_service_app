package com.leyikao.onlinelearn.serviceapp.td.dao;

import java.util.List;
import java.util.Map;

public interface IUserDao {

	/**
	 * 手机用户注册
	 * @param mobilePhone
	 * @param password
	 * @param nickname
	 * @return
	 */
	public int registerMobileClient(String mobilePhone, String password, String nickname);
	
	/**
	 * 删除手机用户
	 * @param mobilePhone
	 * @return
	 */
	public int deleteMobilePhoneUser(String mobilePhone);
	
	/**
	 * 第三方注册
	 * @param thirdPartyLoginId
	 * @param thirdPartyLoginType
	 * @return
	 */
	public int thirdPartyRegister(String thirdPartyLoginId, String thirdPartyLoginType);
	
	/**
	 * 删除第三方用户
	 * @param thirdPartyLoginId
	 * @param thirdPartyLoginType
	 * @return
	 */
	public int deleteThirdPartyUser(String thirdPartyLoginId, String thirdPartyLoginType);

	/**
	 * 查看手机用户信息
	 * 
	 * @param mobilePhone
	 * @return
	 */
	public List<Map<String, String>> queryUser(String mobilePhone);
	
	
	/**
	 * 查看第三方信息
	 * @param thirdPartyLoginId
	 * @param thirdPartyLoginType
	 * @return
	 */
	public List<Map<String, String>> queryUser(String thirdPartyLoginId, String thirdPartyLoginType);
	
	
	/**
	 * app 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	public List<Map<String, String>> loginMobileClient(String username, String password);
	
	/**
	 * 第三方登录
	 * @param thirdPartyLoginId
	 * @param thirdPartyLoginType
	 * @return
	 */
	public List<Map<String, String>> thirdPartyLogin(String thirdPartyLoginId, String thirdPartyLoginType);

	/**
	 * 修改密码
	 * @param username
	 * @param password
	 * @return
	 */
	public int revisePassword(String username, String password);
	
	/**
	 * 根据用户ID查询用户信息
	 * @param userId
	 * @return
	 */
	public List<Map<String, String>> queryUserById(String userId);
	
	public List<Map<String, String>> queryUserByName(String nickname);
	
	public Integer userCount();

	
	public int reviseNickName(String userId, String nickname);

	
	public int saveExamType(String userId, String examType);
	
	public int saveUserImage(String userId, String picUrl);
	
	public List<Map<String, String>> teacherInfoList();
	
	public List<String> teacherImageList();
	
	public List<Map<String, String>> teacherDetail(String userId);

}
