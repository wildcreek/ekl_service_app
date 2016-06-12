package com.leyikao.onlinelearn.serviceapp.td.dao;

import java.util.List;
import java.util.Map;

public interface ICoreDao {

	public List<Map<String, Object>> findIntegralRule(String actionid);
/*	public Map<String, Map<String, Object>> addIntegralPool(String userId,String actionid);
	public Map<String, Map<String, Object>> updateIntegralPool(String userId,String actionid);*/

	public int updateUserCore(String userid, int integralNumber);

	public int insertIntegralPool(String uId, String actionId,
								  int integralNumber);

	public  List<Map<String, Object>> findIntegralPool(String userId, String actionId);

	public int updateIntegralPool(String uId, String actionId, String lastModifyTime);

	public Map<String, String> findReply(String string);

	public Map<String, Object> countPractice(String userId);

	public int updateUserCoreBythirdPartyLoginId(String uId,
												 Integer integralNumber);


}
