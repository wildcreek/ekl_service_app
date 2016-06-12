package com.leyikao.onlinelearn.serviceapp.td.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.leyikao.onlinelearn.serviceapp.td.dao.ICoreDao;
import com.leyikao.onlinelearn.serviceapp.td.mapper.ICourseMapper;
import com.leyikao.onlinelearn.serviceapp.td.mapper.IIntegralMapper;

public class CoreDao implements ICoreDao{
	@Resource
	private IIntegralMapper integral;
	/**
	 * 查找积分规则表
	 * @param userid
	 * @param actionid 
	 * @return
	 */
	@Override
	public  List<Map<String, Object>> findIntegralRule(
			String actionid) {
		// TODO Auto-generated method stub
		Map<String, String> map = new ConcurrentHashMap<>();

		map.put("actionid", actionid);
		return integral.findIntegralRule(map);
	}
	/**
	 * 更新用户积分
	 * @param userid
	 * @param integralNumber 用户改变的积分
	 * @return
	 */

	@Override
	public int updateUserCore(String userid, int integralNumber) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new ConcurrentHashMap<>();
		map.put("userId", userid);
		map.put("integralNumber", integralNumber);
		return integral.updateUserCore(map);
	}
	@Override
	public int insertIntegralPool(String uId, String actionId,
			int integralNumber) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new ConcurrentHashMap<>();
		map.put("userId", uId);
		map.put("integralBalance", integralNumber+"");
		map.put("actionId", actionId);
		
		return integral.insertIntegralPool(map);
		
	}
	@Override
	public List<Map<String, Object>> findIntegralPool(String userId, String actionId) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap();
		map.put("userId", userId);
		map.put("actionId", actionId);
		return  integral.findIntegralPool(map);
	}
	@Override
	public int updateIntegralPool(String uId, String actionId,String lastModifyTime) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new ConcurrentHashMap<>();
		map.put("userId", uId);
		map.put("actionId", actionId);
		map.put("lastModifyTime", lastModifyTime);
		return  integral.updateIntegralPool(map);
	}
	@Override
	public Map<String, String> findReply(String replyid) {
		// TODO Auto-generated method stub
		return integral.findReply(replyid);
	}
	@Override
	public Map<String, Object> countPractice(String userId) {
		// TODO Auto-generated method stub
		return integral.countPractice(userId);
	}
	@Override
	public int updateUserCoreBythirdPartyLoginId(String uId,
			Integer integralNumber) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new ConcurrentHashMap<>();
		map.put("userId", uId);
		map.put("integralNumber", integralNumber);
		return integral.updateUserCoreBythirdPartyLoginId(map);
	}

}
