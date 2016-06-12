package com.leyikao.onlinelearn.serviceapp.td.dao.redis;

import java.util.List;
import java.util.Map;

import com.leyikao.onlinelearn.serviceapp.td.dao.IUserDao;

public class UserDao implements IUserDao {


	@Override
	public int deleteMobilePhoneUser(String mobilePhone) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int thirdPartyRegister(String thirdPartyLoginId,
			String thirdPartyLoginType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteThirdPartyUser(String thirdPartyLoginId,
			String thirdPartyLoginType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, String>> queryUser(String mobilePhone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> queryUser(String thirdPartyLoginId,
			String thirdPartyLoginType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> loginMobileClient(String username,
			String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> thirdPartyLogin(String thirdPartyLoginId,
			String thirdPartyLoginType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int revisePassword(String username, String password) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, String>> queryUserById(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer userCount() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int reviseNickName(String userId, String nickname) {
		// TODO Auto-generated method stub
		return 0;
	}


	public int saveExamType(String userId, String examType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int saveUserImage(String userId, String picUrl) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, String>> queryUserByName(String nickname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> teacherInfoList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> teacherImageList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> teacherDetail(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int registerMobileClient(String mobilePhone, String password,
			String nickname) {
		// TODO Auto-generated method stub
		return 0;
	}


}
